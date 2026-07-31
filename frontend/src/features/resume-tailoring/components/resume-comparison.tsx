"use client";

import { motion } from "framer-motion";
import { ArrowRight } from "lucide-react";
import { cn } from "@/lib/utils";
import type { ResumeSection } from "../types";

interface ResumeComparisonProps {
  sections: ResumeSection[];
}

const changeStyles: Record<ResumeSection["changeType"], string> = {
  improved: "border-l-emerald-500",
  added: "border-l-blue-500",
  removed: "border-l-red-500",
  unchanged: "border-l-muted-foreground/30",
};

const changeLabels: Record<ResumeSection["changeType"], string> = {
  improved: "Improved",
  added: "Added",
  removed: "Removed",
  unchanged: "Unchanged",
};

const changeLabelStyles: Record<ResumeSection["changeType"], string> = {
  improved: "text-emerald-600 dark:text-emerald-400",
  added: "text-blue-600 dark:text-blue-400",
  removed: "text-red-600 dark:text-red-400",
  unchanged: "text-muted-foreground",
};

export function ResumeComparison({ sections }: ResumeComparisonProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.1 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <h3 className="font-semibold text-sm mb-4">Side-by-Side Comparison</h3>
      <div className="space-y-4">
        {sections.map((section) => (
          <div
            key={section.title}
            className={cn("rounded-lg border-l-2 p-4 bg-muted/20", changeStyles[section.changeType])}
          >
            <div className="flex items-center justify-between mb-3">
              <p className="text-xs font-semibold">{section.title}</p>
              <span
                className={cn(
                  "text-[10px] font-medium uppercase",
                  changeLabelStyles[section.changeType]
                )}
              >
                {changeLabels[section.changeType]}
              </span>
            </div>
            <div className="grid gap-3 md:grid-cols-2">
              <div className="space-y-1">
                <p className="text-[10px] font-medium text-muted-foreground uppercase">
                  Original
                </p>
                <p className="text-xs text-muted-foreground leading-relaxed">
                  {section.original}
                </p>
              </div>
              <div className="relative space-y-1">
                <ArrowRight className="absolute -left-4 top-3 hidden h-3 w-3 text-primary md:block" />
                <p className="text-[10px] font-medium text-primary uppercase">Tailored</p>
                <p className="text-xs leading-relaxed">{section.tailored}</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
