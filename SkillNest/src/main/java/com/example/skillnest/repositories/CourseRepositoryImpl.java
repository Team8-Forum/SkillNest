package com.example.skillnest.repositories;

import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Course;
import com.example.skillnest.models.CourseFilterOptions;
import com.example.skillnest.repositories.contracts.CourseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Course> getAll(CourseFilterOptions filterOptions) {
        try(Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            filterOptions.getTopic().ifPresent(value -> {
                filters.add("topic like :topic");
                params.put("topic", String.format("%%%s%%", value));
            });

            filterOptions.getStartingDate().ifPresent(value -> {
                filters.add("startingDate = :startingDate");
                params.put("startingDate", value);
            });

            StringBuilder queryString = new StringBuilder("from Course");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }

            queryString.append(generateOrderBy(filterOptions));
            Query<Course> query = session.createQuery(queryString.toString(), Course.class);
            query.setProperties(params);
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
    public Course get(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course where title = :title", Course.class);
            query.setParameter("title", title);

            List<Course> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }

            return result.get(0);
        }
    }

    @Override
    public List<Course> getAllByUserEnrolled(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createNativeQuery(
                    "select c.* from courses c " +
                            "join skillnest.enrolled_courses ec on c.course_id = ec.course_id " +
                            "where ec.user_id = :id " +
                            "and ec.is_graduated=0", Course.class);
            query.setParameter("id", userId);
            return query.list();
        }
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

    private String generateOrderBy(CourseFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "topic":
                orderBy = "topic";
                break;
            case "startingDate":
                orderBy = "startingDate";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
