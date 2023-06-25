package com.boots.external_handlers;

import com.boots.dto.CommentDTO;
import com.boots.service.CommentService;
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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Component
@ExternalTaskSubscription("error_log")
@Slf4j
public class ErrorLogHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {
        String message = extTask.getVariable("message");

        log.info("Called error log handler with " + message);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("error_log.txt", "UTF-8");
            writer.println(message);
            writer.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        extTaskService.complete(extTask);
    }
}

