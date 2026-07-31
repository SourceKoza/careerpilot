"use client";

import { motion } from "framer-motion";
import { Shield } from "lucide-react";
import type { ATSScore } from "../types";
import { cn } from "@/lib/utils";

interface ATSScoreCardProps {
  score: ATSScore;
}

function getScoreColor(score: number): string {
  if (score >= 90) return "text-emerald-500";
  if (score >= 75) return "text-blue-500";
  if (score >= 60) return "text-amber-500";
  return "text-red-500";
}

function ScoreRing({ value, label }: { value: number; label: string }) {
  const circumference = 2 * Math.PI * 28;
  const offset = circumference - (value / 100) * circumference;

  return (
    <div className="flex flex-col items-center gap-1">
      <div className="relative h-16 w-16">
        <svg className="h-16 w-16 -rotate-90" viewBox="0 0 64 64">
          <circle cx="32" cy="32" r="28" fill="none" strokeWidth="4" className="stroke-border" />
          <motion.circle
            cx="32" cy="32" r="28" fill="none" strokeWidth="4"
            strokeLinecap="round"
            className={cn("stroke-current", getScoreColor(value))}
            initial={{ strokeDashoffset: circumference }}
            animate={{ strokeDashoffset: offset }}
            transition={{ duration: 1, delay: 0.3 }}
            strokeDasharray={circumference}
          />
        </svg>
        <span className={cn("absolute inset-0 flex items-center justify-center text-sm font-bold", getScoreColor(value))}>
          {value}
        </span>
      </div>
      <span className="text-[10px] text-muted-foreground">{label}</span>
    </div>
  );
}

export function ATSScoreCard({ score }: ATSScoreCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <Shield className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">ATS Score</h3>
      </div>
      <div className="flex items-center justify-center mb-4">
        <div className="relative h-24 w-24">
          <svg className="h-24 w-24 -rotate-90" viewBox="0 0 96 96">
            <circle cx="48" cy="48" r="42" fill="none" strokeWidth="6" className="stroke-border" />
            <motion.circle
              cx="48" cy="48" r="42" fill="none" strokeWidth="6"
              strokeLinecap="round"
              className={cn("stroke-current", getScoreColor(score.overall))}
              initial={{ strokeDashoffset: 2 * Math.PI * 42 }}
              animate={{ strokeDashoffset: 2 * Math.PI * 42 - (score.overall / 100) * 2 * Math.PI * 42 }}
              transition={{ duration: 1.2 }}
              strokeDasharray={2 * Math.PI * 42}
            />
          </svg>
          <div className="absolute inset-0 flex flex-col items-center justify-center">
            <span className={cn("text-2xl font-bold", getScoreColor(score.overall))}>{score.overall}</span>
            <span className="text-[10px] text-muted-foreground">/ 100</span>
          </div>
        </div>
      </div>
      <div className="flex justify-around">
        <ScoreRing value={score.formatting} label="Format" />
        <ScoreRing value={score.keywords} label="Keywords" />
        <ScoreRing value={score.experience} label="Experience" />
      </div>
    </motion.div>
  );
}
