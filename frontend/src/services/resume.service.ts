import type { Resume, ResumeUpdateRequest } from "@/types/resume";

/**
 * Resume service abstraction.
 * Currently uses mock data. Replace implementations with real API calls
 * when the file-upload backend endpoints are available.
 */

let mockResumes: Resume[] = [
  {
    id: "1",
    title: "Senior Frontend Engineer",
    summary: "Tailored for React/Next.js positions",
    targetRole: "Senior Frontend Engineer",
    fileName: "resume-frontend-senior.pdf",
    fileType: "pdf",
    fileSize: 245760,
    isDefault: true,
    createdAt: "2026-07-25T10:30:00Z",
    updatedAt: "2026-07-28T15:45:00Z",
  },
  {
    id: "2",
    title: "Full Stack Developer",
    summary: "General purpose resume for full-stack roles",
    targetRole: "Full Stack Developer",
    fileName: "resume-fullstack.pdf",
    fileType: "pdf",
    fileSize: 198432,
    isDefault: false,
    createdAt: "2026-07-20T08:15:00Z",
    updatedAt: "2026-07-26T12:00:00Z",
  },
  {
    id: "3",
    title: "Backend Java Engineer",
    summary: "Optimized for Spring Boot/Java positions",
    targetRole: "Backend Engineer",
    fileName: "resume-backend-java.docx",
    fileType: "docx",
    fileSize: 312000,
    isDefault: false,
    createdAt: "2026-07-15T14:20:00Z",
    updatedAt: "2026-07-22T09:30:00Z",
  },
];

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export const resumeService = {
  async listResumes(): Promise<Resume[]> {
    await delay(500);
    return [...mockResumes];
  },

  async getResume(id: string): Promise<Resume | undefined> {
    await delay(300);
    return mockResumes.find((r) => r.id === id);
  },

  async uploadResume(file: File, title?: string): Promise<Resume> {
    await delay(1500); // Simulate upload time
    const ext = file.name.split(".").pop()?.toLowerCase();
    const newResume: Resume = {
      id: String(Date.now()),
      title: title || file.name.replace(/\.(pdf|docx)$/i, ""),
      summary: null,
      targetRole: null,
      fileName: file.name,
      fileType: ext === "docx" ? "docx" : "pdf",
      fileSize: file.size,
      isDefault: mockResumes.length === 0,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
    mockResumes = [newResume, ...mockResumes];
    return newResume;
  },

  async updateResume(id: string, data: ResumeUpdateRequest): Promise<Resume> {
    await delay(400);
    mockResumes = mockResumes.map((r) => {
      if (r.id === id) {
        return { ...r, title: data.title, updatedAt: new Date().toISOString() };
      }
      if (data.isDefault) {
        return { ...r, isDefault: r.id === id };
      }
      return r;
    });
    if (data.isDefault) {
      mockResumes = mockResumes.map((r) => ({
        ...r,
        isDefault: r.id === id,
      }));
    }
    return mockResumes.find((r) => r.id === id)!;
  },

  async setDefault(id: string): Promise<void> {
    await delay(300);
    mockResumes = mockResumes.map((r) => ({
      ...r,
      isDefault: r.id === id,
    }));
  },

  async deleteResume(id: string): Promise<void> {
    await delay(400);
    mockResumes = mockResumes.filter((r) => r.id !== id);
  },

  async downloadResume(_id: string): Promise<void> {
    await delay(300);
    // In real implementation, trigger file download from backend
  },
};
