package com.example.skillnest.services;

import com.example.skillnest.exceptions.EntityNotFoundException;
import com.example.skillnest.models.Image;
import com.example.skillnest.repositories.contracts.ImageRepository;
import com.example.skillnest.services.contracts.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository repository;

    @Autowired
    public ImageServiceImpl(ImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Image getImage(int userId) {
        return repository.getImage(userId);
    }

    @Override
    public void saveImage(int userId, String image) {
        Image newImage = new Image();
        newImage.setImage(image);
        newImage.setUserId(userId);

        try {
            repository.getImage(userId);
            update(newImage);
        } catch (EntityNotFoundException e) {
            repository.create(newImage);
        }
    }

    @Override
    public void update(Image image) {
        repository.update(image);
    }
}
