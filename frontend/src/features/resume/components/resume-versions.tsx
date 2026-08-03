"use client";

import { motion } from "framer-motion";
import { FileText, CheckCircle2, Clock } from "lucide-react";
import type { ResumeVersion } from "../types";

interface ResumeVersionsProps {
  versions: ResumeVersion[];
}

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleDateString("en-US", { month: "short", day: "numeric", year: "numeric" });
}

export function ResumeVersions({ versions }: ResumeVersionsProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.3 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <FileText className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Resume Versions</h3>
      </div>
      <div className="space-y-2">
        {versions.map((version) => (
          <div
            key={version.id}
            className="flex items-center justify-between rounded-lg p-3 hover:bg-secondary/50 transition-colors"
          >
            <div className="flex items-center gap-3">
              <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary/10">
                <FileText className="h-4 w-4 text-primary" />
              </div>
              <div>
                <p className="text-sm font-medium">{version.name}</p>
                <p className="text-[10px] text-muted-foreground flex items-center gap-1">
                  <Clock className="h-2.5 w-2.5" />
                  {formatDate(version.lastUpdated)}
                </p>
              </div>
            </div>
            {version.isActive && (
              <span className="flex items-center gap-1 text-[10px] font-medium text-emerald-500 bg-emerald-500/10 rounded-full px-2 py-0.5">
                <CheckCircle2 className="h-3 w-3" />
                Active
              </span>
            )}
          </div>
        ))}
      </div>
    </motion.div>
  );
}
