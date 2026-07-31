package com.sourcekoza.careerpilot.resume.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Response after uploading a resume file.
 *
 * @since Sprint-15
 */
public record ResumeFileUploadResponse(
        UUID id,
        String name,
        String fileName,
        String fileType,
        long fileSize,
        Instant uploadedAt,
        Instant updatedAt,
        boolean isMaster
) {
}
