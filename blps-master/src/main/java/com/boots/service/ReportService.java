package com.boots.service;

import com.boots.entity.Report;
import com.boots.entity.Report;
import com.boots.repository.ReportRepo;
import com.boots.repository.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
   @Autowired
   private ReportRepo reportRepo;

   public Report getById(Long id){
       Report report = reportRepo.findById(id).orElse(null);
       return report;
   }

   public void save(Report report){
       reportRepo.save(report);
   }

   public List<Report> getAllByVideoId(Long videoId){
       return reportRepo.findByVideoId(videoId);
   }

}
