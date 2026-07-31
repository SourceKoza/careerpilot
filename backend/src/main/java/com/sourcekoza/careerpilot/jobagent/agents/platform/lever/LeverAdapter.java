package com.sourcekoza.careerpilot.jobagent.agents.platform.lever;

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
 * Lever platform adapter returning realistic mock data.
 *
 * @since Sprint-15
 */
@Component
public class LeverAdapter implements JobPlatformAdapter {

    private static final Logger log = LoggerFactory.getLogger(LeverAdapter.class);

    @Override
    public PlatformType platform() {
        return PlatformType.LEVER;
    }

    @Override
    public List<JobSearchResult> search(JobSearchRequest request) {
        log.info("Lever adapter searching: keywords='{}'", request.keywords());
        String kw = request.keywords();
        String loc = request.location() != null ? request.location() : "London, UK";
        List<JobSearchResult> results = new ArrayList<>();

        results.add(JobSearchResult.builder()
                .platform(PlatformType.LEVER)
                .externalJobId("lever-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Senior " + kw + " Developer")
                .company("FinTech Solutions Ltd")
                .location(loc)
                .salary("£90,000 - £120,000/year")
                .description("FinTech Solutions is building next-gen payment infrastructure. "
                        + "Role: Senior " + kw + " Developer working on real-time trading systems. "
                        + "Requirements: " + kw + ", event sourcing, CQRS, low-latency systems. "
                        + "Benefits: 25 days PTO, private healthcare, stock options. "
                        + "Contact: david.williams@fintechsolutions.co.uk")
                .jobUrl("https://jobs.lever.co/fintechsolutions/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("David Williams")
                
                .recruiterRole("Engineering Director")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.LEVER)
                .externalJobId("lever-" + UUID.randomUUID().toString().substring(0, 8))
                .title(kw + " Backend Engineer - HealthTech")
                .company("MedConnect Pro")
                .location("Boston, MA")
                .salary("$145,000 - $175,000/year")
                .description("MedConnect Pro is revolutionizing healthcare data exchange. "
                        + "We need a " + kw + " Backend Engineer to build HIPAA-compliant APIs. "
                        + "Stack: " + kw + ", Spring Boot, PostgreSQL, FHIR, HL7. "
                        + "Impact: Your code will serve 50M+ patient records. "
                        + "Apply: hiring@medconnectpro.com")
                .jobUrl("https://jobs.lever.co/medconnect/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("Dr. Rachel Green")
                
                .recruiterRole("VP Product & Engineering")
                .build());

        log.info("Lever adapter completed: {} results", results.size());
        return results;
    }
}
