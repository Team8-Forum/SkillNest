package com.example.skillnest.repositories;

import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Image;
import com.example.skillnest.repositories.contracts.ImageRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public ImageRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Image getImage(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Image image = session.get(Image.class, userId);
            if (image == null) {
                throw new EntityNotFoundException("Image", userId);
            }
            return image;
        }
    }

    @Override
    public void create(Image image) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(image);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Image image) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(image);
            session.getTransaction().commit();
        }
    }
}
