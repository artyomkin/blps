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

   public void save(CommentDTO commentDTO, Long videoId) throws SystemException, Exception {
       try {
           userTransaction.begin();
           Comment comment = new Comment();
           comment.setVideoId(videoId);
           comment.setAuthor(commentDTO.author);
           comment.setText(commentDTO.text);
           commentRepo.save(comment);
           userTransaction.commit();
       } catch (Exception e) {
           userTransaction.rollback();
           throw new Exception("Transaction rolled back.");
       }
   }

   public void delete(Long comment_id) throws Exception {
         try{
             userTransaction.begin();
             commentRepo.deleteById(comment_id);
             userTransaction.commit();
         } catch (Exception e){
             userTransaction.rollback();
             throw new Exception("Transaction rolled back.");
         }
   }

}
