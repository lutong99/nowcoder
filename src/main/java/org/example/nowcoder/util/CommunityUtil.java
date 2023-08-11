package org.example.nowcoder.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.SimpleTimeZone;
import java.util.UUID;

public class CommunityUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return DigestUtils.appendMd5DigestAsHex(key.getBytes(), new StringBuilder()).toString();

    }

}
