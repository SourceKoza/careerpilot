"use client";

import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { resumeTailoringService } from "../services/resume-tailoring.service";
import type { ResumeVersion } from "../types";

const TAILORING_KEY = ["resume-tailoring"];

export function useMasterResume() {
  return useQuery({
    queryKey: [...TAILORING_KEY, "master"],
    queryFn: () => resumeTailoringService.getMasterResume(),
  });
}

export function useTailoredResume() {
  return useQuery({
    queryKey: [...TAILORING_KEY, "tailored"],
    queryFn: () => resumeTailoringService.generateTailoredResume(),
  });
}

export function useResumeComparison() {
  return useQuery({
    queryKey: [...TAILORING_KEY, "comparison"],
    queryFn: () => resumeTailoringService.compareResume(),
  });
}

export function useResumeVersions() {
  const queryClient = useQueryClient();

  const versionsQuery = useQuery({
    queryKey: [...TAILORING_KEY, "versions"],
    queryFn: () => resumeTailoringService.getResumeVersions(),
  });

  const saveMutation = useMutation({
    mutationFn: (version: Partial<ResumeVersion>) =>
      resumeTailoringService.saveResumeVersion(version),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [...TAILORING_KEY, "versions"] });
    },
  });

  const duplicateMutation = useMutation({
    mutationFn: (versionId: string) =>
      resumeTailoringService.duplicateResumeVersion(versionId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [...TAILORING_KEY, "versions"] });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (versionId: string) =>
      resumeTailoringService.deleteResumeVersion(versionId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [...TAILORING_KEY, "versions"] });
    },
  });

  return {
    ...versionsQuery,
    saveVersion: saveMutation.mutate,
    duplicateVersion: duplicateMutation.mutate,
    deleteVersion: deleteMutation.mutate,
    isSaving: saveMutation.isPending,
    isDuplicating: duplicateMutation.isPending,
    isDeleting: deleteMutation.isPending,
  };
}

export function useExportResume() {
  const exportPdfMutation = useMutation({
    mutationFn: () => resumeTailoringService.exportResumePdf(),
  });

  const exportDocxMutation = useMutation({
    mutationFn: () => resumeTailoringService.exportResumeDocx(),
  });

  return {
    exportPdf: exportPdfMutation.mutate,
    exportDocx: exportDocxMutation.mutate,
    isExportingPdf: exportPdfMutation.isPending,
    isExportingDocx: exportDocxMutation.isPending,
    pdfResult: exportPdfMutation.data,
    docxResult: exportDocxMutation.data,
  };
}
