package com.example.skillnest.models.dtos;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.Video;
import org.hibernate.sql.ast.tree.update.Assignment;

public class LectureDto {
    private String title;
    private String description;
    private Assignment assignment;
    private Course course;
    private Video video;


    public LectureDto(String title, String description, Assignment assignment, Course course, Video video) {
        this.title = title;
        this.description = description;
        this.assignment = assignment;
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

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
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
