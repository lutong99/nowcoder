package org.example.nowcoder.config;

import org.example.nowcoder.quartz.AlphaJob;
import org.example.nowcoder.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {

    // @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();

        jobDetailFactoryBean.setJobClass(AlphaJob.class);
        jobDetailFactoryBean.setName("alphaJob");
        jobDetailFactoryBean.setGroup("alphaJobGroup");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setRequestsRecovery(true);

        return jobDetailFactoryBean;
    }

    // @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) {
        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();

        triggerFactoryBean.setJobDetail(alphaJobDetail);
        triggerFactoryBean.setGroup("alphaTriggerGroup");
        triggerFactoryBean.setName("alphaTrigger");
        triggerFactoryBean.setRepeatInterval(3000);
        triggerFactoryBean.setJobDataAsMap(new JobDataMap());

        return triggerFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean postScoreRefreshJob() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();

        jobDetailFactoryBean.setJobClass(PostScoreRefreshJob.class);
        jobDetailFactoryBean.setName("postScoreRefreshJob");
        jobDetailFactoryBean.setGroup("postScoreRefreshJobGroup");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setRequestsRecovery(true);

        return jobDetailFactoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshJobTrigger(JobDetail postScoreRefreshJob) {
        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();

        triggerFactoryBean.setJobDetail(postScoreRefreshJob);
        triggerFactoryBean.setGroup("postScoreRefreshJobTriggerGroup");
        triggerFactoryBean.setName("postScoreRefreshJobTrigger");
        triggerFactoryBean.setRepeatInterval(1000 * 60 * 1); // 5 分钟统计一次
        triggerFactoryBean.setJobDataAsMap(new JobDataMap());
        return triggerFactoryBean;
    }

}
