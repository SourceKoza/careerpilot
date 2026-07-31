export interface ResumeSection {
  title: string;
  original: string;
  tailored: string;
  changeType: "improved" | "added" | "removed" | "unchanged";
}

export interface ResumeChange {
  id: string;
  section: string;
  description: string;
  type: "addition" | "modification" | "removal" | "rewrite";
  impact: "high" | "medium" | "low";
}

export interface KeywordAnalysis {
  matched: string[];
  missing: string[];
  added: string[];
  recommended: string[];
  density: number;
}

export interface ATSImprovement {
  originalScore: number;
  tailoredScore: number;
  improvementPercentage: number;
  factors: ATSFactor[];
}

export interface ATSFactor {
  name: string;
  originalScore: number;
  tailoredScore: number;
}

export interface TailoringScore {
  overall: number;
  keywordMatch: number;
  formatCompliance: number;
  contentRelevance: number;
  experienceAlignment: number;
}

export interface AISuggestion {
  id: string;
  text: string;
  category: "summary" | "skills" | "experience" | "projects" | "achievements";
  priority: "high" | "medium" | "low";
  applied: boolean;
}

export interface ResumeComparison {
  id: string;
  sections: ResumeSection[];
  changes: ResumeChange[];
  keywordAnalysis: KeywordAnalysis;
  atsImprovement: ATSImprovement;
  tailoringScore: TailoringScore;
  suggestions: AISuggestion[];
}

export type ResumeVersionType = "master" | "tailored" | "company-specific";

export interface ResumeVersion {
  id: string;
  name: string;
  type: ResumeVersionType;
  targetCompany?: string;
  targetRole?: string;
  createdAt: string;
  updatedAt: string;
  isActive: boolean;
  atsScore: number;
}

export interface TailoredResume {
  id: string;
  originalResumeId: string;
  jobTitle: string;
  company: string;
  summary: string;
  skills: string[];
  experience: TailoredExperience[];
  projects: TailoredProject[];
  comparison: ResumeComparison;
  versions: ResumeVersion[];
  createdAt: string;
}

export interface TailoredExperience {
  title: string;
  company: string;
  duration: string;
  highlights: string[];
}

export interface TailoredProject {
  name: string;
  description: string;
  technologies: string[];
}

export interface ExportResponse {
  success: boolean;
  format: "pdf" | "docx";
  downloadUrl: string;
  fileName: string;
}
