package org.example.nowcoder.component;

import org.example.nowcoder.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserHostHolder {

    private final ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clearUsers() {
        users.remove();
    }
}
