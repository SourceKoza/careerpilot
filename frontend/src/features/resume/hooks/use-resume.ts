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
      // Clear analysis cache when resume changes so user can re-analyze
      queryClient.removeQueries({ queryKey: ANALYSIS_KEY });
    },
  });
}

export function useResumeAnalysis() {
  return useQuery({
    queryKey: ANALYSIS_KEY,
    queryFn: () => resumeIntelligenceService.analyzeResume(),
    // Never auto-refetch — analysis is expensive (calls LLM)
    staleTime: Infinity,
    gcTime: 1000 * 60 * 30, // Keep cached for 30 minutes
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    refetchOnReconnect: false,
    // Only run if explicitly enabled
    enabled: false,
  });
}

export function useAnalyzeResume() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => resumeIntelligenceService.analyzeResume(),
    onSuccess: (data) => {
      // Cache the result
      queryClient.setQueryData(ANALYSIS_KEY, data);
    },
  });
}

export function useResumeVersions() {
  return useQuery({
    queryKey: VERSIONS_KEY,
    queryFn: () => resumeIntelligenceService.getResumeVersions(),
  });
}
