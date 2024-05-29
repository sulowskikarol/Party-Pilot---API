package com.partypilot.api.repository;

import com.partypilot.api.model.Event;
import com.partypilot.api.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findRegistrationsByEventId(Long eventId);
}
