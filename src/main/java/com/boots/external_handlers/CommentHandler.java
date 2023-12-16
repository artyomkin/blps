package com.boots.external_handlers;

import com.boots.dto.CommentDTO;
import com.boots.dto.ReportDTO;
import com.boots.service.CommentService;
import com.boots.service.ReportService;
import com.boots.service.UserService;
import com.boots.service.serviceResponses.ReportStatus;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.ExceptionUtils;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("comment")
@Slf4j
public class CommentHandler implements ExternalTaskHandler {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginModule loginModule;

    private Gson gson = new Gson();
    @Autowired
    public CommentHandler(CommentService commentService, UserService userService, LoginModule loginModule) {
        this.commentService = commentService;
        this.userService = userService;
        this.loginModule = loginModule;
    }

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {
        String author = extTask.getVariable("author");
        String text = extTask.getVariable("text");
        Long video_id = Long.parseLong(extTask.getVariable("video_id"));

        log.info("Called comment handler with " + author + " " + text + " " + video_id);

        VariableMap variables = Variables.createVariables();

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.author = author;
        commentDTO.text = text;

        try{
            if (!loginModule.isLoggedIn()) {
                variables.put("comment_response", "Unauthorized");
                extTaskService.complete(extTask, variables);
                return;
            }

            commentService.save(commentDTO, videoId);
            variables.put("comment_response", "OK");
            extTaskService.complete(extTask, variables);
        } catch (Exception e){
            variables.put("comment_response", "Не удалось оставить комментарий.");
            extTaskService.handleBpmnError(extTask, e.getMessage(), e.getMessage(), variables);
            return;
        }
    }
}
