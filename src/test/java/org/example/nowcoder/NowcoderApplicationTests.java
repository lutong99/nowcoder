package org.example.nowcoder;

import org.example.nowcoder.dao.AlphaDao;
import org.example.nowcoder.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = NowcoderApplication.class)
public class NowcoderApplicationTests {

    private ApplicationContext applicationContext;

    public NowcoderApplicationTests(@Autowired ApplicationContext applicationContext) {
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

    @Test
    public void test2() {
        AlphaService alphaService = applicationContext.getBean(AlphaService.class);
        System.out.println("alphaService = " + alphaService);
    }

    @Test
    public void test3() {
        SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
        String date = simpleDateFormat.format(new Date());
        System.out.println("date = " + date);

    }


}
