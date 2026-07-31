"use client";

import { motion } from "framer-motion";
import { Search } from "lucide-react";
import { cn } from "@/lib/utils";
import type { KeywordAnalysis } from "../types";

interface KeywordOptimizationCardProps {
  data: KeywordAnalysis;
}

function KeywordBadge({
  keyword,
  variant,
}: {
  keyword: string;
  variant: "matched" | "missing" | "added" | "recommended";
}) {
  const styles = {
    matched: "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20",
    missing: "bg-red-500/10 text-red-600 dark:text-red-400 border-red-500/20",
    added: "bg-blue-500/10 text-blue-600 dark:text-blue-400 border-blue-500/20",
    recommended: "bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20",
  };

  return (
    <span
      className={cn(
        "inline-flex items-center rounded-md border px-2 py-0.5 text-xs font-medium",
        styles[variant]
      )}
    >
      {keyword}
    </span>
  );
}

export function KeywordOptimizationCard({ data }: KeywordOptimizationCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.15 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Search className="h-5 w-5 text-primary" />
          <h3 className="font-semibold text-sm">Keyword Optimization</h3>
        </div>
        <span className="text-xs text-muted-foreground">
          Density: {data.density}%
        </span>
      </div>

      <div className="space-y-4">
        {/* Matched */}
        <div>
          <p className="text-xs font-medium text-emerald-600 dark:text-emerald-400 mb-2">
            Matched ({data.matched.length})
          </p>
          <div className="flex flex-wrap gap-1.5">
            {data.matched.map((kw) => (
              <KeywordBadge key={kw} keyword={kw} variant="matched" />
            ))}
          </div>
        </div>

        {/* AI Added */}
        <div>
          <p className="text-xs font-medium text-blue-600 dark:text-blue-400 mb-2">
            AI Added ({data.added.length})
          </p>
          <div className="flex flex-wrap gap-1.5">
            {data.added.map((kw) => (
              <KeywordBadge key={kw} keyword={kw} variant="added" />
            ))}
          </div>
        </div>

        {/* Missing */}
        <div>
          <p className="text-xs font-medium text-red-600 dark:text-red-400 mb-2">
            Missing ({data.missing.length})
          </p>
          <div className="flex flex-wrap gap-1.5">
            {data.missing.map((kw) => (
              <KeywordBadge key={kw} keyword={kw} variant="missing" />
            ))}
          </div>
        </div>

        {/* Recommended */}
        <div>
          <p className="text-xs font-medium text-amber-600 dark:text-amber-400 mb-2">
            Recommended ({data.recommended.length})
          </p>
          <div className="flex flex-wrap gap-1.5">
            {data.recommended.map((kw) => (
              <KeywordBadge key={kw} keyword={kw} variant="recommended" />
            ))}
          </div>
        </div>
      </div>
    </motion.div>
  );
}
