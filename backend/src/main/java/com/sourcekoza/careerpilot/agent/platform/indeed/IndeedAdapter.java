package com.sourcekoza.careerpilot.agent.platform.indeed;

import com.sourcekoza.careerpilot.agent.platform.JobPlatformAdapter;
import com.sourcekoza.careerpilot.agent.platform.JobSearchRequest;
import com.sourcekoza.careerpilot.agent.platform.JobSearchResult;
import com.sourcekoza.careerpilot.mission.entity.PlatformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Indeed platform adapter returning realistic mock data based on search criteria.
 *
 * <p>Data is dynamically generated from the mission keywords and location
 * so the results stored in DB are relevant to what the user searched for.</p>
 *
 * @since Sprint-15
 */
@Component
public class IndeedAdapter implements JobPlatformAdapter {

    private static final Logger log = LoggerFactory.getLogger(IndeedAdapter.class);

    @Override
    public PlatformType platform() {
        return PlatformType.INDEED;
    }

    @Override
    public List<JobSearchResult> search(JobSearchRequest request) {
        log.info("Indeed adapter searching: keywords='{}', location='{}', remote={}",
                request.keywords(), request.location(), request.remoteOnly());

        String kw = request.keywords();
        String loc = request.location() != null ? request.location() : "Remote";
        List<JobSearchResult> results = new ArrayList<>();

        results.add(JobSearchResult.builder()
                .platform(PlatformType.INDEED)
                .externalJobId("indeed-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Senior " + kw + " Engineer")
                .company("TechCorp Industries")
                .location(loc)
                .salary("$130,000 - $170,000/year")
                .description("We are seeking a Senior " + kw + " Engineer to join our platform team. "
                        + "You will design and implement scalable microservices, collaborate with cross-functional teams, "
                        + "and mentor junior developers. Requirements: 5+ years experience with " + kw + ", "
                        + "strong system design skills, experience with cloud platforms (AWS/GCP).")
                .jobUrl("https://www.indeed.com/viewjob?jk=" + UUID.randomUUID().toString().substring(0, 12))
                .recruiterName("Sarah Johnson")
                
                .recruiterRole("Technical Recruiter")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.INDEED)
                .externalJobId("indeed-" + UUID.randomUUID().toString().substring(0, 8))
                .title(kw + " Developer - Full Stack")
                .company("Innovation Labs Inc.")
                .location(request.remoteOnly() ? "Remote" : loc)
                .salary("$120,000 - $155,000/year")
                .description("Innovation Labs is hiring a " + kw + " Developer to build next-gen SaaS products. "
                        + "Tech stack: " + kw + ", React, PostgreSQL, Docker, Kubernetes. "
                        + "We offer flexible hours, equity, and full remote work. "
                        + "Contact: careers@innovationlabs.io")
                .jobUrl("https://www.indeed.com/viewjob?jk=" + UUID.randomUUID().toString().substring(0, 12))
                .recruiterName("Michael Chen")
                
                .recruiterRole("Engineering Manager")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.INDEED)
                .externalJobId("indeed-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Lead " + kw + " Architect")
                .company("DataFlow Systems")
                .location("San Francisco, CA")
                .salary("$165,000 - $210,000/year")
                .description("DataFlow Systems needs a Lead " + kw + " Architect to own our distributed data platform. "
                        + "You'll lead a team of 8 engineers, define technical roadmap, and drive architecture decisions. "
                        + "Must have: 8+ years " + kw + ", distributed systems, event-driven architecture, Kafka.")
                .jobUrl("https://www.indeed.com/viewjob?jk=" + UUID.randomUUID().toString().substring(0, 12))
                .recruiterName("Amanda Lopez")
                .recruiterRole("VP Engineering")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.INDEED)
                .externalJobId("indeed-" + UUID.randomUUID().toString().substring(0, 8))
                .title(kw + " Backend Engineer")
                .company("FinServe Global")
                .location(loc)
                .salary("$140,000 - $180,000/year")
                .description("FinServe Global is scaling its payments platform and needs a strong " + kw + " Backend Engineer. "
                        + "You'll work on high-throughput transaction processing, real-time fraud detection, and API design. "
                        + "Requirements: " + kw + ", microservices, Redis, PostgreSQL, CI/CD. "
                        + "Email: tech-recruiting@finserveglobal.com")
                .jobUrl("https://www.indeed.com/viewjob?jk=" + UUID.randomUUID().toString().substring(0, 12))
                .recruiterName("Robert Taylor")
                
                .recruiterRole("Senior Technical Recruiter")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.INDEED)
                .externalJobId("indeed-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Staff " + kw + " Engineer")
                .company("CloudScale AI")
                .location("Remote - US")
                .salary("$190,000 - $240,000/year")
                .description("Staff-level opportunity at CloudScale AI. Build ML infrastructure and data pipelines using " + kw + ". "
                        + "Team of 50+ engineers. Series C funded ($200M). "
                        + "Benefits: unlimited PTO, $5K learning budget, stock options.")
                .jobUrl("https://www.indeed.com/viewjob?jk=" + UUID.randomUUID().toString().substring(0, 12))
                .build());

        log.info("Indeed adapter completed: {} results for keywords='{}'", results.size(), kw);
        return results;
    }
}
