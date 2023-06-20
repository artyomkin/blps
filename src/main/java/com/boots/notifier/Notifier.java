package com.boots.notifier;

import com.boots.service.INotifier;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class Notifier implements INotifier {

    private final String URL = "tcp://localhost:61616";
    @Override
    public void notify(String email, String message) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("pre_ban_notification");
        MessageProducer producer = session.createProducer(destination);
        TextMessage textMessage = session.createTextMessage("Your account got reported 3 times. If you get one more report we will delete your account");
        producer.send(textMessage);
        connection.close();
    }
}
