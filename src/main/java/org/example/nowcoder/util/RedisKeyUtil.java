package org.example.nowcoder.util;

public class RedisKeyUtil {

    public static final String SPLIT = ":";

    public static final String LIKE_ENTITY_PREFIX = "like:entity";

    private static final String LIKE_USER_PREFIX = "like:user";

    public static String getLikeEntityKey(Integer entityType, Integer entityId) {
        return LIKE_ENTITY_PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getLikeUserKey(Integer userId) {
        return LIKE_USER_PREFIX + SPLIT + userId;
    }

}
