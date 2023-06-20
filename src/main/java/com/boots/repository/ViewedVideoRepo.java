package com.boots.repository;

import com.boots.entity.ViewedVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

@Repository
public interface ViewedVideoRepo extends JpaRepository<ViewedVideo, Long> {
    @Modifying
    @Transactional
    @Query("delete from ViewedVideo w where w.userId = :user_id and w.videoId = :video_id")
    void deleteByUserIdAndVideoId(@Param("user_id") Long user_id, @Param("video_id") Long video_id);
}
