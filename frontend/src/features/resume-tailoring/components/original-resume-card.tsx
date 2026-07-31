"use client";

import { motion } from "framer-motion";
import { FileText } from "lucide-react";

interface OriginalResumeCardProps {
  summary: string;
  skills: string[];
}

export function OriginalResumeCard({ summary, skills }: OriginalResumeCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <FileText className="h-5 w-5 text-muted-foreground" />
        <h3 className="font-semibold text-sm">Original Resume</h3>
        <span className="ml-auto rounded-full bg-muted px-2 py-0.5 text-[10px] font-medium text-muted-foreground">
          Before
        </span>
      </div>

      <div className="space-y-4">
        <div>
          <p className="text-xs font-medium text-muted-foreground mb-1">Summary</p>
          <p className="text-sm leading-relaxed">{summary}</p>
        </div>
        <div>
          <p className="text-xs font-medium text-muted-foreground mb-2">Skills</p>
          <div className="flex flex-wrap gap-1.5">
            {skills.map((skill) => (
              <span
                key={skill}
                className="inline-flex items-center rounded-md border border-border bg-muted/50 px-2 py-0.5 text-xs"
              >
                {skill}
              </span>
            ))}
          </div>
        </div>
      </div>
    </motion.div>
  );
}
