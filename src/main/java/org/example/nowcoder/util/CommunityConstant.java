package org.example.nowcoder.util;

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
    int DEFAUTL_EXPIRED_SECONDS = 3600;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 48;

}
