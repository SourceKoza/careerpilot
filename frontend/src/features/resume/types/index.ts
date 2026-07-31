export interface Resume {
  id: string;
  name: string;
  fileName: string;
  fileType: "pdf" | "docx";
  fileSize: number;
  uploadedAt: string;
  updatedAt: string;
  isMaster: boolean;
}

export interface ResumeVersion {
  id: string;
  name: string;
  lastUpdated: string;
  isActive: boolean;
}

export interface ATSScore {
  overall: number;
  formatting: number;
  keywords: number;
  experience: number;
}

export interface SkillGap {
  skill: string;
  importance: "high" | "medium" | "low";
}

export interface ResumeSuggestion {
  id: string;
  text: string;
  category: "content" | "formatting" | "keywords";
}

export interface ResumeAnalysis {
  atsScore: ATSScore;
  strengths: string[];
  missingSkills: SkillGap[];
  suggestions: ResumeSuggestion[];
  keywordMatch: number;
}

export interface UploadResponse {
  success: boolean;
  resume: Resume;
}
