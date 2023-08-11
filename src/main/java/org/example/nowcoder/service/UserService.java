package org.example.nowcoder.service;

import org.example.nowcoder.entity.User;

import java.util.Map;

public interface UserService {

    User getUser(String userId);

    Map<String, Object> register(User user);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    int activate(Integer userId, String code);
}
