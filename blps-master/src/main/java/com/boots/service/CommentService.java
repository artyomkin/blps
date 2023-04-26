package com.boots.service;

import com.boots.dto.CommentDTO;
import com.boots.entity.Comment;
import com.boots.exceptions.CommentBodyException;
import com.boots.exceptions.CommentIdException;
import com.boots.exceptions.VideoIdException;
import com.boots.repository.CommentRepo;
import com.boots.repository.UserRepository;
import com.boots.repository.VideoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;

@Slf4j
@Service
public class CommentService {
   @Autowired
   private CommentRepo commentRepo;
   @Autowired
   private UserRepository userRepo;
   @Autowired
   private VideoRepo videoRepo;
   @Resource
   private UserTransaction userTransaction;

   public Comment getById(Long id){
       Comment comment = commentRepo.findById(id).orElse(null);
       return comment;
   }

   public void save(Comment comment){
       commentRepo.save(comment);
   }

   public List<Comment> getAllByVideoId(Long videoId){
       return commentRepo.findByVideoId(videoId);
   }

   public void save(CommentDTO commentDTO, Long videoId) throws SystemException, CommentBodyException, VideoIdException {
       try {
           userTransaction.begin();
           Comment comment = new Comment();
           comment.setVideoId(videoId);
           comment.setAuthor(commentDTO.author);
           comment.setText(commentDTO.text);
           if (commentRepo.existsById(comment.getId())) {
               log.info("Comment with id {} exist.", comment.getId());
               throw new CommentIdException("Comment with id {} exist." + comment.getId());
           }
           if (!videoRepo.existsById(comment.getVideoId())) {
               log.info("Video with id {} hasn't in database", comment.getVideoId());
               throw new VideoIdException("Video with id {} hasn't in database" + comment.getVideoId());
           }
           if (comment.getText().length() == 0) {
               log.info("Comment with body {} has bad length", comment.getText());
               throw new CommentBodyException("Comment with body {} has bad length" + comment.getText());

           }
           log.info("Savin comment " + comment.getText());
           commentRepo.save(comment);
           userTransaction.commit();
       } catch (Exception e) {
           if (e instanceof VideoIdException) {
               throw (VideoIdException) e;
           } else {
               if (e instanceof CommentBodyException) {
                   throw (CommentBodyException) e;
               }
           }
       }
       if (userTransaction != null) {
           userTransaction.rollback();
       }
   }

}
