package com.partypilot.api.controller;

import com.partypilot.api.config.UserAuthProvider;
import com.partypilot.api.dto.CredentialsDto;
import com.partypilot.api.dto.EventAuthorizationDto;
import com.partypilot.api.dto.SignUpDto;
import com.partypilot.api.dto.UserDto;
import com.partypilot.api.service.EventAuthorizationService;
import com.partypilot.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final EventAuthorizationService eventAuthorizationService;

    public AuthController(UserService userService, UserAuthProvider userAuthProvider, EventAuthorizationService eventAuthorizationService) {
        this.userService = userService;
        this.userAuthProvider = userAuthProvider;
        this.eventAuthorizationService = eventAuthorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @GetMapping("/check-authorization/{eventId}")
    public ResponseEntity<EventAuthorizationDto> checkUserRegistration(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventAuthorizationService.generateEventAuthorizationDto(eventId));
    }
}
