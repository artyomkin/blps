package com.boots.service;

import com.boots.dto.VideoDTO;
import com.boots.entity.Video;
import com.boots.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(VideoDTO videoDTO, Long authorId){
        Video video = new Video();
        video.setLink(videoDTO.link);
        video.setTitle(videoDTO.title);
        video.setAuthorId(authorId);
        videoRepo.save(video);
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
