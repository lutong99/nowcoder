package org.example.nowcoder.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebUtil {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request == null || StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("请求参数不正确");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
