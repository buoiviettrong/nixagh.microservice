package com.nixagh.gallerycatalog.controller;

import com.nixagh.gallerycatalog.entity.Gallery;
import com.nixagh.gallerycatalog.service.GalleryService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GalleryController {
    private final GalleryService galleryService;
    private final Environment env;

    public GalleryController(GalleryService galleryService, Environment env) {
        this.galleryService = galleryService;
        this.env = env;
    }

    @RequestMapping("/")
    public String home() {
        // This is useful for debugging
        // When having multiple instance of gallery service running at different ports.
        // We load balance among them, and display which instance received the request.
        return "Hello from Gallery Service running at port: " + env.getProperty("local.server.port");
    }

    @RequestMapping("/{id}")
    public Gallery getGallery(@PathVariable final int id) {
        return galleryService.getGallery(id);
    }

    // -------- Admin Area --------
    // This method should only be accessed by users with role of 'admin'
    // We'll add the logic of role based auth later
    @RequestMapping("/admin")
    public String homeAdmin() {
        return "This is the admin area of Gallery service running at port: " + env.getProperty("local.server.port");
    }
}
