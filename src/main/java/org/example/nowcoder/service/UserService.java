package org.example.nowcoder.service;

import org.example.nowcoder.entity.LoginTicket;
import org.example.nowcoder.entity.User;

import java.util.Map;

public interface UserService {

    User getUser(Integer userId);

    Map<String, Object> register(User user);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    int activate(Integer userId, String code);

    Map<String, Object> login(String username, String password, int expiredSeconds);

    void logout(String ticket);

    Map<String, Object> sendResetCode(String email);

    Map<String, Object> resetPassword(String email, String password);

    LoginTicket getLoginTicketByTicket(String ticket);

    int updateHeader(Integer userId, String headerUrl);

    /**
     * @param password 加密后的密码
     */
    int updateUserPass(Integer userId, String password);
}
