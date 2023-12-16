package com.boots.external_handlers;

import com.boots.entity.Video;
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

import java.util.List;

@Component
@ExternalTaskSubscription("get_all_videos")
@Slf4j
public class GetAllVideosHandler implements ExternalTaskHandler {

    @Autowired
    private VideoService videoService;
    private Gson gson = new Gson();

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {
        log.info("Called get all video handler");
        List<Video> videos = videoService.getAll();
        String videosJson = gson.toJson(videos);

        VariableMap variables = Variables.createVariables();
        variables.put("videos_response", videosJson);
        extTaskService.complete(extTask,variables);
    }
}
