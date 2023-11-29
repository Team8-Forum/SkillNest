package com.example.skillnest.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int videoId;
    @Column(name = "file_path")
    private String videoUrl;
    @jakarta.persistence.Id
    private Integer id;


    public Video(int videoId, String videoUrl) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
    }

    public Video() {
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
