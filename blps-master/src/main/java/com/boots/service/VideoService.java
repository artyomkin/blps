package com.boots.service;

import com.boots.entity.Video;
import com.boots.repository.LikesRepo;
import com.boots.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private LikeService likeService;

    public List<Video> getAll(){
        List<Video> videos = videoRepo.findAll()
                .stream()
                .map(video -> {
                    video.setLikes(likeService.countLikesByVideoId(video.getId()));
                    video.setDislikes(likeService.countDislikesByVideoId(video.getId()));
                    return video;
                })
                .collect(Collectors.toList());
        return videos;
    }

    public Video getById(Long id){
        Video video = videoRepo.findById(id).orElse(null);
        if (video == null){
            return null;
        }
        video.setLikes(likeService.countLikesByVideoId(video.getId()));
        video.setDislikes(likeService.countDislikesByVideoId(video.getId()));
        return video;
    }

    public List<Video> getAllViewed(Long uid){
        List<Video> viewed = videoRepo.findAllViewed(uid)
                .stream()
                .map(video -> {
                    video.setLikes(likeService.countLikesByVideoId(video.getId()));
                    video.setDislikes(likeService.countDislikesByVideoId(video.getId()));
                    return video;
                })
                .collect(Collectors.toList());
        return viewed;
    }

}
