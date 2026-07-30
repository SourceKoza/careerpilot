"use client";

import { useState, useMemo } from "react";
import { motion } from "framer-motion";
import { Search, FileText, Upload, SlidersHorizontal, ArrowUpDown } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { EmptyState } from "@/features/dashboard/components/shared/empty-state";
import { StatCard } from "@/features/dashboard/components/shared/stat-card";
import { ResumeCard } from "./resume-card";
import { ResumeUploader } from "./resume-uploader";
import { ResumePreview } from "./resume-preview";
import {
  useResumes,
  useDeleteResume,
  useUpdateResume,
  useSetDefaultResume,
} from "../hooks/use-resumes";
import { resumeService } from "@/services/resume.service";
import type { Resume, ResumeFileFilter, ResumeSortField, ResumeSortOrder } from "@/types/resume";

export function ResumePage() {
  const { data: resumes, isLoading } = useResumes();
  const deleteMutation = useDeleteResume();
  const updateMutation = useUpdateResume();
  const setDefaultMutation = useSetDefaultResume();

  const [showUploader, setShowUploader] = useState(false);
  const [previewResume, setPreviewResume] = useState<Resume | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [fileFilter, setFileFilter] = useState<ResumeFileFilter>("all");
  const [sortField, setSortField] = useState<ResumeSortField>("createdAt");
  const [sortOrder, setSortOrder] = useState<ResumeSortOrder>("desc");

  const filteredResumes = useMemo(() => {
    if (!resumes) return [];
    let result = [...resumes];

    // Search
    if (searchQuery) {
      const q = searchQuery.toLowerCase();
      result = result.filter(
        (r) =>
          r.title.toLowerCase().includes(q) ||
          r.fileName.toLowerCase().includes(q)
      );
    }

    // Filter
    if (fileFilter !== "all") {
      result = result.filter((r) => r.fileType === fileFilter);
    }

    // Sort
    result.sort((a, b) => {
      let cmp = 0;
      if (sortField === "title") cmp = a.title.localeCompare(b.title);
      else if (sortField === "fileSize") cmp = a.fileSize - b.fileSize;
      else cmp = new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      return sortOrder === "desc" ? -cmp : cmp;
    });

    return result;
  }, [resumes, searchQuery, fileFilter, sortField, sortOrder]);

  const defaultResume = resumes?.find((r) => r.isDefault);
  const totalResumes = resumes?.length ?? 0;

  const handleDownload = (id: string) => {
    resumeService.downloadResume(id);
  };

  const handleDelete = (id: string) => {
    deleteMutation.mutate(id);
  };

  const handleRename = (id: string, newTitle: string) => {
    updateMutation.mutate({ id, data: { title: newTitle } });
  };

  const handleSetDefault = (id: string) => {
    setDefaultMutation.mutate(id);
  };

  const toggleSort = (field: ResumeSortField) => {
    if (sortField === field) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortOrder("desc");
    }
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-8 w-48" />
        <div className="grid gap-4 sm:grid-cols-3">
          <Skeleton className="h-24" />
          <Skeleton className="h-24" />
          <Skeleton className="h-24" />
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <Skeleton className="h-32" />
          <Skeleton className="h-32" />
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4"
      >
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold">Resumes</h1>
          <p className="text-muted-foreground">
            Manage and organize your resumes.
          </p>
        </div>
        <Button onClick={() => setShowUploader(!showUploader)}>
          <Upload className="h-4 w-4" />
          Upload Resume
        </Button>
      </motion.div>

      {/* Stats */}
      <div className="grid gap-4 sm:grid-cols-3">
        <StatCard
          icon={FileText}
          label="Total Resumes"
          value={totalResumes}
          color="text-violet-500"
          delay={0.05}
        />
        <StatCard
          icon={FileText}
          label="Default Resume"
          value={defaultResume?.title || "None set"}
          color="text-blue-500"
          delay={0.1}
        />
        <StatCard
          icon={Upload}
          label="Last Updated"
          value={
            resumes && resumes.length > 0
              ? new Date(resumes[0].updatedAt).toLocaleDateString()
              : "—"
          }
          color="text-green-500"
          delay={0.15}
        />
      </div>

      {/* Upload Section */}
      {showUploader && (
        <motion.div
          initial={{ opacity: 0, height: 0 }}
          animate={{ opacity: 1, height: "auto" }}
          exit={{ opacity: 0, height: 0 }}
        >
          <ResumeUploader />
        </motion.div>
      )}

      {/* Empty State */}
      {totalResumes === 0 && !showUploader ? (
        <EmptyState
          icon={FileText}
          title="No resumes uploaded yet"
          description="Upload your first resume to get started with AI-powered optimization."
          actionLabel="Upload Resume"
          onAction={() => setShowUploader(true)}
        />
      ) : totalResumes > 0 ? (
        <>
          {/* Search, Filter, Sort */}
          <div className="flex flex-col sm:flex-row gap-3">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search resumes..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9"
              />
            </div>
            <div className="flex gap-2">
              <select
                value={fileFilter}
                onChange={(e) => setFileFilter(e.target.value as ResumeFileFilter)}
                className="rounded-lg border border-input bg-transparent px-3 py-2 text-sm"
                aria-label="Filter by file type"
              >
                <option value="all">All Types</option>
                <option value="pdf">PDF</option>
                <option value="docx">DOCX</option>
              </select>
              <Button
                variant="outline"
                size="icon"
                onClick={() => toggleSort("createdAt")}
                aria-label="Sort resumes"
              >
                <ArrowUpDown className="h-4 w-4" />
              </Button>
              <Button
                variant="outline"
                size="icon"
                onClick={() => toggleSort("title")}
                aria-label="Sort by name"
              >
                <SlidersHorizontal className="h-4 w-4" />
              </Button>
            </div>
          </div>

          {/* Resume Grid */}
          {filteredResumes.length === 0 ? (
            <div className="text-center py-12 text-muted-foreground">
              No resumes match your search.
            </div>
          ) : (
            <div className="grid gap-4 sm:grid-cols-2">
              {filteredResumes.map((resume, index) => (
                <ResumeCard
                  key={resume.id}
                  resume={resume}
                  onPreview={setPreviewResume}
                  onDownload={handleDownload}
                  onDelete={handleDelete}
                  onRename={handleRename}
                  onSetDefault={handleSetDefault}
                  delay={index * 0.05}
                />
              ))}
            </div>
          )}
        </>
      ) : null}

      {/* Preview Panel */}
      <ResumePreview
        resume={previewResume}
        onClose={() => setPreviewResume(null)}
        onDownload={handleDownload}
      />
    </div>
  );
}
