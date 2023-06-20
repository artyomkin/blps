package com.boots.config;

import com.boots.service.PaymentService;
import com.boots.sheduler.PaymentScheduler;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.quartz.spi.JobFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;


@Configuration
public class QuartzConfig {

    // Определяем задание, которое должно быть выполнено
    @Bean
    public JobDetailFactoryBean paymentJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PaymentScheduler.class);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    // Определяем триггер, который определяет расписание выполнения задания
    @Bean
    public CronTriggerFactoryBean paymentJobTrigger(JobDetail paymentJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(paymentJobDetail);
        factoryBean.setCronExpression("0 0 0 * * ?");
        return factoryBean;
    }

    // Конфигурируем планировщик Quartz
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(CronTrigger paymentJobTrigger) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(paymentJobTrigger);
        return schedulerFactoryBean;
    }
}