package com.example.skillnest.models.dtos;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.Video;
import org.hibernate.sql.ast.tree.update.Assignment;

public class LectureDto {
    private String title;
    private String description;
    private String assignmentUrl;
    private Course course;
    private Video video;


    public LectureDto(String title, String description, String assignmentUrl, Course course, Video video) {
        this.title = title;
        this.description = description;
        this.assignmentUrl = assignmentUrl;
        this.course = course;
        this.video = video;
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

    public Course getCourse() {return course;}

    public void setCourse(Course course) {this.course = course;}

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
