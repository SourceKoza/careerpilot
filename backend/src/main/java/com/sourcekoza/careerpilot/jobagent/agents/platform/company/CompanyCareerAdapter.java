package com.sourcekoza.careerpilot.jobagent.agents.platform.company;

import com.sourcekoza.careerpilot.jobagent.agents.platform.JobPlatformAdapter;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobSearchRequest;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobSearchResult;
import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Company career page adapter returning realistic mock data.
 *
 * @since Sprint-15
 */
@Component
public class CompanyCareerAdapter implements JobPlatformAdapter {

    private static final Logger log = LoggerFactory.getLogger(CompanyCareerAdapter.class);

    @Override
    public PlatformType platform() {
        return PlatformType.COMPANY_CAREER;
    }

    @Override
    public List<JobSearchResult> search(JobSearchRequest request) {
        log.info("Company Career adapter searching: keywords='{}'", request.keywords());
        String kw = request.keywords();
        String loc = request.location() != null ? request.location() : "Seattle, WA";
        List<JobSearchResult> results = new ArrayList<>();

        results.add(JobSearchResult.builder()
                .platform(PlatformType.COMPANY_CAREER)
                .externalJobId("career-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Principal " + kw + " Engineer")
                .company("MegaCorp Technologies")
                .location(loc)
                .salary("$210,000 - $280,000/year + RSU")
                .description("MegaCorp Technologies is hiring a Principal " + kw + " Engineer. "
                        + "You will define technical vision for a 200+ engineer organization. "
                        + "Responsibilities: architecture reviews, tech strategy, cross-team alignment. "
                        + "Requirements: 10+ years " + kw + ", distributed systems at scale, leadership experience. "
                        + "Direct report to CTO. Apply: principal-hiring@megacorp.tech")
                .jobUrl("https://careers.megacorp.tech/jobs/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("Lisa Thompson")
                
                .recruiterRole("Executive Recruiter")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.COMPANY_CAREER)
                .externalJobId("career-" + UUID.randomUUID().toString().substring(0, 8))
                .title(kw + " Engineer - Core Platform")
                .company("Stripe")
                .location("Remote (US/EU)")
                .salary("$180,000 - $230,000/year")
                .description("Stripe's Core Platform team builds the foundation that powers global payments. "
                        + "We're looking for a " + kw + " Engineer who can design resilient, high-throughput systems. "
                        + "What you'll do: build APIs processing $billions, optimize for p99 latency, scale globally. "
                        + "Stack: " + kw + ", Ruby, Go, AWS, MySQL. "
                        + "Contact: platform-hiring@stripe.com")
                .jobUrl("https://stripe.com/jobs/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("Alex Rivera")
                
                .recruiterRole("Technical Sourcer")
                .build());

        log.info("Company Career adapter completed: {} results", results.size());
        return results;
    }
}
