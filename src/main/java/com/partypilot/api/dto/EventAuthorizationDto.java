package com.partypilot.api.dto;

public record EventAuthorizationDto(
        boolean isRegistered,
        boolean isApproved,
        boolean isOrganizer,
        boolean isObserving
) {}
