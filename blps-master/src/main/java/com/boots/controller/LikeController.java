package com.boots.controller;

import com.boots.entity.User;
import com.boots.service.LikeService;
import com.boots.service.UserService;
import com.boots.service.serviceResponses.LikeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/like/{videoId}")
    public ResponseEntity<String> addLike(@PathVariable Long videoId, Principal principal) {
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        LikeStatus likeStatus = likeService.saveLike(videoId, uid);
        if (likeStatus.equals(LikeStatus.ALREADY_LIKED)){
            return ResponseEntity.badRequest().body("This user already liked video " + videoId + ".");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_USER)){
            return ResponseEntity.badRequest().body("Invalid user.");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_VIDEO)){
            return ResponseEntity.badRequest().body("Invalid video.");
        }
        else {
            return ResponseEntity.ok("Liked.");
        }
    }

    @GetMapping("/api/v1/dislike/{videoId}")
    public ResponseEntity<String> addDisLike(@PathVariable Long videoId, Principal principal) {
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        LikeStatus likeStatus = likeService.saveDislike(videoId, uid);
        if (likeStatus.equals(LikeStatus.ALREADY_LIKED)){
            return ResponseEntity.badRequest().body("This user already disliked video " + videoId + ".");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_USER)){
            return ResponseEntity.badRequest().body("Invalid user.");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_VIDEO)){
            return ResponseEntity.badRequest().body("Invalid video.");
        }
        else {
            return ResponseEntity.ok("Disliked.");
        }
    }

    @DeleteMapping("/api/v1/like/{videoId}")
    public ResponseEntity<String> undoLike(@PathVariable Long videoId, Principal principal) {
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        LikeStatus likeStatus = likeService.removeLike(videoId, uid);
        if (likeStatus.equals(LikeStatus.NOT_FOUND)){
            return ResponseEntity.badRequest().body("This user did not like video " + videoId + " yet.");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_USER)){
            return ResponseEntity.badRequest().body("Invalid user.");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_VIDEO)){
            return ResponseEntity.badRequest().body("Invalid video.");
        }
        else {
            return ResponseEntity.ok("Removed like.");
        }
    }


    @DeleteMapping("/api/v1/dislike/{videoId}")
    public ResponseEntity<String> undoDisLike(@PathVariable Long videoId, Principal principal) {
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        LikeStatus likeStatus = likeService.removeDislike(videoId, uid);
        if (likeStatus.equals(LikeStatus.NOT_FOUND)){
            return ResponseEntity.badRequest().body("This user did not dislike video " + videoId + " yet.");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_USER)){
            return ResponseEntity.badRequest().body("Invalid user.");
        }
        else if (likeStatus.equals(LikeStatus.INVALID_VIDEO)){
            return ResponseEntity.badRequest().body("Invalid video.");
        }
        else {
            return ResponseEntity.ok("Removed dislike.");
        }
    }
}

