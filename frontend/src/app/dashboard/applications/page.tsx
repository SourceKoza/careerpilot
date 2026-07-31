"use client";

import { useEffect, useState } from "react";
import { apiClient } from "@/services/api";
import { Briefcase, MapPin, DollarSign, ExternalLink, Star, Building2 } from "lucide-react";

interface DiscoveredJob {
  id: string;
  missionId: string;
  platform: string;
  title: string;
  company: string;
  location: string | null;
  salary: string | null;
  description: string | null;
  jobUrl: string | null;
  jobStatus: string;
  matchScore: number | null;
  matchReason: string | null;
  createdAt: string;
}

interface ApiResponse<T> {
  success: boolean;
  data: T;
}

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
}

interface Mission {
  id: string;
  name: string;
  status: string;
}

function getScoreColor(score: number | null): string {
  if (score === null) return "text-muted-foreground";
  if (score >= 80) return "text-green-400";
  if (score >= 60) return "text-yellow-400";
  if (score >= 40) return "text-orange-400";
  return "text-red-400";
}

function getScoreBg(score: number | null): string {
  if (score === null) return "bg-muted/30";
  if (score >= 80) return "bg-green-500/10 border-green-500/30";
  if (score >= 60) return "bg-yellow-500/10 border-yellow-500/30";
  if (score >= 40) return "bg-orange-500/10 border-orange-500/30";
  return "bg-red-500/10 border-red-500/30";
}

function getStatusBadge(status: string) {
  const map: Record<string, { label: string; className: string }> = {
    NEW: { label: "New", className: "bg-blue-500/10 text-blue-400 border-blue-500/30" },
    REVIEWED: { label: "Reviewed", className: "bg-purple-500/10 text-purple-400 border-purple-500/30" },
    APPLIED: { label: "Applied", className: "bg-green-500/10 text-green-400 border-green-500/30" },
    REJECTED: { label: "Rejected", className: "bg-red-500/10 text-red-400 border-red-500/30" },
    IGNORED: { label: "Ignored", className: "bg-muted text-muted-foreground border-border" },
  };
  const badge = map[status] || map["NEW"];
  return <span className={`px-2 py-0.5 rounded-full text-xs border ${badge.className}`}>{badge.label}</span>;
}

export default function ApplicationsPage() {
  const [jobs, setJobs] = useState<DiscoveredJob[]>([]);
  const [loading, setLoading] = useState(true);
  const [missionName, setMissionName] = useState("");

  useEffect(() => {
    async function loadJobs() {
      try {
        // Get latest mission
        const missionsResp = await apiClient.get<ApiResponse<PageResponse<Mission>>>("/api/v1/missions?page=0&size=1&sortBy=createdAt&sortDir=desc");
        const missions = missionsResp.data.data.content;
        if (missions.length === 0) {
          setLoading(false);
          return;
        }
        const missionId = missions[0].id;
        setMissionName(missions[0].name);

        // Get discovered jobs
        const jobsResp = await apiClient.get<ApiResponse<PageResponse<DiscoveredJob>>>(`/api/v1/missions/${missionId}/jobs?page=0&size=50`);
        const sorted = jobsResp.data.data.content.sort((a, b) => (b.matchScore || 0) - (a.matchScore || 0));
        setJobs(sorted);
      } catch (e) {
        console.error("Failed to load jobs:", e);
      } finally {
        setLoading(false);
      }
    }
    loadJobs();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-center space-y-4">
          <div className="animate-spin h-8 w-8 border-2 border-primary border-t-transparent rounded-full mx-auto" />
          <p className="text-muted-foreground">Loading discovered jobs...</p>
        </div>
      </div>
    );
  }

  if (jobs.length === 0) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-center space-y-4">
          <Briefcase className="h-12 w-12 text-muted-foreground mx-auto" />
          <h2 className="text-2xl font-bold">No Jobs Discovered Yet</h2>
          <p className="text-muted-foreground">Start a mission to discover jobs matched to your resume</p>
        </div>
      </div>
    );
  }

  const highMatches = jobs.filter((j) => (j.matchScore || 0) >= 80).length;
  const avgScore = Math.round(jobs.reduce((sum, j) => sum + (j.matchScore || 0), 0) / jobs.length);

  return (
    <div className="space-y-6 p-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Discovered Jobs</h1>
          <p className="text-muted-foreground">
            {jobs.length} jobs from mission: {missionName}
          </p>
        </div>
        <div className="flex gap-4">
          <div className="text-center px-4 py-2 rounded-xl bg-card border">
            <p className="text-2xl font-bold text-primary">{jobs.length}</p>
            <p className="text-xs text-muted-foreground">Total Jobs</p>
          </div>
          <div className="text-center px-4 py-2 rounded-xl bg-card border">
            <p className="text-2xl font-bold text-green-400">{highMatches}</p>
            <p className="text-xs text-muted-foreground">High Match</p>
          </div>
          <div className="text-center px-4 py-2 rounded-xl bg-card border">
            <p className="text-2xl font-bold text-yellow-400">{avgScore}%</p>
            <p className="text-xs text-muted-foreground">Avg Score</p>
          </div>
        </div>
      </div>

      <div className="grid gap-4">
        {jobs.map((job) => (
          <div key={job.id} className={`p-5 rounded-xl border ${getScoreBg(job.matchScore)} transition-all hover:shadow-lg`}>
            <div className="flex items-start justify-between gap-4">
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-3 mb-1">
                  <h3 className="font-semibold text-lg truncate">{job.title}</h3>
                  {getStatusBadge(job.jobStatus)}
                  <span className="px-2 py-0.5 rounded text-xs bg-muted text-muted-foreground">
                    {job.platform}
                  </span>
                </div>
                <div className="flex items-center gap-4 text-sm text-muted-foreground mb-2">
                  <span className="flex items-center gap-1">
                    <Building2 className="h-3.5 w-3.5" />
                    {job.company}
                  </span>
                  {job.location && (
                    <span className="flex items-center gap-1">
                      <MapPin className="h-3.5 w-3.5" />
                      {job.location}
                    </span>
                  )}
                  {job.salary && (
                    <span className="flex items-center gap-1">
                      <DollarSign className="h-3.5 w-3.5" />
                      {job.salary}
                    </span>
                  )}
                </div>
                {job.matchReason && (
                  <p className="text-sm text-muted-foreground line-clamp-2 mt-1">
                    <Star className="h-3.5 w-3.5 inline mr-1 text-yellow-500" />
                    <span className="italic">{job.matchReason}</span>
                  </p>
                )}
              </div>
              <div className="flex flex-col items-center gap-2">
                {job.matchScore !== null && (
                  <div className={`text-center min-w-[60px] ${getScoreColor(job.matchScore)}`}>
                    <p className="text-2xl font-bold">{job.matchScore}%</p>
                    <p className="text-[10px] uppercase tracking-wide">match</p>
                  </div>
                )}
                {job.jobUrl && (
                  <a
                    href={job.jobUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center gap-1 text-xs text-primary hover:underline"
                  >
                    <ExternalLink className="h-3 w-3" />
                    View
                  </a>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
