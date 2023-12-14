package com.example.skillnest.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.data.annotation.Id;
@Entity
@Table(name="lectures")
public class Lecture {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "lecture_id")
    private int id;
    @Size(min = 5, max = 50)
    @Column(name= "title")
    private String title;
    @Size(max = 1000)
    @Column(name= "description")
    private String description;
    @Column(name = "assignment_url")
    private String assignmentUrl;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @Column(name = "video_url")
    private String videoUrl;
   @ManyToOne
   @JoinColumn(name = "user_id")
   @JsonManagedReference
   private User createdBy;

    public Lecture() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getAssignmentUrl() {
        return assignmentUrl;
    }

    public void setAssignmentUrl(String assignmentUrl) {
        this.assignmentUrl = assignmentUrl;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public User getCreatedBy() { return createdBy; }

    public void setCreatedBy(User createdBy) { this.createdBy = createdBy;}
}

