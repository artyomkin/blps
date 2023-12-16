package com.boots.notifier;

import com.boots.dto.NotificationDTO;
import com.boots.service.INotifier;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Map;


@Component
public class Notifier implements INotifier {

    @Autowired
    JmsTemplate jmsTemplate;
    private Gson gson = new Gson();
    private final String URL = "tcp://localhost:61616";
    @Override
    public void notifyUser(String email, String message) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEmail(email);
        notificationDTO.setMessage(message);

        String jsonMessage = gson.toJson(notificationDTO);

        try {
            jmsTemplate.send("pre_ban_notification", session -> {
                TextMessage textMessage = session.createTextMessage(jsonMessage);
                return textMessage;
            });
        } catch (JmsException e) {
            // Обработка исключений
            e.printStackTrace();
        }
    }
}
