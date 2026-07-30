export interface Resume {
  id: string;
  title: string;
  summary: string | null;
  targetRole: string | null;
  fileName: string;
  fileType: "pdf" | "docx";
  fileSize: number;
  isDefault: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ResumeUploadRequest {
  file: File;
  title?: string;
}

export interface ResumeUpdateRequest {
  title: string;
  isDefault?: boolean;
}

export type ResumeSortField = "title" | "createdAt" | "fileSize";
export type ResumeSortOrder = "asc" | "desc";
export type ResumeFileFilter = "all" | "pdf" | "docx";
