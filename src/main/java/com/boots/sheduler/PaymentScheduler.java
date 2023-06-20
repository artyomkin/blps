package com.boots.sheduler;

import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.service.PaymentService;
import com.boots.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class PaymentScheduler implements Job {

    @Autowired
    private PaymentService paymentService;

    /*@Scheduled(cron = "0 0 0 * * 1") // Запускать каждую неделю в полночь в понедельник
    public void processPayment(User user) {
        // Логика для зачисления денег на баланс автора за просмотры его новых видео
        VideoService videoService = new VideoService();
        List<Video> videos = null;
        videoService.getAll().stream().filter(video -> video.getAuthorId()==user.getId()).forEach(video-> videos.add(video));
        for(int i=0; i<videos.size(); i++){
        paymentService.processPayments(videos.get(i));
        }
    }*/

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
