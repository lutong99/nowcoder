package org.example.nowcoder.api;

import lombok.extern.slf4j.Slf4j;
import org.example.nowcoder.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class TheadTests {

    /*
        测试 JDK 和 Spring 提供的线程池
            JDK：
                普通
                定时任务
            Spring 需要在配置文件中配置
                普通
                定时任务：还需要在Configuration中配置
            Spring 可以使用注解来简化操作
                @Async
                @Schedule...
     */

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testThreadPoolSchedulingExecutorSimple() {
        sleep(10000);
    }

    @Test
    public void testThreadPoolTaskExecutorSimple() {

        for (int i = 0; i < 10; i++) {
            alphaService.testThreadPoolExecutor();
        }

        sleep(100000);

    }

    @Test
    public void testThreadPoolTaskExecutor() {
        Runnable task = () -> log.debug("Hello testThreadPoolTaskExecutor");

        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.submit(task);
        }

        sleep(30000);

    }

    @Test
    public void testThreadPoolTaskScheduler() {
        Runnable task = () -> log.debug("Hello testThreadPoolTaskScheduler");

        threadPoolTaskScheduler.scheduleWithFixedDelay(task, new Date(System.currentTimeMillis() + 5000), 1000);

        sleep(300000);

    }

    public void sleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFixedThreadPool() {
        Runnable task = () -> log.debug("Hello FixedThreadPool");

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }

        sleep(30000);
    }

    @Test
    public void testScheduledThreadPool() {
        Runnable task = () -> log.debug("Hello testScheduledThreadPool");

        scheduledExecutorService.scheduleAtFixedRate(task, 5000, 1000, TimeUnit.MILLISECONDS);

        sleep(300000);

    }

}
