package com.example.skillnest.helpers;

import com.example.skillnest.models.Lecture;
import com.example.skillnest.models.dtos.LectureDto;
import com.example.skillnest.services.contracts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {
    private final LectureService lectureService;
    @Autowired
    public LectureMapper(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public Lecture dtoToObject(LectureDto lectureDTO, int id) {
        Lecture lecture = lectureService.getById(id);
        lecture.setTitle(lectureDTO.getTitle());
        lecture.setDescription(lectureDTO.getDescription());
        lecture.setAssignmentUrl(lectureDTO.getAssignment());
        lecture.setVideoUrl(lectureDTO.getVideoUrl());
        return lecture;
    }

    public Lecture dtoToObject(LectureDto lectureDTO) {
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDTO.getTitle());
        lecture.setDescription(lectureDTO.getDescription());
        lecture.setAssignmentUrl(lectureDTO.getAssignment());
        lecture.setVideoUrl(lectureDTO.getVideoUrl());
        return lecture;
    }

    public LectureDto objectToDto(Lecture lecture) {
        LectureDto lectureDTO = new LectureDto();
        lectureDTO.setTitle(lecture.getTitle());
        lectureDTO.setDescription(lecture.getDescription());
        lectureDTO.setAssignment(lecture.getAssignmentUrl());
        lectureDTO.setVideoUrl(lecture.getVideoUrl());
        return lectureDTO;


    }


}
