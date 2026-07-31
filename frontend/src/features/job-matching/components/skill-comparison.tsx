"use client";

import { motion } from "framer-motion";
import { CheckCircle2, XCircle, Lightbulb, Star } from "lucide-react";
import type { SkillComparison as SkillComparisonType } from "../types";

interface SkillComparisonProps {
  data: SkillComparisonType;
}

export function SkillComparison({ data }: SkillComparisonProps) {
  return (
    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.1 }} className="rounded-xl border border-border bg-card p-5">
      <h3 className="font-semibold text-sm mb-4">Skill Analysis</h3>
      <div className="space-y-4">
        <div>
          <div className="flex items-center gap-2 mb-2">
            <CheckCircle2 className="h-4 w-4 text-emerald-500" />
            <span className="text-xs font-medium text-emerald-500">Matched ({data.matched.length})</span>
          </div>
          <div className="flex flex-wrap gap-1.5">
            {data.matched.map((s) => (
              <span key={s} className="rounded-md bg-emerald-500/10 text-emerald-500 px-2 py-0.5 text-[10px] font-medium">{s}</span>
            ))}
          </div>
        </div>
        <div>
          <div className="flex items-center gap-2 mb-2">
            <XCircle className="h-4 w-4 text-red-500" />
            <span className="text-xs font-medium text-red-500">Missing ({data.missing.length})</span>
          </div>
          <div className="flex flex-wrap gap-1.5">
            {data.missing.map((s) => (
              <span key={s} className="rounded-md bg-red-500/10 text-red-500 px-2 py-0.5 text-[10px] font-medium">{s}</span>
            ))}
          </div>
        </div>
        <div>
          <div className="flex items-center gap-2 mb-2">
            <Lightbulb className="h-4 w-4 text-amber-500" />
            <span className="text-xs font-medium text-amber-500">Recommended</span>
          </div>
          <div className="flex flex-wrap gap-1.5">
            {data.recommended.map((s) => (
              <span key={s} className="rounded-md bg-amber-500/10 text-amber-500 px-2 py-0.5 text-[10px] font-medium">{s}</span>
            ))}
          </div>
        </div>
        <div>
          <div className="flex items-center gap-2 mb-2">
            <Star className="h-4 w-4 text-violet-500" />
            <span className="text-xs font-medium text-violet-500">Preferred</span>
          </div>
          <div className="flex flex-wrap gap-1.5">
            {data.preferred.map((s) => (
              <span key={s} className="rounded-md bg-violet-500/10 text-violet-500 px-2 py-0.5 text-[10px] font-medium">{s}</span>
            ))}
          </div>
        </div>
      </div>
    </motion.div>
  );
}
