package com.boots.service;
import com.boots.entity.User;
import com.boots.entity.Video;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    public void payment() throws  NullPointerException{
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
            processPayments(video);
        }
    }
    @Transactional
    public void processPayments(Video video) {
        VideoService videoService = new VideoService();
        UserService  userService = new UserService();
        int money = videoService.getById(video.getId()).getLikes()*2;
        User user = userService.findUserById(video.getAuthorId());
        user.setBalance(user.getBalance()+money);
    }
}
