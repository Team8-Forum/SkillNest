package com.example.skillnest.controllers.rest;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.LectureDto;
import com.example.skillnest.services.contracts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {
    private final LectureService lectureService;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public LectureRestController(LectureService lectureService, AuthenticationHelper authenticationHelper) {
        this.lectureService = lectureService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public Lecture getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return lectureService.getById(id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Lecture create(@RequestHeader HttpHeaders headers, @RequestBody LectureDto lectureDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.create(lectureDto, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{lectureId}")
    public Lecture update(@RequestHeader HttpHeaders headers, @PathVariable int lectureId,
                          @RequestBody LectureDto lectureDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return lectureService.update(lectureDto, user, lectureId);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{lectureId}")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int lectureId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            lectureService.delete(lectureId,user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
