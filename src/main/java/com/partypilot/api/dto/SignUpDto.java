package com.partypilot.api.dto;

public record SignUpDto(String firstName, String lastName, String phoneNumber, String email, char[] password) {
}
