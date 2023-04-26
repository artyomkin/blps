package com.boots.controller;

import com.boots.dto.ReportDTO;
import com.boots.entity.Report;
import com.boots.entity.Video;
import com.boots.service.ReportService;
import com.boots.service.VideoService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private VideoService videoService;
    private Gson gson = new Gson();

    @GetMapping("/api/v1/reports/{videoId}")
    public ResponseEntity getReports(@PathVariable Long videoId){
        List<Report> reports = reportService.getAllByVideoId(videoId);
        return ResponseEntity.ok(gson.toJson(reports));
    }

    @PostMapping("/api/v1/reports/{videoId}")
    public ResponseEntity addReport(@RequestBody ReportDTO reportDTO, @PathVariable Long videoId){
        Video existingVideo = videoService.getById(videoId);
        if (existingVideo == null){
            return ResponseEntity.badRequest().body("Invalid video id.");
        }
        String author = reportDTO.author;
        String text = reportDTO.text;
        Report report = new Report();
        report.setAuthor(author);
        report.setText(text);
        report.setVideoId(videoId);
        reportService.save(report);
        return ResponseEntity.ok("Reported.");

    }

}
