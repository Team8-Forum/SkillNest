package com.example.skillnest.controllers;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.helpers.LectureMapper;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.LectureDto;
import com.example.skillnest.services.contracts.CourseService;
import com.example.skillnest.services.contracts.LectureService;
import com.example.skillnest.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping
@Controller
public class LectureMvcController {

    private final LectureService lectureService;
    private final LectureMapper lectureMapper;
    private final UserService userService;
    private final CourseService courseService;
    private final AuthenticationHelper authenticationHelper;

    public LectureMvcController(LectureService lectureService,
                                LectureMapper lectureMapper,
                                UserService userService,
                                CourseService courseService,
                                AuthenticationHelper authenticationHelper) {
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
        this.userService = userService;
        this.courseService = courseService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
    @GetMapping("/courses/{courseId}/lectures/create")
    public String showLectureForm(@PathVariable("courseId") int courseId, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        Course course = courseService.get(courseId);
        LectureDto lectureDTO = new LectureDto();
        model.addAttribute("course", course);
        model.addAttribute("lecture", lectureDTO);
        return "LectureFormView";
    }

    @PostMapping("/courses/{courseId}/lectures/create")
    public String createLecture(@PathVariable("courseId") int courseId, @Valid @ModelAttribute("lecture") LectureDto lectureDTO,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session) {


        if (bindingResult.hasErrors()) {
            return "LectureFormView";
        }

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Course course = courseService.get(courseId);
            Lecture lecture = lectureMapper.dtoToObject(lectureDTO);
            lectureService.create(lectureDTO, user);
            return "redirect:/courses/" + courseId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }  catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/{courseId}/lectures")
    public String ShowAllLectures(@PathVariable("courseId") int courseId, Model model) {
        Course course = courseService.get(courseId);
        List<Lecture> lectures = course.getLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("course", course);

        return "CoursesView";
    }

    @GetMapping("/courses/{courseId}/lectures/{id}/update")
    public String updateLectureForm(@PathVariable int id, Model model, HttpSession httpSession){
        try {
            authenticationHelper.tryGetCurrentUser(httpSession);
            Lecture lecture = lectureService.getById(id);
            LectureDto lectureDTO = lectureMapper.objectToDto(lecture);
            model.addAttribute("lecture", lectureDTO);
            return "LectureUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/courses/{courseId}/lectures/{id}/update")
    public String updateLecture(@PathVariable int id,
                                @Valid @ModelAttribute("lecture") LectureDto lectureDTO,
                                BindingResult result,
                                Model model,
                                HttpSession httpSession){
        if (result.hasErrors()) {
            return "CourseUpdateView";
        }

        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Lecture lecture = lectureMapper.dtoToObject(lectureDTO, id);
            lectureService.update(lectureDTO, user, lecture.getId());
            return "redirect:/courses/{courseId}/lectures/{id}/update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteLecture(@PathVariable int id, Model model, HttpSession httpSession) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            lectureService.delete(id, user);
            return "redirect:/courses";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
