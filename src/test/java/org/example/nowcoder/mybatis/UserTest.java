package org.example.nowcoder.mybatis;

import org.example.nowcoder.NowcoderApplication;
import org.example.nowcoder.entity.User;
import org.example.nowcoder.entity.UserExample;
import org.example.nowcoder.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


@SpringBootTest
@ContextConfiguration(classes = NowcoderApplication.class)
public class UserTest {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testUser() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(101);
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        List<User> users = userMapper.selectByExample(userExample);
        User user = users.get(0);
        System.out.println("user = " + user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();UserMapper userMapper = applicationContext.getBean(UserMapper.class);

        user.setUsername("Hello ");
        userMapper.insert(user);
        System.out.println("user = " + user);

    }
}
