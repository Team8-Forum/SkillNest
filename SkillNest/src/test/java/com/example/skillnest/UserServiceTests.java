package com.example.skillnest;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.User;
import com.example.skillnest.repositories.contracts.UserRepository;
import com.example.skillnest.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.example.skillnest.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;


    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getById_should_return_User_when_userExists() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        User result = service.getById(mockUser.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), result.getId()),
                () -> Assertions.assertEquals(mockUser.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(mockUser.isAdmin(), result.isAdmin()),
                () -> Assertions.assertEquals(mockUser.getPassword(), result.getPassword())
        );
    }



    @Test
    public void update_should_return_updateUser_when_userIsAdmin() {
        User mockInitiator = createMockAdmin();
        User mockUserObject = createMockUser();

        Mockito.when(mockRepository.getByEmail(mockUserObject.getEmail()))
                .thenThrow(EntityNotFoundException.class);

        service.update(mockInitiator, mockUserObject);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockUserObject);

    }

    @Test
    public void updateAdmin_should_throw_when_executingUserIsNotAdmin() {
        User executingUser = createMockUser();
        User mockUserToBeUpdated = createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUserToBeUpdated);

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.updateAdmin(executingUser, mockUserToBeUpdated.getId()));
    }
}
