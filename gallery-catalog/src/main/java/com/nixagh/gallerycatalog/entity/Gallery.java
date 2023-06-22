package com.nixagh.gallerycatalog.entity;

import com.nixagh.gallerycatalog.dto.ImageDTO;

import java.util.List;

public class Gallery {
    private int id;
    private List<ImageDTO> images;

    public Gallery() {
    }

    public Gallery(int galleryId) {
        this.id = galleryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

}
