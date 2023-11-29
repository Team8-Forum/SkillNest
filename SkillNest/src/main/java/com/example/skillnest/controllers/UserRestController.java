package com.example.skillnest.controllers;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.helpers.AuthenticationHelper;
import com.example.skillnest.helpers.UserMapper;
import com.example.skillnest.models.User;
import com.example.skillnest.models.UserFilterOptions;
import com.example.skillnest.models.dtos.RegisterUserDto;
import com.example.skillnest.models.dtos.UserDto;
import com.example.skillnest.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper,
                              AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll(
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder){
        try{
            User user = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(user.getEmail(),user.getPassword());
            UserFilterOptions userFilterOptions =
                    new UserFilterOptions(firstName, lastName, email, sortBy, sortOrder);
            return userService.getAll(userFilterOptions, user);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id, @RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(user.getEmail(),user.getPassword());
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody RegisterUserDto registerUserDto){
        try{
            User user = userMapper.registerDTOToObject(registerUserDto);
            userService.create(user);
            return user;
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                       @Valid @RequestBody UserDto userDto){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(executeUser.getEmail(), executeUser.getPassword());
            User updateUser = userMapper.dtoToObject(userDto,id);
            userService.update(executeUser,updateUser);
            return updateUser;
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/admin")
    public User updateAdmin(@RequestHeader HttpHeaders headers,@PathVariable int id){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            User updateUser = userService.updateAdmin(executeUser,id);
            return updateUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/blocked")
    public User updateTeacher(@RequestHeader HttpHeaders headers,@PathVariable int id){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            User updateUser = userService.updateTeacher(executeUser,id);
            return updateUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User executingUser = authenticationHelper.tryGetUser(headers);
            userService.delete(executingUser, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}