"use client";

import { motion } from "framer-motion";
import {
  Briefcase,
  FileText,
  Send,
  Bot,
  TrendingUp,
  Target,
} from "lucide-react";
import { useAuthStore } from "@/stores/auth.store";

const quickStats = [
  { icon: Briefcase, label: "Jobs Found", value: "—", color: "text-blue-500" },
  { icon: FileText, label: "Resumes", value: "—", color: "text-violet-500" },
  { icon: Send, label: "Applications", value: "—", color: "text-green-500" },
  { icon: Target, label: "Interviews", value: "—", color: "text-orange-500" },
];

export function DashboardWelcome() {
  const user = useAuthStore((state) => state.user);
  const firstName = user?.firstName || "there";

  return (
    <div className="space-y-8">
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-2"
      >
        <h1 className="text-2xl sm:text-3xl font-bold">
          Welcome back, {firstName} 👋
        </h1>
        <p className="text-muted-foreground">
          Here&apos;s an overview of your job search progress.
        </p>
      </motion.div>

      {/* Quick Stats */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {quickStats.map((stat, index) => (
          <motion.div
            key={stat.label}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.1 }}
            className="rounded-xl border border-border bg-card p-5"
          >
            <div className="flex items-center justify-between">
              <stat.icon className={`h-5 w-5 ${stat.color}`} />
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </div>
            <p className="mt-3 text-2xl font-bold">{stat.value}</p>
            <p className="text-sm text-muted-foreground">{stat.label}</p>
          </motion.div>
        ))}
      </div>

      {/* Activity placeholder */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.4 }}
        className="rounded-xl border border-border bg-card p-6"
      >
        <div className="flex items-center gap-2 mb-4">
          <Bot className="h-5 w-5 text-primary" />
          <h2 className="text-lg font-semibold">AI Agent Activity</h2>
        </div>
        <div className="flex items-center justify-center py-12 text-center">
          <div className="space-y-3">
            <div className="mx-auto w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center">
              <Bot className="h-6 w-6 text-primary" />
            </div>
            <p className="text-muted-foreground">
              No agent activity yet. Start by uploading your resume.
            </p>
          </div>
        </div>
      </motion.div>
    </div>
  );
}
