"use client";

import { motion, AnimatePresence } from "framer-motion";
import { X, FileText, Download, Star, Calendar, HardDrive } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { Resume } from "@/types/resume";

interface ResumePreviewProps {
  resume: Resume | null;
  onClose: () => void;
  onDownload: (id: string) => void;
}

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleDateString("en-US", {
    month: "long",
    day: "numeric",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

export function ResumePreview({ resume, onClose, onDownload }: ResumePreviewProps) {
  return (
    <AnimatePresence>
      {resume && (
        <>
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50"
            onClick={onClose}
          />
          <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: 100 }}
            transition={{ type: "spring", damping: 25, stiffness: 300 }}
            className="fixed right-0 top-0 bottom-0 w-full max-w-md bg-card border-l border-border shadow-xl z-50 overflow-y-auto"
          >
            <div className="p-6">
              {/* Header */}
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-lg font-semibold">Resume Preview</h2>
                <Button variant="ghost" size="icon" onClick={onClose}>
                  <X className="h-4 w-4" />
                </Button>
              </div>

              {/* File icon and title */}
              <div className="flex items-center gap-4 mb-6">
                <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-primary/10">
                  <FileText className="h-7 w-7 text-primary" />
                </div>
                <div>
                  <h3 className="font-semibold">{resume.title}</h3>
                  <p className="text-sm text-muted-foreground">
                    {resume.fileName}
                  </p>
                  {resume.isDefault && (
                    <span className="inline-flex items-center gap-1 mt-1 text-xs text-primary">
                      <Star className="h-3 w-3 fill-primary" /> Default Resume
                    </span>
                  )}
                </div>
              </div>

              {/* Metadata */}
              <div className="space-y-4 mb-6">
                {resume.summary && (
                  <div>
                    <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide mb-1">
                      Summary
                    </p>
                    <p className="text-sm">{resume.summary}</p>
                  </div>
                )}
                {resume.targetRole && (
                  <div>
                    <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide mb-1">
                      Target Role
                    </p>
                    <p className="text-sm">{resume.targetRole}</p>
                  </div>
                )}
              </div>

              {/* File details */}
              <div className="rounded-lg border border-border p-4 space-y-3 mb-6">
                <div className="flex items-center justify-between text-sm">
                  <span className="flex items-center gap-2 text-muted-foreground">
                    <FileText className="h-4 w-4" /> Type
                  </span>
                  <span className="font-medium uppercase">{resume.fileType}</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span className="flex items-center gap-2 text-muted-foreground">
                    <HardDrive className="h-4 w-4" /> Size
                  </span>
                  <span className="font-medium">{formatFileSize(resume.fileSize)}</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span className="flex items-center gap-2 text-muted-foreground">
                    <Calendar className="h-4 w-4" /> Uploaded
                  </span>
                  <span className="font-medium">{formatDate(resume.createdAt)}</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span className="flex items-center gap-2 text-muted-foreground">
                    <Calendar className="h-4 w-4" /> Last Modified
                  </span>
                  <span className="font-medium">{formatDate(resume.updatedAt)}</span>
                </div>
              </div>

              {/* Actions */}
              <Button
                onClick={() => onDownload(resume.id)}
                className="w-full"
              >
                <Download className="h-4 w-4" />
                Download Resume
              </Button>
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  );
}
