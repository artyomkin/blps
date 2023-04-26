package com.boots.service;

import com.boots.entity.MyLike;
import com.boots.entity.Dislike;
import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.repository.DislikesRepo;
import com.boots.repository.LikesRepo;
import com.boots.service.serviceResponses.LikeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikesRepo likesRepo;
    @Autowired
    private DislikesRepo dislikesRepo;
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    private LikeStatus validate(Long videoId, Long userId){
        if (userId == null){
            return LikeStatus.INVALID_USER;
        }
        Video existingVideo = videoService.getById(videoId);
        if (existingVideo == null){
            return LikeStatus.INVALID_VIDEO;
        }
        return LikeStatus.OK;
    }
    public LikeStatus saveLike(Long videoId, Long userId) {
        LikeStatus validateStatus = this.validate(videoId, userId);

        if (!validateStatus.equals(LikeStatus.OK)){
            return validateStatus;
        }

        MyLike existingMyLike = likesRepo.findByVideoIdAndUserId(userId, videoId);
        if (existingMyLike != null){
            return LikeStatus.ALREADY_LIKED;
        }

        MyLike newMyLike = new MyLike();
        newMyLike.setUserId(userId);
        newMyLike.setVideoId(videoId);
        likesRepo.save(newMyLike);
        return LikeStatus.OK;
    }

    public LikeStatus saveDislike(Long videoId, Long userId){
        LikeStatus validateStatus = this.validate(videoId, userId);

        if (!validateStatus.equals(LikeStatus.OK)){
            return validateStatus;
        }
        Dislike existingDislike = dislikesRepo.findByVideoIdAndUserId(userId, videoId);
        if (existingDislike != null){
            return LikeStatus.ALREADY_LIKED;
        }

        Dislike newDislike = new Dislike();
        newDislike.setUserId(userId);
        newDislike.setVideoId(videoId);
        dislikesRepo.save(newDislike);
        return LikeStatus.OK;
    }

    public LikeStatus removeLike(Long videoId, Long userId){
        LikeStatus validateStatus = this.validate(videoId, userId);

        if (!validateStatus.equals(LikeStatus.OK)){
            return validateStatus;
        }

        MyLike existingMyLike = likesRepo.findByVideoIdAndUserId(userId, videoId);
        if (existingMyLike == null){
            return LikeStatus.NOT_FOUND;
        }
        likesRepo.deleteById(existingMyLike.getId());
        return LikeStatus.OK;
    }

    public LikeStatus removeDislike(Long videoId, Long userId){
        LikeStatus validateStatus = this.validate(videoId, userId);

        if (!validateStatus.equals(LikeStatus.OK)){
            return validateStatus;
        }


        Dislike existingDislike = dislikesRepo.findByVideoIdAndUserId(userId, videoId);
        if (existingDislike == null){
            return LikeStatus.NOT_FOUND;
        }

        dislikesRepo.deleteById(existingDislike.getId());
        return LikeStatus.OK;
    }

    public Integer countLikesByVideoId(Long videoId){
        return likesRepo.countByVideoId(videoId);
    }
    public Integer countDislikesByVideoId(Long videoId) {
        return dislikesRepo.countByVideoId(videoId);
    }


}

