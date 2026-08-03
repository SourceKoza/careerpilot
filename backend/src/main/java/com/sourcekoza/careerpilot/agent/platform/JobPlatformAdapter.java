package com.sourcekoza.careerpilot.agent.platform;

import com.sourcekoza.careerpilot.mission.entity.PlatformType;

import java.util.List;

/**
 * Contract for job platform adapters.
 *
 * @since Sprint-15
 */
public interface JobPlatformAdapter {

    PlatformType platform();

    List<JobSearchResult> search(JobSearchRequest request);
}
