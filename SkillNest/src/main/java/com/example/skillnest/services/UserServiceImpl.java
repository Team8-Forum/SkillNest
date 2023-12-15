package com.example.skillnest.services;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.User;
import com.example.skillnest.models.UserFilterOptions;
import com.example.skillnest.repositories.contracts.UserRepository;
import com.example.skillnest.services.contracts.CourseService;
import com.example.skillnest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseService courseService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CourseService courseService) {
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
    @Override
    public List<User> getAll(UserFilterOptions filterOptions, User user) {
        return userRepository.getAll(filterOptions);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByEmai(String email){
        return userRepository.getByEmail(email);
    }

    @Override
    public void create(User user) {
        checkIfUserIsUnique(user);
        userRepository.create(user);
    }


    @Override
    public void update(User executeUser, User updateUser) {
        if(!executeUser.isAdmin()){
            if(!executeUser.getEmail().equalsIgnoreCase(updateUser.getEmail())){
                throw new AuthorizationException("Only admin or account's owners can modify a user");
            }
        }
        userRepository.update(updateUser);
    }

    @Override
    public User enrollCourse(User user, int courseId) {
        Course course = courseService.get(courseId);
        user.addCourse(course);
        userRepository.update(user);
        return user;
    }

    @Override
    public User addProfilePhoto(User user, String url) {
        user.setPicUrl(url);
        userRepository.update(user);
        return user;
    }

    @Transactional
    @Override
    public void setToGraduated(int userId, int courseId){
        userRepository.setToGraduated(userId, courseId);
    }

    @Override
    public User updateAdmin(User executeUser, int id) {
        User userToBeUpdated = getById(id);
        if(!executeUser.isAdmin()){
            throw new AuthorizationException("Only admin can make admins");
        }
        boolean newAdminStatus = !userToBeUpdated.isAdmin();
        userToBeUpdated.setAdmin(newAdminStatus);
        userRepository.update(userToBeUpdated);
        return userToBeUpdated;
    }

    @Override
    public User updateTeacher (User executeUser, int id) {
        User userToBeUpdated = getById(id);
        if(!executeUser.isAdmin()){
            throw new AuthorizationException("Only admin can make teachers");
        }
        boolean newTeacherStatus = !userToBeUpdated.isTeacher();
        userToBeUpdated.setAdmin(newTeacherStatus);
        userRepository.update(userToBeUpdated);
        return userToBeUpdated;
    }



    @Override
    public void changePassword(User user, String oldPassword, String newPassword, String confirmedNewPassword) {
        if(!user.getPassword().equals(oldPassword)) {
            throw new AuthorizationException("Your old password is incorrect");
        }

        if(!newPassword.equals(confirmedNewPassword)) {
            throw new InputMismatchException("New password do not match with confirmed one");
        }
        user.setPassword(newPassword);
        userRepository.update(user);
    }

    @Override
    public void delete(User executeUser, int id) {
        User updateUser = getById(id);
        if(!executeUser.isAdmin()){
            if(!executeUser.getEmail().equalsIgnoreCase(updateUser.getEmail())){
                throw new AuthorizationException("Only admin or account's owners can modify a user");
            }
        }
        updateUser.setDeleted(true);
        userRepository.update(updateUser);
    }

    private void checkIfUserIsUnique(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        duplicateExists = true;

        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getEmail());
        }
    }
}

