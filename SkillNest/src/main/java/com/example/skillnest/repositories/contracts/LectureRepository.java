package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.Lecture;

public interface LectureRepository {
    Lecture findLectureById(int id);

    Lecture create(Lecture lecture);

    void update(Lecture lecture);

    void delete(int lectureId);
}
