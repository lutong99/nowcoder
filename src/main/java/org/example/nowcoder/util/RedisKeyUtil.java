package org.example.nowcoder.util;

public class RedisKeyUtil {

    public static final String SPLIT = ":";

    public static final String PREFIX = "like:entity";

    public static String getLikeEntityKey(Integer entityType, Integer entityId) {
        return PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

}
