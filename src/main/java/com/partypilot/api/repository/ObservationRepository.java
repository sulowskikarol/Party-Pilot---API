package com.partypilot.api.repository;

import com.partypilot.api.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {

    List<Observation> findObservationsByUserId(Long userId);

    void deleteByEventId(Long eventId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
