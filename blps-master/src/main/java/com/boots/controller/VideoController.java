package com.boots.controller;


import com.boots.entity.Playlist;
import com.boots.entity.Video;
import com.boots.security.jwt.JwtTokenProvider;
import com.boots.service.PlaylistService;
import com.boots.service.VideoService;
import com.boots.service.ViewService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class VideoController {

    //@Autowired
    //private StreamingService service;

    @Autowired
    private VideoService videoService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private Gson gson = new Gson();

    //@GetMapping(value = "video/{id}", produces = "video/mp4")
    //public Mono<Resource> getVideos(@PathVariable Long id, @RequestHeader("Range") String range){
    //    return service.getVideo(id);
    //}

    @GetMapping("/api/v1/videos")
    public ResponseEntity allVideos(){
        List<Video> videos = videoService.getAll();
        return ResponseEntity.ok(gson.toJson(videos));
    }

    @GetMapping("/api/v1/videos/{id}")
    public ResponseEntity getVideo(@PathVariable Long id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token != null){
            Long uid = jwtTokenProvider.getUid(token);
            viewService.save(id, uid);
            playlistService.assertWatchLaterExists(uid);
            playlistService.removeVideoFromWatchLater(id, uid);
        }
        Video video = videoService.getById(id);
        if (video == null){
            return ResponseEntity.badRequest().body("Video " + id + " not found.");
        }

        return ResponseEntity.ok(gson.toJson(video));
    }

    @GetMapping("/api/v1/videos/{id}/share")
    public ResponseEntity shareVideo(@PathVariable Long id){
        Video video = videoService.getById(id);
        if (video == null){
            return ResponseEntity.badRequest().body("Video " + id + " not found.");
        }
        return ResponseEntity.ok(gson.toJson(video.getLink()));
    }

    @GetMapping("/api/v1/videos/viewed")
    public ResponseEntity viewedVideos(HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        List<Video> viewedVideos = videoService.getAllViewed(uid);
        return ResponseEntity.ok(gson.toJson(viewedVideos));
    }

    @DeleteMapping("/api/v1/videos/{id}/viewed")
    public ResponseEntity removeFromViewed(@PathVariable Long id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        if (!viewService.delete(id, uid)){
            return ResponseEntity.badRequest().body("Unauthorized user cannot remove videos from viewed.");
        }
        return ResponseEntity.ok("Removed.");
    }
}
