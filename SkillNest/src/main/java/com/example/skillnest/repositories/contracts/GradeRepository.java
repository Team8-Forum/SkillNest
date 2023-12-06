package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.Grade;

public interface GradeRepository {
    Grade getById(int id);
}
