package com.boots.service;

import com.boots.entity.Playlist;
import com.boots.entity.Video;
import com.boots.repository.PlaylistRepo;
import com.boots.service.serviceResponses.PlaylistStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlaylistService {
    @Autowired
    private PlaylistRepo playlistRepo;
    @Autowired
    private VideoService videoService;

    public boolean save(String playlistName, boolean isWatchLater, Long uid){
        if (isWatchLater){
            Playlist watchLater = getWatchLater(uid);
            if (watchLater != null){
                return false;
            }
        }
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setAuthorUserId(uid);
        playlist.setWatchLater(isWatchLater);
        playlistRepo.save(playlist);
        return true;
    }

    public boolean delete(Long id, Long uid){
        Playlist playlist = playlistRepo.findById(id).orElse(null);
        if (playlist == null){
            return false;
        }
        if (playlist.getAuthorUserId() != uid){
            return false;
        }
        playlistRepo.deleteById(id);
        return true;
    }

    public PlaylistStatus addVideoToPlaylist(Long playlist_id, Long video_id, Long uid){
        Playlist playlist = playlistRepo.findById(playlist_id).orElse(playlistRepo.findById(playlist_id).orElse(null));
        if (playlist == null){
            log.info("Playlist " + playlist_id + " not found.");
            return PlaylistStatus.PLAYLIST_NOT_FOUND;
        }
        else if (playlist.getAuthorUserId() != uid){
            log.info("Playlist author " + playlist.getAuthorUserId() + " doesnt match with user id " + uid +  ".");
            return PlaylistStatus.PLAYLIST_NOT_FOUND;
        }
        Video video = videoService.getById(video_id);
        if (video == null){
            return PlaylistStatus.VIDEO_NOT_FOUND;
        }

        List<Video> videos = playlist.getVideos();
        videos.add(video);
        playlist.setVideos(videos);
        playlistRepo.save(playlist);
        return PlaylistStatus.OK;
    }

    public PlaylistStatus removeVideoFromPlaylist(Long playlist_id, Long video_id, Long uid){
        Playlist playlist = playlistRepo.findById(playlist_id).orElse(null);
        if (playlist == null || playlist.getAuthorUserId() != uid){
            return PlaylistStatus.PLAYLIST_NOT_FOUND;
        }

        List<Video> videos = playlist.getVideos();
        if (videos != null){
            videos = videos.stream()
                    .filter(video -> video.getId() != video_id)
                    .collect(Collectors.toList());
            playlist.setVideos(videos);
        }
        playlistRepo.save(playlist);
        return PlaylistStatus.OK;
    }

    public List<Playlist> allPlaylistsByUser(Long uid){
        List<Playlist> playlists = playlistRepo.findAllByAuthorUserId(uid);
        return playlists;
    }

    public Playlist getWatchLater(Long uid){
        Playlist playlist = playlistRepo.findByAuthorUserIdAndWatchLater(uid);
        if (playlist == null){
            log.info("Playlist watch later not found. User " + uid);
            return null;
        }
        log.info("Playlist watch later found. User " + uid);
        return playlist;
    }

    public boolean assertWatchLaterExists(Long uid){
        if (getWatchLater(uid) != null){
            log.info("Playlist watch later already exists");
            return true;
        }
        log.info("Playlist watch later doesn't exist");
        Playlist watchLater = new Playlist();
        watchLater.setWatchLater(true);
        watchLater.setAuthorUserId(uid);
        watchLater.setName("Watch later");
        playlistRepo.save(watchLater);
        return true;
    }

    public PlaylistStatus addVideoToWatchLater(Long id, Long uid){
        log.info("Want to add " + id + " video to watch later.");
        Playlist playlist = getWatchLater(uid);
        if (playlist == null){
            log.info("Watch later is null.");
            return PlaylistStatus.PLAYLIST_NOT_FOUND;
        }
        return addVideoToPlaylist(playlist.getId(), id, uid);
    }

    public PlaylistStatus removeVideoFromWatchLater(Long id, Long uid){
        Playlist playlist = getWatchLater(uid);
        if (playlist == null){
            return PlaylistStatus.PLAYLIST_NOT_FOUND;
        }
        return removeVideoFromPlaylist(playlist.getId(), id, uid);
    }


    public Playlist getByIdAndAuthorUserIdWithVideos(Long id, Long uid){
        Playlist playlist = playlistRepo.findByIdAndAuthorUserIdWithVideos(id, uid).orElse(
                playlistRepo.findByIdAndAuthorUserId(id, uid).orElse(null)
        );
        return playlist;
    }
}
