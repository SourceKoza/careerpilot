export type MissionStatus = "active" | "paused" | "completed" | "failed" | "scheduled";

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
}
