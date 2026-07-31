"use client";

import { motion } from "framer-motion";
import { Target } from "lucide-react";
import { cn } from "@/lib/utils";
import type { TailoringScore } from "../types";

interface TailoringScoreCardProps {
  score: TailoringScore;
}

function getColor(val: number): string {
  if (val >= 90) return "text-emerald-500";
  if (val >= 75) return "text-blue-500";
  if (val >= 60) return "text-amber-500";
  return "text-red-500";
}

function getBarColor(val: number): string {
  if (val >= 90) return "bg-emerald-500";
  if (val >= 75) return "bg-blue-500";
  if (val >= 60) return "bg-amber-500";
  return "bg-red-500";
}

function ScoreBar({ label, value }: { label: string; value: number }) {
  return (
    <div className="space-y-1">
      <div className="flex items-center justify-between text-xs">
        <span className="text-muted-foreground">{label}</span>
        <span className={cn("font-bold", getColor(value))}>{value}%</span>
      </div>
      <div className="h-2 rounded-full bg-border overflow-hidden">
        <motion.div
          className={cn("h-full rounded-full", getBarColor(value))}
          initial={{ width: 0 }}
          animate={{ width: `${value}%` }}
          transition={{ duration: 0.8, delay: 0.2 }}
        />
      </div>
    </div>
  );
}

export function TailoringScoreCard({ score }: TailoringScoreCardProps) {
  const circumference = 2 * Math.PI * 44;
  const offset = circumference - (score.overall / 100) * circumference;

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <Target className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Tailoring Score</h3>
      </div>
      <div className="flex items-center justify-center mb-6">
        <div className="relative h-28 w-28">
          <svg className="h-28 w-28 -rotate-90" viewBox="0 0 96 96">
            <circle cx="48" cy="48" r="44" fill="none" strokeWidth="6" className="stroke-border" />
            <motion.circle
              cx="48"
              cy="48"
              r="44"
              fill="none"
              strokeWidth="6"
              strokeLinecap="round"
              className={cn("stroke-current", getColor(score.overall))}
              initial={{ strokeDashoffset: circumference }}
              animate={{ strokeDashoffset: offset }}
              transition={{ duration: 1.2 }}
              strokeDasharray={circumference}
            />
          </svg>
          <div className="absolute inset-0 flex flex-col items-center justify-center">
            <span className={cn("text-3xl font-bold", getColor(score.overall))}>
              {score.overall}
            </span>
            <span className="text-[10px] text-muted-foreground">/ 100</span>
          </div>
        </div>
      </div>
      <div className="space-y-3">
        <ScoreBar label="Keyword Match" value={score.keywordMatch} />
        <ScoreBar label="Format Compliance" value={score.formatCompliance} />
        <ScoreBar label="Content Relevance" value={score.contentRelevance} />
        <ScoreBar label="Experience Alignment" value={score.experienceAlignment} />
      </div>
    </motion.div>
  );
}
