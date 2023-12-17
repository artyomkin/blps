package com.boots.test.service_test;
import com.boots.entity.MyLike;
import com.boots.entity.Dislike;
import com.boots.entity.Video;
import com.boots.repository.DislikesRepo;
import com.boots.repository.LikesRepo;
import com.boots.repository.VideoRepo;
import com.boots.service.LikeService;
import com.boots.service.serviceResponses.LikeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LikeServiceTest {

    @Mock
    private LikesRepo likesRepo;

    @Mock
    private DislikesRepo dislikesRepo;

    @Mock
    private VideoRepo videoRepo;

    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setup() {
        // Настройка объектов, которые могут понадобиться для тестов
    }

    @Test
    public void testSaveLike_WhenUserHasNotLikedAlready_ShouldSaveLike() {
        // Подготовка данных для теста
        Long userId = 1L;
        Long videoId = 1L;

        when(likesRepo.findByVideoIdAndUserId(userId, videoId)).thenReturn(null);
        when(videoRepo.getById(videoId)).thenReturn(new Video());

        LikeStatus status = likeService.saveLike(videoId, userId);

        assertEquals(LikeStatus.OK, status);
        verify(likesRepo, times(1)).save(any(MyLike.class));
    }

    @Test
    public void testSaveDislike_WhenUserHasNotDislikedAlready_ShouldSaveDislike() {
        // Подготовка данных для теста
        Long userId = 1L;
        Long videoId = 1L;

        when(dislikesRepo.findByVideoIdAndUserId(userId, videoId)).thenReturn(null);
        when(videoRepo.getById(videoId)).thenReturn(new Video());

        LikeStatus status = likeService.saveDislike(videoId, userId);

        assertEquals(LikeStatus.OK, status);
        verify(dislikesRepo, times(1)).save(any(Dislike.class));
    }
}

