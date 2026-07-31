import type { JobMatch, SkillComparison, Recommendation, CompatibilityScore } from "../types";

const mockJobMatch: JobMatch = {
  id: "match-1",
  jobTitle: "Senior Backend Engineer",
  company: "Stripe",
  matchScore: { overall: 92, ats: 88, skills: 94, experience: 91, education: 95, seniority: 90 },
  summary: "You satisfy most backend requirements. Adding Terraform and Kubernetes experience would significantly improve your chances. Your strong Spring Boot and distributed systems background is an excellent fit.",
  applyReadiness: "ready",
  skillComparison: {
    matched: ["Java", "Spring Boot", "Kafka", "Redis", "Docker", "PostgreSQL", "REST APIs", "Microservices", "Git", "CI/CD"],
    missing: ["Terraform", "AWS Lambda", "Kubernetes"],
    recommended: ["Go", "gRPC", "Prometheus"],
    preferred: ["GraphQL", "Event Sourcing", "CQRS"],
  },
  strengths: [
    { name: "Backend Architecture", category: "technical" },
    { name: "Distributed Systems", category: "technical" },
    { name: "API Design", category: "technical" },
    { name: "Spring Boot Expertise", category: "technical" },
    { name: "Financial Domain", category: "domain" },
    { name: "System Design", category: "technical" },
    { name: "Team Leadership", category: "soft" },
    { name: "Problem Solving", category: "soft" },
  ],
  gapAnalysis: {
    technicalGaps: ["Infrastructure as Code (Terraform)", "Serverless (AWS Lambda)", "Container Orchestration (Kubernetes)"],
    experienceGaps: ["No visible cloud-native production deployment at scale"],
    certificationSuggestions: ["AWS Solutions Architect", "Kubernetes CKA"],
    projectSuggestions: ["Deploy a microservice to EKS with Terraform", "Build a serverless event processor with Lambda"],
  },
  recommendations: [
    { id: "r1", text: "Add Kubernetes project deployment to experience section", priority: "high" },
    { id: "r2", text: "Mention Docker production deployments with metrics", priority: "high" },
    { id: "r3", text: "Include measurable business impact (e.g., reduced latency by 40%)", priority: "medium" },
    { id: "r4", text: "Add AI/ML project to demonstrate modern tech awareness", priority: "medium" },
    { id: "r5", text: "Improve professional summary with target role keywords", priority: "low" },
  ],
  compatibility: [
    { category: "Technical Skills", score: 94, maxScore: 100 },
    { category: "Experience", score: 91, maxScore: 100 },
    { category: "Education", score: 95, maxScore: 100 },
    { category: "Keywords", score: 85, maxScore: 100 },
    { category: "Domain Knowledge", score: 88, maxScore: 100 },
    { category: "Soft Skills", score: 90, maxScore: 100 },
  ],
};

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export const jobMatchingService = {
  async getJobMatch(): Promise<JobMatch> {
    await delay(800);
    return mockJobMatch;
  },

  async getSkillComparison(): Promise<SkillComparison> {
    await delay(500);
    return mockJobMatch.skillComparison;
  },

  async getRecommendations(): Promise<Recommendation[]> {
    await delay(500);
    return mockJobMatch.recommendations;
  },

  async getCompatibilityBreakdown(): Promise<CompatibilityScore[]> {
    await delay(500);
    return mockJobMatch.compatibility;
  },

  async analyzeJobMatch(): Promise<JobMatch> {
    await delay(2000);
    return mockJobMatch;
  },
};
