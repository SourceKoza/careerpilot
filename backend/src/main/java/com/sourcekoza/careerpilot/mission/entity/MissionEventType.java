package com.sourcekoza.careerpilot.mission.entity;

/**
 * Types of events that can occur during mission lifecycle.
 *
 * @since Sprint-15
 */
public enum MissionEventType {
    MISSION_STARTED,
    SEARCH_STARTED,
    PLATFORM_STARTED,
    PLATFORM_COMPLETED,
    JOBS_DISCOVERED,
    CONTACTS_DISCOVERED,
    MISSION_COMPLETED,
    MISSION_FAILED
}
