package com.example.skillnest.services.contracts;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.User;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    Course get(int id);

    void create(Course course, User user);

    void update(Course course, User user);

    void delete (int id, User user);

}
