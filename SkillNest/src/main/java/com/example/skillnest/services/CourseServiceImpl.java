package com.example.skillnest.services;

import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.User;
import com.example.skillnest.repositories.CourseRepositoryImpl;
import com.example.skillnest.services.contracts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepositoryImpl courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepositoryImpl courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.getAll();
    }

    @Override
    public Course get(int id) {
        return courseRepository.get(id);
    }

    @Override
    public void create(Course course, User user) {
        boolean duplicateExists = true;
        try {
            courseRepository.get(course.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Course", "title", course.getTitle());
        }
        courseRepository.create(course);
    }

    @Override
    public void update(Course course, User user) {
        courseRepository.update(course);
    }

    @Override
    public void delete(int id, User user) {
        Course course = get(id);
        courseRepository.delete(course.getId());
    }
}
