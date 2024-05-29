package com.partypilot.api.service;

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
    private final RegistrationMapper registrationMapper;

    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
    }

    public List<RegistrationDto> getRegistrationsForEvent(Long eventId) {
        return registrationRepository.findRegistrationsByEventId(eventId)
                .stream().map(registrationMapper::registrationToDto)
                .collect(Collectors.toList());
    }

    public RegistrationDto createRegistration(RegistrationDto registrationDto) {
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

    public void deleteRegistration(Long registrationId) {
        registrationRepository.deleteById(registrationId);
    }
}
