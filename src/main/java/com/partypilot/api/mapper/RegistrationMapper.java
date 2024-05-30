package com.partypilot.api.mapper;

import com.partypilot.api.dto.RegistrationDto;
import com.partypilot.api.exception.AppException;
import com.partypilot.api.model.Event;
import com.partypilot.api.model.Registration;
import com.partypilot.api.model.User;
import com.partypilot.api.repository.EventRepository;
import com.partypilot.api.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public abstract class RegistrationMapper {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EventRepository eventRepository;

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "eventId", target = "event")
    public abstract Registration dtoToRegistration(RegistrationDto registrationDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    public abstract RegistrationDto registrationToDto(Registration registration);

    protected User mapUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    protected Event mapEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException("Event not found", HttpStatus.NOT_FOUND));
    }
}
