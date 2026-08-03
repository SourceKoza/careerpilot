package com.sourcekoza.careerpilot.admin.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @since Sprint-17
 */
public record GlobalSettingUpdateRequest(@NotBlank String value) {}
