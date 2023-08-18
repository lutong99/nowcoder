package org.example.nowcoder.constant;

public interface CommunityConstant {

    int ACTIVATE_SUCCESS = 0;

    int ACTIVATE_REPEAT = 1;

    int ACTIVATE_FAILURE = 2;

    String OPERATE_RESULT_MSG = "msg";

    String OPERATE_RESULT_TARGET = "target";

    String KAPTCHA = "kaptcha";

    /*
    登陆超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 48;

    String PAGE_SIZE_COMMENT = "5";

    String PAGE_SIZE_POST = "10";

    String PAGE_SIZE_MESSAGE = "10";

    String PAGE_OFFSET = "1";

    String TOPIC_LIKE = "like";
    String TOPIC_COMMENT = "comment";
    String TOPIC_FOLLOW = "follow";
    String TOPIC_HIGHLIGHT = "highlight";
    String TOPIC_TOP = "top";
    String TOPIC_INVALID = "invalid";

    String TOPIC_PUBLISH = "publish";

    String SYSTEM_USER = "SYSTEM";

    Integer SYSTEM_USER_ID = 1;

    String AUTHORITY_ADMIN = "ADMIN";
    String AUTHORITY_USER = "USER";
    String AUTHORITY_MODERATOR = "MODERATOR";

}
