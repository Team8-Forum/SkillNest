package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.Lecture;

import java.util.List;

public interface LectureRepository {
    Lecture findLectureById(int id);

    List<Lecture> get();

    Lecture create(Lecture lecture);

    void update(Lecture lecture);

    void delete(int lectureId);
}
