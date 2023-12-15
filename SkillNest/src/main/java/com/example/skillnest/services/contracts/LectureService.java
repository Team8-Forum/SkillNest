package com.example.skillnest.services.contracts;

import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.LectureDto;

import java.util.List;

public interface LectureService {
    Lecture getById(int id);

    List<Lecture> get();

    Lecture create(Lecture lecture, User user);

    Lecture update(LectureDto lectureDto, User user, int lectureId);

    void delete(int lectureId, User user);
}
