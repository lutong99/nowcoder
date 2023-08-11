package org.example.nowcoder.mail;

import org.example.nowcoder.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTests {

    private MailClient mailClient;

    @Autowired
    public void setMailClient(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Test
    public void testSendMail() {
        mailClient.sendMail("", "测试主题", "你好，我是测试邮件");
    }
}
