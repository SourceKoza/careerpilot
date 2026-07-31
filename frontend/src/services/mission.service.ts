import type { Mission, MissionProgress, MissionTimelineItem, CreateMissionData } from "@/types/mission";

let mockMissions: Mission[] = [
  {
    id: "1",
    name: "Senior Frontend – Remote US",
    status: "active",
    keywords: "React, Next.js, TypeScript",
    preferredTitle: "Senior Frontend Engineer",
    experienceLevel: "Senior",
    location: "United States",
    remote: true,
    hybrid: false,
    salaryMin: 150000,
    currency: "USD",
    employmentType: "Full-time",
    platforms: ["LinkedIn", "Indeed", "Wellfound"],
    resumeId: "1",
    resumeTitle: "Senior Frontend Engineer",
    schedule: "Daily",
    timezone: "America/New_York",
    createdAt: "2026-07-25T10:00:00Z",
    lastRun: "2026-07-31T06:00:00Z",
    nextRun: "2026-08-01T06:00:00Z",
    jobsFound: 142,
    applicationsSubmitted: 34,
    successRate: 24,
  },
  {
    id: "2",
    name: "Java Backend – London",
    status: "paused",
    keywords: "Java, Spring Boot, Microservices",
    preferredTitle: "Backend Engineer",
    experienceLevel: "Senior",
    location: "London, UK",
    remote: false,
    hybrid: true,
    salaryMin: 90000,
    currency: "GBP",
    employmentType: "Full-time",
    platforms: ["LinkedIn", "Indeed"],
    resumeId: "3",
    resumeTitle: "Backend Java Engineer",
    schedule: "Weekly",
    timezone: "Europe/London",
    createdAt: "2026-07-20T14:00:00Z",
    lastRun: "2026-07-28T08:00:00Z",
    nextRun: null,
    jobsFound: 67,
    applicationsSubmitted: 12,
    successRate: 18,
  },
  {
    id: "3",
    name: "Full Stack – Remote Anywhere",
    status: "completed",
    keywords: "Full Stack, Node.js, React",
    preferredTitle: "Full Stack Developer",
    experienceLevel: "Mid",
    location: "Remote",
    remote: true,
    hybrid: false,
    salaryMin: 100000,
    currency: "USD",
    employmentType: "Full-time",
    platforms: ["LinkedIn", "Wellfound", "Company Sites"],
    resumeId: "2",
    resumeTitle: "Full Stack Developer",
    schedule: "Run Once",
    timezone: "UTC",
    createdAt: "2026-07-15T09:00:00Z",
    lastRun: "2026-07-15T09:05:00Z",
    nextRun: null,
    jobsFound: 89,
    applicationsSubmitted: 21,
    successRate: 28,
  },
];

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export const missionService = {
  async listMissions(): Promise<Mission[]> {
    await delay(500);
    return [...mockMissions];
  },

  async getMission(id: string): Promise<Mission | undefined> {
    await delay(300);
    return mockMissions.find((m) => m.id === id);
  },

  async createMission(data: CreateMissionData): Promise<Mission> {
    await delay(1000);
    const mission: Mission = {
      id: String(Date.now()),
      ...data,
      resumeTitle: data.resumeId ? "Selected Resume" : null,
      status: "scheduled",
      createdAt: new Date().toISOString(),
      lastRun: null,
      nextRun: new Date(Date.now() + 3600000).toISOString(),
      jobsFound: 0,
      applicationsSubmitted: 0,
      successRate: 0,
    };
    mockMissions = [mission, ...mockMissions];
    return mission;
  },

  async pauseMission(id: string): Promise<void> {
    await delay(300);
    mockMissions = mockMissions.map((m) =>
      m.id === id ? { ...m, status: "paused" as const, nextRun: null } : m
    );
  },

  async resumeMission(id: string): Promise<void> {
    await delay(300);
    mockMissions = mockMissions.map((m) =>
      m.id === id ? { ...m, status: "active" as const, nextRun: new Date(Date.now() + 3600000).toISOString() } : m
    );
  },

  async runNow(id: string): Promise<void> {
    await delay(500);
    mockMissions = mockMissions.map((m) =>
      m.id === id ? { ...m, status: "active" as const, lastRun: new Date().toISOString() } : m
    );
  },

  async deleteMission(id: string): Promise<void> {
    await delay(400);
    mockMissions = mockMissions.filter((m) => m.id !== id);
  },

  async getMissionProgress(id: string): Promise<MissionProgress> {
    await delay(300);
    const mission = mockMissions.find((m) => m.id === id);
    if (!mission) {
      return { overall: 0, currentActivity: "", platforms: [] };
    }
    return {
      overall: mission.status === "completed" ? 100 : 74,
      currentActivity: mission.status === "active" ? "Analyzing LinkedIn Results..." : "Mission paused",
      platforms: mission.platforms.map((p, i) => ({
        name: p,
        progress: mission.status === "completed" ? 100 : Math.min(100, 50 + i * 15),
        jobsFound: Math.floor(mission.jobsFound / mission.platforms.length),
      })),
    };
  },

  async getMissionTimeline(id: string): Promise<MissionTimelineItem[]> {
    await delay(300);
    const mission = mockMissions.find((m) => m.id === id);
    if (!mission) return [];
    const items: MissionTimelineItem[] = [
      { id: "t1", event: "Mission Created", timestamp: mission.createdAt, status: "completed" },
    ];
    if (mission.lastRun) {
      items.push({ id: "t2", event: "Mission Started", timestamp: mission.lastRun, status: "completed" });
      mission.platforms.forEach((p, i) => {
        items.push({ id: `t3-${i}`, event: `Searching ${p}`, timestamp: mission.lastRun!, status: mission.status === "completed" ? "completed" : i < 2 ? "completed" : "active" });
      });
      if (mission.status === "completed") {
        items.push({ id: "t4", event: "Resume Tailoring", timestamp: mission.lastRun, status: "completed" });
        items.push({ id: "t5", event: `${mission.applicationsSubmitted} Applications Submitted`, timestamp: mission.lastRun, status: "completed" });
        items.push({ id: "t6", event: "Mission Completed", timestamp: mission.lastRun, status: "completed" });
      }
    }
    return items;
  },
};
