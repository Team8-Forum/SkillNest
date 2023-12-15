package com.example.skillnest.controllers.mvc;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.helpers.ImageHelper;
import com.example.skillnest.helpers.UserMapper;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.UserDto;
import com.example.skillnest.models.dtos.ChangePasswordDto;
import com.example.skillnest.services.contracts.CourseService;
import com.example.skillnest.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ImageHelper imageHelper;
    private final CourseService courseService;

    public UserMvcController(AuthenticationHelper authenticationHelper, UserMapper userMapper, UserService userService,
                             ImageHelper imageHelper, CourseService courseService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.imageHelper = imageHelper;
        this.courseService = courseService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isTeacher")
    public boolean populateIsTeacher(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            return (user.isTeacher() || user.isAdmin());
        } catch (AuthorizationException e) {
            return false;
        }
    }

    @GetMapping("/{id}")
    public String showUserPage(HttpSession httpSession, Model model ,@PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
                model.addAttribute("user", user);
                model.addAttribute("courses", user.getCourses());
                return "ProfileView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User existingUser = userService.getById(id);
            model.addAttribute("user", existingUser);
            model.addAttribute("userId", existingUser.getId());
            return "EditProfileView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute("updateUser") UserDto userDto, BindingResult errors,
                             Model model, HttpSession session) {
        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if(errors.hasErrors()) {
            User existingUser = userService.getById(id);
            model.addAttribute("user", existingUser);
            model.addAttribute("userId", existingUser.getId());
            return "EditProfileView";
        }

        try {
            User userToBeUpdated = userMapper.dtoToObject(userDto, id);
            userService.update(executingUser, userToBeUpdated);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }


    }

    @GetMapping("/{id}/update-password")
    public String showUpdatePasswordPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User existingUser = userService.getById(id);
            model.addAttribute("password", new ChangePasswordDto());
            model.addAttribute("user", existingUser);
            return "EditPasswordView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update-password")
    public String changePassword (@Valid @ModelAttribute("password") ChangePasswordDto changePasswordDto, BindingResult errors,
                                  @PathVariable int id, Model model, HttpSession session ) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        user = userService.getById(id);
        model.addAttribute("user", user);

        if(errors.hasErrors()) {
            return "EditPasswordView";
        }

        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();
        String confirmPassword = changePasswordDto.getConfirmPassword();

        try {
            userService.changePassword(user, oldPassword, newPassword, confirmPassword);
            return "redirect:/users/" + user.getId();
        } catch (AuthorizationException | InputMismatchException e) {
            errors.rejectValue("newPassword", "password_mismatch", e.getMessage());
            return "EditPasswordView";
        }

    }

    @PostMapping("/picture")
    public String addProfilePhoto(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            String url = imageHelper.uploadImage(file);
            userService.addProfilePhoto(user, url);
            return "redirect:/users/update";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            return "ErrorView";
        }
    }

    @PostMapping("/course/{courseId}/enroll")
    public String enrollCourse(HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            userService.enrollCourse(user, courseId);
            model.addAttribute("loggedIn", user);
            model.addAttribute("enrolledCourses", courseService.getByUserEnrolled(user.getId()));
            return "redirect:/users/" + user.getId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "index";
        }

        try {
            User user = userService.getById(id);
            userService.delete(user,id);
            return "redirect:/auth/logout";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/admin")
    public String adminUser(@PathVariable int id, Model model, HttpSession session) {
        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.updateAdmin(executingUser,id);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
