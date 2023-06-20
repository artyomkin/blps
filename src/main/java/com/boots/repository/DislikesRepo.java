package com.boots.repository;

import com.boots.entity.Dislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DislikesRepo extends JpaRepository<Dislike, Long> {

    @Query("select count(d) from Dislike d where d.videoId = :video_id")
    Integer countByVideoId(@Param("video_id") Long videoId);

    @Query("select d from Dislike d where d.userId = :user_id and d.videoId = :video_id")
    Dislike findByVideoIdAndUserId(@Param("user_id") Long user_id, @Param("video_id") Long video_id);
}
