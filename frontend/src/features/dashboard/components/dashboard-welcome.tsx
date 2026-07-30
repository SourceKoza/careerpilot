"use client";

import { motion } from "framer-motion";
import {
  Briefcase,
  FileText,
  Send,
  Target,
  Search,
} from "lucide-react";
import { useAuthStore } from "@/stores/auth.store";
import { StatCard } from "./shared/stat-card";
import { QuickActions } from "./quick-actions";
import { ActivityTimeline } from "./activity-timeline";
import { AiAgentStatus } from "./ai-agent-status";

const stats = [
  {
    icon: Briefcase,
    label: "Jobs Found",
    value: "142",
    trend: { value: "+12%", positive: true },
    color: "text-blue-500",
  },
  {
    icon: Search,
    label: "AI Searches",
    value: "28",
    trend: { value: "+5", positive: true },
    color: "text-violet-500",
  },
  {
    icon: Send,
    label: "Applications",
    value: "34",
    trend: { value: "+8", positive: true },
    color: "text-green-500",
  },
  {
    icon: FileText,
    label: "Resume Score",
    value: "92",
    trend: { value: "+4", positive: true },
    color: "text-amber-500",
  },
  {
    icon: Target,
    label: "Interviews",
    value: "5",
    trend: { value: "+2", positive: true },
    color: "text-orange-500",
  },
];

export function DashboardWelcome() {
  const user = useAuthStore((state) => state.user);
  const firstName = user?.firstName || "there";

  return (
    <div className="space-y-8">
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
          Here&apos;s an overview of your AI-powered job search progress.
        </p>
      </motion.div>

      {/* Quick Actions */}
      <QuickActions />

      {/* Stats Grid */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-5">
        {stats.map((stat, index) => (
          <StatCard
            key={stat.label}
            icon={stat.icon}
            label={stat.label}
            value={stat.value}
            trend={stat.trend}
            color={stat.color}
            delay={0.1 + index * 0.05}
          />
        ))}
      </div>

      {/* Two-column layout for activity & agents */}
      <div className="grid gap-6 lg:grid-cols-2">
        <ActivityTimeline />
        <AiAgentStatus />
      </div>
    </div>
  );
}
