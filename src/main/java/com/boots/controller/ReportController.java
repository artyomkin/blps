package com.boots.controller;

import com.boots.dto.ReportDTO;
import com.boots.entity.Report;
import com.boots.entity.User;
import com.boots.entity.Video;
import com.boots.service.ReportService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import com.boots.service.serviceResponses.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    @GetMapping("/api/v1/reports/{videoId}")
    public ResponseEntity getReports(@PathVariable Long videoId){
        List<Report> reports = reportService.getAllByVideoId(videoId);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/api/v1/reports/{videoId}")
    public ResponseEntity addReport(Principal principal, @RequestBody ReportDTO reportDTO, @PathVariable Long videoId){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        Video existingVideo = videoService.getById(videoId);
        if (existingVideo == null){
            return ResponseEntity.badRequest().body("Invalid video id.");
        }
        ReportStatus reportStatus = reportService.save(reportDTO, videoId, uid);
        if (reportStatus.equals(ReportStatus.ALREADY_REPORTED)){
           return ResponseEntity.badRequest().body("This user has already reported this video.");
        }
        return ResponseEntity.ok("Reported.");

    }

}
