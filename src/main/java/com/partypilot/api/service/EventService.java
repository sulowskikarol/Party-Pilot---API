package com.partypilot.api.service;

import com.partypilot.api.dto.EventShortDto;
import com.partypilot.api.mapper.EventMapper;
import com.partypilot.api.model.Event;
import com.partypilot.api.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
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
}
