package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    Course get(int id);

    Course get(String name);

    void create(Course course);

    void update(Course course);

    void delete(int id);
}
