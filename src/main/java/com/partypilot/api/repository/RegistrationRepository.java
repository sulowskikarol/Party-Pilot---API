package com.partypilot.api.repository;

import com.partypilot.api.model.Event;
import com.partypilot.api.model.Registration;
import com.partypilot.api.model.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findRegistrationsByEventId(Long eventId);
    List<Registration> findRegistrationsByEventIdAndStatus(Long eventId, RegistrationStatus status);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
}
