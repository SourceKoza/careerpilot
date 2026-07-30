/**
 * Job search agent — the first production AI business agent.
 *
 * <p>The JobSearchAgent receives search criteria, delegates to JobSiteManager,
 * normalizes and persists results through JobService, and returns a structured
 * response. Per ADR-006, it communicates directly with Application Services.</p>
 *
 * @since Sprint-14
 */
package com.sourcekoza.careerpilot.job.search.agent;
