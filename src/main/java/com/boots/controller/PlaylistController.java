package com.boots.controller;

import com.boots.dto.PlaylistDTO;
import com.boots.entity.Playlist;
import com.boots.entity.User;
import com.boots.service.PlaylistService;
import com.boots.service.UserService;
import com.boots.service.VideoService;
import com.boots.service.serviceResponses.PlaylistStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
public class PlaylistController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/playlists")
    public ResponseEntity createPlaylist(@RequestBody PlaylistDTO playlistDTO, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        String playlistName = playlistDTO.name;
        playlistService.save(playlistName, false, uid);
        return ResponseEntity.ok("Created.");
    }

    @GetMapping("/api/v1/playlists")
    public ResponseEntity allPlaylists(Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        return ResponseEntity.ok(playlistService.allPlaylistsByUser(uid));
    }


    @GetMapping("/api/v1/playlists/{id}")
    public ResponseEntity allPlaylists(@PathVariable Long id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        Playlist playlist = playlistService.getByIdAndAuthorUserIdWithVideos(id, uid);
        if (playlist == null){
            return ResponseEntity.badRequest().body("This user does not have playlist " + id + ".");
        }
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/api/v1/playlists/{id}")
    public ResponseEntity deletePlaylist(@PathVariable Long id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        if (!playlistService.delete(id, uid)){
            return ResponseEntity.badRequest().body("You do not have playlist " + id + ".");
        }
        return ResponseEntity.ok("Deleted.");
    }

    @PutMapping("/api/v1/playlists/{playlist_id}/{video_id}")
    public ResponseEntity addVideo(@PathVariable Long playlist_id, @PathVariable Long video_id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
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
    public ResponseEntity deleteVideo(@PathVariable Long playlist_id, @PathVariable Long video_id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        PlaylistStatus status = playlistService.removeVideoFromPlaylist(playlist_id, video_id, uid);
        if (status == PlaylistStatus.PLAYLIST_NOT_FOUND){
            return ResponseEntity.badRequest().body("Playlist " + playlist_id + " not found.");
        }
        return ResponseEntity.ok("Ok.");
    }

    @GetMapping("/api/v1/playlists/watchLater")
    public ResponseEntity getWatchLaterPlaylist(Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
        playlistService.assertWatchLaterExists(uid);
        return ResponseEntity.ok(playlistService.getWatchLater(uid));
    }

    @PutMapping("/api/v1/playlists/watchLater/{video_id}")
    public ResponseEntity addToWatchLater(@PathVariable Long video_id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
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
    public ResponseEntity removeFromWatchLater(@PathVariable Long video_id, Principal principal){
        String username = principal.getName();
        if (username == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("Unauthorized.");
        }
        Long uid = user.getId();
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
