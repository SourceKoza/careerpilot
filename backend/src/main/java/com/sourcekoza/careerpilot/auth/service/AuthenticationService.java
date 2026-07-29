package com.sourcekoza.careerpilot.auth.service;

import com.sourcekoza.careerpilot.auth.domain.Role;
import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.dto.AuthResponse;
import com.sourcekoza.careerpilot.auth.dto.LoginRequest;
import com.sourcekoza.careerpilot.auth.dto.RegisterRequest;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service handling user registration and login operations.
 */
@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the system.
     *
     * @param request registration details
     * @return authentication response with JWT token
     * @throws BusinessException if email is already registered
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email is already registered");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", request.email());

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request login credentials
     * @return authentication response with JWT token
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        log.info("User logged in successfully: {}", request.email());

        String token = jwtService.generateToken(request.email());
        return new AuthResponse(token);
    }
}
