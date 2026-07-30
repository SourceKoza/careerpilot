"use client";

import { Search, FileText, Send, Bot, CheckCircle2 } from "lucide-react";
import type { LucideIcon } from "lucide-react";
import { DashboardCard } from "./shared/dashboard-card";
import { cn } from "@/lib/utils";

interface ActivityItem {
  id: string;
  icon: LucideIcon;
  title: string;
  description: string;
  time: string;
  color: string;
}

const mockActivities: ActivityItem[] = [
  {
    id: "1",
    icon: Bot,
    title: "AI Agent completed search",
    description: "Found 12 matching roles on LinkedIn",
    time: "2 min ago",
    color: "text-violet-500 bg-violet-500/10",
  },
  {
    id: "2",
    icon: Send,
    title: "Application submitted",
    description: "Senior Frontend Engineer at Vercel",
    time: "1 hour ago",
    color: "text-green-500 bg-green-500/10",
  },
  {
    id: "3",
    icon: FileText,
    title: "Resume optimized",
    description: "Tailored for React/Next.js roles",
    time: "3 hours ago",
    color: "text-blue-500 bg-blue-500/10",
  },
  {
    id: "4",
    icon: Search,
    title: "Job search started",
    description: "Searching across 5 platforms",
    time: "5 hours ago",
    color: "text-orange-500 bg-orange-500/10",
  },
  {
    id: "5",
    icon: CheckCircle2,
    title: "Interview scheduled",
    description: "Technical round at Stripe",
    time: "Yesterday",
    color: "text-emerald-500 bg-emerald-500/10",
  },
];

export function ActivityTimeline() {
  return (
    <DashboardCard title="Recent Activity" icon={Search} delay={0.3}>
      <div className="space-y-4">
        {mockActivities.map((activity, index) => (
          <div key={activity.id} className="flex items-start gap-3">
            <div className="relative">
              <div
                className={cn(
                  "flex h-8 w-8 items-center justify-center rounded-full",
                  activity.color
                )}
              >
                <activity.icon className="h-4 w-4" />
              </div>
              {index < mockActivities.length - 1 && (
                <div className="absolute left-1/2 top-8 h-full w-px -translate-x-1/2 bg-border" />
              )}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium truncate">{activity.title}</p>
              <p className="text-xs text-muted-foreground truncate">
                {activity.description}
              </p>
            </div>
            <span className="text-xs text-muted-foreground whitespace-nowrap">
              {activity.time}
            </span>
          </div>
        ))}
      </div>
    </DashboardCard>
  );
}
