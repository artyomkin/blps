package com.boots.service;

import com.boots.entity.ViewedVideo;
import com.boots.repository.ViewedVideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ViewService {
    @Autowired
    private ViewedVideoRepo viewRepo;

    public ViewedVideo save(Long videoId, Long uid){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null){
            return null;
        }

        ViewedVideo view = new ViewedVideo();
        view.setUserId(uid);
        view.setVideoId(videoId);
        view.setViewDate(new Date());
        return viewRepo.save(view);
    }

    public boolean delete(Long videoId, Long uid){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null){
            return false;
        }
        viewRepo.deleteByUserIdAndVideoId(uid, videoId);
        return true;
    }
}
