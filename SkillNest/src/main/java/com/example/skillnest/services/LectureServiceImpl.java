package com.example.skillnest.services;

import com.example.skillnest.exceptions.AuthorizationException;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.User;
import com.example.skillnest.models.dtos.LectureDto;
import com.example.skillnest.repositories.contracts.CourseRepository;
import com.example.skillnest.repositories.contracts.LectureRepository;
import com.example.skillnest.services.contracts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final CourseRepository courseRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, CourseRepository courseRepository) {
        this.lectureRepository = lectureRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public Lecture getById(int id) {
        return lectureRepository.findLectureById(id);
    }

    @Override
    public Lecture create(LectureDto lectureDto, User user) {
        //user validation
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAssignmentUrl(lectureDto.getAssignment());
        lecture.setCourse(lectureDto.getCourse());
        lecture.setVideo(lectureDto.getVideo());
        return lectureRepository.create(lecture);
    }

    @Override
    public Lecture update(LectureDto lectureDto, User user, int lectureId) {
        Lecture lecture = lectureRepository.findLectureById(lectureId);
        if (!(user.isAdmin()|| lecture.getCreatedBy().equals(user))) {
            throw new AuthorizationException("You may not remove this lecture");
        }
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAssignmentUrl(lectureDto.getAssignment());
        lecture.setCourse(lectureDto.getCourse());
        lecture.setVideo(lectureDto.getVideo());

        return lecture;
    }

    @Override
    public void delete(int lectureId, User user) {
        Lecture lecture = lectureRepository.findLectureById(lectureId);
        if (!(user.isAdmin()|| lecture.getCreatedBy().equals(user))) {
            throw new AuthorizationException("You may not remove this lecture");
        }
        lectureRepository.delete(lectureId);
    }
}
