package com.boots.sheduler;

import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.service.PaymentService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@DisallowConcurrentExecution
public class PaymentScheduler implements Job {

    @Override
    public void execute(JobExecutionContext context){
    //    log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
    //    paymentService.payment();
    //    log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
