package com.boots.repository;

import com.boots.entity.MyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepo  extends JpaRepository<MyLike, Long> {
    @Query("select count(l) from MyLike l where l.videoId = :video_id")
    Integer countByVideoId(@Param("video_id") Long videoId);
    @Query("select l from MyLike l where l.userId = :user_id and l.videoId = :video_id")
    MyLike findByVideoIdAndUserId(@Param("user_id") Long user_id, @Param("video_id") Long video_id);
}