"use client";

import { motion } from "framer-motion";
import { AlertTriangle } from "lucide-react";
import type { SkillGap } from "../types";
import { cn } from "@/lib/utils";

interface MissingSkillsCardProps {
  skills: SkillGap[];
}

const importanceStyles = {
  high: "bg-red-500/10 text-red-500 border-red-500/20",
  medium: "bg-amber-500/10 text-amber-500 border-amber-500/20",
  low: "bg-blue-500/10 text-blue-500 border-blue-500/20",
};

export function MissingSkillsCard({ skills }: MissingSkillsCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.1 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <AlertTriangle className="h-5 w-5 text-amber-500" />
        <h3 className="font-semibold text-sm">Missing Skills</h3>
      </div>
      <div className="flex flex-wrap gap-2">
        {skills.map((skill) => (
          <span
            key={skill.skill}
            className={cn("rounded-lg border px-3 py-1.5 text-xs font-medium", importanceStyles[skill.importance])}
          >
            {skill.skill}
          </span>
        ))}
      </div>
      <p className="text-[10px] text-muted-foreground mt-3">
        Based on target roles in your active missions.
      </p>
    </motion.div>
  );
}
