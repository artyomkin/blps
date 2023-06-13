package com.boots.sheduler;

import com.boots.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduler {

    @Autowired
    private PaymentService paymentService;

    @Scheduled(cron = "0 0 0 * * 1") // Запускать каждую неделю в полночь в понедельник
    public void processPayment() {
        // Логика для зачисления денег на баланс автора за просмотры его новых видео
        paymentService.processPayments();
    }
}
