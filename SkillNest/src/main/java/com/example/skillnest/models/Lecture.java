package com.example.skillnest.models;

import jakarta.persistence.*;
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
    @Column(name= "title")
    private String title;
    @Column(name= "description")
    private String description;
    @OneToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

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

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}

