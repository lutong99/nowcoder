package org.example.nowcoder.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;

@SpringBootTest
public class RedisTests {


    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Test
    public void testRedisTransactional() {

        List<Object> execute = redisTemplate.execute(new SessionCallback<>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:trans";
                operations.multi();
                BoundSetOperations boundSetOperations = operations.boundSetOps("test:trans");
                boundSetOperations.add("张三", "李四", "王五", "吕布", "赵云");
                List exec = operations.exec();
                return exec;
            }
        });

        System.out.println("execute = " + execute);

    }

    @Test
    public void testRedisTransactional2() {

        Object execute = redisTemplate.execute(new SessionCallback<>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:trans";
                operations.multi();
                BoundSetOperations boundSetOperations = operations.boundSetOps("test:trans");
                boundSetOperations.add("张三", "李四", "王五", "吕布", "赵云");
                return operations.exec();
            }
        });

        System.out.println("execute = " + execute);

    }

}
