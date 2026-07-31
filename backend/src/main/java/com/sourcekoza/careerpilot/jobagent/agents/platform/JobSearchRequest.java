package com.sourcekoza.careerpilot.jobagent.agents.platform;

/**
 * Request model for platform adapter job searches.
 *
 * @since Sprint-15
 */
public record JobSearchRequest(
        String keywords,
        String location,
        String experienceLevel,
        boolean remoteOnly,
        String employmentType,
        int maxResults
) {

    public static JobSearchRequest of(String keywords, String location,
                                       String experienceLevel, boolean remoteOnly) {
        return new JobSearchRequest(keywords, location, experienceLevel, remoteOnly, null, 25);
    }
}
