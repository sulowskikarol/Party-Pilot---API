package com.partypilot.api.service;

import com.partypilot.api.config.UserAuthProvider;
import com.partypilot.api.dto.EventAuthorizationDto;
import com.partypilot.api.dto.RegistrationDto;
import com.partypilot.api.exception.AppException;
import com.partypilot.api.mapper.RegistrationMapper;
import com.partypilot.api.model.Registration;
import com.partypilot.api.model.enums.RegistrationStatus;
import com.partypilot.api.repository.RegistrationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventService eventService;
    private final RegistrationMapper registrationMapper;
    private final UserAuthProvider userAuthProvider;

    public RegistrationService(RegistrationRepository registrationRepository, EventService eventService, RegistrationMapper registrationMapper, UserAuthProvider userAuthProvider) {
        this.registrationRepository = registrationRepository;
        this.eventService = eventService;
        this.registrationMapper = registrationMapper;
        this.userAuthProvider = userAuthProvider;
    }

    public List<RegistrationDto> getRegistrationsForEvent(Long eventId) {
        return registrationRepository.findRegistrationsByEventId(eventId)
                .stream().map(registrationMapper::registrationToDto)
                .collect(Collectors.toList());
    }

    public List<RegistrationDto> getConfirmedRegistrationsForEvent(Long eventId) {
        return registrationRepository.findRegistrationsByEventIdAndStatus(eventId, RegistrationStatus.CONFIRMED)
                .stream().map(registrationMapper::registrationToDto)
                .collect(Collectors.toList());
    }

    public RegistrationDto createRegistration(RegistrationDto registrationDto) {
        if (isUserRegisteredForEvent(registrationDto.getEventId())) {
            throw new AppException("User already registered", HttpStatus.CONFLICT);
        }
        registrationDto.setUserId(userAuthProvider.getUserIdFromToken());
        Registration registration = registrationMapper.dtoToRegistration(registrationDto);
        registration.setStatus(RegistrationStatus.PENDING);
        Registration saved = registrationRepository.save(registration);
        return registrationMapper.registrationToDto(saved);
    }

    public RegistrationDto confirmRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .map(registration -> {
                    registration.setStatus(RegistrationStatus.CONFIRMED);
                    return registrationMapper.registrationToDto(registrationRepository.save(registration));
                }).orElseThrow(() -> new AppException("Registration not found", HttpStatus.NOT_FOUND));
    }

    public RegistrationDto cancelRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .map(registration -> {
                    registration.setStatus(RegistrationStatus.CANCELED);
                    return registrationMapper.registrationToDto(registrationRepository.save(registration));
                }).orElseThrow(() -> new AppException("Registration not found", HttpStatus.NOT_FOUND));
    }

    public void deleteRegistration(Long eventId) {
        Long userId = userAuthProvider.getUserIdFromToken();
        registrationRepository.findByUserIdAndEventId(userId, eventId)
                        .map(registration -> {
                            registrationRepository.deleteById(registration.getId());
                            return null;
                        });
    }

    public boolean isUserApprovedForEvent(Long eventId) {
        Long userId = userAuthProvider.getUserIdFromToken();
        Optional<Registration> registration = registrationRepository.findByUserIdAndEventId(userId, eventId);
        return registration.isPresent() && registration.get().getStatus().equals(RegistrationStatus.CONFIRMED);
    }

    public boolean isUserRegisteredForEvent(Long eventId) {
        Long userId = userAuthProvider.getUserIdFromToken();
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }
}
