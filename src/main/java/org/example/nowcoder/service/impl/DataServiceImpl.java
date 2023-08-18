package org.example.nowcoder.service.impl;

import org.example.nowcoder.service.DataService;
import org.example.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {


    private RedisTemplate<String, Object> redisTemplate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long statisticUV(Date from, Date end) {

        if (from == null || end == null) {
            throw new IllegalArgumentException("请求的统计日期不正确");
        }

        String uvKey = RedisKeyUtil.getUVKey(dateFormat.format(from), dateFormat.format(end));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        List<String> uvKeyList = new ArrayList<>();
        while (!calendar.getTime().after(end)) {
            uvKeyList.add(RedisKeyUtil.getUVKey(dateFormat.format(calendar.getTime())));
            calendar.add(Calendar.DATE, 1);
        }

        redisTemplate.opsForHyperLogLog().union(uvKey, uvKeyList.toArray(new String[0]));
        return redisTemplate.opsForHyperLogLog().size(uvKey);
    }

    @Override
    public void recordIP(String ip) {
        String uvKey = RedisKeyUtil.getUVKey(dateFormat.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(uvKey, ip);
    }

    @Override
    public Long statisticDAU(Date from, Date end) {
        if (from == null || end == null) {
            throw new IllegalArgumentException("请求的统计日期不正确");
        }
        String dauKey = RedisKeyUtil.getDAUKey(dateFormat.format(from), dateFormat.format(end));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);

        List<byte[]> keyByteList = new ArrayList<>();
        while (!calendar.getTime().after(end)) {
            byte[] bytesKey = RedisKeyUtil.getDAUKey(dateFormat.format(calendar.getTime())).getBytes();
            keyByteList.add(bytesKey);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return (Long) redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.bitOp(RedisStringCommands.BitOperation.OR, dauKey.getBytes(), keyByteList.toArray(new byte[0][0]));
            return connection.bitCount(dauKey.getBytes());
        });
    }

    @Override
    public void recordDAU(Integer userId) {
        String dauKey = RedisKeyUtil.getDAUKey(dateFormat.format(new Date()));
        redisTemplate.opsForValue().setBit(dauKey, userId, true);
    }
}
