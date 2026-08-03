package com.sourcekoza.careerpilot.admin.service;

import com.sourcekoza.careerpilot.admin.dto.AdminUserResponse;
import com.sourcekoza.careerpilot.auth.domain.Role;
import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Admin user management service.
 *
 * @since Sprint-17
 */
@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<AdminUserResponse> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    public AdminUserResponse getUser(UUID userId) {
        return toResponse(findUser(userId));
    }

    @Transactional
    public AdminUserResponse changeRole(UUID userId, Role role) {
        User user = findUser(userId);
        user.setRole(role);
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public AdminUserResponse disableUser(UUID userId) {
        User user = findUser(userId);
        user.setEnabled(false);
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public AdminUserResponse enableUser(UUID userId) {
        User user = findUser(userId);
        user.setEnabled(true);
        return toResponse(userRepository.save(user));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private AdminUserResponse toResponse(User u) {
        return new AdminUserResponse(u.getId(), u.getFirstName(), u.getLastName(),
                u.getEmail(), u.getRole().name(), u.isEnabled(), u.getCreatedAt());
    }
}
