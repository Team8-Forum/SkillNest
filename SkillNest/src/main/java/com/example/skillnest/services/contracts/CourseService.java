package com.example.skillnest.services.contracts;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.CourseFilterOptions;
import com.example.skillnest.models.User;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    List<Course> get(CourseFilterOptions filterOptions);

    Course get(int id);

    public List<Course>getByUserEnrolled(int user_id);

    void create(Course course, User user);

    void update(Course course, User user);

    void delete (int id, User user);

}
