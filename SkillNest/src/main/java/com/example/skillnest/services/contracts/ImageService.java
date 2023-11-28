package com.example.skillnest.services.contracts;

import com.example.skillnest.models.Image;

public interface ImageService {
    Image getImage(int userId);

    void saveImage(int userId, String image);

    void update(Image image);
}
