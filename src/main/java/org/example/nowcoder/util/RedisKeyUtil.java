package org.example.nowcoder.util;

public class RedisKeyUtil {

    public static final String SPLIT = ":";

    public static final String LIKE_ENTITY_PREFIX = "like:entity";

    private static final String LIKE_USER_PREFIX = "like:user";

    private static final String FOLLOWEE_PREFIX = "followee";
    private static final String FOLLOWER_PREFIX = "follower";

    public static String getLikeEntityKey(Integer entityType, Integer entityId) {
        return LIKE_ENTITY_PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getLikeUserKey(Integer userId) {
        return LIKE_USER_PREFIX + SPLIT + userId;
    }

    public static String getFollowerKey(int entityType, int entityId) {
        // follower:entityType:entityId -> zset(userId, date)
        return FOLLOWER_PREFIX + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getFolloweeKey(int userId, int entityType) {
        // followee:userId:entityType -> zset(entityId)
        return FOLLOWEE_PREFIX + SPLIT + userId + SPLIT + entityType;
    }

}
