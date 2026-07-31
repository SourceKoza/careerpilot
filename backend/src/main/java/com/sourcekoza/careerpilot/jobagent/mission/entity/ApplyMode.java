package com.sourcekoza.careerpilot.jobagent.mission.entity;

/**
 * Controls how the auto-apply pipeline behaves for a mission.
 *
 * <ul>
 *   <li>SEMI_AUTO: User reviews tailored resumes and approves before sending</li>
 *   <li>FULL_AUTO: Emails sent automatically for jobs scoring &gt;= 80%</li>
 * </ul>
 *
 * @since Sprint-16
 */
public enum ApplyMode {
    SEMI_AUTO,
    FULL_AUTO
}
