package com.partypilot.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partypilot.api.dto.UserDto;
import com.partypilot.api.model.User;
import com.partypilot.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final String UPLOAD_FOLDER = "src/main/resources/static/profilePhotos/";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> modifyUser(@PathVariable Long id, @RequestParam("user") String userStr, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(userStr, User.class);

        if (file != null && !file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            user.setProfilePhotoPath(file.getOriginalFilename());
        }

        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
