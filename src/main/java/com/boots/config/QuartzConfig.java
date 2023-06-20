package com.boots.config;
import com.boots.sheduler.PaymentScheduler;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail videoViewsJobDetail() {
        return JobBuilder.newJob(PaymentScheduler.class)
                .withIdentity("videoViewsJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger videoViewsTrigger() {
        return (Trigger) TriggerBuilder.newTrigger()
                .forJob(videoViewsJobDetail())
                .withIdentity("videoViewsTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1 * ?")).build();
    }
}
