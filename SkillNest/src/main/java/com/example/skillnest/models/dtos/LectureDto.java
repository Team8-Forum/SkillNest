package com.example.skillnest.models.dtos;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.Video;
import org.hibernate.sql.ast.tree.update.Assignment;

public class LectureDto {
    private String title;
    private String description;
    private String assignmentUrl;
    private String videoUrl;


    public LectureDto(String title, String description, String assignmentUrl, Course course, String videoUrl) {
        this.title = title;
        this.description = description;
        this.assignmentUrl = assignmentUrl;
        this.videoUrl = videoUrl;
    }

    public LectureDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignment() {
        return assignmentUrl;
    }

    public void setAssignment(String assignmentUrl) {
        this.assignmentUrl = assignmentUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
