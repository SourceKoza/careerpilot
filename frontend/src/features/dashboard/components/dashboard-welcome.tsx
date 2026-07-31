"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import {
  Briefcase,
  FileText,
  Send,
  Target,
  Rocket,
  ArrowRight,
} from "lucide-react";
import Link from "next/link";
import { useAuthStore } from "@/stores/auth.store";
import { apiClient } from "@/services/api";
import { ROUTES } from "@/lib/constants";

interface DashboardStats {
  totalJobs: number;
  highMatchJobs: number;
  appliedJobs: number;
  avgScore: number;
  totalMissions: number;
  activeMissions: number;
  applyMode: string;
}

interface ApiResponse<T> {
  success: boolean;
  data: T;
}

interface PageResponse<T> {
  content: T[];
  totalElements: number;
}

interface MissionSummary {
  id: string;
  name: string;
  status: string;
  applyMode: string;
}

interface JobSummary {
  id: string;
  matchScore: number | null;
  jobStatus: string;
}

export function DashboardWelcome() {
  const user = useAuthStore((state) => state.user);
  const firstName = user?.firstName || "there";
  const [stats, setStats] = useState<DashboardStats>({
    totalJobs: 0,
    highMatchJobs: 0,
    appliedJobs: 0,
    avgScore: 0,
    totalMissions: 0,
    activeMissions: 0,
    applyMode: "SEMI_AUTO",
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadStats() {
      try {
        // Load missions
        const missionsResp = await apiClient.get<ApiResponse<PageResponse<MissionSummary>>>("/api/v1/missions?page=0&size=10");
        const missions = missionsResp.data.data.content;
        const totalMissions = missionsResp.data.data.totalElements;
        const activeMissions = missions.filter((m) => m.status === "RUNNING" || m.status === "CREATED").length;
        const applyMode = missions[0]?.applyMode || "SEMI_AUTO";

        // Load jobs from latest mission
        let totalJobs = 0;
        let highMatchJobs = 0;
        let appliedJobs = 0;
        let avgScore = 0;

        if (missions.length > 0) {
          const jobsResp = await apiClient.get<ApiResponse<PageResponse<JobSummary>>>(`/api/v1/missions/${missions[0].id}/jobs?page=0&size=100`);
          const jobs = jobsResp.data.data.content;
          totalJobs = jobs.length;
          highMatchJobs = jobs.filter((j) => (j.matchScore || 0) >= 80).length;
          appliedJobs = jobs.filter((j) => j.jobStatus === "APPLIED").length;
          avgScore = totalJobs > 0 ? Math.round(jobs.reduce((sum, j) => sum + (j.matchScore || 0), 0) / totalJobs) : 0;
        }

        setStats({ totalJobs, highMatchJobs, appliedJobs, avgScore, totalMissions, activeMissions, applyMode });
      } catch (e) {
        console.error("Failed to load dashboard stats:", e);
      } finally {
        setLoading(false);
      }
    }
    loadStats();
  }, []);

  return (
    <div className="space-y-8 p-6">
      {/* Welcome Header */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-2"
      >
        <h1 className="text-2xl sm:text-3xl font-bold">
          Welcome back, {firstName} 👋
        </h1>
        <p className="text-muted-foreground">
          Here&apos;s your AI-powered job search progress at a glance.
        </p>
      </motion.div>

      {/* Stats Grid */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-5">
        <StatTile icon={Briefcase} label="Jobs Found" value={stats.totalJobs} color="text-blue-400" loading={loading} />
        <StatTile icon={Target} label="High Match (80%+)" value={stats.highMatchJobs} color="text-green-400" loading={loading} />
        <StatTile icon={Send} label="Applied" value={stats.appliedJobs} color="text-violet-400" loading={loading} />
        <StatTile icon={FileText} label="Avg Score" value={`${stats.avgScore}%`} color="text-amber-400" loading={loading} />
        <StatTile icon={Rocket} label="Missions" value={stats.totalMissions} color="text-pink-400" loading={loading} />
      </div>

      {/* Mode indicator */}
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2 rounded-lg border border-border bg-card px-4 py-2">
          <span className="text-sm text-muted-foreground">Apply Mode:</span>
          <span className={`text-sm font-semibold ${stats.applyMode === "FULL_AUTO" ? "text-orange-400" : "text-primary"}`}>
            {stats.applyMode === "FULL_AUTO" ? "Full Auto" : "Semi Auto"}
          </span>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <QuickActionCard
          href={ROUTES.APPLICATIONS}
          title="View Discovered Jobs"
          description="Review jobs, tailor resumes, and send applications"
          color="from-blue-500/20 to-blue-600/5"
        />
        <QuickActionCard
          href={ROUTES.AGENTS}
          title="Launch New Mission"
          description="Start an AI-powered job search mission"
          color="from-emerald-500/20 to-emerald-600/5"
        />
        <QuickActionCard
          href={ROUTES.RESUME}
          title="Resume Intelligence"
          description="Analyze and optimize your resume"
          color="from-violet-500/20 to-violet-600/5"
        />
      </div>
    </div>
  );
}

function StatTile({ icon: Icon, label, value, color, loading }: {
  icon: React.ComponentType<{ className?: string }>;
  label: string;
  value: string | number;
  color: string;
  loading: boolean;
}) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="rounded-xl border border-border bg-card p-4"
    >
      <div className="flex items-center gap-3">
        <div className={`flex h-9 w-9 items-center justify-center rounded-lg bg-muted ${color}`}>
          <Icon className="h-4 w-4" />
        </div>
        <div>
          {loading ? (
            <div className="h-6 w-10 animate-pulse rounded bg-muted" />
          ) : (
            <p className="text-xl font-bold">{value}</p>
          )}
          <p className="text-xs text-muted-foreground">{label}</p>
        </div>
      </div>
    </motion.div>
  );
}

function QuickActionCard({ href, title, description, color }: {
  href: string;
  title: string;
  description: string;
  color: string;
}) {
  return (
    <Link
      href={href}
      className={`group flex items-center justify-between rounded-xl border border-border p-5 transition-all hover:border-primary/30 hover:shadow-md bg-gradient-to-br ${color}`}
    >
      <div>
        <p className="font-medium">{title}</p>
        <p className="text-sm text-muted-foreground mt-1">{description}</p>
      </div>
      <ArrowRight className="h-4 w-4 text-muted-foreground group-hover:text-primary transition-colors" />
    </Link>
  );
}
