package com.example.skillnest.services.contracts;

import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.LectureDto;

public interface LectureService {
    Lecture getById(int id);

    Lecture create(LectureDto lectureDto, User user);

    Lecture update(LectureDto lectureDto, User user, int lectureId);

    void delete(int lectureId, User user);
}
