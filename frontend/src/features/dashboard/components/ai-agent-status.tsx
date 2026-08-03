"use client";

import { Bot, Circle } from "lucide-react";
import { DashboardCard } from "./shared/dashboard-card";
import { cn } from "@/lib/utils";

interface AgentStatus {
  name: string;
  status: "active" | "idle" | "completed";
  lastRun: string;
}

const mockAgents: AgentStatus[] = [
  { name: "Job Search Agent", status: "active", lastRun: "Running now..." },
  { name: "Resume Optimizer", status: "idle", lastRun: "Last run: 2h ago" },
  { name: "Auto Apply Agent", status: "completed", lastRun: "Completed 5 apps" },
  { name: "Interview Prep", status: "idle", lastRun: "Last run: Yesterday" },
];

const statusStyles = {
  active: "text-green-500 animate-pulse",
  idle: "text-muted-foreground",
  completed: "text-blue-500",
};

const statusLabels = {
  active: "Active",
  idle: "Idle",
  completed: "Done",
};

export function AiAgentStatus() {
  return (
    <DashboardCard title="AI Agents" icon={Bot} delay={0.4}>
      <div className="space-y-3">
        {mockAgents.map((agent) => (
          <div
            key={agent.name}
            className="flex items-center justify-between rounded-lg p-2 hover:bg-secondary/50 transition-colors"
          >
            <div className="flex items-center gap-3">
              <Circle
                className={cn("h-2.5 w-2.5 fill-current", statusStyles[agent.status])}
              />
              <div>
                <p className="text-sm font-medium">{agent.name}</p>
                <p className="text-xs text-muted-foreground">{agent.lastRun}</p>
              </div>
            </div>
            <span
              className={cn(
                "text-xs font-medium px-2 py-0.5 rounded-full",
                agent.status === "active" && "bg-green-500/10 text-green-500",
                agent.status === "idle" && "bg-muted text-muted-foreground",
                agent.status === "completed" && "bg-blue-500/10 text-blue-500"
              )}
            >
              {statusLabels[agent.status]}
            </span>
          </div>
        ))}
      </div>
    </DashboardCard>
  );
}
