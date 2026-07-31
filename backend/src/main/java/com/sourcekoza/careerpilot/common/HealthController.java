package com.sourcekoza.careerpilot.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health check controller providing application status information.
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Application health endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns application health status")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "CareerPilot AI",
                "version", "0.1.0",
                "timestamp", Instant.now().toString()
        ));
    }
}
