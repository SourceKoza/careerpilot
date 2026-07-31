"use client";

import { motion } from "framer-motion";
import { Brain, Target } from "lucide-react";
import { Skeleton } from "@/components/ui/skeleton";
import { EmptyState } from "@/features/dashboard/components/shared/empty-state";
import { MatchScoreCard } from "./match-score-card";
import { SkillComparison } from "./skill-comparison";
import { StrengthCard } from "./strength-card";
import { GapAnalysisCard } from "./gap-analysis-card";
import { RecommendationCard } from "./recommendation-card";
import { CompatibilityBreakdown } from "./compatibility-breakdown";
import { ApplyReadinessBadge } from "./apply-readiness-badge";
import { useJobMatch } from "../hooks/use-job-matching";

export function JobMatchDashboard() {
  const { data: match, isLoading, isError } = useJobMatch();

  if (isLoading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-8 w-48" />
        <Skeleton className="h-4 w-72" />
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          <Skeleton className="h-80" />
          <Skeleton className="h-80" />
          <Skeleton className="h-80" />
        </div>
      </div>
    );
  }

  if (isError || !match) {
    return (
      <div className="space-y-6">
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
          <h1 className="text-2xl sm:text-3xl font-bold">AI Job Matching</h1>
          <p className="text-muted-foreground">AI-powered resume vs. job compatibility analysis.</p>
        </motion.div>
        <EmptyState
          icon={Target}
          title="No Match Available"
          description="Upload your resume and start an AI mission to see job compatibility analysis."
        />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold">AI Job Matching</h1>
          <p className="text-muted-foreground">
            {match.jobTitle} at {match.company}
          </p>
        </div>
        <ApplyReadinessBadge readiness={match.applyReadiness} />
      </motion.div>

      {/* AI Summary */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.05 }}
        className="rounded-xl border border-primary/30 bg-gradient-to-br from-primary/5 to-violet-500/5 p-5"
      >
        <div className="flex items-center gap-2 mb-2">
          <Brain className="h-5 w-5 text-primary" />
          <h3 className="font-semibold text-sm">AI Assessment</h3>
        </div>
        <p className="text-sm text-muted-foreground leading-relaxed">{match.summary}</p>
      </motion.div>

      {/* Main Grid */}
      <div className="grid gap-4 lg:grid-cols-3">
        <MatchScoreCard score={match.matchScore} />
        <SkillComparison data={match.skillComparison} />
        <StrengthCard strengths={match.strengths} />
      </div>

      {/* Secondary Grid */}
      <div className="grid gap-4 lg:grid-cols-2">
        <GapAnalysisCard data={match.gapAnalysis} />
        <RecommendationCard recommendations={match.recommendations} />
      </div>

      {/* Compatibility */}
      <CompatibilityBreakdown data={match.compatibility} />
    </div>
  );
}
