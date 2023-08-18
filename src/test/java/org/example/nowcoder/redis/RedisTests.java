package org.example.nowcoder.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Test
    public void testHyperLogLog() {
        String redisKey = "test:hll:01";
        for (int i = 1; i <= 10000; ++i) {
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }
        Long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println("size = " + size);
    }


    @Test
    public void testHyperLogLog11() {
        List<String> keyList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String key = "test:hll:a" + i;
            keyList.add(key);
            redisTemplate.opsForHyperLogLog().add(key, UUID.randomUUID());
            redisTemplate.opsForHyperLogLog().add(key, UUID.randomUUID());
            redisTemplate.opsForHyperLogLog().add(key, UUID.randomUUID());
            redisTemplate.opsForHyperLogLog().add(key, UUID.randomUUID());
        }
        String[] keyListArray = keyList.toArray(new String[0]);
        redisTemplate.opsForHyperLogLog().union("test:hll:aunion", keyListArray);
        System.out.println(redisTemplate.opsForHyperLogLog().size(keyListArray).equals(redisTemplate.opsForHyperLogLog().size("test:hll:aunion")));

    }

    @Test
    public void testHyperLogLog3() {

        String redisKey = "test:hll:union";
        for (int i = 0; i < 7; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey, UUID.randomUUID().toString());
        }
    }

    @Test
    public void testHyperLogLog2() {
        String redisKey02 = "test:hll:02";
        for (int i = 1; i <= 10000; ++i) {
            redisTemplate.opsForHyperLogLog().add(redisKey02, i);
        }

        String redisKey03 = "test:hll:03";
        for (int i = 5001; i <= 15001; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey03, i);
        }

        String redisKey04 = "test:hll:04";
        for (int i = 10001; i <= 20001; ++i) {
            redisTemplate.opsForHyperLogLog().add(redisKey04, i);
        }

        Long size = redisTemplate.opsForHyperLogLog().size(redisKey02, redisKey03, redisKey04);
        System.out.println("size = " + size);
    }

    @Test
    public void testBitMap() {
        String redisKey01 = "test:bm:01";
        redisTemplate.opsForValue().setBit(redisKey01, 1, true);
        redisTemplate.opsForValue().setBit(redisKey01, 4, true);
        redisTemplate.opsForValue().setBit(redisKey01, 7, true);

        Boolean bit01 = redisTemplate.opsForValue().getBit(redisKey01, 0);
        Boolean bit02 = redisTemplate.opsForValue().getBit(redisKey01, 1);
        Boolean bit03 = redisTemplate.opsForValue().getBit(redisKey01, 2);

        System.out.println("bit01 = " + bit01);
        System.out.println("bit02 = " + bit02);
        System.out.println("bit03 = " + bit03);

        Object count = redisTemplate.execute((RedisCallback<Object>) connection -> connection.bitCount(redisKey01.getBytes()));
        System.out.println("count = " + count);

    }

    @Test
    public void testBitMap2() {
        String redisKey02 = "test:bm:02";
        String redisKey03 = "test:bm:03";
        String redisKey04 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey02, 0, true);
        redisTemplate.opsForValue().setBit(redisKey02, 1, true);
        redisTemplate.opsForValue().setBit(redisKey02, 2, true);
        redisTemplate.opsForValue().setBit(redisKey03, 2, true);
        redisTemplate.opsForValue().setBit(redisKey03, 3, true);
        redisTemplate.opsForValue().setBit(redisKey03, 4, true);
        redisTemplate.opsForValue().setBit(redisKey04, 4, true);
        redisTemplate.opsForValue().setBit(redisKey04, 5, true);
        redisTemplate.opsForValue().setBit(redisKey04, 6, true);

        String redisKey05 = "test:bm:or";

        Object res = redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.bitOp(RedisStringCommands.BitOperation.OR, redisKey05.getBytes(), redisKey02.getBytes(), redisKey03.getBytes(), redisKey04.getBytes());
            return connection.bitCount(redisKey05.getBytes());

        });
        System.out.println("res = " + res);
        for (int i = 0; i < 7; i++) {
            System.out.println(redisTemplate.opsForValue().getBit(redisKey05, 1));

        }

    }


}
