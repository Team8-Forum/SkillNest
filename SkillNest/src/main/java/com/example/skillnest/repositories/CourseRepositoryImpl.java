package com.example.skillnest.repositories;

import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Course;
import com.example.skillnest.repositories.contracts.CourseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CourseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course", Course.class);
            return query.list();
        }
    }

    @Override
    public Course get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Course post = session.get(Course.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Course", id);
            }
            return post;
        }
    }

    @Override
    public Course get(String name) {
    }

    @Override
    public void create(Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(course);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(course);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Course courseToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(courseToDelete);
            session.getTransaction().commit();
        }
    }
}
