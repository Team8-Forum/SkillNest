package com.example.skillnest.helpers;

import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.RegisterDto;
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

    public User registerDTOToObject(RegisterDto registerUserDTO) {
        User user = new User();
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(registerUserDTO.getPassword());
        return user;
    }

    public User fromRegisterDto(RegisterDto registerMvcDto){
        User user=new User();
        user.setFirstName(registerMvcDto.getFirstName());
        user.setLastName(registerMvcDto.getLastName());
        user.setEmail(registerMvcDto.getEmail());

        if (registerMvcDto.isTeacher()){
            user.setTeacher(true);
        } else{
            user.setTeacher(false);
        }

        user.setPassword(registerMvcDto.getPassword());
        user.setPicUrl("url");

        return user;
    }
}

