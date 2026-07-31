package com.sourcekoza.careerpilot.jobagent.agents.core;

import java.util.Optional;

/**
 * Interface for contact discovery capabilities (future implementation).
 *
 * @since Sprint-15
 */
public interface ContactDiscoveryService {

    Optional<ContactInfo> findRecruiter(String company, String jobTitle);

    Optional<ContactInfo> findHiringManager(String company, String department);

    Optional<String> findCompanyEmail(String company, String domain);

    record ContactInfo(String name, String email, String linkedInUrl, String role, double confidenceScore) {
    }
}
