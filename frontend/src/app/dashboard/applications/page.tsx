"use client";

import { useEffect, useState } from "react";
import { apiClient } from "@/services/api";
import {
  Briefcase,
  MapPin,
  DollarSign,
  ExternalLink,
  Star,
  Building2,
  FileText,
  Check,
  X,
  Pencil,
  Download,
  Loader2,
  Send,
} from "lucide-react";
import type { TailoredResume } from "@/types/mission";

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
  tailoredResumeId: string | null;
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
  applyMode: string;
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
    IGNORED: { label: "Skipped", className: "bg-muted text-muted-foreground border-border" },
  };
  const badge = map[status] || map["NEW"];
  return <span className={`px-2 py-0.5 rounded-full text-xs border ${badge.className}`}>{badge.label}</span>;
}

function getTailoredStatusBadge(status: string) {
  const map: Record<string, { label: string; className: string }> = {
    DRAFT: { label: "Draft", className: "bg-yellow-500/10 text-yellow-400 border-yellow-500/30" },
    APPROVED: { label: "Approved", className: "bg-blue-500/10 text-blue-400 border-blue-500/30" },
    SENT: { label: "Sent", className: "bg-green-500/10 text-green-400 border-green-500/30" },
    REJECTED: { label: "Rejected", className: "bg-red-500/10 text-red-400 border-red-500/30" },
  };
  const badge = map[status] || map["DRAFT"];
  return <span className={`px-2 py-0.5 rounded-full text-xs border ${badge.className}`}>{badge.label}</span>;
}

