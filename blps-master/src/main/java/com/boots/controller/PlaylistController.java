package com.boots.controller;

import com.boots.dto.PlaylistDTO;
import com.boots.entity.Playlist;
import com.boots.entity.Video;
import com.boots.security.jwt.JwtTokenProvider;
import com.boots.service.PlaylistService;
import com.boots.service.VideoService;
import com.boots.service.serviceResponses.PlaylistStatus;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PlaylistController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private Gson gson = new Gson();

    @PostMapping("/api/v1/playlists")
    public ResponseEntity createPlaylist(@RequestBody PlaylistDTO playlistDTO, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        String playlistName = playlistDTO.name;
        playlistService.save(playlistName, false, uid);
        return ResponseEntity.ok("Created.");
    }

    @GetMapping("/api/v1/playlists")
    public ResponseEntity allPlaylists(HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        return ResponseEntity.ok(gson.toJson(playlistService.allPlaylistsByUser(uid)));
    }


    @GetMapping("/api/v1/playlists/{id}")
    public ResponseEntity allPlaylists(@PathVariable Long id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        Playlist playlist = playlistService.getByIdAndAuthorUserIdWithVideos(id, uid);
        if (playlist == null){
            return ResponseEntity.badRequest().body("This user does not have playlist " + id + ".");
        }
        return ResponseEntity.ok(gson.toJson(playlist));
    }

    @DeleteMapping("/api/v1/playlists/{id}")
    public ResponseEntity deletePlaylist(@PathVariable Long id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        if (!playlistService.delete(id, uid)){
            return ResponseEntity.badRequest().body("You do not have playlist " + id + ".");
        }
        return ResponseEntity.ok("Deleted.");
    }

    @PutMapping("/api/v1/playlists/{playlist_id}/{video_id}")
    public ResponseEntity addVideo(@PathVariable Long playlist_id, @PathVariable Long video_id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        PlaylistStatus status = playlistService.addVideoToPlaylist(playlist_id, video_id, uid);
        if (status == PlaylistStatus.PLAYLIST_NOT_FOUND){
            return ResponseEntity.badRequest().body("Playlist " + playlist_id + " not found.");
        }
        else if (status == PlaylistStatus.VIDEO_NOT_FOUND){
            return ResponseEntity.badRequest().body("Video " + video_id + " not found.");
        }
        return ResponseEntity.ok("Video " + video_id + " added to playlist " + playlist_id + ".");
    }

    @DeleteMapping("/api/v1/playlists/{playlist_id}/{video_id}")
    public ResponseEntity deleteVideo(@PathVariable Long playlist_id, @PathVariable Long video_id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        PlaylistStatus status = playlistService.removeVideoFromPlaylist(playlist_id, video_id, uid);
        if (status == PlaylistStatus.PLAYLIST_NOT_FOUND){
            return ResponseEntity.badRequest().body("Playlist " + playlist_id + " not found.");
        }
        return ResponseEntity.ok("Ok.");
    }

    @GetMapping("/api/v1/playlists/watchLater")
    public ResponseEntity getWatchLaterPlaylist(HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        playlistService.assertWatchLaterExists(uid);
        return ResponseEntity.ok(gson.toJson(playlistService.getWatchLater(uid)));
    }

    @PutMapping("/api/v1/playlists/watchLater/{video_id}")
    public ResponseEntity addToWatchLater(@PathVariable Long video_id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        playlistService.assertWatchLaterExists(uid);
        PlaylistStatus status = playlistService.addVideoToWatchLater(video_id, uid);
        if (status == PlaylistStatus.PLAYLIST_NOT_FOUND){
            return ResponseEntity.badRequest().body("Watch later playlist is not initialized.");
        }
        else if (status == PlaylistStatus.VIDEO_NOT_FOUND){
            return ResponseEntity.badRequest().body("Video " + video_id + " not found.");
        }
        return ResponseEntity.ok("Added.");
    }

    @DeleteMapping("/api/v1/playlists/watchLater/{video_id}")
    public ResponseEntity removeFromWatchLater(@PathVariable Long video_id, HttpServletRequest req){
        String token = jwtTokenProvider.resolveToken(req);
        if (token == null){
            return new ResponseEntity("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
        Long uid = jwtTokenProvider.getUid(token);
        playlistService.assertWatchLaterExists(uid);
        PlaylistStatus status = playlistService.removeVideoFromWatchLater(video_id, uid);
        if (status == PlaylistStatus.PLAYLIST_NOT_FOUND){
            return ResponseEntity.badRequest().body("Watch later playlist is not initialized.");
        }
        else if (status == PlaylistStatus.VIDEO_NOT_FOUND){
            return ResponseEntity.badRequest().body("Video " + video_id + " is not in watch later playlist.");
        }
        return ResponseEntity.ok("Ok.");
    }
}
