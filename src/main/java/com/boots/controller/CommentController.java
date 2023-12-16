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
    public ResponseEntity<List<Comment>> getComments(@PathVariable("video_id") String videoId) {
        log.debug("getting all comments of video " + videoId);
        Long vidId;
        try {
            vidId = Long.parseLong(videoId);
        } catch (NumberFormatException e) {
            String errorText = "Incorrect video id.";
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Comment> comments = commentService.getAllByVideoId(vidId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/api/v1/comments/{video_id}")
    public ResponseEntity<Comment> postComments(@RequestBody CommentDTO commentDTO, @PathVariable("video_id") Long videoId) {
        try {
            Comment savedComment = commentService.save(commentDTO, videoId);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        } catch (CommentBodyException | SystemException | VideoIdException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/admin/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment_id") Long commentId) {
        try {
            commentService.delete(commentId);
            return new ResponseEntity<>("Deleted.", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Transaction rolled back.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}