export default function ApplicationsPage() {
  const [jobs, setJobs] = useState<DiscoveredJob[]>([]);
  const [loading, setLoading] = useState(true);
  const [missionName, setMissionName] = useState("");
  const [missionId, setMissionId] = useState<string | null>(null);
  const [applyMode, setApplyMode] = useState<string>("SEMI_AUTO");
  const [tailoredResumes, setTailoredResumes] = useState<Record<string, TailoredResume>>({});
  const [expandedJob, setExpandedJob] = useState<string | null>(null);
  const [feedbackText, setFeedbackText] = useState("");
  const [actionLoading, setActionLoading] = useState<string | null>(null);

  useEffect(() => {
    async function loadJobs() {
      try {
        const missionsResp = await apiClient.get<ApiResponse<PageResponse<Mission>>>("/api/v1/missions?page=0&size=1&sortBy=createdAt&sortDir=desc");
        const missions = missionsResp.data.data.content;
        if (missions.length === 0) {
          setLoading(false);
          return;
        }
        const mission = missions[0];
        setMissionId(mission.id);
        setMissionName(mission.name);
        setApplyMode(mission.applyMode || "SEMI_AUTO");

        const jobsResp = await apiClient.get<ApiResponse<PageResponse<DiscoveredJob>>>(`/api/v1/missions/${mission.id}/jobs?page=0&size=50`);
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

  const loadTailoredResume = async (jobId: string) => {
    if (!missionId) return;
    try {
      const resp = await apiClient.get<ApiResponse<TailoredResume | null>>(
        `/api/v1/missions/${missionId}/jobs/${jobId}/tailored-resume`
      );
      if (resp.data.data) {
        setTailoredResumes((prev) => ({ ...prev, [jobId]: resp.data.data! }));
      }
    } catch (e) {
      console.error("Failed to load tailored resume:", e);
    }
  };

  const handleTailor = async (jobId: string) => {
    if (!missionId) return;
    setActionLoading(jobId);
    try {
      const resp = await apiClient.post<ApiResponse<TailoredResume>>(
        `/api/v1/missions/${missionId}/jobs/${jobId}/tailor`
      );
      setTailoredResumes((prev) => ({ ...prev, [jobId]: resp.data.data }));
      setExpandedJob(jobId);
    } catch (e) {
      console.error("Tailoring failed:", e);
    } finally {
      setActionLoading(null);
    }
  };

  const handleApprove = async (jobId: string) => {
    if (!missionId) return;
    setActionLoading(jobId);
    try {
      const resp = await apiClient.post<ApiResponse<TailoredResume>>(
        `/api/v1/missions/${missionId}/jobs/${jobId}/approve`
      );
      setTailoredResumes((prev) => ({ ...prev, [jobId]: resp.data.data }));
      setJobs((prev) => prev.map((j) => j.id === jobId ? { ...j, jobStatus: "APPLIED" } : j));
    } catch (e) {
      console.error("Approve failed:", e);
    } finally {
      setActionLoading(null);
    }
  };

  const handleRegenerate = async (jobId: string) => {
    if (!missionId || !feedbackText.trim()) return;
    setActionLoading(jobId);
    try {
      const resp = await apiClient.post<ApiResponse<TailoredResume>>(
        `/api/v1/missions/${missionId}/jobs/${jobId}/regenerate`,
        { feedback: feedbackText }
      );
      setTailoredResumes((prev) => ({ ...prev, [jobId]: resp.data.data }));
      setFeedbackText("");
    } catch (e) {
      console.error("Regenerate failed:", e);
    } finally {
      setActionLoading(null);
    }
  };

  const handleSkip = async (jobId: string) => {
    if (!missionId) return;
    setActionLoading(jobId);
    try {
      await apiClient.post(`/api/v1/missions/${missionId}/jobs/${jobId}/skip`);
      setJobs((prev) => prev.map((j) => j.id === jobId ? { ...j, jobStatus: "IGNORED" } : j));
      setExpandedJob(null);
    } catch (e) {
      console.error("Skip failed:", e);
    } finally {
      setActionLoading(null);
    }
  };

  const handleDownload = (jobId: string) => {
    if (!missionId) return;
    window.open(`${apiClient.defaults.baseURL}/api/v1/missions/${missionId}/jobs/${jobId}/resume-download`, "_blank");
  };

  const toggleExpanded = (jobId: string) => {
    if (expandedJob === jobId) {
      setExpandedJob(null);
    } else {
      setExpandedJob(jobId);
      if (!tailoredResumes[jobId]) {
        loadTailoredResume(jobId);
      }
    }
  };

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
  const appliedCount = jobs.filter((j) => j.jobStatus === "APPLIED").length;

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
          <div className="text-center px-4 py-2 rounded-xl bg-card border">
            <p className="text-2xl font-bold text-blue-400">{appliedCount}</p>
            <p className="text-xs text-muted-foreground">Applied</p>
          </div>
          <div className="text-center px-3 py-2 rounded-xl bg-card border">
            <p className="text-sm font-bold text-primary">{applyMode === "FULL_AUTO" ? "Auto" : "Semi"}</p>
            <p className="text-[10px] text-muted-foreground">Mode</p>
          </div>
        </div>
      </div>

      <div className="grid gap-4">
        {jobs.map((job) => {
          const tailored = tailoredResumes[job.id];
          const isExpanded = expandedJob === job.id;
          const hasTailored = job.tailoredResumeId !== null || tailored !== undefined;
          const isLoading = actionLoading === job.id;

          return (
            <div key={job.id} className={`rounded-xl border ${getScoreBg(job.matchScore)} transition-all hover:shadow-lg`}>
              <div className="p-5">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-3 mb-1">
                      <h3 className="font-semibold text-lg truncate">{job.title}</h3>
                      {getStatusBadge(job.jobStatus)}
                      {tailored && getTailoredStatusBadge(tailored.status)}
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
                    {tailored?.tailoredScore && tailored.tailoredScore !== job.matchScore && (
                      <div className="text-center min-w-[60px] text-green-400">
                        <p className="text-lg font-bold">{tailored.tailoredScore}%</p>
                        <p className="text-[10px] uppercase tracking-wide">tailored</p>
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

                {/* Action buttons */}
                {job.jobStatus === "NEW" && job.matchScore !== null && job.matchScore >= 60 && (
                  <div className="flex items-center gap-2 mt-3 pt-3 border-t border-border/50">
                    {!hasTailored && (
                      <button
                        onClick={() => handleTailor(job.id)}
                        disabled={isLoading}
                        className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-primary/10 text-primary text-sm font-medium hover:bg-primary/20 transition-colors disabled:opacity-50"
                      >
                        {isLoading ? <Loader2 className="h-3.5 w-3.5 animate-spin" /> : <FileText className="h-3.5 w-3.5" />}
                        Tailor Resume
                      </button>
                    )}
                    {hasTailored && tailored?.status === "DRAFT" && (
                      <>
                        <button
                          onClick={() => toggleExpanded(job.id)}
                          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-500/10 text-blue-400 text-sm font-medium hover:bg-blue-500/20 transition-colors"
                        >
                          <FileText className="h-3.5 w-3.5" />
                          {isExpanded ? "Hide Preview" : "Preview"}
                        </button>
                        <button
                          onClick={() => handleApprove(job.id)}
                          disabled={isLoading}
                          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-green-500/10 text-green-400 text-sm font-medium hover:bg-green-500/20 transition-colors disabled:opacity-50"
                        >
                          {isLoading ? <Loader2 className="h-3.5 w-3.5 animate-spin" /> : <Send className="h-3.5 w-3.5" />}
                          Approve & Send
                        </button>
                        <button
                          onClick={() => toggleExpanded(job.id)}
                          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-yellow-500/10 text-yellow-400 text-sm font-medium hover:bg-yellow-500/20 transition-colors"
                        >
                          <Pencil className="h-3.5 w-3.5" />
                          Change
                        </button>
                        <button
                          onClick={() => handleSkip(job.id)}
                          disabled={isLoading}
                          className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-red-500/10 text-red-400 text-sm font-medium hover:bg-red-500/20 transition-colors disabled:opacity-50"
                        >
                          <X className="h-3.5 w-3.5" />
                          Skip
                        </button>
                      </>
                    )}
                    {hasTailored && (tailored?.status === "SENT" || tailored?.status === "APPROVED") && (
                      <button
                        onClick={() => handleDownload(job.id)}
                        className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-primary/10 text-primary text-sm font-medium hover:bg-primary/20 transition-colors"
                      >
                        <Download className="h-3.5 w-3.5" />
                        Download DOCX
                      </button>
                    )}
                  </div>
                )}
              </div>

              {/* Expanded: Tailored Resume Preview */}
              {isExpanded && tailored && (
                <div className="border-t border-border/50 p-5 bg-card/50">
                  <div className="space-y-3">
                    <div className="flex items-center justify-between">
                      <h4 className="text-sm font-semibold flex items-center gap-2">
                        <FileText className="h-4 w-4 text-primary" />
                        Tailored Resume Preview
                      </h4>
                      {tailored.filePath && (
                        <button
                          onClick={() => handleDownload(job.id)}
                          className="flex items-center gap-1 text-xs text-primary hover:underline"
                        >
                          <Download className="h-3 w-3" />
                          Download DOCX
                        </button>
                      )}
                    </div>

                    {tailored.summary && (
                      <div className="rounded-lg border border-border p-3">
                        <p className="text-xs font-medium text-muted-foreground mb-1">Summary</p>
                        <p className="text-sm">{tailored.summary}</p>
                      </div>
                    )}

                    {tailored.skillsJson && (
                      <div className="rounded-lg border border-border p-3">
                        <p className="text-xs font-medium text-muted-foreground mb-1">Top Skills</p>
                        <div className="flex flex-wrap gap-1.5">
                          {JSON.parse(tailored.skillsJson).slice(0, 10).map((skill: string, i: number) => (
                            <span key={i} className="px-2 py-0.5 rounded-full text-xs bg-primary/10 text-primary border border-primary/20">
                              {skill}
                            </span>
                          ))}
                        </div>
                      </div>
                    )}

                    {/* Feedback input for regeneration */}
                    {tailored.status === "DRAFT" && (
                      <div className="space-y-2 pt-2 border-t border-border/50">
                        <p className="text-xs text-muted-foreground">Want changes? Describe what to adjust:</p>
                        <div className="flex gap-2">
                          <input
                            type="text"
                            value={feedbackText}
                            onChange={(e) => setFeedbackText(e.target.value)}
                            placeholder="e.g. Add more about my Kafka experience"
                            className="flex-1 rounded-lg border border-input bg-transparent px-3 py-2 text-sm"
                          />
                          <button
                            onClick={() => handleRegenerate(job.id)}
                            disabled={isLoading || !feedbackText.trim()}
                            className="flex items-center gap-1.5 px-3 py-2 rounded-lg bg-primary text-primary-foreground text-sm font-medium hover:bg-primary/90 transition-colors disabled:opacity-50"
                          >
                            {isLoading ? <Loader2 className="h-3.5 w-3.5 animate-spin" /> : <Pencil className="h-3.5 w-3.5" />}
                            Regenerate
                          </button>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
