package com.boots.controller;


import com.boots.dto.VideoDTO;
import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.security.Principal;
import java.util.List;

@RestController
public class VideoController {

    //@Autowired
    //private StreamingService service;
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private INotifier __testNotifier;

    //@GetMapping(value = "video/{id}", produces = "video/mp4")
    //public Mono<Resource> getVideos(@PathVariable Long id, @RequestHeader("Range") String range){
    //    return service.getVideo(id);
    //}

    @GetMapping("/api/v1/videos")
    public ResponseEntity allVideos(Authentication authentication){
        List<Video> videos = videoService.getAll();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/api/v1/videos/{id}")
    public ResponseEntity getVideo(@PathVariable Long id, Principal principal){
        String username = principal.getName();
        if (username != null){
            User user = userService.findByUsername(username);
            if (user != null) {
                Long uid = user.getId();
                viewService.save(id, uid);
                playlistService.assertWatchLaterExists(uid);
                playlistService.removeVideoFromWatchLater(id, uid);
            } else {
                return ResponseEntity.badRequest().body("Unauthorized.");
            }
        }
        Video video = videoService.getById(id);
        if (video == null){
            return ResponseEntity.badRequest().body("Video " + id + " not found.");
        }

        return ResponseEntity.ok(video);
    }

    @GetMapping("/api/v1/videos/{id}/share")
    public ResponseEntity shareVideo(@PathVariable Long id){
        Video video = videoService.getById(id);
        if (video == null){
            return ResponseEntity.badRequest().body("Video " + id + " not found.");
        }
        return ResponseEntity.ok(video.getLink());
    }

    @GetMapping("/api/v1/videos/viewed")
    public ResponseEntity viewedVideos(Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        List<Video> viewedVideos = videoService.getAllViewed(uid);
        return ResponseEntity.ok(viewedVideos);
    }

    @PostMapping("/api/v1/videos")
    public ResponseEntity addVideo(@RequestBody VideoDTO videoDTO, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();

        videoService.save(videoDTO, uid);
        return ResponseEntity.ok("Added video.");
    }

    @DeleteMapping("/api/v1/videos/{id}/viewed")
    public ResponseEntity removeFromViewed(@PathVariable Long id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        if (!viewService.delete(id, uid)){
            return ResponseEntity.badRequest().body("Unauthorized user cannot remove videos from viewed.");
        }
        return ResponseEntity.ok("Removed.");
    }
}
