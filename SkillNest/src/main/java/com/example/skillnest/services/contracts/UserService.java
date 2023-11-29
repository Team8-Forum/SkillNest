package com.example.skillnest.services.contracts;

import com.example.skillnest.models.User;
import com.example.skillnest.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<User> getAll();

    List<User> getAll(UserFilterOptions filterOptions, User user);

    User getById(int id);

    User getByEmai(String emaill);

    void create(User user);

    void update(User executingUser, User userToUpdate);

    User updateAdmin(User executingUser, int id);

    User updateTeacher(User executingUser, int id);

    void delete(User executingUser, int id);

    void changePassword(User user, String oldPassword, String newPassword, String confirmedNewPassword);
}
