package com.example.skillnest.controllers.mvc;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.helpers.CourseMapper;
import com.example.skillnest.helpers.LectureMapper;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.CourseFilterOptions;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.CourseCreateDto;
import com.example.skillnest.models.dtos.CourseDto;
import com.example.skillnest.models.dtos.FilterCourseDto;
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

@RequestMapping("/courses")
@Controller
public class CourseMvcController {
    private final CourseService courseService;

    private final LectureService lectureService;

    private final CourseMapper courseMapper;

    private final AuthenticationHelper authenticationHelper;

    public CourseMvcController(CourseService courseService, LectureService lectureService, CourseMapper courseMapper,
                               AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.courseMapper = courseMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return  session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {return  request.getRequestURI();}
    @ModelAttribute("lectures")
    public List<Lecture> populateLectures() {return  lectureService.get();}

    @GetMapping
    public String showAllCourses(Model model) {
        model.addAttribute("courses", courseService.get(new CourseFilterOptions()));
        return "CourseView";
    }
    @GetMapping("/{id}")
    public String showSingleCourse(@PathVariable int id, Model model) {
        try {
            Course course = courseService.get(id);
            model.addAttribute("course", course);
            return "CourseView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @GetMapping("/new")
    public String showNewCoursePage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("course", new CourseDto());
        return "CourseCreateView";
    }
    @PostMapping("/new")
    public String createBeer(@Valid @ModelAttribute("course") CourseDto courseDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CourseCreateView";
        }

        try {
            Course course = courseMapper.fromDto(courseDto);
            courseService.create(course, user);
            return "redirect:/courses";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_course", e.getMessage());
            return "CourseCreateView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditCoursePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Course course = courseService.get(id);
            CourseDto courseDto = courseMapper.toDto(course);
            model.addAttribute("courseId", id);
            model.addAttribute("course", courseDto);
            return "CourseUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateCourse(@PathVariable int id,
                             @Valid @ModelAttribute("course") CourseDto dto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CourseUpdateView";
        }

        try {
            Course course = courseMapper.fromDto(id, dto);
            courseService.update(course, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "CourseUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCourse(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            courseService.delete(id, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}