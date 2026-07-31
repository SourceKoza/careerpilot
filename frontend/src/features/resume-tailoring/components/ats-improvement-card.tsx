"use client";

import { motion } from "framer-motion";
import { TrendingUp } from "lucide-react";
import { cn } from "@/lib/utils";
import type { ATSImprovement } from "../types";

interface ATSImprovementCardProps {
  data: ATSImprovement;
}

function getColor(val: number): string {
  if (val >= 90) return "text-emerald-500";
  if (val >= 75) return "text-blue-500";
  if (val >= 60) return "text-amber-500";
  return "text-red-500";
}

export function ATSImprovementCard({ data }: ATSImprovementCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.1 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <TrendingUp className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">ATS Improvement</h3>
      </div>

      {/* Score comparison */}
      <div className="grid grid-cols-3 gap-3 mb-6">
        <div className="text-center">
          <p className="text-[10px] text-muted-foreground uppercase tracking-wide mb-1">
            Original
          </p>
          <p className={cn("text-2xl font-bold", getColor(data.originalScore))}>
            {data.originalScore}
          </p>
        </div>
        <div className="text-center flex flex-col items-center justify-center">
          <div className="h-px w-full bg-border mb-1" />
          <span className="text-xs font-bold text-emerald-500">
            +{data.improvementPercentage}%
          </span>
          <div className="h-px w-full bg-border mt-1" />
        </div>
        <div className="text-center">
          <p className="text-[10px] text-muted-foreground uppercase tracking-wide mb-1">
            Tailored
          </p>
          <p className={cn("text-2xl font-bold", getColor(data.tailoredScore))}>
            {data.tailoredScore}
          </p>
        </div>
      </div>

      {/* Factor breakdown */}
      <div className="space-y-3">
        {data.factors.map((factor) => (
          <div key={factor.name} className="space-y-1">
            <div className="flex items-center justify-between text-xs">
              <span className="text-muted-foreground">{factor.name}</span>
              <span className="text-emerald-500 font-medium">
                {factor.originalScore} → {factor.tailoredScore}
              </span>
            </div>
            <div className="relative h-2 rounded-full bg-border overflow-hidden">
              <motion.div
                className="absolute inset-y-0 left-0 rounded-full bg-muted-foreground/20"
                initial={{ width: 0 }}
                animate={{ width: `${factor.originalScore}%` }}
                transition={{ duration: 0.6 }}
              />
              <motion.div
                className="absolute inset-y-0 left-0 rounded-full bg-emerald-500"
                initial={{ width: 0 }}
                animate={{ width: `${factor.tailoredScore}%` }}
                transition={{ duration: 0.8, delay: 0.3 }}
              />
            </div>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
