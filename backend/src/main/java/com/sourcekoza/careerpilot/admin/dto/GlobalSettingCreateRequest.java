package com.sourcekoza.careerpilot.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @since Sprint-17
 */
public record GlobalSettingCreateRequest(
        @NotBlank @Size(max = 100) String key,
        @NotBlank String value,
        @Size(max = 50) String category,
        @Size(max = 500) String description
) {}
