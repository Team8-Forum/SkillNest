package com.example.skillnest;

import com.example.skillnest.models.Course;
import com.example.skillnest.models.CourseFilterOptions;
import com.example.skillnest.models.User;

import java.time.LocalDate;
import java.util.Date;

public class Helpers {

    public static CourseFilterOptions createMockFilterOptions() {
        return new CourseFilterOptions("title", "topic",
                LocalDate.now(), "sort", "order");
    }

    public static Course createMockCourse() {
        var mockCourse = new Course();
        mockCourse.setId(1);
        mockCourse.setTitle("Mock Title");
        mockCourse.setDescription("Mock Description");
        mockCourse.setTopic("Mock Topic");
        mockCourse.setStartingDate(LocalDate.now());
        return mockCourse;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setPassword("MockPassword");
        mockUser.setEmail("mock@user.com");
        mockUser.setAdmin(true);
        mockUser.setTeacher(false);
        return mockUser;
    }

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }


}
