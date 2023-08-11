package org.example.nowcoder.mybatis;

import org.example.nowcoder.entity.LoginTicket;
import org.example.nowcoder.mapper.LoginTicketMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootTest
public class LoginTicketMapperTests {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testLoginTicketMapper() {
        LoginTicketMapper loginTicketMapper = applicationContext.getBean(LoginTicketMapper.class);
        List<LoginTicket> loginTickets = loginTicketMapper.selectByExample(null);
        loginTickets.forEach(System.out::println);
    }



}
