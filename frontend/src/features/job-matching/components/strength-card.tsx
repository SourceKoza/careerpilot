"use client";

import { motion } from "framer-motion";
import { Zap } from "lucide-react";
import type { Strength } from "../types";

interface StrengthCardProps {
  strengths: Strength[];
}

const categoryColors = {
  technical: "bg-blue-500/10 text-blue-500",
  domain: "bg-violet-500/10 text-violet-500",
  soft: "bg-emerald-500/10 text-emerald-500",
};

export function StrengthCard({ strengths }: StrengthCardProps) {
  return (
    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.15 }} className="rounded-xl border border-border bg-card p-5">
      <div className="flex items-center gap-2 mb-4">
        <Zap className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Strengths</h3>
      </div>
      <div className="flex flex-wrap gap-2">
        {strengths.map((s) => (
          <span key={s.name} className={`rounded-lg px-3 py-1.5 text-xs font-medium ${categoryColors[s.category]}`}>
            {s.name}
          </span>
        ))}
      </div>
    </motion.div>
  );
}
