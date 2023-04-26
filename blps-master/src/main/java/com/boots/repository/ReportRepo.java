package com.boots.repository;

import com.boots.entity.Comment;
import com.boots.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepo extends JpaRepository<Report, Long> {
    public List<Report> findByVideoId(Long videoId);
}
