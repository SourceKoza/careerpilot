package com.sourcekoza.careerpilot.admin.repository;

import com.sourcekoza.careerpilot.admin.entity.SecurityConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @since Sprint-17
 */
public interface SecurityConfigRepository extends JpaRepository<SecurityConfig, UUID> {
    Optional<SecurityConfig> findByConfigKey(String configKey);
}
