package org.example.nowcoder.service.impl;

import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId) {
//        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
//        // 可以点赞，也可以取消点赞
//        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeEntityKey, userId))) {
//            redisTemplate.opsForSet().remove(likeEntityKey, userId);
//        } else {
//            redisTemplate.opsForSet().add(likeEntityKey, userId);
//        }

        redisTemplate.execute(new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public Object execute(@NonNull RedisOperations operations) throws DataAccessException {
                String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
                String likeUserKey = RedisKeyUtil.getLikeUserKey(entityUserId);
                boolean equals = Boolean.TRUE.equals(operations.opsForSet().isMember(likeEntityKey, userId));
                operations.multi(); // 在multi() 事务中是不可以进行查询的
                if (equals) {
                    // 如果已经点赞：则向里面移除一个数据，移除的是当前实体的点赞的用户id
                    // 还要改变用户的点赞数量
                    operations.opsForSet().remove(likeEntityKey, userId);
                    System.out.println("删除了点赞");
                    operations.opsForValue().decrement(likeUserKey);
                } else {
                    operations.opsForSet().add(likeEntityKey, userId);
                    operations.opsForValue().increment(likeUserKey);
                }
                return operations.exec();
            }
        });

    }

    @Override
    public Long likeCount(Integer entityType, Integer entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeEntityKey);
    }

    @Override
    public int likeStatus(Integer userId, Integer entityType, Integer entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeEntityKey, userId)) ? LIKE_STATUS_LIKED : LIKE_STATUS_UNLIKED;

    }

    @Override
    public int likeUserCount(Integer userId) {
        String likeUserKey = RedisKeyUtil.getLikeUserKey(userId);
        Object o = redisTemplate.opsForValue().get(likeUserKey);
        if (o != null) {
            return (Integer) o;
        }
        return 0;
    }
}
