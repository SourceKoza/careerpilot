"use client";

import { motion } from "framer-motion";
import { AlertTriangle, Award, FolderOpen, Code } from "lucide-react";
import type { GapAnalysis } from "../types";

interface GapAnalysisCardProps {
  data: GapAnalysis;
}

export function GapAnalysisCard({ data }: GapAnalysisCardProps) {
  const sections = [
    { icon: Code, title: "Technical Gaps", items: data.technicalGaps, color: "text-red-500 bg-red-500/10" },
    { icon: AlertTriangle, title: "Experience Gaps", items: data.experienceGaps, color: "text-amber-500 bg-amber-500/10" },
    { icon: Award, title: "Certifications", items: data.certificationSuggestions, color: "text-blue-500 bg-blue-500/10" },
    { icon: FolderOpen, title: "Project Ideas", items: data.projectSuggestions, color: "text-violet-500 bg-violet-500/10" },
  ];

  return (
    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.2 }} className="rounded-xl border border-border bg-card p-5">
      <div className="flex items-center gap-2 mb-4">
        <AlertTriangle className="h-5 w-5 text-amber-500" />
        <h3 className="font-semibold text-sm">Gap Analysis</h3>
      </div>
      <div className="space-y-4">
        {sections.map((section) => (
          section.items.length > 0 && (
            <div key={section.title}>
              <div className="flex items-center gap-2 mb-2">
                <div className={`flex h-5 w-5 items-center justify-center rounded ${section.color}`}>
                  <section.icon className="h-3 w-3" />
                </div>
                <span className="text-xs font-medium">{section.title}</span>
              </div>
              <ul className="space-y-1 ml-7">
                {section.items.map((item, i) => (
                  <li key={i} className="text-xs text-muted-foreground">• {item}</li>
                ))}
              </ul>
            </div>
          )
        ))}
      </div>
    </motion.div>
  );
}
