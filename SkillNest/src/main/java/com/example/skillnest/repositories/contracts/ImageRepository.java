package com.example.skillnest.repositories.contracts;

import com.example.skillnest.models.Image;

public interface ImageRepository {
    Image getImage(int userId);

    void create(Image image);

    void update(Image image);
}
