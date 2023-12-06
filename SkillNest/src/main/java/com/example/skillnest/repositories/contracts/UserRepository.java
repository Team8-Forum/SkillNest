package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.User;
import com.example.skillnest.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    List<User> getAll(UserFilterOptions filterOptions);

    User getById(int id);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void setToGraduated(int userId, int courseId);
}
