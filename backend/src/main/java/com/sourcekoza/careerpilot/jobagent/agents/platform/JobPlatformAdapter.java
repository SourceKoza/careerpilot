package com.sourcekoza.careerpilot.jobagent.agents.platform;

import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;

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
