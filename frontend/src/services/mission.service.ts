import { apiClient } from "./api";
import type { Mission, MissionProgress, MissionTimelineItem, CreateMissionData } from "@/types/mission";

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

interface BackendMission {
  id: string;
  userId: string;
  name: string;
  keywords: string;
  preferredTitle: string | null;
  experienceLevel: string | null;
  location: string | null;
  remote: boolean;
  hybrid: boolean;
  salaryMin: number | null;
  currency: string | null;
  employmentType: string | null;
  platforms: string[];
  resumeId: string | null;
  schedule: string | null;
  timezone: string | null;
  status: string;
  createdAt: string;
  updatedAt: string;
}

interface BackendExecution {
  id: string;
  missionId: string;
  status: string;
  startedAt: string;
  completedAt: string | null;
  durationMs: number | null;
  jobsFound: number;
  contactsFound: number;
  errorMessage: string | null;
}

interface BackendEvent {
  id: string;
  missionId: string;
  executionId: string | null;
  eventType: string;
  message: string;
  eventTime: string;
}

interface BackendLog {
  id: string;
  executionId: string;
  level: string;
  message: string;
  logTime: string;
}

function mapStatus(backendStatus: string): Mission["status"] {
  switch (backendStatus) {
    case "CREATED": return "scheduled";
    case "RUNNING": return "active";
    case "PAUSED": return "paused";
    case "COMPLETED": return "completed";
    case "FAILED": return "failed";
    case "CANCELLED": return "paused";
    default: return "scheduled";
  }
}

function mapMission(b: BackendMission): Mission {
  return {
    id: b.id,
    name: b.name,
    status: mapStatus(b.status),
    keywords: b.keywords,
    preferredTitle: b.preferredTitle || "",
    experienceLevel: b.experienceLevel || "",
    location: b.location || "",
    remote: b.remote,
    hybrid: b.hybrid,
    salaryMin: b.salaryMin,
    currency: b.currency || "USD",
    employmentType: b.employmentType || "",
    platforms: b.platforms || [],
    resumeId: b.resumeId,
    resumeTitle: null,
    schedule: b.schedule || "",
    timezone: b.timezone || "UTC",
    createdAt: b.createdAt,
    lastRun: b.updatedAt,
    nextRun: null,
    jobsFound: 0,
    applicationsSubmitted: 0,
    successRate: 0,
  };
}

export const missionService = {
  async listMissions(): Promise<Mission[]> {
    const response = await apiClient.get<ApiResponse<PageResponse<BackendMission>>>("/api/v1/missions");
    const page = response.data.data;
    const missions = page.content.map(mapMission);

    // Enrich with execution data (jobsFound)
    for (const mission of missions) {
      try {
        const execResp = await apiClient.get<ApiResponse<PageResponse<BackendExecution>>>(
          `/api/v1/missions/${mission.id}/executions?page=0&size=1`
        );
        const execs = execResp.data.data.content;
        if (execs.length > 0) {
          mission.jobsFound = execs[0].jobsFound;
          mission.lastRun = execs[0].startedAt;
        }
      } catch {
        // ignore - mission may not have executions yet
      }
    }

    return missions;
  },

  async getMission(id: string): Promise<Mission | undefined> {
    try {
      const response = await apiClient.get<ApiResponse<BackendMission>>(`/api/v1/missions/${id}`);
      return mapMission(response.data.data);
    } catch {
      return undefined;
    }
  },

  async createMission(data: CreateMissionData): Promise<Mission> {
    const payload = {
      name: data.name,
      keywords: data.keywords,
      preferredTitle: data.preferredTitle,
      experienceLevel: data.experienceLevel,
      location: data.location,
      remote: data.remote,
      hybrid: data.hybrid,
      salaryMin: data.salaryMin,
      currency: data.currency,
      employmentType: data.employmentType,
      platforms: data.platforms,
      resumeId: data.resumeId,
      schedule: data.schedule,
      timezone: data.timezone,
    };
    const response = await apiClient.post<ApiResponse<BackendMission>>("/api/v1/missions", payload);
    return mapMission(response.data.data);
  },

  async pauseMission(id: string): Promise<void> {
    await apiClient.post(`/api/v1/missions/${id}/pause`);
  },

  async resumeMission(id: string): Promise<void> {
    await apiClient.post(`/api/v1/missions/${id}/resume`);
  },

  async runNow(id: string): Promise<void> {
    await apiClient.post(`/api/v1/missions/${id}/start`, {});
  },

  async deleteMission(_id: string): Promise<void> {
    // Delete not implemented in Sprint-15 backend; no-op for now
  },

  async getMissionProgress(id: string): Promise<MissionProgress> {
    // Derived from executions — for now return a simple summary
    try {
      const response = await apiClient.get<ApiResponse<PageResponse<BackendExecution>>>(
        `/api/v1/missions/${id}/executions?page=0&size=1`
      );
      const executions = response.data.data.content;
      if (executions.length === 0) {
        return { overall: 0, currentActivity: "No executions yet", platforms: [] };
      }
      const latest = executions[0];
      const isRunning = latest.status === "RUNNING";
      return {
        overall: latest.status === "COMPLETED" ? 100 : isRunning ? 50 : 0,
        currentActivity: isRunning ? "Searching platforms..." : latest.status === "COMPLETED" ? "Completed" : "Idle",
        platforms: [],
      };
    } catch {
      return { overall: 0, currentActivity: "", platforms: [] };
    }
  },

  async getMissionTimeline(id: string): Promise<MissionTimelineItem[]> {
    try {
      const response = await apiClient.get<ApiResponse<PageResponse<BackendEvent>>>(
        `/api/v1/missions/${id}/events?page=0&size=50`
      );
      const events = response.data.data.content;
      return events.map((e) => ({
        id: e.id,
        event: e.message,
        timestamp: e.eventTime,
        status: "completed" as const,
      }));
    } catch {
      return [];
    }
  },
};
