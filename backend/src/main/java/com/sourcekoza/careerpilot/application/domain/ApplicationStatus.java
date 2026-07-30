package com.sourcekoza.careerpilot.application.domain;

/**
 * Enumeration of job application lifecycle statuses.
 */
public enum ApplicationStatus {
    DRAFT,
    READY,
    SUBMITTED,
    INTERVIEW,
    ASSESSMENT,
    OFFER,
    REJECTED,
    WITHDRAWN
}
