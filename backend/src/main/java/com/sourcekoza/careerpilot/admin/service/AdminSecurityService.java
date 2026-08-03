package com.sourcekoza.careerpilot.admin.service;

import com.sourcekoza.careerpilot.admin.dto.SecurityConfigResponse;
import com.sourcekoza.careerpilot.admin.entity.SecurityConfig;
import com.sourcekoza.careerpilot.admin.repository.SecurityConfigRepository;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Manages security configuration settings.
 *
 * @since Sprint-17
 */
@Service
public class AdminSecurityService {

    private final SecurityConfigRepository repository;

    public AdminSecurityService(SecurityConfigRepository repository) {
        this.repository = repository;
    }

    public List<SecurityConfigResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public SecurityConfigResponse update(String key, String value, Boolean enabled) {
        SecurityConfig config = repository.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("SecurityConfig", "configKey", key));
        if (value != null) config.setConfigValue(value);
        if (enabled != null) config.setEnabled(enabled);
        return toResponse(repository.save(config));
    }

    private SecurityConfigResponse toResponse(SecurityConfig c) {
        return new SecurityConfigResponse(c.getId(), c.getConfigKey(), c.getConfigValue(),
                c.isEnabled(), c.getUpdatedAt());
    }
}
