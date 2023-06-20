package com.boots.service;
import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.entity.ViewedVideo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    @Transactional
    public void processPayments(Video video) {
        VideoService videoService = new VideoService();
        UserService  userService = new UserService();
        int money = videoService.getById(video.getId()).getLikes()*2;
        User user = userService.findUserById(video.getAuthorId());
        user.setBalance(user.getBalance()+money);
    }
}
