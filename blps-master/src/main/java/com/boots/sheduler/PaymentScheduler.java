package com.boots.sheduler;

import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.service.PaymentService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentScheduler implements Job {

    private final PaymentService paymentService;

    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // Логика для зачисления денег на баланс автора за просмотры его новых видео
        UserService userService = new UserService();
        VideoService videoService = new VideoService();
        List<Video> videos = new ArrayList<>();
        for (User user : userService.allUsers()) {
            videoService.getAll().stream()
                    .filter(video -> video.getAuthorId().equals(user.getId()))
                    .forEach(videos::add);
        }
        for (Video video : videos) {
            paymentService.processPayments(video);
        }
    }
}
