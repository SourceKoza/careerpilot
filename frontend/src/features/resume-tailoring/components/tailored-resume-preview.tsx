"use client";

import { motion } from "framer-motion";
import { Wand2, Briefcase, Code, FolderKanban } from "lucide-react";
import type { TailoredExperience, TailoredProject } from "../types";

interface TailoredResumePreviewProps {
  summary: string;
  skills: string[];
  experience: TailoredExperience[];
  projects: TailoredProject[];
}

export function TailoredResumePreview({
  summary,
  skills,
  experience,
  projects,
}: TailoredResumePreviewProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.1 }}
      className="rounded-xl border border-primary/20 bg-card p-5 space-y-5"
    >
      <div className="flex items-center gap-2">
        <Wand2 className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Tailored Resume Preview</h3>
      </div>

      {/* Summary */}
      <div>
        <p className="text-xs font-medium text-muted-foreground mb-1">
          Professional Summary
        </p>
        <p className="text-sm leading-relaxed">{summary}</p>
      </div>

      {/* Skills */}
      <div>
        <div className="flex items-center gap-1.5 mb-2">
          <Code className="h-3.5 w-3.5 text-muted-foreground" />
          <p className="text-xs font-medium text-muted-foreground">Technical Skills</p>
        </div>
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

      {/* Experience */}
      <div>
        <div className="flex items-center gap-1.5 mb-3">
          <Briefcase className="h-3.5 w-3.5 text-muted-foreground" />
          <p className="text-xs font-medium text-muted-foreground">Experience</p>
        </div>
        <div className="space-y-4">
          {experience.map((exp) => (
            <div key={exp.title + exp.company} className="space-y-1.5">
              <div className="flex items-center justify-between">
                <p className="text-sm font-medium">{exp.title}</p>
                <span className="text-[10px] text-muted-foreground">{exp.duration}</span>
              </div>
              <p className="text-xs text-muted-foreground">{exp.company}</p>
              <ul className="space-y-1 ml-3">
                {exp.highlights.map((h, i) => (
                  <li key={i} className="text-xs text-muted-foreground list-disc">
                    {h}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>

      {/* Projects */}
      <div>
        <div className="flex items-center gap-1.5 mb-3">
          <FolderKanban className="h-3.5 w-3.5 text-muted-foreground" />
          <p className="text-xs font-medium text-muted-foreground">Projects</p>
        </div>
        <div className="space-y-3">
          {projects.map((project) => (
            <div key={project.name} className="space-y-1">
              <p className="text-sm font-medium">{project.name}</p>
              <p className="text-xs text-muted-foreground">{project.description}</p>
              <div className="flex flex-wrap gap-1">
                {project.technologies.map((tech) => (
                  <span
                    key={tech}
                    className="rounded bg-muted px-1.5 py-0.5 text-[10px] text-muted-foreground"
                  >
                    {tech}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    </motion.div>
  );
}
