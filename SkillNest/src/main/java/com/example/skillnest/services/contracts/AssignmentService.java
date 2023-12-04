package com.example.skillnest.services.contracts;

import com.example.skillnest.models.Assignment;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {
    List<Assignment> getAll();
    List<Assignment> getByUserSubmitted(int userId);
    List<Assignment> getAllAssignmentsForCourse(int courseId);
    List<Assignment> getByUserSubmittedToCourse(int userId, int courseId);


    Assignment getById(int assignmentId);
    Lecture submitAssignment(User user, int lectureId, MultipartFile multipartFile);
    double getGradeForCourse(int userId, int courseId);

    Assignment grade(int assignmentId, int gradeId, int courseId, int studentId);

}

