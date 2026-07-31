"use client";

import { motion } from "framer-motion";
import { Target } from "lucide-react";
import { cn } from "@/lib/utils";

interface KeywordMatchCardProps {
  matchPercentage: number;
  strengths: string[];
}

export function KeywordMatchCard({ matchPercentage, strengths }: KeywordMatchCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.15 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Target className="h-5 w-5 text-primary" />
          <h3 className="font-semibold text-sm">Keyword Match</h3>
        </div>
        <span className={cn(
          "text-lg font-bold",
          matchPercentage >= 80 ? "text-emerald-500" : matchPercentage >= 60 ? "text-amber-500" : "text-red-500"
        )}>
          {matchPercentage}%
        </span>
      </div>
      <div className="h-2 rounded-full bg-border overflow-hidden mb-4">
        <motion.div
          className="h-full bg-gradient-to-r from-primary to-violet-500 rounded-full"
          initial={{ width: 0 }}
          animate={{ width: `${matchPercentage}%` }}
          transition={{ duration: 1, delay: 0.3 }}
        />
      </div>
      <div>
        <p className="text-xs text-muted-foreground mb-2">Top Strengths</p>
        <div className="flex flex-wrap gap-1.5">
          {strengths.slice(0, 8).map((skill) => (
            <span key={skill} className="rounded-md bg-emerald-500/10 text-emerald-500 px-2 py-0.5 text-[10px] font-medium">
              {skill}
            </span>
          ))}
        </div>
      </div>
    </motion.div>
  );
}
