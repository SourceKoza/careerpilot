import { apiClient } from "@/services/api";
import type { Resume, ResumeVersion, ResumeAnalysis } from "../types";

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

interface BackendResumeFile {
  id: string;
  name: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploadedAt: string;
  updatedAt: string;
  isMaster: boolean;
}

interface BackendAnalysis {
  atsScore: { overall: number; formatting: number; keywords: number; experience: number };
  strengths: string[];
  missingSkills: { skill: string; importance: string }[];
  suggestions: { id: string; text: string; category: string }[];
  keywordMatch: number;
}

interface BackendResumeSummary {
  id: string;
  title: string;
  summary: string | null;
  targetRole: string | null;
  createdAt: string;
  updatedAt: string;
}

interface BackendVersionResponse {
  id: string;
  resumeId: string;
  versionNumber: number;
  markdownContent: string | null;
  jsonContent: string | null;
  changeDescription: string | null;
  createdAt: string;
}

function mapResumeFile(b: BackendResumeFile): Resume {
  return {
    id: b.id,
    name: b.name,
    fileName: b.fileName,
    fileType: (b.fileType === "docx" ? "docx" : "pdf") as "pdf" | "docx",
    fileSize: b.fileSize,
    uploadedAt: b.uploadedAt,
    updatedAt: b.updatedAt,
    isMaster: b.isMaster,
  };
}

export const resumeIntelligenceService = {
  async getResume(): Promise<Resume | null> {
    try {
      const response = await apiClient.get<ApiResponse<BackendResumeFile>>("/api/v1/resume-intelligence/current");
      return mapResumeFile(response.data.data);
    } catch {
      return null;
    }
  },

  async uploadResume(file: File): Promise<Resume> {
    const formData = new FormData();
    formData.append("file", file);
    const response = await apiClient.post<ApiResponse<BackendResumeFile>>(
      "/api/v1/resume-intelligence/upload",
      formData,
      { headers: { "Content-Type": "multipart/form-data" } }
    );
    return mapResumeFile(response.data.data);
  },

  async replaceResume(file: File): Promise<Resume> {
    const formData = new FormData();
    formData.append("file", file);
    const response = await apiClient.post<ApiResponse<BackendResumeFile>>(
      "/api/v1/resume-intelligence/replace",
      formData,
      { headers: { "Content-Type": "multipart/form-data" } }
    );
    return mapResumeFile(response.data.data);
  },

  async analyzeResume(): Promise<ResumeAnalysis> {
    const response = await apiClient.post<ApiResponse<BackendAnalysis>>("/api/v1/resume-intelligence/analyze");
    const data = response.data.data;
    return {
      atsScore: data.atsScore,
      strengths: data.strengths,
      missingSkills: data.missingSkills.map((s) => ({
        skill: s.skill,
        importance: s.importance as "high" | "medium" | "low",
      })),
      suggestions: data.suggestions.map((s) => ({
        id: s.id,
        text: s.text,
        category: s.category as "content" | "formatting" | "keywords",
      })),
      keywordMatch: data.keywordMatch,
    };
  },

  async getResumeVersions(): Promise<ResumeVersion[]> {
    try {
      // Use the existing resumes list API and map to versions
      const response = await apiClient.get<ApiResponse<{ content: BackendResumeSummary[] }>>("/api/v1/resumes?page=0&size=10");
      const resumes = response.data.data.content;
      return resumes.map((r, idx) => ({
        id: r.id,
        name: r.title,
        lastUpdated: r.updatedAt,
        isActive: idx === 0,
      }));
    } catch {
      return [];
    }
  },

  async generateResumeVersion(targetRole: string): Promise<ResumeVersion> {
    // Create a new resume record for this version
    const response = await apiClient.post<ApiResponse<{ id: string; title: string; createdAt: string; updatedAt: string }>>(
      "/api/v1/resumes",
      {
        title: `${targetRole} Resume`,
        summary: `Tailored resume for ${targetRole} role`,
        targetRole: targetRole,
        experiences: [],
        educations: [],
        skills: [],
        certifications: [],
        projects: [],
        languages: [],
      }
    );
    const data = response.data.data;
    return {
      id: data.id,
      name: `${targetRole} Resume`,
      lastUpdated: data.updatedAt || data.createdAt,
      isActive: false,
    };
  },
};
