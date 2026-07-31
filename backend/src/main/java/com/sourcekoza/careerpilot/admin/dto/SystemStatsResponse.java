package com.sourcekoza.careerpilot.admin.dto;

/**
 * @since Sprint-17
 */
public record SystemStatsResponse(
        long totalUsers,
        long totalMissions,
        long totalJobs,
        long totalApplicationsSent,
        long activeUsers
) {}
