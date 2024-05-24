package com.partypilot.api.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ResourceController {

    @GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        Path filePath = Paths.get("src/main/resources/static/profilePhotos/").resolve(filename).normalize();
        return getResource(filePath);
    }

    @GetMapping("/banner/{filename}")
    public ResponseEntity<Resource> getBanner(@PathVariable String filename) {
        Path filePath = Paths.get("src/main/resources/static/banners/").resolve(filename).normalize();
        return getResource(filePath);
    }

    private ResponseEntity<Resource> getResource(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf(Files.probeContentType(filePath)))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            return ResponseEntity.status(500).build();
        }
    }
}
