package com.sourcekoza.careerpilot.auth.dto;

/**
 * Response DTO returned after successful authentication.
 *
 * @param token JWT access token
 * @param type  token type (always "Bearer")
 */
public record AuthResponse(
        String token,
        String type
) {
    public AuthResponse(String token) {
        this(token, "Bearer");
    }
}
