"use client";

import { useState, useCallback } from "react";
import { motion } from "framer-motion";
import { Upload, FileText, X, Loader2, CheckCircle2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useUploadResume } from "../hooks/use-resume";
import { cn } from "@/lib/utils";

const ACCEPTED_TYPES = ["application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"];
const MAX_SIZE = 10 * 1024 * 1024;

export function ResumeUploader() {
  const [dragActive, setDragActive] = useState(false);
  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const uploadMutation = useUploadResume();

  const validate = useCallback((f: File): string | null => {
    if (!ACCEPTED_TYPES.includes(f.type)) return "Only PDF and DOCX files are supported.";
    if (f.size > MAX_SIZE) return "File must be less than 10MB.";
    return null;
  }, []);

  const handleFile = useCallback((f: File) => {
    const err = validate(f);
    if (err) { setError(err); setFile(null); return; }
    setError(null);
    setFile(f);
  }, [validate]);

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setDragActive(false);
    const f = e.dataTransfer.files[0];
    if (f) handleFile(f);
  }, [handleFile]);

  const handleUpload = () => {
    if (!file) return;
    uploadMutation.mutate(file, { onSuccess: () => setFile(null) });
  };

  const formatSize = (bytes: number) => bytes < 1024 * 1024 ? `${(bytes / 1024).toFixed(1)} KB` : `${(bytes / (1024 * 1024)).toFixed(1)} MB`;

  return (
    <div className="space-y-4">
      <div
        onDragOver={(e) => { e.preventDefault(); setDragActive(true); }}
        onDragLeave={() => setDragActive(false)}
        onDrop={handleDrop}
        className={cn(
          "relative rounded-xl border-2 border-dashed p-8 text-center transition-all duration-200",
          dragActive ? "border-primary bg-primary/5 scale-[1.01]" : "border-border hover:border-primary/50 hover:bg-secondary/30"
        )}
      >
        <input type="file" accept=".pdf,.docx" onChange={(e) => { const f = e.target.files?.[0]; if (f) handleFile(f); }} className="absolute inset-0 cursor-pointer opacity-0" aria-label="Upload resume" />
        <motion.div animate={dragActive ? { scale: 1.1, y: -4 } : { scale: 1, y: 0 }} className="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-primary/10">
          <Upload className="h-7 w-7 text-primary" />
        </motion.div>
        <p className="text-sm font-medium mt-3">{dragActive ? "Drop your file here" : "Drag & drop or click to upload"}</p>
        <p className="text-xs text-muted-foreground mt-1">PDF or DOCX, max 10MB</p>
      </div>

      {error && <p className="text-sm text-destructive flex items-center gap-1"><X className="h-3.5 w-3.5" /> {error}</p>}

      {file && (
        <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: "auto" }} className="rounded-lg border border-border bg-card p-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10"><FileText className="h-5 w-5 text-primary" /></div>
              <div><p className="text-sm font-medium truncate max-w-[200px]">{file.name}</p><p className="text-xs text-muted-foreground">{formatSize(file.size)}</p></div>
            </div>
            <div className="flex gap-2">
              <Button variant="ghost" size="icon" onClick={() => setFile(null)} disabled={uploadMutation.isPending}><X className="h-4 w-4" /></Button>
              <Button onClick={handleUpload} disabled={uploadMutation.isPending} size="sm">
                {uploadMutation.isPending ? <Loader2 className="h-4 w-4 animate-spin" /> : uploadMutation.isSuccess ? <CheckCircle2 className="h-4 w-4" /> : <Upload className="h-4 w-4" />}
                {uploadMutation.isPending ? "Uploading..." : "Upload"}
              </Button>
            </div>
          </div>
          {uploadMutation.isPending && (
            <div className="mt-3 h-1.5 rounded-full bg-primary/20 overflow-hidden">
              <motion.div className="h-full bg-primary rounded-full" initial={{ width: "0%" }} animate={{ width: "90%" }} transition={{ duration: 1.5 }} />
            </div>
          )}
        </motion.div>
      )}
    </div>
  );
}
