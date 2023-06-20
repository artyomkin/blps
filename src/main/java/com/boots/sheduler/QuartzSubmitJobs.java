package com.boots.sheduler;

import com.boots.config.QuartzConfig;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzSubmitJobs {
    private static final String CRON_EVERY_MINUTE = "0 * * ? * *";

    @Bean(name = "paymentVideo")
    public JobDetailFactoryBean jobPaymentVideo() {
        return QuartzConfig.createJobDetail(PaymentScheduler.class, "Money for videos");
    }
    @Bean(name = "paymentVideoTrigger")
    public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("paymentVideo") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, 60000, "Payment videos Trigger");
    }
}


