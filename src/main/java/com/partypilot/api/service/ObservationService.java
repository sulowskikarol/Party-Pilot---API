package com.partypilot.api.service;

import com.partypilot.api.config.UserAuthProvider;
import com.partypilot.api.dto.ObservationDto;
import com.partypilot.api.mapper.ObservationMapper;
import com.partypilot.api.model.Observation;
import com.partypilot.api.repository.ObservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObservationService {
    private final UserAuthProvider userAuthProvider;
    private final ObservationRepository observationRepository;
    private final ObservationMapper observationMapper;

    public ObservationService(UserAuthProvider userAuthProvider, ObservationRepository observationRepository, ObservationMapper observationMapper) {
        this.userAuthProvider = userAuthProvider;
        this.observationRepository = observationRepository;
        this.observationMapper = observationMapper;
    }

    public List<ObservationDto> getUserObservations() {
        Long userId = userAuthProvider.getUserIdFromToken();
        return observationRepository.findObservationsByUserId(userId)
                .stream().map(observationMapper::observationToDto).toList();
    }

    public ObservationDto addObservation(Long eventId) {
        Long userId = userAuthProvider.getUserIdFromToken();
        Observation observation = observationMapper.dtoToObservation(new ObservationDto(eventId, userId));
        return observationMapper.observationToDto(observationRepository.save(observation));
    }

    public void deleteObservation(Long eventId) {
        observationRepository.deleteByEventId(eventId);
    }

    public boolean checkIfObserving(Long eventId) {
        Long userId = userAuthProvider.getUserIdFromToken();
        return observationRepository.existsByEventIdAndUserId(eventId, userId);
    }
}
