package com.partypilot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.partypilot.api.dto.CommentDto;
import com.partypilot.api.dto.EventDto;
import com.partypilot.api.dto.EventShortDto;
import com.partypilot.api.mapper.CommentMapper;
import com.partypilot.api.mapper.EventMapper;
import com.partypilot.api.model.Event;
import com.partypilot.api.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;
    private static final String UPLOAD_FOLDER = "src/main/resources/static/banners/";

    public EventService(EventRepository eventRepository, EventMapper eventMapper, CommentMapper commentMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.commentMapper = commentMapper;
    }

    public List<EventShortDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<EventDto> getEventByIdWithComments(Long id) {
        return eventRepository.findByIdWithComments(id)
                .map(event -> {
                    EventDto eventDto = eventMapper.toEventDto(event);
                    List<CommentDto> commentDtos = event.getComments().stream()
                            .map(commentMapper::commentToDto).toList();
                    eventDto.setComments(commentDtos);
                    return eventDto;
                });
    }

    public EventShortDto saveEvent(Event event) {
        return eventMapper.toEventShortDto(eventRepository.save(event));
    }

    public Optional<Event> updateEvent(Long id, Event event) {
        return eventRepository.findById(id)
                .map(eventFromDb -> {
                    eventFromDb.setEventName(event.getEventName());
                    eventFromDb.setDescription(event.getDescription());
                    eventFromDb.setStartTime(event.getStartTime());
                    eventFromDb.setEndTime(event.getEndTime());
                    eventFromDb.setLocation(event.getLocation());
                    eventFromDb.setBannerPath(event.getBannerPath());
                    return eventRepository.save(eventFromDb);
                });
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Event createEventFromRequest(String eventStr, MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Event event = mapper.readValue(eventStr, Event.class);

        if (file != null && !file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            event.setBannerPath(file.getOriginalFilename());
        }

        return event;
    }
}
