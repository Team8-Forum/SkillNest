package com.example.skillnest;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.exceptions.EntityDuplicateException;
import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.CourseFilterOptions;
import com.example.skillnest.models.User;
import com.example.skillnest.repositories.CourseRepositoryImpl;
import com.example.skillnest.services.CourseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.skillnest.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTests {

    @Mock
    CourseRepositoryImpl mockRepository;
    @InjectMocks
    CourseServiceImpl service;

    @Test
    void get_Should_CallRepository(){
        // Arrange
        CourseFilterOptions mockFilterOptions = createMockFilterOptions();
        Mockito.when(mockRepository.getAll(mockFilterOptions))
                .thenReturn(null);

        //Act
        service.get(mockFilterOptions);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).
                getAll(mockFilterOptions);
    }

    @Test
    public void create_Should_CallRepository_When_CourseWithSameNameDoesNotExist() {
        // Arrange
        Course mockCourse = createMockCourse();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.get(mockCourse.getTitle()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.create(mockCourse, mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockCourse);
    }

    @Test
    public void create_Should_Throw_When_CourseWithSameNameExists() {
        // Arrange
        Course mockCourse = createMockCourse();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.get(mockCourse.getTitle()))
                .thenReturn(mockCourse);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.create(mockCourse, mockUser));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Course mockCourse = createMockCourse();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockCourse);

        // Act
        service.delete(1, mockUserAdmin);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

}
