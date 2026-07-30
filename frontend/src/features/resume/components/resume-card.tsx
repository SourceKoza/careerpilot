"use client";

import { motion } from "framer-motion";
import {
  FileText,
  Download,
  Trash2,
  Star,
  MoreVertical,
  Pencil,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import type { Resume } from "@/types/resume";
import { cn } from "@/lib/utils";
import { useState, useRef, useEffect } from "react";

interface ResumeCardProps {
  resume: Resume;
  onPreview: (resume: Resume) => void;
  onDownload: (id: string) => void;
  onDelete: (id: string) => void;
  onRename: (id: string, newTitle: string) => void;
  onSetDefault: (id: string) => void;
  delay?: number;
}

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(dateStr: string): string {
  return new Date(dateStr).toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
}

export function ResumeCard({
  resume,
  onPreview,
  onDownload,
  onDelete,
  onRename,
  onSetDefault,
  delay = 0,
}: ResumeCardProps) {
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    function handleClick(e: MouseEvent) {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        setMenuOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClick);
    return () => document.removeEventListener("mousedown", handleClick);
  }, []);

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay }}
      className={cn(
        "group relative rounded-xl border bg-card p-4 transition-all duration-200 hover:shadow-md hover:border-primary/30",
        resume.isDefault && "border-primary/40 ring-1 ring-primary/20"
      )}
    >
      {resume.isDefault && (
        <div className="absolute top-3 right-3">
          <span className="inline-flex items-center gap-1 rounded-full bg-primary/10 px-2 py-0.5 text-[10px] font-medium text-primary">
            <Star className="h-2.5 w-2.5 fill-primary" />
            Default
          </span>
        </div>
      )}

      <button
        onClick={() => onPreview(resume)}
        className="w-full text-left"
        aria-label={`Preview ${resume.title}`}
      >
        <div className="flex items-start gap-3">
          <div
            className={cn(
              "flex h-11 w-11 shrink-0 items-center justify-center rounded-lg",
              resume.fileType === "pdf"
                ? "bg-red-500/10 text-red-500"
                : "bg-blue-500/10 text-blue-500"
            )}
          >
            <FileText className="h-5 w-5" />
          </div>
          <div className="min-w-0 flex-1">
            <p className="text-sm font-semibold truncate pr-16">{resume.title}</p>
            <p className="text-xs text-muted-foreground mt-0.5">
              {resume.fileName}
            </p>
            <div className="flex items-center gap-3 mt-2 text-xs text-muted-foreground">
              <span className="uppercase font-medium">{resume.fileType}</span>
              <span>{formatFileSize(resume.fileSize)}</span>
              <span>{formatDate(resume.updatedAt)}</span>
            </div>
          </div>
        </div>
      </button>

      {/* Actions menu */}
      <div className="absolute bottom-3 right-3" ref={menuRef}>
        <Button
          variant="ghost"
          size="icon"
          className="h-8 w-8 opacity-0 group-hover:opacity-100 transition-opacity"
          onClick={() => setMenuOpen(!menuOpen)}
          aria-label="Resume actions"
        >
          <MoreVertical className="h-4 w-4" />
        </Button>

        {menuOpen && (
          <div className="absolute right-0 bottom-10 w-44 rounded-lg border border-border bg-card shadow-lg z-10 p-1">
            <button
              onClick={() => {
                setMenuOpen(false);
                onDownload(resume.id);
              }}
              className="flex w-full items-center gap-2 rounded-md px-3 py-1.5 text-sm hover:bg-secondary transition-colors"
            >
              <Download className="h-3.5 w-3.5" /> Download
            </button>
            <button
              onClick={() => {
                setMenuOpen(false);
                const newTitle = prompt("Rename resume:", resume.title);
                if (newTitle && newTitle !== resume.title) {
                  onRename(resume.id, newTitle);
                }
              }}
              className="flex w-full items-center gap-2 rounded-md px-3 py-1.5 text-sm hover:bg-secondary transition-colors"
            >
              <Pencil className="h-3.5 w-3.5" /> Rename
            </button>
            {!resume.isDefault && (
              <button
                onClick={() => {
                  setMenuOpen(false);
                  onSetDefault(resume.id);
                }}
                className="flex w-full items-center gap-2 rounded-md px-3 py-1.5 text-sm hover:bg-secondary transition-colors"
              >
                <Star className="h-3.5 w-3.5" /> Set as Default
              </button>
            )}
            <button
              onClick={() => {
                setMenuOpen(false);
                if (confirm("Delete this resume? This cannot be undone.")) {
                  onDelete(resume.id);
                }
              }}
              className="flex w-full items-center gap-2 rounded-md px-3 py-1.5 text-sm text-destructive hover:bg-destructive/10 transition-colors"
            >
              <Trash2 className="h-3.5 w-3.5" /> Delete
            </button>
          </div>
        )}
      </div>
    </motion.div>
  );
}
