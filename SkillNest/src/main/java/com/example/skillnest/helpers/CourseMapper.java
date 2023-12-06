package com.example.skillnest.helpers;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.dtos.CourseDto;
import com.example.skillnest.services.CourseServiceImpl;
import com.example.skillnest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    private final CourseServiceImpl courseService;
    private final UserService userService;

    @Autowired
    public CourseMapper(CourseServiceImpl courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    public Course fromDto(int id, CourseDto dto) {
        Course courseToBeUpdated = courseService.get(id);
        courseToBeUpdated.setTitle(dto.getTitle());
        courseToBeUpdated.setDescription(dto.getContent());
        return courseToBeUpdated;
    }

    public Course fromDto(CourseDto dto) {
        Course post = new Course();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getContent());
        return post;
    }

    public CourseDto toDto(Course post) {
        CourseDto dto = new CourseDto();
        dto.setTitle(post.getTitle());
        dto.setContent(post.getDescription());
        return dto;
    }
}
