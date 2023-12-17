package com.boots.config;

import com.boots.sheduler.PaymentScheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;


import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Properties;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class QuartzConfig {
    private ApplicationContext applicationContext;
    private DataSource dataSource;

}