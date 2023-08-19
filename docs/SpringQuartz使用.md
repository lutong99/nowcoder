## SpringQuartz 使用

### 导包

先导入 `spring-boot-starter-quartz` 这个包，这个包一般不需要添加`version`，因为SpringBoot 已经预设了 `version`

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

### 在数据库中创建表

在数据库中创建表存储任务

因为可以分布式部署，所以存到数据库中，方便调度

### 创建一个工作调试的类

这个类需要实现 `org.quartz.Job` 接口，实现`public void execute(JobExecutionContext context) throws JobExecutionException`
这个方法，这个方法就是要进行的任务

```java
public class AlphaJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(Thread.currentThread().getName() + ": Hello world quartz programming");
    }
}

```

### 进行配置，写入到数据库中

进行一个 Configuration 的类的配置

配置类 和 Spring Boot 的配置文件，会将调度的任务写入到数据库中

```java
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();

        jobDetailFactoryBean.setJobClass(AlphaJob.class);
        jobDetailFactoryBean.setName("alphaJob");
        jobDetailFactoryBean.setGroup("alphaJobGroup");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setRequestsRecovery(true);

        return jobDetailFactoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) {
        SimpleTriggerFactoryBean triggerFactoryBean = new SimpleTriggerFactoryBean();
        
        triggerFactoryBean.setJobDetail(alphaJobDetail);
        triggerFactoryBean.setGroup("alphaTriggerGroup");
        triggerFactoryBean.setName("alphaTrigger");
        triggerFactoryBean.setRepeatInterval(3000);
        triggerFactoryBean.setJobDataAsMap(new JobDataMap());

        return triggerFactoryBean;
    }

}
```

> 注意：FactoryBean 可以简化Bean 的实例化过程 \
> 1、通过FactoryBean 封装 Bean 的实例化过程 \
> 2、将FactoryBean 装配到 Spring 容器中 \
> 3、将FactoryBean 注入给其他的 Bean \
> 4、这个其他的Bean 就是 FactoryBean 所管理的对象的实例

当服务器启动时，则会按照我们配置的 Configuration 来进行任务的调度，默认是将任务写到内存中，如果需要将任务写入到数据库中，则需要进行配置

### SpringBoot 配置

```yaml
spring:
  quartz:
  job-store-type: jdbc
  properties:
    org.quartz.scheduler.instanceId: AUTO
    # springboot>2.5.6后使用这个 org.springframework.scheduling.quartz.LocalDataSourceJobStore，以前使用的 org.quartz.impl.jdbcjobstore.JobStoreTX
    org.quartz.jobStore.class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
    org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
    org.quartz.jobStore.isClustered: true
    org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
    org.quartz.threadPool.threadCount: 5
  scheduler-name: communityNowcoderScheduler
```

### 删除一个任务

```java
class QuartzTest {
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
```

