"use client";

import { motion } from "framer-motion";
import { Wand2 } from "lucide-react";
import {
  useMasterResume,
  useTailoredResume,
  useResumeComparison,
  useResumeVersions,
  useExportResume,
} from "../hooks/use-resume-tailoring";
import { LoadingSkeleton } from "./loading-skeleton";
import { ErrorState } from "./error-state";
import { EmptyState } from "./empty-state";
import { OriginalResumeCard } from "./original-resume-card";
import { TailoredResumeCard } from "./tailored-resume-card";
import { TailoringScoreCard } from "./tailoring-score-card";
import { ATSImprovementCard } from "./ats-improvement-card";
import { KeywordOptimizationCard } from "./keyword-optimization-card";
import { ResumeChangesCard } from "./resume-changes-card";
import { AISuggestionsCard } from "./ai-suggestions-card";
import { ResumeComparison } from "./resume-comparison";
import { ResumeVersionHistory } from "./resume-version-history";
import { ExportActions } from "./export-actions";
import { TailoredResumePreview } from "./tailored-resume-preview";

export function ResumeTailoringDashboard() {
  const { data: master, isLoading: masterLoading } = useMasterResume();
  const { data: tailored, isLoading: tailoredLoading, isError } = useTailoredResume();
  const { data: comparison } = useResumeComparison();
  const {
    data: versions,
    saveVersion,
    duplicateVersion,
    deleteVersion,
    isSaving,
  } = useResumeVersions();
  const { exportPdf, exportDocx, isExportingPdf, isExportingDocx } = useExportResume();

  const isLoading = masterLoading || tailoredLoading;

  if (isLoading) {
    return <LoadingSkeleton />;
  }

  if (isError) {
    return (
      <div className="space-y-6">
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
          <h1 className="text-2xl sm:text-3xl font-bold">AI Resume Tailoring</h1>
          <p className="text-muted-foreground">
            Generate job-specific resume versions with AI optimization.
          </p>
        </motion.div>
        <ErrorState />
      </div>
    );
  }

  if (!tailored || !master) {
    return (
      <div className="space-y-6">
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}>
          <h1 className="text-2xl sm:text-3xl font-bold">AI Resume Tailoring</h1>
          <p className="text-muted-foreground">
            Generate job-specific resume versions with AI optimization.
          </p>
        </motion.div>
        <EmptyState
          title="No Resume Data"
          description="Upload your master resume to start generating tailored versions for specific jobs."
        />
      </div>
    );
  }

  const handleSaveVersion = () => {
    saveVersion({
      name: `${tailored.company} - ${tailored.jobTitle}`,
      type: "tailored",
      targetCompany: tailored.company,
      targetRole: tailored.jobTitle,
    });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4"
      >
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold">AI Resume Tailoring</h1>
          <p className="text-muted-foreground">
            {tailored.jobTitle} at {tailored.company}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <span className="inline-flex items-center gap-1.5 rounded-full bg-primary/10 px-3 py-1 text-xs font-medium text-primary">
            <Wand2 className="h-3.5 w-3.5" />
            AI Optimized
          </span>
        </div>
      </motion.div>

      {/* Original vs Tailored */}
      <div className="grid gap-4 lg:grid-cols-2">
        <OriginalResumeCard summary={master.summary} skills={master.skills} />
        <TailoredResumeCard
          summary={tailored.summary}
          skills={tailored.skills}
          jobTitle={tailored.jobTitle}
          company={tailored.company}
        />
      </div>

      {/* Score + ATS + Keywords */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {comparison && <TailoringScoreCard score={comparison.tailoringScore} />}
        {comparison && <ATSImprovementCard data={comparison.atsImprovement} />}
        {comparison && <KeywordOptimizationCard data={comparison.keywordAnalysis} />}
      </div>

      {/* Comparison */}
      {comparison && <ResumeComparison sections={comparison.sections} />}

      {/* Changes + Suggestions */}
      <div className="grid gap-4 lg:grid-cols-2">
        {comparison && <ResumeChangesCard changes={comparison.changes} />}
        {comparison && <AISuggestionsCard suggestions={comparison.suggestions} />}
      </div>

      {/* Tailored Resume Preview */}
      <TailoredResumePreview
        summary={tailored.summary}
        skills={tailored.skills}
        experience={tailored.experience}
        projects={tailored.projects}
      />

      {/* Version History */}
      {versions && (
        <ResumeVersionHistory
          versions={versions}
          onSave={handleSaveVersion}
          onDuplicate={duplicateVersion}
          onDelete={deleteVersion}
          isSaving={isSaving}
        />
      )}

      {/* Export Actions */}
      <ExportActions
        onExportPdf={exportPdf}
        onExportDocx={exportDocx}
        isExportingPdf={isExportingPdf}
        isExportingDocx={isExportingDocx}
      />
    </div>
  );
}
