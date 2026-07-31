"use client";

import { motion } from "framer-motion";
import { Sparkles, FileText, Hash, Layout } from "lucide-react";
import type { ResumeSuggestion } from "../types";
import type { LucideIcon } from "lucide-react";

interface AISuggestionsCardProps {
  suggestions: ResumeSuggestion[];
}

const categoryIcons: Record<string, LucideIcon> = {
  content: FileText,
  keywords: Hash,
  formatting: Layout,
};

const categoryColors: Record<string, string> = {
  content: "bg-violet-500/10 text-violet-500",
  keywords: "bg-blue-500/10 text-blue-500",
  formatting: "bg-emerald-500/10 text-emerald-500",
};

export function AISuggestionsCard({ suggestions }: AISuggestionsCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.2 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <Sparkles className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">AI Suggestions</h3>
      </div>
      <div className="space-y-3">
        {suggestions.map((suggestion) => {
          const Icon = categoryIcons[suggestion.category] || FileText;
          return (
            <div key={suggestion.id} className="flex items-start gap-3">
              <div className={`flex h-7 w-7 shrink-0 items-center justify-center rounded-lg ${categoryColors[suggestion.category]}`}>
                <Icon className="h-3.5 w-3.5" />
              </div>
              <p className="text-sm text-muted-foreground leading-relaxed">{suggestion.text}</p>
            </div>
          );
        })}
      </div>
    </motion.div>
  );
}
