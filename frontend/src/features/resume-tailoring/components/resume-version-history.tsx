"use client";

import { motion } from "framer-motion";
import { History, Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ResumeVersionCard } from "./resume-version-card";
import type { ResumeVersion } from "../types";

interface ResumeVersionHistoryProps {
  versions: ResumeVersion[];
  onSave: () => void;
  onDuplicate: (id: string) => void;
  onDelete: (id: string) => void;
  isSaving?: boolean;
}

export function ResumeVersionHistory({
  versions,
  onSave,
  onDuplicate,
  onDelete,
  isSaving,
}: ResumeVersionHistoryProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.3 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <History className="h-5 w-5 text-primary" />
          <h3 className="font-semibold text-sm">Version History</h3>
        </div>
        <Button size="sm" variant="outline" onClick={onSave} disabled={isSaving}>
          <Plus className="h-3.5 w-3.5 mr-1" />
          Save Version
        </Button>
      </div>

      <div className="space-y-2">
        {versions.map((version) => (
          <ResumeVersionCard
            key={version.id}
            version={version}
            onDuplicate={onDuplicate}
            onDelete={onDelete}
          />
        ))}
      </div>
    </motion.div>
  );
}
