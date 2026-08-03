"use client";

import { motion } from "framer-motion";
import { Wand2 } from "lucide-react";

interface TailoredResumeCardProps {
  summary: string;
  skills: string[];
  jobTitle: string;
  company: string;
}

export function TailoredResumeCard({
  summary,
  skills,
  jobTitle,
  company,
}: TailoredResumeCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.05 }}
      className="rounded-xl border border-primary/30 bg-gradient-to-br from-primary/5 to-violet-500/5 p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <Wand2 className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Tailored Resume</h3>
        <span className="ml-auto rounded-full bg-primary/10 px-2 py-0.5 text-[10px] font-medium text-primary">
          AI Generated
        </span>
      </div>

      <p className="text-xs text-muted-foreground mb-3">
        Optimized for {jobTitle} at {company}
      </p>

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
                className="inline-flex items-center rounded-md border border-primary/20 bg-primary/5 px-2 py-0.5 text-xs text-primary"
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
