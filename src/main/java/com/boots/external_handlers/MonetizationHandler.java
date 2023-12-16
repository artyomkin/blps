package com.boots.external_handlers;

import com.boots.dto.CommentDTO;
import com.boots.entity.User;
import com.boots.service.CommentService;
import com.boots.service.PaymentService;
import com.boots.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("monetization")
@Slf4j
public class MonetizationHandler implements ExternalTaskHandler {

    @Autowired
    private PaymentService paymentService;

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {
        log.info("Called monetization handler");

        try{
            paymentService.payment();
        } catch (Exception e){
            extTaskService.handleBpmnError(extTask, e.getMessage(), e.getMessage());
            return;
        }

        extTaskService.complete(extTask);
    }
}
