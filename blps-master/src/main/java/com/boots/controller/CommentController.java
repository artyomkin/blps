package com.boots.controller;

import com.boots.dto.CommentDTO;
import com.boots.entity.Comment;
import com.boots.exceptions.CommentBodyException;
import com.boots.exceptions.VideoIdException;
import com.boots.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.SystemException;
import java.util.List;

@Slf4j
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/api/v1/comments/{video_id}")
    public ResponseEntity<String> getComments(@PathVariable("video_id") String videoId) {
        log.debug("getting all comments of video " + videoId);
        Long vidId = 0L;
        try {
            vidId = Long.parseLong(videoId);
        } catch (NumberFormatException e) {
            String errorText = "Incorrect video id.";
            return new ResponseEntity(errorText, HttpStatus.BAD_REQUEST);
        }
        List<Comment> comments = commentService.getAllByVideoId(vidId);
        return new ResponseEntity(comments, HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/v1/comments/{video_id}")
    public ResponseEntity postComments(@RequestBody CommentDTO commentDTO, @PathVariable("video_id") Long video_id) throws CommentBodyException, SystemException, VideoIdException {
        try {
            commentService.save(commentDTO, video_id);
            return new ResponseEntity(commentDTO, HttpStatus.ACCEPTED);
        } catch (Exception e){
            return new ResponseEntity("Comment transaction rolled back.",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/admin/comments/{comment_id}")
    public ResponseEntity deleteComment(@PathVariable("comment_id") Long comment_id) throws Exception{
        try{
            commentService.delete(comment_id);
            return new ResponseEntity("Deleted.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity("Transaction rolled back.", HttpStatus.ACCEPTED);
        }
    }

}