export interface MatchScore {
  overall: number;
  ats: number;
  skills: number;
  experience: number;
  education: number;
  seniority: number;
}

export type ApplyReadiness = "ready" | "minor-improvements" | "needs-updates" | "not-recommended";

export interface SkillComparison {
  matched: string[];
  missing: string[];
  recommended: string[];
  preferred: string[];
}

export interface Strength {
  name: string;
  category: "technical" | "domain" | "soft";
}

export interface GapAnalysis {
  technicalGaps: string[];
  experienceGaps: string[];
  certificationSuggestions: string[];
  projectSuggestions: string[];
}

export interface Recommendation {
  id: string;
  text: string;
  priority: "high" | "medium" | "low";
}

export interface CompatibilityScore {
  category: string;
  score: number;
  maxScore: number;
}

export interface JobMatch {
  id: string;
  jobTitle: string;
  company: string;
  matchScore: MatchScore;
  summary: string;
  applyReadiness: ApplyReadiness;
  skillComparison: SkillComparison;
  strengths: Strength[];
  gapAnalysis: GapAnalysis;
  recommendations: Recommendation[];
  compatibility: CompatibilityScore[];
}
