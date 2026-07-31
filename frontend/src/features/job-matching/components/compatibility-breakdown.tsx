"use client";

import { motion } from "framer-motion";
import { BarChart3 } from "lucide-react";
import type { CompatibilityScore } from "../types";
import { cn } from "@/lib/utils";

interface CompatibilityBreakdownProps {
  data: CompatibilityScore[];
}

function getBarColor(score: number): string {
  if (score >= 90) return "bg-emerald-500";
  if (score >= 75) return "bg-blue-500";
  if (score >= 60) return "bg-amber-500";
  return "bg-red-500";
}

export function CompatibilityBreakdown({ data }: CompatibilityBreakdownProps) {
  return (
    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.25 }} className="rounded-xl border border-border bg-card p-5">
      <div className="flex items-center gap-2 mb-4">
        <BarChart3 className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Compatibility Breakdown</h3>
      </div>
      <div className="space-y-3">
        {data.map((item, index) => (
          <div key={item.category} className="space-y-1">
            <div className="flex items-center justify-between text-xs">
              <span className="text-muted-foreground">{item.category}</span>
              <span className="font-medium">{item.score}/{item.maxScore}</span>
            </div>
            <div className="h-2 rounded-full bg-border overflow-hidden">
              <motion.div
                className={cn("h-full rounded-full", getBarColor(item.score))}
                initial={{ width: 0 }}
                animate={{ width: `${(item.score / item.maxScore) * 100}%` }}
                transition={{ duration: 0.7, delay: 0.3 + index * 0.1 }}
              />
            </div>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
