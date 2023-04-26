package com.boots.entity;

import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.List;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long authorUserId;
    private boolean isWatchLater;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "playlist_video",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "video_id", referencedColumnName = "id")})
    private List<Video> videos;

    public Playlist(){}

    public boolean isWatchLater() {
        return isWatchLater;
    }

    public void setWatchLater(boolean watchLater) {
        isWatchLater = watchLater;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }
}
