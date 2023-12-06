package com.example.skillnest.repositories;

import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Lecture;
import com.example.skillnest.repositories.contracts.LectureRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public LectureRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Lecture findLectureById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Lecture lecture = session.get(Lecture.class, id);
            if (lecture == null) {
                throw new EntityNotFoundException("Comment", id);
            }
            return lecture;
        }
    }

    @Override
    public Lecture create(Lecture lecture) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(lecture);
            session.getTransaction().commit();
        }
        return lecture;
    }

    @Override
    public void update(Lecture lecture) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(lecture);
            session.getTransaction().commit();
        }
    }


    @Override
    public void delete(int lectureId) {
        Lecture lectureToDelete = findLectureById(lectureId);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(lectureToDelete);
            session.getTransaction().commit();
        }
    }
}
