"use client";

import { motion } from "framer-motion";
import { Copy, Trash2 } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import type { ResumeVersion } from "../types";

interface ResumeVersionCardProps {
  version: ResumeVersion;
  onDuplicate: (id: string) => void;
  onDelete: (id: string) => void;
}

const typeLabels: Record<ResumeVersion["type"], string> = {
  master: "Master",
  tailored: "Tailored",
  "company-specific": "Company",
};

const typeStyles: Record<ResumeVersion["type"], string> = {
  master: "bg-violet-500/10 text-violet-600 dark:text-violet-400",
  tailored: "bg-blue-500/10 text-blue-600 dark:text-blue-400",
  "company-specific": "bg-amber-500/10 text-amber-600 dark:text-amber-400",
};

function getScoreColor(score: number): string {
  if (score >= 90) return "text-emerald-500";
  if (score >= 75) return "text-blue-500";
  if (score >= 60) return "text-amber-500";
  return "text-red-500";
}

export function ResumeVersionCard({
  version,
  onDuplicate,
  onDelete,
}: ResumeVersionCardProps) {
  const formattedDate = new Date(version.updatedAt).toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.98 }}
      animate={{ opacity: 1, scale: 1 }}
      className={cn(
        "flex items-center justify-between rounded-lg border p-3",
        version.isActive ? "border-primary/40 bg-primary/5" : "border-border"
      )}
    >
      <div className="flex items-center gap-3 min-w-0">
        <span
          className={cn(
            "shrink-0 rounded-md px-2 py-0.5 text-[10px] font-medium",
            typeStyles[version.type]
          )}
        >
          {typeLabels[version.type]}
        </span>
        <div className="min-w-0">
          <p className="text-sm font-medium truncate">{version.name}</p>
          <p className="text-[10px] text-muted-foreground">{formattedDate}</p>
        </div>
      </div>

      <div className="flex items-center gap-2 shrink-0">
        <span className={cn("text-sm font-bold", getScoreColor(version.atsScore))}>
          {version.atsScore}
        </span>
        {version.isActive && (
          <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[10px] font-medium text-emerald-500">
            Active
          </span>
        )}
        <Button
          variant="ghost"
          size="icon"
          className="h-7 w-7"
          onClick={() => onDuplicate(version.id)}
          aria-label={`Duplicate ${version.name}`}
        >
          <Copy className="h-3.5 w-3.5" />
        </Button>
        <Button
          variant="ghost"
          size="icon"
          className="h-7 w-7 text-destructive hover:text-destructive"
          onClick={() => onDelete(version.id)}
          disabled={version.type === "master"}
          aria-label={`Delete ${version.name}`}
        >
          <Trash2 className="h-3.5 w-3.5" />
        </Button>
      </div>
    </motion.div>
  );
}
