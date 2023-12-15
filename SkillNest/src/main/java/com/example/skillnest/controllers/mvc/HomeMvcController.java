package com.example.skillnest.controllers.mvc;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.services.contracts.CourseService;
import com.example.skillnest.services.contracts.LectureService;
import com.example.skillnest.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final UserService userService;
    private final CourseService courseService;
    private final LectureService lectureService;
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(UserService userService, CourseService courseService, LectureService lectureService,
                             AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.courseService = courseService;
        this.lectureService = lectureService;
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

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("courses", courseService.getAll());
        model.addAttribute("users", userService.getAll());
        return "index";
    }

    @GetMapping("/about")
    public String showAboutPage(Model model, HttpSession httpSession) {
        try {
            model.addAttribute("loggedIn", authenticationHelper.tryGetCurrentUser(httpSession));
            return "about";
        } catch (AuthorizationException e) {
            return "about";
        }
    }

    @GetMapping("/contact")
    public String showContactPage(Model model, HttpSession httpSession) {
        try {
            model.addAttribute("loggedIn", authenticationHelper.tryGetCurrentUser(httpSession));
            return "contact";
        } catch (AuthorizationException e) {
            return "contact";
        }
    }
}
