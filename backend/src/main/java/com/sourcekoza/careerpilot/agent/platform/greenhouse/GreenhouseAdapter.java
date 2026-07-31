package com.sourcekoza.careerpilot.agent.platform.greenhouse;

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
 * Greenhouse platform adapter returning realistic mock data.
 *
 * @since Sprint-15
 */
@Component
public class GreenhouseAdapter implements JobPlatformAdapter {

    private static final Logger log = LoggerFactory.getLogger(GreenhouseAdapter.class);

    @Override
    public PlatformType platform() {
        return PlatformType.GREENHOUSE;
    }

    @Override
    public List<JobSearchResult> search(JobSearchRequest request) {
        log.info("Greenhouse adapter searching: keywords='{}'", request.keywords());
        String kw = request.keywords();
        String loc = request.location() != null ? request.location() : "Austin, TX";
        List<JobSearchResult> results = new ArrayList<>();

        results.add(JobSearchResult.builder()
                .platform(PlatformType.GREENHOUSE)
                .externalJobId("gh-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Staff " + kw + " Engineer")
                .company("ScaleUp Inc.")
                .location(loc)
                .salary("$180,000 - $225,000/year")
                .description("ScaleUp Inc. is hiring a Staff " + kw + " Engineer to lead platform architecture. "
                        + "You'll own critical services handling 10M+ requests/day. "
                        + "Stack: " + kw + ", Kubernetes, Terraform, PostgreSQL, gRPC. "
                        + "Apply at: careers@scaleup.io")
                .jobUrl("https://boards.greenhouse.io/scaleup/jobs/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("Emily Park")
                
                .recruiterRole("VP of Engineering")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.GREENHOUSE)
                .externalJobId("gh-" + UUID.randomUUID().toString().substring(0, 8))
                .title(kw + " Platform Engineer")
                .company("CloudNative Co.")
                .location("Remote - US")
                .salary("$150,000 - $185,000/year")
                .description("CloudNative Co. needs a " + kw + " Platform Engineer to build and operate cloud infrastructure. "
                        + "Responsibilities: service mesh, observability, CI/CD pipelines, infrastructure as code. "
                        + "Experience needed: " + kw + ", AWS, Docker, Prometheus, Grafana.")
                .jobUrl("https://boards.greenhouse.io/cloudnative/jobs/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("James Wilson")
                .recruiterRole("Platform Team Lead")
                .build());

        results.add(JobSearchResult.builder()
                .platform(PlatformType.GREENHOUSE)
                .externalJobId("gh-" + UUID.randomUUID().toString().substring(0, 8))
                .title("Senior " + kw + " Engineer - AI Products")
                .company("NeuralPath AI")
                .location("New York, NY (Hybrid)")
                .salary("$170,000 - $210,000/year")
                .description("NeuralPath AI is building the next generation of AI-powered developer tools. "
                        + "We need a Senior " + kw + " Engineer to build backend services for our ML inference platform. "
                        + "Must have: " + kw + ", Python, FastAPI/Flask, model serving, vector databases.")
                .jobUrl("https://boards.greenhouse.io/neuralpath/jobs/" + UUID.randomUUID().toString().substring(0, 8))
                .recruiterName("Dr. Priya Sharma")
                
                .recruiterRole("CTO & Co-founder")
                .build());

        log.info("Greenhouse adapter completed: {} results", results.size());
        return results;
    }
}
