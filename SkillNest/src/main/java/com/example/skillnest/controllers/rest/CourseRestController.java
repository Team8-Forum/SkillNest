package com.example.skillnest.controllers.rest;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.helpers.CourseMapper;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.CourseFilterOptions;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.CourseDto;
import com.example.skillnest.services.CourseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {
    private final CourseServiceImpl service;
    private final AuthenticationHelper authenticationHelper;
    private final CourseMapper courseMapper;
    @Autowired
    public CourseRestController(CourseServiceImpl service, AuthenticationHelper authenticationHelper, CourseMapper courseMapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public List<Course> get(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String topic,
                            @RequestParam(required = false) LocalDate startingDate,
                            @RequestParam(required = false) String sortBy,
                            @RequestParam(required = false) String sortOrder) {
        CourseFilterOptions filterOptions = new CourseFilterOptions(title, topic,
                startingDate, sortBy, sortOrder);
        return service.get(filterOptions);

    }

    @GetMapping("/{id}")
    public Course get(@PathVariable int id) {
        try {
            return service.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Course create(@RequestHeader HttpHeaders headers,
                       @Valid @RequestBody CourseDto courseDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Course course = courseMapper.fromDto(courseDto,user);
            service.create(course, user);
            return course;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Course update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                       @Valid @RequestBody CourseDto courseDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Course course = courseMapper.fromDto(id, courseDto, user);
            service.update(course, user);
            return course;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
