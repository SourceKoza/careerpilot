package com.sourcekoza.careerpilot.auth.dto;

import com.sourcekoza.careerpilot.auth.domain.Role;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO representing user information.
 * Passwords are never exposed.
 *
 * @param id        user's unique identifier
 * @param firstName user's first name
 * @param lastName  user's last name
 * @param email     user's email address
 * @param role      user's role
 * @param createdAt account creation timestamp
 */
public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Instant createdAt
) {
}
