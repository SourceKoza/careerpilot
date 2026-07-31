"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { resumeIntelligenceService } from "../services/resume.service";

const RESUME_KEY = ["resume"];
const ANALYSIS_KEY = ["resume-analysis"];
const VERSIONS_KEY = ["resume-versions"];

export function useResume() {
  return useQuery({
    queryKey: RESUME_KEY,
    queryFn: () => resumeIntelligenceService.getResume(),
  });
}

export function useUploadResume() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (file: File) => resumeIntelligenceService.uploadResume(file),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: RESUME_KEY });
    },
  });
}

export function useResumeAnalysis() {
  return useQuery({
    queryKey: ANALYSIS_KEY,
    queryFn: () => resumeIntelligenceService.analyzeResume(),
  });
}

export function useResumeVersions() {
  return useQuery({
    queryKey: VERSIONS_KEY,
    queryFn: () => resumeIntelligenceService.getResumeVersions(),
  });
}
