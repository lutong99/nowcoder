package org.example.nowcoder.transction;

import org.example.nowcoder.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootTest
public class TransactionTests {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testSave() {
        AlphaService alphaService = applicationContext.getBean(AlphaService.class);
        String res = alphaService.save1();
        System.out.println("res = " + res);

    }
}
