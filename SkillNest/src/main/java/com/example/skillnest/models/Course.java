package com.example.skillnest.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "courses")

public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description")
    private String description;

    @Column(name = "starting_date")
    private LocalDate startingDate;
}
