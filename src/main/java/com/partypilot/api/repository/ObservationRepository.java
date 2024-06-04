package com.partypilot.api.repository;

import com.partypilot.api.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {

    List<Observation> findObservationsByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByEventId(Long eventId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
