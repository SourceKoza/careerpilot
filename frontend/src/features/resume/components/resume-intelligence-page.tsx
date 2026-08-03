"use client";

import { useState } from "react";
import { motion } from "framer-motion";
import { FileText, Upload, Brain, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { EmptyState } from "@/features/dashboard/components/shared/empty-state";
import { ResumeUploader } from "./resume-uploader";
import { ATSScoreCard } from "./ats-score-card";
import { MissingSkillsCard } from "./missing-skills-card";
import { KeywordMatchCard } from "./keyword-match-card";
import { AISuggestionsCard } from "./ai-suggestions-card";
import { ResumeVersions } from "./resume-versions";
import { useResume, useAnalyzeResume, useResumeVersions } from "../hooks/use-resume";
import { useQueryClient } from "@tanstack/react-query";

function formatSize(bytes: number): string {
  return bytes < 1024 * 1024 ? `${(bytes / 1024).toFixed(0)} KB` : `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleDateString("en-US", { month: "short", day: "numeric", year: "numeric" });
}

export function ResumeIntelligencePage() {
  const { data: resume, isLoading: resumeLoading } = useResume();
  const queryClient = useQueryClient();
  const cachedAnalysis = queryClient.getQueryData<import("../types").ResumeAnalysis>(["resume-analysis"]);
  const analyzeMutation = useAnalyzeResume();
  const { data: versions, isLoading: versionsLoading } = useResumeVersions();
  const [showUploader, setShowUploader] = useState(false);

  const analysis = cachedAnalysis || analyzeMutation.data;
  const analysisLoading = analyzeMutation.isPending;

  if (resumeLoading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-8 w-48" />
        <Skeleton className="h-4 w-72" />
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          <Skeleton className="h-64" />
          <Skeleton className="h-64" />
          <Skeleton className="h-64" />
        </div>
      </div>
    );
  }

  if (!resume && !showUploader) {
    return (
      <div className="space-y-6">
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
          <h1 className="text-2xl sm:text-3xl font-bold">Resume Intelligence</h1>
          <p className="text-muted-foreground">Upload your master resume to unlock AI-powered insights.</p>
        </motion.div>
        <EmptyState
          icon={FileText}
          title="No Resume Uploaded"
          description="Upload your master resume and let AI analyze it for ATS optimization, keyword matching, and skill gaps."
          actionLabel="Upload Resume"
          onAction={() => setShowUploader(true)}
        />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold">Resume Intelligence</h1>
          <p className="text-muted-foreground">AI-powered analysis of your master resume.</p>
        </div>
        <Button variant="outline" onClick={() => setShowUploader(!showUploader)}>
          <Upload className="h-4 w-4" />
          {resume ? "Replace Resume" : "Upload Resume"}
        </Button>
      </motion.div>

      {/* Uploader */}
      {showUploader && (
        <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: "auto" }}>
          <ResumeUploader />
        </motion.div>
      )}

      {/* Master Resume Card */}
      {resume && (
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.05 }} className="rounded-xl border border-primary/30 bg-gradient-to-br from-primary/5 to-violet-500/5 p-5">
          <div className="flex items-center gap-4">
            <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-primary/10">
              <FileText className="h-7 w-7 text-primary" />
            </div>
            <div className="flex-1">
              <div className="flex items-center gap-2">
                <h3 className="font-semibold">{resume.name}</h3>
                <span className="text-[10px] bg-primary/10 text-primary px-2 py-0.5 rounded-full font-medium">Master</span>
              </div>
              <p className="text-sm text-muted-foreground">{resume.fileName}</p>
              <div className="flex gap-3 mt-1 text-xs text-muted-foreground">
                <span className="uppercase font-medium">{resume.fileType}</span>
                <span>{formatSize(resume.fileSize)}</span>
                <span>Updated {formatDate(resume.updatedAt)}</span>
              </div>
            </div>
            <div className="hidden sm:flex items-center gap-2">
              <Brain className="h-4 w-4 text-primary" />
              <span className="text-xs text-primary font-medium">AI Analyzed</span>
            </div>
          </div>
        </motion.div>
      )}

      {/* Analysis Grid */}
      {analysisLoading ? (
        <div className="flex items-center justify-center py-12 text-center">
          <div className="space-y-3">
            <Loader2 className="h-8 w-8 text-primary animate-spin mx-auto" />
            <p className="text-sm text-muted-foreground">Analyzing your resume with AI... (may take 10-15s)</p>
          </div>
        </div>
      ) : analysis ? (
        <>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            <ATSScoreCard score={analysis.atsScore} />
            <MissingSkillsCard skills={analysis.missingSkills} />
            <KeywordMatchCard matchPercentage={analysis.keywordMatch} strengths={analysis.strengths} />
          </div>
          <div className="grid gap-4 lg:grid-cols-2">
            <AISuggestionsCard suggestions={analysis.suggestions} />
            {versionsLoading ? (
              <Skeleton className="h-64" />
            ) : versions ? (
              <ResumeVersions versions={versions} />
            ) : null}
          </div>
          <div className="flex justify-center pt-2">
            <Button variant="outline" size="sm" onClick={() => analyzeMutation.mutate()} disabled={analyzeMutation.isPending}>
              <Brain className="h-4 w-4" />
              Re-analyze Resume
            </Button>
          </div>
        </>
      ) : (
        <div className="flex flex-col items-center justify-center py-12 text-center gap-4">
          <Brain className="h-12 w-12 text-muted-foreground" />
          <div className="space-y-1">
            <p className="font-medium">Resume not yet analyzed</p>
            <p className="text-sm text-muted-foreground">Click below to run AI analysis (ATS score, skill gaps, suggestions)</p>
          </div>
          <Button onClick={() => analyzeMutation.mutate()} disabled={analyzeMutation.isPending}>
            {analyzeMutation.isPending ? <Loader2 className="h-4 w-4 animate-spin" /> : <Brain className="h-4 w-4" />}
            Analyze with AI
          </Button>
        </div>
      )}
    </div>
  );
}
