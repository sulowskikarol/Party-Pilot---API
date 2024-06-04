package com.partypilot.api.controller;

import com.partypilot.api.dto.ObservationDto;
import com.partypilot.api.repository.ObservationRepository;
import com.partypilot.api.service.ObservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observations")
public class ObservationController {
    private final ObservationService observationService;

    public ObservationController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @GetMapping
    public ResponseEntity<List<ObservationDto>> getUserObservations() {
        return ResponseEntity.ok(observationService.getUserObservations());
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<ObservationDto> addObservation(@PathVariable Long eventId) {
        return ResponseEntity.ok(observationService.addObservation(eventId));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteObservation(@PathVariable Long eventId) {
        observationService.deleteObservation(eventId);
        return ResponseEntity.ok().build();
    }
}
