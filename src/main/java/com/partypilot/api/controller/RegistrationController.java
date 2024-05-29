package com.partypilot.api.controller;

import com.partypilot.api.dto.RegistrationDto;
import com.partypilot.api.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<RegistrationDto>> getRegistrationsForEvent(@PathVariable Long eventId) {
        List<RegistrationDto> registrationDtoList = registrationService.getRegistrationsForEvent(eventId);
        return ResponseEntity.ok(registrationDtoList);
    }

    @PostMapping
    public ResponseEntity<RegistrationDto> createRegistration(@RequestBody RegistrationDto registrationDto) {
        RegistrationDto response = registrationService.createRegistration(registrationDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<RegistrationDto> confirmRegistration(@PathVariable Long id) {
        RegistrationDto response = registrationService.confirmRegistration(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RegistrationDto> cancelRegistration(@PathVariable Long id) {
        RegistrationDto response = registrationService.cancelRegistration(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.ok().build();
    }
}
