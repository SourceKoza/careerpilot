"use client";

import { motion } from "framer-motion";
import { GitCompare } from "lucide-react";
import { cn } from "@/lib/utils";
import type { ResumeChange } from "../types";

interface ResumeChangesCardProps {
  changes: ResumeChange[];
}

const typeIcons: Record<ResumeChange["type"], string> = {
  addition: "+",
  modification: "~",
  removal: "-",
  rewrite: "↻",
};

const typeStyles: Record<ResumeChange["type"], string> = {
  addition: "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400",
  modification: "bg-blue-500/10 text-blue-600 dark:text-blue-400",
  removal: "bg-red-500/10 text-red-600 dark:text-red-400",
  rewrite: "bg-violet-500/10 text-violet-600 dark:text-violet-400",
};

const impactStyles: Record<ResumeChange["impact"], string> = {
  high: "text-red-500",
  medium: "text-amber-500",
  low: "text-blue-500",
};

export function ResumeChangesCard({ changes }: ResumeChangesCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.2 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <GitCompare className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Resume Changes</h3>
        <span className="ml-auto text-xs text-muted-foreground">
          {changes.length} changes
        </span>
      </div>

      <div className="space-y-2">
        {changes.map((change) => (
          <div
            key={change.id}
            className="flex items-start gap-3 rounded-lg border border-border/50 p-3"
          >
            <span
              className={cn(
                "flex h-6 w-6 shrink-0 items-center justify-center rounded-md text-xs font-bold",
                typeStyles[change.type]
              )}
            >
              {typeIcons[change.type]}
            </span>
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2 mb-0.5">
                <p className="text-xs font-medium">{change.section}</p>
                <span
                  className={cn(
                    "text-[10px] font-medium uppercase",
                    impactStyles[change.impact]
                  )}
                >
                  {change.impact}
                </span>
              </div>
              <p className="text-xs text-muted-foreground">{change.description}</p>
            </div>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
