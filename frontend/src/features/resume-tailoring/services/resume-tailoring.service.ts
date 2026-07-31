import type {
  TailoredResume,
  ResumeComparison,
  ResumeVersion,
  ExportResponse,
  ResumeSection,
  ResumeChange,
  KeywordAnalysis,
  ATSImprovement,
  TailoringScore,
  AISuggestion,
  TailoredExperience,
  TailoredProject,
} from "../types";

function delay(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

const mockSections: ResumeSection[] = [
  {
    title: "Professional Summary",
    original:
      "Experienced backend engineer with 6+ years building scalable systems using Java, Spring Boot, and microservices architecture.",
    tailored:
      "Senior Backend Engineer with 6+ years delivering high-throughput distributed systems using Java 21, Spring Boot 3, Kafka, and Kubernetes. Proven track record reducing API latency by 40% and scaling platforms to handle 10M+ daily transactions.",
    changeType: "improved",
  },
  {
    title: "Skills",
    original: "Java, Spring Boot, PostgreSQL, Redis, Docker, REST APIs, Git, Maven",
    tailored:
      "Java 21, Spring Boot 3, Kafka, Redis, PostgreSQL, Docker, Kubernetes, Terraform, REST APIs, gRPC, Microservices, CI/CD, AWS, Prometheus, Grafana",
    changeType: "improved",
  },
  {
    title: "Experience - Senior Engineer",
    original:
      "Built microservices for payment processing. Implemented caching layer. Maintained CI/CD pipelines.",
    tailored:
      "Architected event-driven payment processing system handling 2M+ daily transactions using Kafka and Spring Boot. Reduced API response time by 40% through Redis caching strategy. Designed and maintained CI/CD pipelines deploying 15+ microservices to Kubernetes.",
    changeType: "improved",
  },
  {
    title: "Projects",
    original: "E-commerce Platform - Built REST APIs for product catalog and order management.",
    tailored:
      "AI-Powered E-commerce Platform - Designed distributed REST/gRPC APIs serving 50K concurrent users. Implemented event sourcing with Kafka for real-time inventory management. Deployed to AWS EKS with Terraform IaC.",
    changeType: "improved",
  },
  {
    title: "Achievements",
    original: "Improved system performance. Mentored junior developers.",
    tailored:
      "Reduced system latency by 40% serving 10M+ daily requests. Mentored 5 engineers resulting in 2 promotions. Led migration of monolith to 12 microservices achieving 99.99% uptime.",
    changeType: "improved",
  },
];

const mockChanges: ResumeChange[] = [
  {
    id: "ch-1",
    section: "Professional Summary",
    description: "Added quantifiable metrics and target role keywords",
    type: "rewrite",
    impact: "high",
  },
  {
    id: "ch-2",
    section: "Skills",
    description: "Added Kafka, Kubernetes, Terraform, gRPC, and AWS",
    type: "addition",
    impact: "high",
  },
  {
    id: "ch-3",
    section: "Experience",
    description: "Quantified achievements with specific metrics",
    type: "modification",
    impact: "high",
  },
  {
    id: "ch-4",
    section: "Projects",
    description: "Enhanced with scale metrics and modern tech stack",
    type: "modification",
    impact: "medium",
  },
  {
    id: "ch-5",
    section: "Achievements",
    description: "Added measurable business impact",
    type: "rewrite",
    impact: "high",
  },
  {
    id: "ch-6",
    section: "Keywords",
    description: "Optimized keyword density for ATS parsing",
    type: "addition",
    impact: "medium",
  },
];

const mockKeywordAnalysis: KeywordAnalysis = {
  matched: ["Java", "Spring Boot", "Microservices", "REST APIs", "Docker", "PostgreSQL", "Redis", "CI/CD"],
  missing: ["Terraform", "AWS Lambda", "Machine Learning"],
  added: ["Kafka", "Kubernetes", "gRPC", "Prometheus", "Grafana", "AWS EKS", "Event Sourcing"],
  recommended: ["System Design", "Distributed Systems", "High Availability", "Observability"],
  density: 4.2,
};

const mockATSImprovement: ATSImprovement = {
  originalScore: 62,
  tailoredScore: 94,
  improvementPercentage: 52,
  factors: [
    { name: "Keyword Match", originalScore: 58, tailoredScore: 96 },
    { name: "Format Compliance", originalScore: 75, tailoredScore: 92 },
    { name: "Content Relevance", originalScore: 60, tailoredScore: 95 },
    { name: "Section Structure", originalScore: 70, tailoredScore: 90 },
    { name: "Action Verbs", originalScore: 55, tailoredScore: 93 },
  ],
};

const mockTailoringScore: TailoringScore = {
  overall: 91,
  keywordMatch: 96,
  formatCompliance: 92,
  contentRelevance: 88,
  experienceAlignment: 89,
};

const mockSuggestions: AISuggestion[] = [
  { id: "s-1", text: "Improve summary to highlight Kafka expertise", category: "summary", priority: "high", applied: true },
  { id: "s-2", text: "Mention Docker production deployment with metrics", category: "experience", priority: "high", applied: true },
  { id: "s-3", text: "Include Kubernetes cluster management experience", category: "experience", priority: "high", applied: false },
  { id: "s-4", text: "Quantify achievements with specific percentages", category: "achievements", priority: "medium", applied: true },
  { id: "s-5", text: "Add AI/ML platform project to demonstrate modern awareness", category: "projects", priority: "medium", applied: false },
  { id: "s-6", text: "Mention system design for distributed systems", category: "skills", priority: "low", applied: false },
];

const mockExperience: TailoredExperience[] = [
  {
    title: "Senior Backend Engineer",
    company: "FinTech Corp",
    duration: "2021 - Present",
    highlights: [
      "Architected event-driven payment processing system handling 2M+ daily transactions using Kafka and Spring Boot 3",
      "Reduced API response time by 40% through Redis caching strategy serving 10M+ daily requests",
      "Designed and maintained CI/CD pipelines deploying 15+ microservices to Kubernetes on AWS EKS",
      "Led migration of monolith to microservices achieving 99.99% uptime with zero-downtime deployments",
    ],
  },
  {
    title: "Backend Engineer",
    company: "TechStart Inc",
    duration: "2019 - 2021",
    highlights: [
      "Built RESTful APIs serving 500K daily users with Spring Boot and PostgreSQL",
      "Implemented distributed caching with Redis reducing database load by 60%",
      "Designed event-driven notification system with Kafka processing 1M+ events daily",
      "Mentored 3 junior developers and established code review standards",
    ],
  },
];

const mockProjects: TailoredProject[] = [
  {
    name: "AI-Powered E-commerce Platform",
    description:
      "Distributed REST/gRPC APIs serving 50K concurrent users with event sourcing for real-time inventory management",
    technologies: ["Java 21", "Spring Boot 3", "Kafka", "Redis", "PostgreSQL", "Kubernetes", "AWS"],
  },
  {
    name: "Real-time Analytics Pipeline",
    description:
      "Stream processing platform ingesting 5M+ events/hour for business intelligence dashboards",
    technologies: ["Kafka Streams", "Spring Cloud", "Prometheus", "Grafana", "Docker"],
  },
];

const mockVersions: ResumeVersion[] = [
  {
    id: "v-1",
    name: "Master Resume",
    type: "master",
    createdAt: "2024-01-15T10:00:00Z",
    updatedAt: "2024-03-20T14:30:00Z",
    isActive: false,
    atsScore: 62,
  },
  {
    id: "v-2",
    name: "Stripe - Senior Backend Engineer",
    type: "tailored",
    targetCompany: "Stripe",
    targetRole: "Senior Backend Engineer",
    createdAt: "2024-03-21T09:00:00Z",
    updatedAt: "2024-03-21T09:00:00Z",
    isActive: true,
    atsScore: 94,
  },
  {
    id: "v-3",
    name: "Netflix - Platform Engineer",
    type: "company-specific",
    targetCompany: "Netflix",
    targetRole: "Platform Engineer",
    createdAt: "2024-03-18T11:00:00Z",
    updatedAt: "2024-03-19T16:45:00Z",
    isActive: false,
    atsScore: 88,
  },
  {
    id: "v-4",
    name: "Google - Backend SWE",
    type: "tailored",
    targetCompany: "Google",
    targetRole: "Software Engineer, Backend",
    createdAt: "2024-03-10T08:00:00Z",
    updatedAt: "2024-03-10T08:00:00Z",
    isActive: false,
    atsScore: 85,
  },
];

const mockComparison: ResumeComparison = {
  id: "comp-1",
  sections: mockSections,
  changes: mockChanges,
  keywordAnalysis: mockKeywordAnalysis,
  atsImprovement: mockATSImprovement,
  tailoringScore: mockTailoringScore,
  suggestions: mockSuggestions,
};

const mockTailoredResume: TailoredResume = {
  id: "tr-1",
  originalResumeId: "v-1",
  jobTitle: "Senior Backend Engineer",
  company: "Stripe",
  summary:
    "Senior Backend Engineer with 6+ years delivering high-throughput distributed systems using Java 21, Spring Boot 3, Kafka, and Kubernetes. Proven track record reducing API latency by 40% and scaling platforms to handle 10M+ daily transactions.",
  skills: [
    "Java 21",
    "Spring Boot 3",
    "Kafka",
    "Redis",
    "PostgreSQL",
    "Docker",
    "Kubernetes",
    "Terraform",
    "REST APIs",
    "gRPC",
    "Microservices",
    "CI/CD",
    "AWS",
    "Prometheus",
    "Grafana",
  ],
  experience: mockExperience,
  projects: mockProjects,
  comparison: mockComparison,
  versions: mockVersions,
  createdAt: "2024-03-21T09:00:00Z",
};

export const resumeTailoringService = {
  async getMasterResume(): Promise<TailoredResume> {
    await delay(800);
    return {
      ...mockTailoredResume,
      id: "master-1",
      summary:
        "Experienced backend engineer with 6+ years building scalable systems using Java, Spring Boot, and microservices architecture.",
      skills: ["Java", "Spring Boot", "PostgreSQL", "Redis", "Docker", "REST APIs", "Git", "Maven"],
    };
  },

  async generateTailoredResume(): Promise<TailoredResume> {
    await delay(2000);
    return mockTailoredResume;
  },

  async compareResume(): Promise<ResumeComparison> {
    await delay(1000);
    return mockComparison;
  },

  async getResumeVersions(): Promise<ResumeVersion[]> {
    await delay(600);
    return mockVersions;
  },

  async saveResumeVersion(version: Partial<ResumeVersion>): Promise<ResumeVersion> {
    await delay(500);
    return {
      id: `v-${Date.now()}`,
      name: version.name ?? "Untitled Version",
      type: version.type ?? "tailored",
      targetCompany: version.targetCompany,
      targetRole: version.targetRole,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      isActive: false,
      atsScore: 91,
    };
  },

  async duplicateResumeVersion(versionId: string): Promise<ResumeVersion> {
    await delay(500);
    const original = mockVersions.find((v) => v.id === versionId) ?? mockVersions[0];
    return {
      ...original,
      id: `v-${Date.now()}`,
      name: `${original.name} (Copy)`,
      isActive: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
  },

  async deleteResumeVersion(versionId: string): Promise<{ success: boolean }> {
    await delay(400);
    void versionId;
    return { success: true };
  },

  async exportResumePdf(): Promise<ExportResponse> {
    await delay(1500);
    return {
      success: true,
      format: "pdf",
      downloadUrl: "#",
      fileName: "resume-tailored-stripe.pdf",
    };
  },

  async exportResumeDocx(): Promise<ExportResponse> {
    await delay(1500);
    return {
      success: true,
      format: "docx",
      downloadUrl: "#",
      fileName: "resume-tailored-stripe.docx",
    };
  },
};
