package com.sourcekoza.careerpilot.job.search.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Search criteria for job searching operations.
 *
 * <p>Encapsulates the parameters needed to search for jobs across
 * multiple job portals. All JobSite implementations receive this
 * model to execute their site-specific search logic.</p>
 *
 * @param keyword        the search keyword (e.g. "java developer")
 * @param location       preferred location (e.g. "London", "Remote")
 * @param remoteOnly     whether to filter for remote-only positions
 * @param employmentType preferred employment type (e.g. "FULL_TIME")
 * @param page           page number for pagination (1-based)
 * @param size           number of results per page
 * @since Sprint-14
 */
public record JobSearchCriteria(

        @NotBlank(message = "Keyword is required")
        @Size(max = 200, message = "Keyword must not exceed 200 characters")
        String keyword,

        @Size(max = 200, message = "Location must not exceed 200 characters")
        String location,

        Boolean remoteOnly,

        @Size(max = 30, message = "Employment type must not exceed 30 characters")
        String employmentType,

        @Min(value = 1, message = "Page must be at least 1")
        Integer page,

        @Min(value = 1, message = "Size must be at least 1")
        @Max(value = 100, message = "Size must not exceed 100")
        Integer size
) {

    /**
     * Returns the page number, defaulting to 1 if not specified.
     */
    public int pageOrDefault() {
        return page != null ? page : 1;
    }

    /**
     * Returns the page size, defaulting to 25 if not specified.
     */
    public int sizeOrDefault() {
        return size != null ? size : 25;
    }
}
