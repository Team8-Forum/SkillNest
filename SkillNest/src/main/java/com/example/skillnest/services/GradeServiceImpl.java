package com.example.skillnest.services;

import com.example.skillnest.models.Grade;
import com.example.skillnest.repositories.contracts.GradeRepository;
import com.example.skillnest.services.contracts.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Grade getById(int id) {
        return gradeRepository.getById(id);
    }
}
