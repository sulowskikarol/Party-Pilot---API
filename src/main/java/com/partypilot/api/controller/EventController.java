package com.partypilot.api.controller;

import com.partypilot.api.dto.EventDto;
import com.partypilot.api.dto.EventShortDto;
import com.partypilot.api.model.Event;
import com.partypilot.api.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        return eventService.getEventByIdWithComments(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventShortDto> addEvent(@RequestParam("event") String eventStr,
                                                  @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Event event = eventService.createEventFromRequest(eventStr, file);
        EventShortDto savedEvent = eventService.saveEvent(event);
        return ResponseEntity.created(URI.create("/events/" + savedEvent.getId())).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> modifyEvent(@PathVariable Long id,
                                             @RequestParam("event") String eventStr,
                                             @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Event event = eventService.createEventFromRequest(eventStr, file);
        return eventService.updateEvent(id, event)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
