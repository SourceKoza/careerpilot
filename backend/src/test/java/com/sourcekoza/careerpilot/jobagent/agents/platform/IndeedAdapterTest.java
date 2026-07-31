package com.sourcekoza.careerpilot.jobagent.agents.platform;

import com.sourcekoza.careerpilot.jobagent.agents.platform.indeed.IndeedAdapter;
import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IndeedAdapterTest {

    private final IndeedAdapter adapter = new IndeedAdapter();

    @Test
    @DisplayName("platform returns INDEED")
    void platform() {
        assertThat(adapter.platform()).isEqualTo(PlatformType.INDEED);
    }

    @Test
    @DisplayName("search returns realistic mock data")
    void search_returnsMockData() {
        JobSearchRequest request = new JobSearchRequest("Java", "London", "Senior", false, null, 25);
        List<JobSearchResult> results = adapter.search(request);

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(r -> r.platform() == PlatformType.INDEED);
        assertThat(results).allMatch(r -> r.title() != null && !r.title().isBlank());
        assertThat(results).allMatch(r -> r.company() != null && !r.company().isBlank());
    }

    @Test
    @DisplayName("search includes recruiter contacts for some results")
    void search_includesContacts() {
        JobSearchRequest request = new JobSearchRequest("React", "Remote", null, true, null, 25);
        List<JobSearchResult> results = adapter.search(request);

        assertThat(results).anyMatch(r -> r.recruiterName() != null);
    }
}
