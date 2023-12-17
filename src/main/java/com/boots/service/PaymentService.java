package com.boots.service;
import com.boots.entity.User;
import com.boots.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    VideoService videoService;
    @Autowired
    UserService userService;
    @Transactional
    public void payment() throws  NullPointerException{
        for (Video video : videoService.getAll()) {
            User user = userService.findUserById(video.getAuthorId());
            int money = videoService.getById(video.getId()).getLikes()*2;
            user.setBalance(user.getBalance()+money);
        }
    }

    public void processPayments(Video video) {
        VideoService videoService = new VideoService();
        UserService  userService = new UserService();
        int money = videoService.getById(video.getId()).getLikes()*2;
        User user = userService.findUserById(video.getAuthorId());
        user.setBalance(user.getBalance()+money);
    }
}
