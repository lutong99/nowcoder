package org.example.nowcoder.service.impl;

import org.example.nowcoder.entity.User;
import org.example.nowcoder.service.FollowService;
import org.example.nowcoder.service.UserService;
import org.example.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService {

    private RedisTemplate<String, Object> redisTemplate;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings("unchecked")
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                operations.multi();
                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });

    }

    @Override
    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public Object execute(RedisOperations operations) throws DataAccessException {

                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                operations.multi();
                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });


    }

    @Override
    public Long followerCount(Integer entityType, Integer entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);

    }

    @Override
    public Long followeeCount(Integer userId, Integer entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    @Override
    public Boolean followStatus(Integer userId, Integer entityType, Integer entityId) {
        // 用户有没有关注实体，查看实体的关注者中，有没有这个用户
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().score(followerKey, userId) != null;
    }

    @Override
    public List<Map<String, Object>> getFollowers(Integer userId) {
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        return getFollowMapList(followerKey);


    }

    @Override
    public List<Map<String, Object>> getFollowees(Integer userId) {

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        return getFollowMapList(followeeKey);

    }

    private List<Map<String, Object>> getFollowMapList(String followeeKey) {
        Set<Object> zSetElements = redisTemplate.opsForZSet().range(followeeKey, 0, -1);
        List<Map<String, Object>> followerMapList = new ArrayList<>();

        if (zSetElements == null) {
            return null;
        }

        for (Object zSetElement : zSetElements) {
            Map<String, Object> map = new HashMap<>();
            Integer followerUserId = (Integer) zSetElement;
            User followerUser = userService.getById(followerUserId);
            map.put("user", followerUser);
            Double timeMillis = redisTemplate.opsForZSet().score(followeeKey, zSetElement);
            map.put("date", new Date(Objects.requireNonNull(timeMillis).longValue()));
            followerMapList.add(map);
        }
        return followerMapList;
    }
}
