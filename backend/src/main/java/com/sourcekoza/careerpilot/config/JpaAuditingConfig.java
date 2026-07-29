package com.sourcekoza.careerpilot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA Auditing for automatic population of @CreatedDate and @LastModifiedDate fields.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
