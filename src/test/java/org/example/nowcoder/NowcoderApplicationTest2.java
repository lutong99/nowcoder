package org.example.nowcoder;

import org.example.nowcoder.mapper.AlphaDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = NowcoderApplication.class)
public class NowcoderApplicationTest2 implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    void contextLoads() {
        System.out.println("applicationContext = " + applicationContext);
    }

    @Test
    public void test() {
        AlphaDao bean = applicationContext.getBean(AlphaDao.class);
        System.out.println("bean = " + bean);
        String select = bean.select();
        System.out.println("select = " + select);
    }



}
