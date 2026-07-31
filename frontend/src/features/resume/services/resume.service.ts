import type { Resume, ResumeVersion, ResumeAnalysis } from "../types";

const mockResume: Resume = {
  id: "master-1",
  name: "Master Resume",
  fileName: "ramesh-pandey-resume.pdf",
  fileType: "pdf",
  fileSize: 245760,
  uploadedAt: "2026-07-20T10:00:00Z",
  updatedAt: "2026-07-30T15:30:00Z",
  isMaster: true,
};

const mockVersions: ResumeVersion[] = [
  { id: "v1", name: "Master Resume", lastUpdated: "2026-07-30T15:30:00Z", isActive: true },
  { id: "v2", name: "Java Backend Resume", lastUpdated: "2026-07-28T10:00:00Z", isActive: false },
  { id: "v3", name: "Spring Boot Resume", lastUpdated: "2026-07-26T14:00:00Z", isActive: false },
  { id: "v4", name: "AI Engineer Resume", lastUpdated: "2026-07-22T09:00:00Z", isActive: false },
  { id: "v5", name: "Staff Engineer Resume", lastUpdated: "2026-07-18T11:00:00Z", isActive: false },
];

const mockAnalysis: ResumeAnalysis = {
  atsScore: { overall: 89, formatting: 95, keywords: 82, experience: 91 },
  strengths: ["Java", "Spring Boot", "Kafka", "Redis", "Microservices", "Docker", "PostgreSQL", "System Design"],
  missingSkills: [
    { skill: "Terraform", importance: "high" },
    { skill: "AWS Lambda", importance: "high" },
    { skill: "Kubernetes", importance: "medium" },
    { skill: "GraphQL", importance: "medium" },
    { skill: "Go", importance: "low" },
  ],
  suggestions: [
    { id: "s1", text: "Add quantified achievements (e.g., reduced latency by 40%)", category: "content" },
    { id: "s2", text: "Mention AI Agent Platform experience", category: "keywords" },
    { id: "s3", text: "Improve professional summary with target role keywords", category: "content" },
    { id: "s4", text: "Add a skills section with proficiency indicators", category: "formatting" },
    { id: "s5", text: "Include links to GitHub and portfolio projects", category: "content" },
  ],
  keywordMatch: 78,
};

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

let currentResume: Resume | null = mockResume;

export const resumeIntelligenceService = {
  async getResume(): Promise<Resume | null> {
    await delay(500);
    return currentResume;
  },

  async uploadResume(file: File): Promise<Resume> {
    await delay(1500);
    const ext = file.name.split(".").pop()?.toLowerCase();
    currentResume = {
      id: "master-" + Date.now(),
      name: "Master Resume",
      fileName: file.name,
      fileType: ext === "docx" ? "docx" : "pdf",
      fileSize: file.size,
      uploadedAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      isMaster: true,
    };
    return currentResume;
  },

  async replaceResume(file: File): Promise<Resume> {
    return this.uploadResume(file);
  },

  async analyzeResume(): Promise<ResumeAnalysis> {
    await delay(2000);
    return mockAnalysis;
  },

  async getResumeVersions(): Promise<ResumeVersion[]> {
    await delay(400);
    return [...mockVersions];
  },

  async generateResumeVersion(_targetRole: string): Promise<ResumeVersion> {
    await delay(1500);
    const newVersion: ResumeVersion = {
      id: "v" + Date.now(),
      name: `${_targetRole} Resume`,
      lastUpdated: new Date().toISOString(),
      isActive: false,
    };
    mockVersions.push(newVersion);
    return newVersion;
  },
};
