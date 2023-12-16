package com.boots.external_handlers;

import com.boots.dto.VideoDTO;
import com.boots.entity.Video;
import com.boots.jaas.JaasLoginModule;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.SpringTopicSubscription;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.spring.event.SubscriptionInitializedEvent;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ExternalTaskSubscription("add_video")
@Slf4j
public class AddVideoHandler implements ExternalTaskHandler{

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginModule loginModule;
    @Autowired
    public AddVideoHandler(VideoService videoService, UserService userService, LoginModule loginModule) {
        this.videoService = videoService;
        this.userService = userService;
        this.loginModule = loginModule;
    }

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {

        String title = extTask.getVariable("title");
        String link = extTask.getVariable("link");
        String username = extTask.getVariable("username");
        String password = extTask.getVariable("password");

        log.info("Called add video handler with " + title + " " + link + " " + username + " " + password);

        VariableMap variables = Variables.createVariables();

        if (!loginModule.login(username, password)) {
            variables.put("add_video_response", "Unauthorized");
            extTaskService.complete(extTask, variables);
            return;
        }

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.link = link;
        videoDTO.title = title;
        long userId = userService.findByUsername(username).getId();
        videoService.save(videoDTO, userId);

        variables.put("add_video_response", "Added video " + title);

        extTaskService.complete(extTask, variables);
    };
}