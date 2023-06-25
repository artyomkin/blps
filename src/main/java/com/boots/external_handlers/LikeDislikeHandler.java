package com.boots.external_handlers;

import com.boots.service.LikeService;
import com.boots.service.ReportService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
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
@ExternalTaskSubscription("like_dislike")
@Slf4j
public class LikeDislikeHandler implements ExternalTaskHandler {
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginModule loginModule;
    private Gson gson = new Gson();

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {
        Long video_id = Long.parseLong(extTask.getVariable("video_id"));
        String rating = extTask.getVariable("rating");
        String username = extTask.getVariable("username");
        String password = extTask.getVariable("password");

        log.info("Called like dislike handler with " + video_id + " " + rating + " " + username + " " + password);

        VariableMap variables = Variables.createVariables();

        if (!loginModule.login(username, password)) {
            variables.put("like_dislike_response", "Unauthorized");
            extTaskService.complete(extTask, variables);
            return;
        }

        if (rating.equals("like")){
            likeService.saveLike(video_id, userService.findByUsername(username).getId());
        } else if (rating.equals("dislike")){
            likeService.saveDislike(video_id, userService.findByUsername(username).getId());
        } else {
            variables.put("like_dislike_response", "Оценка видео должна быть либо like либо dislike");
            extTaskService.complete(extTask, variables);
            return;
        }

        variables.put("like_dislike_response", "OK");
        extTaskService.complete(extTask, variables);
    }
}
