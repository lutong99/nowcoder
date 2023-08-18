package org.example.nowcoder.service;

import org.example.nowcoder.constant.CommunityConstant;
import org.example.nowcoder.constant.LoginTicketConstant;
import org.example.nowcoder.constant.UserConstant;
import org.example.nowcoder.entity.LoginTicket;
import org.example.nowcoder.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface UserService extends CommunityConstant, LoginTicketConstant, UserConstant {

    User getById(Integer userId);

    Map<String, Object> register(User user);

    User getByUsername(String username);

    User getByEmail(String email);

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

    default Collection<? extends GrantedAuthority> getAuthorities(Integer userId) {
        return Collections.singleton(() -> {
            User user = getById(userId);
            if (user != null) {
                switch (user.getType()) {
                    case TYPE_ADMIN:
                        return AUTHORITY_ADMIN;
                    case TYPE_MODERATOR:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
            return null;
        });
    }

}
