package com.sourcekoza.careerpilot.auth.controller;

import com.sourcekoza.careerpilot.auth.dto.UserResponse;
import com.sourcekoza.careerpilot.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User information endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Returns the authenticated user's information")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }
}
