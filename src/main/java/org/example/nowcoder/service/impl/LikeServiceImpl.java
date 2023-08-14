package org.example.nowcoder.service.impl;

import org.example.nowcoder.service.LikeService;
import org.example.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void like(Integer userId, Integer entityType, Integer entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
        // 可以点赞，也可以取消点赞
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeEntityKey, userId))) {
            redisTemplate.opsForSet().remove(likeEntityKey, userId);
        } else {
            redisTemplate.opsForSet().add(likeEntityKey, userId);
        }

    }

    @Override
    public Long likeCount(Integer entityType, Integer entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeEntityKey);
    }

    @Override
    public int likeStatus(Integer userId, Integer entityType, Integer entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeEntityKey, userId)) ? 1 : 0;

    }
}
