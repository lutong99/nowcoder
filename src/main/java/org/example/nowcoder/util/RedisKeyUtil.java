package org.example.nowcoder.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_LIKE_ENTITY = "like:entity";

    private static final String PREFIX_LIKE_USER = "like:user";

    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";

    private static final String PREFIX_KAPTCHA = "kaptcha";

    private static final String PREFIX_VERIFY_CODE = "verify:code";

    private static final String PREFIX_TICKET = "login:ticket";

    private static final String PREFIX_USER = "user";

    private static final String PREFIX_UV = "uv";

    private static final String PREFIX_DAU = "dau";

    private static final String PREFIX_POST = "post";

    public static String getLikeEntityKey(Integer entityType, Integer entityId) {
        return PREFIX_LIKE_ENTITY + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getLikeUserKey(Integer userId) {
        return PREFIX_LIKE_USER + SPLIT + userId;
    }

    public static String getFollowerKey(int entityType, int entityId) {
        // follower:entityType:entityId -> zset(userId, date)
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getFolloweeKey(int userId, int entityType) {
        // followee:userId:entityType -> zset(entityId)
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getKaptchaKey(String kaptChaOwner) {
        return PREFIX_KAPTCHA + SPLIT + kaptChaOwner;
    }

    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    public static String getUserKey(Integer userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    public static String getVerifyCodeKey(String email) {
        return PREFIX_VERIFY_CODE + SPLIT + email;
    }

    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    public static String getUVKey(String from, String end) {
        return PREFIX_UV + SPLIT + from + SPLIT + end;
    }

    public static String getDAUKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    public static String getDAUKey(String from, String end) {
        return PREFIX_DAU + SPLIT + from + SPLIT + end;

    }

    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }


}
