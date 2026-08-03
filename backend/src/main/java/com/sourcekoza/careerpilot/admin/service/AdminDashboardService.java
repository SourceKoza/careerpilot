package com.sourcekoza.careerpilot.admin.service;

import com.sourcekoza.careerpilot.admin.dto.SystemStatsResponse;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.mission.repository.MissionRepository;
import org.springframework.stereotype.Service;

/**
 * Provides system-wide statistics for the admin dashboard.
 *
 * @since Sprint-17
 */
@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final DiscoveredJobRepository jobRepository;

    public AdminDashboardService(UserRepository userRepository,
                                  MissionRepository missionRepository,
                                  DiscoveredJobRepository jobRepository) {
        this.userRepository = userRepository;
        this.missionRepository = missionRepository;
        this.jobRepository = jobRepository;
    }

    public SystemStatsResponse getStats() {
        long totalUsers = userRepository.count();
        long totalMissions = missionRepository.count();
        long totalJobs = jobRepository.count();
        // Approximate active users and applications
        return new SystemStatsResponse(totalUsers, totalMissions, totalJobs, 0, totalUsers);
    }
}
