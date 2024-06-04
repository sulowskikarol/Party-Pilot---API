package com.partypilot.api.service;

import com.partypilot.api.dto.EventAuthorizationDto;
import org.springframework.stereotype.Service;

@Service
public class EventAuthorizationService {
    private final EventService eventService;
    private final RegistrationService registrationService;
    private final ObservationService observationService;

    public EventAuthorizationService(EventService eventService, RegistrationService registrationService, ObservationService observationService) {
        this.registrationService = registrationService;
        this.eventService = eventService;
        this.observationService = observationService;
    }

    public EventAuthorizationDto generateEventAuthorizationDto(Long eventId) {
        boolean isRegistered = registrationService.isUserRegisteredForEvent(eventId);
        boolean isApproved = registrationService.isUserApprovedForEvent(eventId);
        boolean isOrganizer = eventService.isOrganizer(eventId);
        boolean isObserving = observationService.checkIfObserving(eventId);

        return new EventAuthorizationDto(isRegistered, isApproved, isOrganizer, isObserving);
    }
}
