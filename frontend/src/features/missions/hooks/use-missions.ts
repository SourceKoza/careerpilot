"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { missionService } from "@/services/mission.service";
import type { CreateMissionData, Mission } from "@/types/mission";

const MISSIONS_KEY = ["missions"];

export function useMissions() {
  const query = useQuery({
    queryKey: MISSIONS_KEY,
    queryFn: () => missionService.listMissions(),
    // Auto-refetch every 5 seconds if any mission is "active" (running)
    refetchInterval: (query) => {
      const missions = query.state.data as Mission[] | undefined;
      const hasRunning = missions?.some((m) => m.status === "active");
      return hasRunning ? 5000 : false;
    },
  });
  return query;
}

export function useMission(id: string) {
  return useQuery({
    queryKey: [...MISSIONS_KEY, id],
    queryFn: () => missionService.getMission(id),
    enabled: !!id,
    refetchInterval: 5000,
  });
}

export function useMissionProgress(id: string) {
  return useQuery({
    queryKey: [...MISSIONS_KEY, id, "progress"],
    queryFn: () => missionService.getMissionProgress(id),
    enabled: !!id,
    refetchInterval: 5000,
  });
}

export function useMissionTimeline(id: string) {
  return useQuery({
    queryKey: [...MISSIONS_KEY, id, "timeline"],
    queryFn: () => missionService.getMissionTimeline(id),
    enabled: !!id,
    refetchInterval: 5000,
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
    onSuccess: () => {
      // Immediately refetch to show "active" status
      queryClient.invalidateQueries({ queryKey: MISSIONS_KEY });
      // Refetch again after a delay to catch completion
      setTimeout(() => queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }), 10000);
      setTimeout(() => queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }), 30000);
      setTimeout(() => queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }), 60000);
      setTimeout(() => queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }), 90000);
    },
  });
}

export function useDeleteMission() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => missionService.deleteMission(id),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: MISSIONS_KEY }); },
  });
}
