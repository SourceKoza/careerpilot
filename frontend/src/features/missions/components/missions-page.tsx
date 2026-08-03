"use client";

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Rocket, Plus, Bot } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { EmptyState } from "@/features/dashboard/components/shared/empty-state";
import { StatCard } from "@/features/dashboard/components/shared/stat-card";
import { MissionCard } from "./mission-card";
import { MissionWizard } from "./mission-wizard";
import { MissionDetails } from "./mission-details";
import { useMissions, usePauseMission, useResumeMission, useRunMissionNow, useDeleteMission } from "../hooks/use-missions";
import type { Mission } from "@/types/mission";

export function MissionsPage() {
  const { data: missions, isLoading } = useMissions();
  const pauseMutation = usePauseMission();
  const resumeMutation = useResumeMission();
  const runNowMutation = useRunMissionNow();
  const deleteMutation = useDeleteMission();

  const [showWizard, setShowWizard] = useState(false);
  const [selectedMission, setSelectedMission] = useState<Mission | null>(null);

  const activeMissions = missions?.filter((m) => m.status === "active").length ?? 0;
  const totalJobsFound = missions?.reduce((sum, m) => sum + m.jobsFound, 0) ?? 0;
  const totalApplied = missions?.reduce((sum, m) => sum + m.applicationsSubmitted, 0) ?? 0;

  if (isLoading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-8 w-48" />
        <div className="grid gap-4 sm:grid-cols-3">
          <Skeleton className="h-24" />
          <Skeleton className="h-24" />
          <Skeleton className="h-24" />
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <Skeleton className="h-48" />
          <Skeleton className="h-48" />
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4"
      >
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold">AI Missions</h1>
          <p className="text-muted-foreground">
            Your AI workforce is searching for opportunities.
          </p>
        </div>
        <Button onClick={() => setShowWizard(true)}>
          <Plus className="h-4 w-4" />
          New Mission
        </Button>
      </motion.div>

      {/* Stats */}
      <div className="grid gap-4 sm:grid-cols-3">
        <StatCard icon={Rocket} label="Active Missions" value={activeMissions} color="text-emerald-500" delay={0.05} />
        <StatCard icon={Bot} label="Jobs Discovered" value={totalJobsFound} color="text-blue-500" delay={0.1} />
        <StatCard icon={Bot} label="Applications Sent" value={totalApplied} color="text-violet-500" delay={0.15} />
      </div>

      {/* Missions Grid */}
      {missions && missions.length === 0 ? (
        <EmptyState
          icon={Rocket}
          title="No AI Search Missions Yet"
          description="Create your first mission and let AI search for opportunities on your behalf."
          actionLabel="Create First Mission"
          onAction={() => setShowWizard(true)}
        />
      ) : (
        <div className="grid gap-4 sm:grid-cols-2">
          {missions?.map((mission, index) => (
            <MissionCard
              key={mission.id}
              mission={mission}
              onView={setSelectedMission}
              onPause={(id) => pauseMutation.mutate(id)}
              onResume={(id) => resumeMutation.mutate(id)}
              onRunNow={(id) => runNowMutation.mutate(id)}
              onDelete={(id) => deleteMutation.mutate(id)}
              delay={index * 0.05}
            />
          ))}
        </div>
      )}

      {/* Mission Wizard Modal */}
      <AnimatePresence>
        {showWizard && <MissionWizard onClose={() => setShowWizard(false)} />}
      </AnimatePresence>

      {/* Mission Details Drawer */}
      <MissionDetails mission={selectedMission} onClose={() => setSelectedMission(null)} />
    </div>
  );
}
