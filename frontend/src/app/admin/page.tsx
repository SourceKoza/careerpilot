"use client";

import { useEffect, useState } from "react";
import { Users, Rocket, Briefcase, Activity } from "lucide-react";
import { apiClient } from "@/services/api";

interface SystemStats {
  totalUsers: number;
  totalMissions: number;
  totalJobs: number;
  totalApplicationsSent: number;
  activeUsers: number;
}

export default function AdminDashboardPage() {
  const [stats, setStats] = useState<SystemStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const resp = await apiClient.get<{ success: boolean; data: SystemStats }>("/api/v1/admin/dashboard/stats");
        setStats(resp.data.data);
      } catch (e) {
        console.error("Failed to load admin stats:", e);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  return (
    <div className="p-8 space-y-8">
      <div>
        <h1 className="text-2xl font-bold">Admin Dashboard</h1>
        <p className="text-muted-foreground">System overview and platform statistics</p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard icon={Users} label="Total Users" value={stats?.totalUsers ?? 0} loading={loading} />
        <StatCard icon={Rocket} label="Total Missions" value={stats?.totalMissions ?? 0} loading={loading} />
        <StatCard icon={Briefcase} label="Jobs Discovered" value={stats?.totalJobs ?? 0} loading={loading} />
        <StatCard icon={Activity} label="Applications Sent" value={stats?.totalApplicationsSent ?? 0} loading={loading} />
      </div>
    </div>
  );
}

function StatCard({ icon: Icon, label, value, loading }: {
  icon: React.ComponentType<{ className?: string }>;
  label: string;
  value: number;
  loading: boolean;
}) {
  return (
    <div className="rounded-xl border border-border bg-card p-5">
      <div className="flex items-center gap-3">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-red-500/10 text-red-500">
          <Icon className="h-5 w-5" />
        </div>
        <div>
          {loading ? (
            <div className="h-7 w-12 animate-pulse rounded bg-muted" />
          ) : (
            <p className="text-2xl font-bold">{value}</p>
          )}
          <p className="text-sm text-muted-foreground">{label}</p>
        </div>
      </div>
    </div>
  );
}
