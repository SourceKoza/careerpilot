package com.sourcekoza.careerpilot.resume.dto;

import java.util.List;

/**
 * Response containing AI analysis of a resume.
 *
 * @since Sprint-15
 */
public record ResumeAnalysisResponse(
        ATSScore atsScore,
        List<String> strengths,
        List<SkillGap> missingSkills,
        List<Suggestion> suggestions,
        int keywordMatch
) {

    public record ATSScore(int overall, int formatting, int keywords, int experience) {
    }

    public record SkillGap(String skill, String importance) {
    }

    public record Suggestion(String id, String text, String category) {
    }
}
