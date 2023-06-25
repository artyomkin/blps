package com.boots.external_handlers;

import com.boots.dto.RegDTO;
import com.boots.dto.ReportDTO;
import com.boots.entity.Video;
import com.boots.service.ReportService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import com.boots.service.serviceResponses.ReportStatus;
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
@ExternalTaskSubscription("add_report")
@Slf4j
public class AddReportHandler implements ExternalTaskHandler {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginModule loginModule;

    private Gson gson = new Gson();

    @Override
    public void execute(ExternalTask extTask, ExternalTaskService extTaskService) {
        String reason = extTask.getVariable("reason");
        String text = extTask.getVariable("text");
        String video_id = extTask.getVariable("video_id");
        String username = extTask.getVariable("username");
        String password = extTask.getVariable("password");

        log.info("Called get add report handler with " + reason + " " + text + " " + username + " " + password + " " + video_id);

        VariableMap variables = Variables.createVariables();
        if (!loginModule.login(username, password)) {
            variables.put("report_response", "Unauthorized");
            extTaskService.complete(extTask, variables);
            return;
        }

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.reason = reason;
        reportDTO.text = text;

        ReportStatus reportStatus = reportService.save(reportDTO, Long.parseLong(video_id), userService.findByUsername(username).getId());
        String report_response = "OK";
        if (reportStatus == ReportStatus.ALREADY_REPORTED){
            report_response = "User " + username + " has already reported video " + video_id;
        }
        variables.put("report_response", report_response);
        extTaskService.complete(extTask,variables);
    }
}
