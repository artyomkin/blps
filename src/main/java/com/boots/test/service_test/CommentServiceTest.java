package com.boots.test.service_test;
import com.boots.dto.CommentDTO;
import com.boots.entity.Comment;
import com.boots.exceptions.SystemException;
import com.boots.repository.CommentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.UserTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private UserTransaction userTransaction;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getById_ValidId_ReturnsComment() {
        Long commentId = 1L;
        Comment expectedComment = new Comment();
        expectedComment.setId(commentId);

        when(commentRepo.findById(commentId)).thenReturn(Optional.of(expectedComment));

        Comment actualComment = commentService.getById(commentId);

        assertNotNull(actualComment);
        assertEquals(commentId, actualComment.getId());
    }

    @Test
    void getById_InvalidId_ReturnsNull() {
        Long commentId = 1L;

        when(commentRepo.findById(commentId)).thenReturn(Optional.empty());

        Comment actualComment = commentService.getById(commentId);

        assertNull(actualComment);
    }

    @Test
    void getAllByVideoId_ValidVideoId_ReturnsListOfComments() {
        Long videoId = 1L;
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment());
        expectedComments.add(new Comment());

        when(commentRepo.findByVideoId(videoId)).thenReturn(expectedComments);

        List<Comment> actualComments = commentService.getAllByVideoId(videoId);

        assertNotNull(actualComments);
        assertEquals(expectedComments.size(), actualComments.size());
    }

    @Test
    void save_ValidCommentDTOAndVideoId_SavesComment() throws Exception {
        Long videoId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor("User");
        commentDTO.setText("Test comment");

        commentService.save(commentDTO, videoId);

        verify(userTransaction).begin();
        verify(commentRepo).save(any(Comment.class));
        verify(userTransaction).commit();
    }

    @Test
    void delete_ValidCommentId_DeletesComment() throws Exception {
        Long commentId = 1L;

        commentService.delete(commentId);

        verify(userTransaction).begin();
        verify(commentRepo).deleteById(commentId);
        verify(userTransaction).commit();
    }
}

