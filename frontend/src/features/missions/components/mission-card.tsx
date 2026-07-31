"use client";

import { motion } from "framer-motion";
import { Play, Pause, RotateCw, Trash2, Circle, Calendar, Briefcase, Send, Eye } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { Mission } from "@/types/mission";
import { cn } from "@/lib/utils";
import Link from "next/link";

interface MissionCardProps {
  mission: Mission;
  onView: (mission: Mission) => void;
  onPause: (id: string) => void;
  onResume: (id: string) => void;
  onRunNow: (id: string) => void;
  onDelete: (id: string) => void;
  delay?: number;
}

const statusConfig = {
  active: { label: "Active", color: "text-emerald-500", dot: "bg-emerald-500 animate-pulse" },
  paused: { label: "Paused", color: "text-amber-500", dot: "bg-amber-500" },
  completed: { label: "Completed", color: "text-blue-500", dot: "bg-blue-500" },
  failed: { label: "Failed", color: "text-red-500", dot: "bg-red-500" },
  scheduled: { label: "Scheduled", color: "text-violet-500", dot: "bg-violet-500" },
};

function formatDate(dateStr: string | null): string {
  if (!dateStr) return "—";
  return new Date(dateStr).toLocaleDateString("en-US", { month: "short", day: "numeric", hour: "2-digit", minute: "2-digit" });
}

export function MissionCard({ mission, onView, onPause, onResume, onRunNow, onDelete, delay: d = 0 }: MissionCardProps) {
  const config = statusConfig[mission.status];

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: d }}
      className="group rounded-xl border border-border bg-card p-5 transition-all duration-200 hover:border-primary/30 hover:shadow-md"
    >
      <button onClick={() => onView(mission)} className="w-full text-left">
        <div className="flex items-start justify-between mb-3">
          <div>
            <h3 className="font-semibold text-sm">{mission.name}</h3>
            <p className="text-xs text-muted-foreground mt-0.5">{mission.keywords}</p>
          </div>
          <span className={cn("flex items-center gap-1.5 text-xs font-medium", config.color)}>
            <Circle className={cn("h-2 w-2 fill-current", config.dot)} />
            {config.label}
          </span>
        </div>

        <div className="grid grid-cols-3 gap-3 mb-3">
          <div className="text-center rounded-lg bg-secondary/50 p-2">
            <p className="text-lg font-bold">{mission.jobsFound}</p>
            <p className="text-[10px] text-muted-foreground">Found</p>
          </div>
          <div className="text-center rounded-lg bg-secondary/50 p-2">
            <p className="text-lg font-bold">{mission.applicationsSubmitted}</p>
            <p className="text-[10px] text-muted-foreground">Applied</p>
          </div>
          <div className="text-center rounded-lg bg-secondary/50 p-2">
            <p className="text-lg font-bold">{mission.successRate}%</p>
            <p className="text-[10px] text-muted-foreground">Success</p>
          </div>
        </div>

        <div className="flex flex-wrap gap-1.5 mb-3">
          {mission.platforms.map((p) => (
            <span key={p} className="rounded bg-primary/10 text-primary px-1.5 py-0.5 text-[10px] font-medium">
              {p}
            </span>
          ))}
        </div>

        {mission.jobsFound > 0 && (
          <Link
            href="/dashboard/applications"
            className="flex items-center gap-1.5 text-xs text-primary hover:underline mb-3"
            onClick={(e) => e.stopPropagation()}
          >
            <Eye className="h-3 w-3" />
            View {mission.jobsFound} Discovered Jobs →
          </Link>
        )}

        <div className="flex items-center gap-3 text-xs text-muted-foreground">
          <span className="flex items-center gap-1"><Calendar className="h-3 w-3" />{mission.schedule}</span>
          <span className="flex items-center gap-1"><Briefcase className="h-3 w-3" />{mission.experienceLevel}</span>
          {mission.remote && <span className="flex items-center gap-1"><Send className="h-3 w-3" />Remote</span>}
        </div>
      </button>

      {/* Actions */}
      <div className="flex items-center justify-between mt-4 pt-3 border-t border-border" onClick={(e) => e.stopPropagation()}>
        <span className="text-[10px] text-muted-foreground">
          Last: {formatDate(mission.lastRun)}
        </span>
        <div className="flex items-center gap-1">
          {mission.status === "active" && (
            <Button variant="ghost" size="icon" className="h-7 w-7" onClick={() => onPause(mission.id)} aria-label="Pause">
              <Pause className="h-3.5 w-3.5" />
            </Button>
          )}
          {mission.status === "paused" && (
            <Button variant="ghost" size="icon" className="h-7 w-7" onClick={() => onResume(mission.id)} aria-label="Resume">
              <Play className="h-3.5 w-3.5" />
            </Button>
          )}
          {mission.status !== "active" && (
            <Button variant="ghost" size="icon" className="h-7 w-7" onClick={() => onRunNow(mission.id)} aria-label="Run now">
              <RotateCw className="h-3.5 w-3.5" />
            </Button>
          )}
          <Button variant="ghost" size="icon" className="h-7 w-7 text-destructive" onClick={() => { if (confirm("Delete this mission?")) onDelete(mission.id); }} aria-label="Delete">
            <Trash2 className="h-3.5 w-3.5" />
          </Button>
        </div>
      </div>
    </motion.div>
  );
}
