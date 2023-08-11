package org.example.nowcoder.mail;

import org.example.nowcoder.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.transform.Templates;

@SpringBootTest
public class MailTests {

    private MailClient mailClient;

    @Autowired
    public void setMailClient(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Test
    public void testSendMail() {
        mailClient.sendMail("xxxxx@qq.com", "测试主题", "你好，我是测试邮件");
    }

    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Test
    public void testSendHtml() {
        Context context= new Context();
        context.setVariable("username", "Lutong99");
        String content = templateEngine.process("/mail/maildemo", context);

        mailClient.sendMail("@aliyun.com", "HTML", content);


    }
}
