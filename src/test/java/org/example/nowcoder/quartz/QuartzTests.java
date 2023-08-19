package org.example.nowcoder.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuartzTests {

    @Autowired
    private Scheduler scheduler;

    @Test
    public void testDeleteJob() {
        try {
            boolean alphaJob = scheduler.deleteJob(new JobKey("alphaJob", "alphaJobGroup"));
            System.out.println("alphaJob = " + alphaJob);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

}
