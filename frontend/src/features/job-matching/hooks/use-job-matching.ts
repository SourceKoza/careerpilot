"use client";

import { useQuery } from "@tanstack/react-query";
import { jobMatchingService } from "../services/job-matching.service";

const MATCH_KEY = ["job-match"];

export function useJobMatch() {
  return useQuery({
    queryKey: MATCH_KEY,
    queryFn: () => jobMatchingService.getJobMatch(),
  });
}

export function useSkillComparison() {
  return useQuery({
    queryKey: [...MATCH_KEY, "skills"],
    queryFn: () => jobMatchingService.getSkillComparison(),
  });
}

export function useRecommendations() {
  return useQuery({
    queryKey: [...MATCH_KEY, "recommendations"],
    queryFn: () => jobMatchingService.getRecommendations(),
  });
}

export function useCompatibility() {
  return useQuery({
    queryKey: [...MATCH_KEY, "compatibility"],
    queryFn: () => jobMatchingService.getCompatibilityBreakdown(),
  });
}
