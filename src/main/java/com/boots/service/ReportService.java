package com.boots.service;

import com.boots.dto.ReportDTO;
import com.boots.entity.Report;
import com.boots.entity.Report;
import com.boots.entity.User;
import com.boots.repository.ReportRepo;
import com.boots.repository.ReportRepo;
import com.boots.repository.UserRepository;
import com.boots.repository.VideoRepo;
import com.boots.service.serviceResponses.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.List;

@Service
public class ReportService {
   @Autowired
   private ReportRepo reportRepo;
   @Autowired
   private VideoRepo videoRepo;
   @Autowired
   private INotifier notifier;
   @Autowired
   private UserRepository userRepository;

   public Report getById(Long id){
       Report report = reportRepo.findById(id).orElse(null);
       return report;
   }

   public ReportStatus save(ReportDTO reportDTO, Long videoId, Long reportAuthorUid){
       Boolean alreadyReported = reportRepo.findByVideoId(videoId).stream()
               .map(rep -> rep.getAuthorId())
               .anyMatch(authorId -> authorId == reportAuthorUid);
       if (alreadyReported){
           return ReportStatus.ALREADY_REPORTED;
       }
       Report report = new Report();
       report.setReason(reportDTO.reason);
       report.setText(reportDTO.text);
       report.setAuthorId(reportAuthorUid);
       report.setVideoId(videoId);
       reportRepo.save(report);

       Long videoAuthorId = videoRepo.getById(videoId).getAuthorId();
       User user = userRepository.findById(videoAuthorId).orElse(null);
       if (user == null){
           return ReportStatus.OK;
       }
       String email = user.getEmail();
       Integer reportCnt = countReportsOfUser(videoAuthorId);
       if (reportCnt == 3){
           try {
               notifier.notify(email, "Your account has been reported 3 times. If you get one more report your account will be deleted.");
           } catch (JMSException jmsException){
               System.out.println("Could not send email.");
           }
       } else if (reportCnt > 3){
           userRepository.deleteById(videoAuthorId);
       }
       return ReportStatus.OK;
   }

   public Integer countReportsOfUser(Long userId){
       Integer reportCount = videoRepo.countReportsByAuthorId(userId).size();
       return reportCount;
   }

   public List<Report> getAllByVideoId(Long videoId){
       return reportRepo.findByVideoId(videoId);
   }

}
