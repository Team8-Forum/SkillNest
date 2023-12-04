package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.Assignment;
import com.example.skillnest.models.Lecture;

import java.util.List;

public interface AssignmentRepository {
    Lecture submit(Assignment assignment);

    List<Assignment> getAll();

    Assignment getById(int assignmentId);

    Assignment grade(Assignment assignment);

    List<Assignment> getByUser(int userId);
    List<Assignment> getByUserCourse(int userId, int courseId);
    Assignment getByUserLecture(int userId, int lectureId);

    Lecture update(Assignment assignment);

    List<Assignment> getAllAssignmentsForCourse(int courseId);
}
