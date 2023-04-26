package com.boots.repository;

import com.boots.entity.Playlist;
import com.boots.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepo extends JpaRepository<Video, Long> {
    @Query("select v from Video v, ViewedVideo w where w.videoId = v.id")
    List<Video> findAllViewed(@Param("user_id") Long user_id);

}