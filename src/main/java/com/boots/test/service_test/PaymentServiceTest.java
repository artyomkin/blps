package com.boots.test.service_test;
import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.service.PaymentService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentServiceTest {

    @Mock
    private VideoService videoService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        // Настройка объектов, которые могут понадобиться для тестов
    }

    @Test
    public void testProcessPayments_ShouldUpdateUserBalanceCorrectly() {
        Video video = new Video();
        video.setId(1L);
        video.setLikes(10);
        video.setAuthorId(1L);

        User user = new User();
        user.setId(1L);
        user.setBalance(100);

        when(videoService.getById(video.getId())).thenReturn(video);
        when(userService.findUserById(video.getAuthorId())).thenReturn(user);

        paymentService.processPayments(video);

        verify(userService, times(1)).findUserById(video.getAuthorId());
        verify(userService, times(1)).save(user);
        // Проверьте, что баланс пользователя был увеличен на правильную сумму
        assert(user.getBalance() == 120); // Пример, подставьте правильное ожидаемое значение
    }

    // Добавьте другие тесты...
}

