"use client";

import { motion } from "framer-motion";
import { Sparkles } from "lucide-react";
import type { Recommendation } from "../types";
import { cn } from "@/lib/utils";

interface RecommendationCardProps {
  recommendations: Recommendation[];
}

const priorityStyles = {
  high: "border-l-red-500 bg-red-500/5",
  medium: "border-l-amber-500 bg-amber-500/5",
  low: "border-l-blue-500 bg-blue-500/5",
};

const priorityLabels = {
  high: "High Priority",
  medium: "Medium",
  low: "Low",
};

export function RecommendationCard({ recommendations }: RecommendationCardProps) {
  return (
    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.3 }} className="rounded-xl border border-border bg-card p-5">
      <div className="flex items-center gap-2 mb-4">
        <Sparkles className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Recommendations</h3>
      </div>
      <div className="space-y-2">
        {recommendations.map((rec) => (
          <div key={rec.id} className={cn("rounded-lg border-l-2 p-3", priorityStyles[rec.priority])}>
            <div className="flex items-center justify-between mb-1">
              <span className="text-[10px] font-medium text-muted-foreground uppercase">{priorityLabels[rec.priority]}</span>
            </div>
            <p className="text-sm">{rec.text}</p>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
