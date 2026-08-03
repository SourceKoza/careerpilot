export type MissionStatus = "active" | "paused" | "completed" | "failed" | "scheduled";

export type ApplyMode = "SEMI_AUTO" | "FULL_AUTO";

export interface Mission {
  id: string;
  name: string;
  status: MissionStatus;
  keywords: string;
  preferredTitle: string;
  experienceLevel: string;
  location: string;
  remote: boolean;
  hybrid: boolean;
  salaryMin: number | null;
  currency: string;
  employmentType: string;
  platforms: string[];
  resumeId: string | null;
  resumeTitle: string | null;
  schedule: string;
  timezone: string;
  applyMode: ApplyMode;
  createdAt: string;
  lastRun: string | null;
  nextRun: string | null;
  jobsFound: number;
  applicationsSubmitted: number;
  successRate: number;
}

export interface MissionProgress {
  overall: number;
  currentActivity: string;
  platforms: { name: string; progress: number; jobsFound: number }[];
}

export interface MissionTimelineItem {
  id: string;
  event: string;
  timestamp: string;
  status: "completed" | "active" | "pending";
}

export interface CreateMissionData {
  name: string;
  keywords: string;
  preferredTitle: string;
  experienceLevel: string;
  location: string;
  remote: boolean;
  hybrid: boolean;
  salaryMin: number | null;
  currency: string;
  employmentType: string;
  platforms: string[];
  resumeId: string | null;
  schedule: string;
  timezone: string;
  applyMode: ApplyMode;
}

export interface TailoredResume {
  id: string;
  missionId: string;
  jobId: string;
  summary: string | null;
  skillsJson: string | null;
  experienceJson: string | null;
  educationJson: string | null;
  tailoredScore: number | null;
  originalScore: number | null;
  status: "DRAFT" | "APPROVED" | "SENT" | "REJECTED";
  feedback: string | null;
  filePath: string | null;
  createdAt: string;
  updatedAt: string;
}
