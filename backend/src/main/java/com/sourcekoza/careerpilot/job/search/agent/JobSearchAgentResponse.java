package com.sourcekoza.careerpilot.job.search.agent;

import com.sourcekoza.careerpilot.job.search.model.JobSearchResult;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Response from the JobSearchAgent containing execution summary and results.
 *
 * <p>This is the structured response returned by the job search REST endpoint.
 * It includes execution metadata, the normalized job list, and persistence summary.</p>
 *
 * @param success        whether the search completed successfully
 * @param message        human-readable summary of the execution
 * @param totalJobs      total number of jobs found across all sites
 * @param persistedJobs  number of jobs successfully persisted to the database
 * @param sitesSearched  list of job site names that were searched
 * @param jobs           the normalized job search results
 * @param durationMs     total execution time in milliseconds
 * @param executedAt     when the search was executed
 * @since Sprint-14
 */
public record JobSearchAgentResponse(
        boolean success,
        String message,
        int totalJobs,
        int persistedJobs,
        List<String> sitesSearched,
        List<JobSearchResult> jobs,
        long durationMs,
        Instant executedAt
) {

    /**
     * Creates a successful response.
     */
    public static JobSearchAgentResponse success(List<JobSearchResult> jobs,
                                                  int persistedCount,
                                                  List<String> sitesSearched,
                                                  Duration duration,
                                                  Instant executedAt) {
        String message = String.format("Job search completed: %d jobs found, %d persisted from %s",
                jobs.size(), persistedCount, sitesSearched);
        return new JobSearchAgentResponse(
                true, message, jobs.size(), persistedCount,
                sitesSearched, jobs, duration.toMillis(), executedAt);
    }

    /**
     * Creates a failure response.
     */
    public static JobSearchAgentResponse failure(String message, Duration duration, Instant executedAt) {
        return new JobSearchAgentResponse(
                false, message, 0, 0, List.of(), List.of(),
                duration.toMillis(), executedAt);
    }
}
