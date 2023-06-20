package com.boots.repository;

import com.boots.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepo extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByAuthorUserId(Long user_id);
    @Query("select p from Playlist p where p.authorUserId = :user_id and p.isWatchLater = 't'")
    Playlist findByAuthorUserIdAndWatchLater(@Param("user_id") Long user_id);

    @Query("select distinct p from Playlist p join p.videos v where p.id = :playlist_id and p.authorUserId = :user_id")
    Optional<Playlist> findByIdAndAuthorUserIdWithVideos(@Param("playlist_id") Long playlist_id, @Param("user_id") Long user_id);

    @Query("select distinct p from Playlist p where p.id = :playlist_id and p.authorUserId = :user_id")
    Optional<Playlist> findByIdAndAuthorUserId(@Param("playlist_id") Long playlist_id, @Param("user_id") Long user_id);
}