package com.nixagh.gallerycatalog.service;

import com.nixagh.gallerycatalog.dto.ImageDTO;
import com.nixagh.gallerycatalog.entity.Gallery;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class GalleryService {
    private final String imageURL = "http://localhost:8801/images/";
    private final RestTemplate restTemplate;

    public GalleryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Gallery getGallery(int id) {
        Gallery gallery = new Gallery();
        gallery.setId(id);

        List<ImageDTO> images = Collections.singletonList(restTemplate.getForObject(imageURL, ImageDTO.class));
        gallery.setImages(images);

        return gallery;
    }
}
