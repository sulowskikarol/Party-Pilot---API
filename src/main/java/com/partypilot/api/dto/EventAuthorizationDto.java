package com.partypilot.api.dto;

public class EventAuthorizationDto {
    private boolean isRegistered;
    private boolean isApproved;
    private boolean isOrganizer;

    public EventAuthorizationDto(boolean isRegistered, boolean isApproved, boolean isOrganizer) {
        this.isRegistered = isRegistered;
        this.isApproved = isApproved;
        this.isOrganizer = isOrganizer;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }
}
