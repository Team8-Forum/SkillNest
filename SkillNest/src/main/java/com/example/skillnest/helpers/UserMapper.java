package com.example.skillnest.helpers;

import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.RegisterUserDto;
import com.example.skillnest.models.dtos.UserDto;
import com.example.skillnest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User dtoToObject(UserDto userDTO, int id) {
        User user = userService.getById(id);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        return user;
    }

    public User registerDTOToObject(RegisterUserDto registerUserDTO) {
        User user = new User();
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(registerUserDTO.getPassword());
        return user;
    }
}

