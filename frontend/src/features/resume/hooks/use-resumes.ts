"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { resumeService } from "@/services/resume.service";
import type { ResumeUpdateRequest } from "@/types/resume";

const RESUMES_KEY = ["resumes"];

export function useResumes() {
  return useQuery({
    queryKey: RESUMES_KEY,
    queryFn: () => resumeService.listResumes(),
  });
}

export function useUploadResume() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ file, title }: { file: File; title?: string }) =>
      resumeService.uploadResume(file, title),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: RESUMES_KEY });
    },
  });
}

export function useUpdateResume() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: ResumeUpdateRequest }) =>
      resumeService.updateResume(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: RESUMES_KEY });
    },
  });
}

export function useSetDefaultResume() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => resumeService.setDefault(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: RESUMES_KEY });
    },
  });
}

export function useDeleteResume() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => resumeService.deleteResume(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: RESUMES_KEY });
    },
  });
}
