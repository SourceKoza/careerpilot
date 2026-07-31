package com.sourcekoza.careerpilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * CareerPilot AI - Main Application Entry Point.
 *
 * <p>AI-powered Job Search and Application Automation Platform.</p>
 */
@SpringBootApplication
@EnableAsync
public class CareerPilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerPilotApplication.class, args);
    }
}
