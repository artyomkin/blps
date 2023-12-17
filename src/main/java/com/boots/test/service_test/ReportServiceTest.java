package com.boots.test.service_test;
import com.boots.dto.ReportDTO;
import com.boots.entity.Report;
import com.boots.repository.ReportRepo;
import com.boots.repository.VideoRepo;
import com.boots.service.serviceResponses.ReportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private VideoRepo videoRepo;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveNewReport() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReason("Inappropriate content");
        reportDTO.setText("Contains violence");

        Long videoId = 1L;
        Long reportAuthorUid = 2L;

        when(reportRepo.findByVideoId(videoId)).thenReturn(new ArrayList<>());
        when(videoRepo.getById(videoId)).thenReturn(new Video());

        ReportStatus reportStatus = reportService.save(reportDTO, videoId, reportAuthorUid);

        assertEquals(ReportStatus.OK, reportStatus);

        verify(reportRepo, times(1)).save(any(Report.class));
    }

    @Test
    void testSaveAlreadyReported() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReason("Inappropriate content");
        reportDTO.setText("Contains violence");

        Long videoId = 1L;
        Long reportAuthorUid = 2L;

        List<Report> existingReports = new ArrayList<>();
        existingReports.add(new Report());
        when(reportRepo.findByVideoId(videoId)).thenReturn(existingReports);

        ReportStatus reportStatus = reportService.save(reportDTO, videoId, reportAuthorUid);

        assertEquals(ReportStatus.ALREADY_REPORTED, reportStatus);

        verify(reportRepo, never()).save(any(Report.class));
    }

    // Add more test cases to cover other scenarios
}
