package com.example.skillnest.controllers.mvc;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.helpers.CourseMapper;
import com.example.skillnest.helpers.LectureMapper;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.CourseDto;
import com.example.skillnest.models.dtos.LectureDto;
import com.example.skillnest.services.contracts.CourseService;
import com.example.skillnest.services.contracts.LectureService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/courses")
@Controller
public class CourseMvcController {
    private final CourseService courseService;

    private final LectureService lectureService;

    private final CourseMapper courseMapper;

    private final LectureMapper lectureMapper;

    private final AuthenticationHelper authenticationHelper;

    public CourseMvcController(CourseService courseService, LectureService lectureService, CourseMapper courseMapper,
                               LectureMapper lectureMapper, AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.courseMapper = courseMapper;
        this.lectureMapper = lectureMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("getUserId")
    public int populateGetUser(HttpSession session) {
        return authenticationHelper.tryGetUserId(session);
    }
    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {return  request.getRequestURI();}
    @ModelAttribute("lectures")
    public List<Lecture> populateLectures() {return  lectureService.get();}

    @GetMapping
    public String showAllCourses(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetCurrentUser(session);
        model.addAttribute("courses", courseService.getAll());
        model.addAttribute("user", user);
        return "courses";
    }
    @GetMapping("/{id}")
    public String showSingleCourse(@PathVariable int id, Model model, HttpSession session) {
        try {
            Course course = courseService.get(id);
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("course", course);
            model.addAttribute("enrolledUsers" ,course.getUsers().size());
            model.addAttribute("user", user);
            return "CourseView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{courseId}/lectures/{lectureId}")
    public String showLecture(@PathVariable int courseId, @PathVariable int lectureId, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            Course course = courseService.get(courseId);
            Lecture lecture = lectureService.getById(lectureId);

            model.addAttribute("lecture", lecture);
            model.addAttribute("course", course);

            return "LectureView";
        } catch (EntityNotFoundException e) {
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{courseId}/lectures")
    public String showAllLectures(@PathVariable("courseId") int courseId, Model model, HttpSession session) {
        Course course = courseService.get(courseId);
        Set<Lecture> lectures = course.getLectures();
        User user = authenticationHelper.tryGetCurrentUser(session);

        model.addAttribute("lectures", lectures);
        model.addAttribute("course", course);
        model.addAttribute("user",user);

        return "LecturesView";
    }
    @GetMapping("/new")
    public String showNewCoursePage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("course", new CourseDto());
        return "CourseFormView";
    }
    @PostMapping("/new")
    public String createCourse(@Valid @ModelAttribute("course") CourseDto courseDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Course course = courseMapper.fromDto(courseDto, user);
            courseService.create(course, user);
            return "redirect:/courses";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_course", e.getMessage());
            return "CourseFormView";
        }
    }

    @GetMapping("/{courseId}/lectures/create")
    public String showLectureForm(@PathVariable("courseId") int courseId, Model model, HttpSession session) {
        User user = authenticationHelper.tryGetCurrentUser(session);

        Course course = courseService.get(courseId);
        LectureDto lectureDTO = new LectureDto();

        model.addAttribute("course", course);
        model.addAttribute("lecture", lectureDTO);

        return "LectureFormView";
    }

    @PostMapping("/{courseId}/lectures/create")
    public String createLecture(@PathVariable("courseId") int courseId, @Valid @ModelAttribute("lecture") LectureDto lectureDTO,
                                BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "LectureFormView";
        }

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Lecture lecture = lectureMapper.dtoToObject(lectureDTO, courseId);
            lectureService.create(lecture, user);
            return "redirect:/courses/" + courseId;
        } catch (EntityNotFoundException e) {
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
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
            Course course = courseMapper.fromDto(id, dto,user);
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
