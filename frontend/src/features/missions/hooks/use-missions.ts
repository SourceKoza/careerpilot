"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { missionService } from "@/services/mission.service";
import type { CreateMissionData } from "@/types/mission";

const MISSIONS_KEY = ["missions"];

export function useMissions() {
  return useQuery({
    queryKey: MISSIONS_KEY,
    queryFn: () => missionService.listMissions(),
  });
}

export function useMission(id: string) {
  return useQuery({
    queryKey: [...MISSIONS_KEY, id],
    queryFn: () => missionService.getMission(id),
    enabled: !!id,
  });
}

export function useMissionProgress(id: string) {
  return useQuery({
    queryKey: [...MISSIONS_KEY, id, "progress"],
    queryFn: () => missionService.getMissionProgress(id),
    enabled: !!id,
  });
}

export function useMissionTimeline(id: string) {
  return useQuery({
    queryKey: [...MISSIONS_KEY, id, "timeline"],
    queryFn: () => missionService.getMissionTimeline(id),
    enabled: !!id,
  });
}

export function useCreateMission() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateMissionData) => missionService.createMission(data),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }); },
  });
}

export function usePauseMission() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => missionService.pauseMission(id),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }); },
  });
}

export function useResumeMission() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => missionService.resumeMission(id),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }); },
  });
}

export function useRunMissionNow() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => missionService.runNow(id),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }); },
  });
}

export function useDeleteMission() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => missionService.deleteMission(id),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }); },
  });
}
