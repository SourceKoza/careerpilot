package com.sourcekoza.careerpilot.agent.tailoring;

import java.util.List;

/**
 * Structured content from the LLM resume tailoring process.
 *
 * @param summary the tailored professional summary
 * @param skills ordered list of skills (matching ones first)
 * @param experiences experience bullets highlighting relevant work
 * @param education education entries (unchanged from original)
 * @since Sprint-16
 */
public record TailoredResumeContent(
        String summary,
        List<String> skills,
        List<ExperienceEntry> experiences,
        List<EducationEntry> education
) {

    public record ExperienceEntry(
            String company,
            String position,
            String location,
            String startDate,
            String endDate,
            String description
    ) {
    }

    public record EducationEntry(
            String institution,
            String degree,
            String fieldOfStudy,
            String startDate,
            String endDate
    ) {
    }
}
