"use client";

import { motion } from "framer-motion";
import { Sparkles, Check } from "lucide-react";
import { cn } from "@/lib/utils";
import type { AISuggestion } from "../types";

interface AISuggestionsCardProps {
  suggestions: AISuggestion[];
}

const priorityStyles: Record<AISuggestion["priority"], string> = {
  high: "border-l-red-500",
  medium: "border-l-amber-500",
  low: "border-l-blue-500",
};

const categoryLabels: Record<AISuggestion["category"], string> = {
  summary: "Summary",
  skills: "Skills",
  experience: "Experience",
  projects: "Projects",
  achievements: "Achievements",
};

export function AISuggestionsCard({ suggestions }: AISuggestionsCardProps) {
  const applied = suggestions.filter((s) => s.applied).length;

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.25 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Sparkles className="h-5 w-5 text-primary" />
          <h3 className="font-semibold text-sm">AI Suggestions</h3>
        </div>
        <span className="text-xs text-muted-foreground">
          {applied}/{suggestions.length} applied
        </span>
      </div>

      <div className="space-y-2">
        {suggestions.map((suggestion) => (
          <div
            key={suggestion.id}
            className={cn(
              "flex items-start gap-3 rounded-lg border-l-2 p-3",
              priorityStyles[suggestion.priority],
              suggestion.applied ? "bg-emerald-500/5" : "bg-muted/30"
            )}
          >
            <div
              className={cn(
                "flex h-5 w-5 shrink-0 items-center justify-center rounded-full border",
                suggestion.applied
                  ? "bg-emerald-500 border-emerald-500"
                  : "border-muted-foreground/30"
              )}
            >
              {suggestion.applied && (
                <Check className="h-3 w-3 text-white" />
              )}
            </div>
            <div className="flex-1 min-w-0">
              <p
                className={cn(
                  "text-sm",
                  suggestion.applied && "line-through text-muted-foreground"
                )}
              >
                {suggestion.text}
              </p>
              <span className="text-[10px] text-muted-foreground uppercase mt-0.5 inline-block">
                {categoryLabels[suggestion.category]}
              </span>
            </div>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
