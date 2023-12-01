package com.example.skillnest.controllers;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.CourseDto;
import com.example.skillnest.services.CourseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {
    private final CourseServiceImpl service;
    @Autowired
    public CourseRestController(CourseServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public List<Course> get() {
      return service.getAll();
    }

    @GetMapping("/{id}")
    public Course get(@PathVariable int id) {
        try {
            return service.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
