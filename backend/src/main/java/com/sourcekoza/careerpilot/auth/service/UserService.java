package com.sourcekoza.careerpilot.auth.service;

import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.dto.UserResponse;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for user-related operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves user information by email.
     *
     * @param email the user's email
     * @return user response DTO (password never exposed)
     * @throws ResourceNotFoundException if user not found
     */
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
