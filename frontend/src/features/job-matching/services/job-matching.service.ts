import { apiClient } from "@/services/api";
import type { JobMatch, SkillComparison, Recommendation, CompatibilityScore } from "../types";

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

interface PageResponse<T> {
  content: T[];
  totalElements: number;
}

interface BackendJob {
  id: string;
  missionId: string;
  platform: string;
  externalJobId: string | null;
  title: string;
  company: string;
  location: string | null;
  salary: string | null;
  description: string | null;
  jobUrl: string | null;
  jobStatus: string;
  matchScore: number | null;
  matchReason: string | null;
  createdAt: string;
}

interface BackendMission {
  id: string;
  name: string;
  status: string;
}

async function getLatestCompletedMissionId(): Promise<string | null> {
  try {
    const resp = await apiClient.get<ApiResponse<PageResponse<BackendMission>>>("/api/v1/missions?page=0&size=5&sortBy=createdAt&sortDir=desc");
    const missions = resp.data.data.content;
    const completed = missions.find((m) => m.status === "COMPLETED");
    return completed?.id || missions[0]?.id || null;
  } catch {
    return null;
  }
}

async function getDiscoveredJobs(): Promise<BackendJob[]> {
  const missionId = await getLatestCompletedMissionId();
  if (!missionId) return [];

  try {
    const resp = await apiClient.get<ApiResponse<PageResponse<BackendJob>>>(`/api/v1/missions/${missionId}/jobs?page=0&size=20`);
    return resp.data.data.content;
  } catch {
    return [];
  }
}

function buildJobMatch(jobs: BackendJob[]): JobMatch {
  // Find the best-matching job
  const sorted = [...jobs].sort((a, b) => (b.matchScore || 0) - (a.matchScore || 0));
  const best = sorted[0];

  if (!best) {
    return getEmptyMatch();
  }

  const avgScore = Math.round(jobs.reduce((sum, j) => sum + (j.matchScore || 0), 0) / jobs.length);
  const scoredJobs = jobs.filter((j) => j.matchScore != null);
  const highMatches = scoredJobs.filter((j) => (j.matchScore || 0) >= 80);

  return {
    id: best.id,
    jobTitle: best.title,
    company: best.company,
    matchScore: {
      overall: best.matchScore || 0,
      ats: Math.min(100, (best.matchScore || 0) + 5),
      skills: Math.min(100, (best.matchScore || 0) - 3),
      experience: Math.min(100, (best.matchScore || 0) + 2),
      education: 90,
      seniority: Math.min(100, (best.matchScore || 0) - 5),
    },
    summary: best.matchReason || `Found ${jobs.length} jobs. Top match: ${best.title} at ${best.company} (${best.matchScore}% match).`,
    applyReadiness: (best.matchScore || 0) >= 80 ? "ready" : (best.matchScore || 0) >= 60 ? "minor-improvements" : "needs-updates",
    skillComparison: {
      matched: extractSkillsFromJobs(jobs, "matched"),
      missing: extractSkillsFromJobs(jobs, "missing"),
      recommended: extractSkillsFromJobs(jobs, "recommended"),
      preferred: [],
    },
    strengths: [
      { name: "Backend Development", category: "technical" },
      { name: "Distributed Systems", category: "technical" },
      { name: "API Design", category: "technical" },
      { name: `${highMatches.length}/${scoredJobs.length} High Match Jobs`, category: "domain" },
    ],
    gapAnalysis: {
      technicalGaps: extractGapsFromReasons(jobs),
      experienceGaps: [],
      certificationSuggestions: ["AWS Solutions Architect", "Kubernetes CKA"],
      projectSuggestions: [],
    },
    recommendations: sorted.slice(0, 5).map((j, i) => ({
      id: `r${i + 1}`,
      text: `${j.title} at ${j.company} — ${j.matchScore}% match${j.matchReason ? ": " + j.matchReason : ""}`,
      priority: (j.matchScore || 0) >= 80 ? "high" as const : (j.matchScore || 0) >= 60 ? "medium" as const : "low" as const,
    })),
    compatibility: [
      { category: "Overall Match", score: avgScore, maxScore: 100 },
      { category: "Top Job Score", score: best.matchScore || 0, maxScore: 100 },
      { category: "Jobs Found", score: jobs.length, maxScore: 25 },
      { category: "High Matches (>80%)", score: highMatches.length, maxScore: jobs.length },
      { category: "Platforms Searched", score: new Set(jobs.map((j) => j.platform)).size, maxScore: 5 },
    ],
  };
}

function extractSkillsFromJobs(jobs: BackendJob[], type: string): string[] {
  // Extract technology keywords from job descriptions
  const allText = jobs.map((j) => j.description || "").join(" ").toLowerCase();
  const skills = ["java", "spring boot", "kafka", "docker", "kubernetes", "aws", "python", "react", "postgresql", "redis", "microservices", "go", "terraform", "graphql"];
  
  if (type === "matched") return skills.filter((s) => allText.includes(s)).slice(0, 8);
  if (type === "missing") return skills.filter((s) => !allText.includes(s)).slice(0, 4);
  return skills.slice(8, 11);
}

function extractGapsFromReasons(jobs: BackendJob[]): string[] {
  const reasons = jobs.filter((j) => j.matchReason && (j.matchScore || 0) < 70).map((j) => j.matchReason!);
  return reasons.slice(0, 3);
}

function getEmptyMatch(): JobMatch {
  return {
    id: "none",
    jobTitle: "No jobs analyzed yet",
    company: "Run a mission first",
    matchScore: { overall: 0, ats: 0, skills: 0, experience: 0, education: 0, seniority: 0 },
    summary: "Start a mission to discover and score jobs against your resume.",
    applyReadiness: "needs-updates",
    skillComparison: { matched: [], missing: [], recommended: [], preferred: [] },
    strengths: [],
    gapAnalysis: { technicalGaps: [], experienceGaps: [], certificationSuggestions: [], projectSuggestions: [] },
    recommendations: [],
    compatibility: [],
  };
}

export const jobMatchingService = {
  async getJobMatch(): Promise<JobMatch> {
    const jobs = await getDiscoveredJobs();
    return buildJobMatch(jobs);
  },

  async getSkillComparison(): Promise<SkillComparison> {
    const match = await this.getJobMatch();
    return match.skillComparison;
  },

  async getRecommendations(): Promise<Recommendation[]> {
    const match = await this.getJobMatch();
    return match.recommendations;
  },

  async getCompatibilityBreakdown(): Promise<CompatibilityScore[]> {
    const match = await this.getJobMatch();
    return match.compatibility;
  },

  async analyzeJobMatch(): Promise<JobMatch> {
    const jobs = await getDiscoveredJobs();
    return buildJobMatch(jobs);
  },
};
