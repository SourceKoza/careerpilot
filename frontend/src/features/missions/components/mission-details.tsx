"use client";

import { motion, AnimatePresence } from "framer-motion";
import { X, Circle, Briefcase, Send, Target, Clock, CheckCircle2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { Mission } from "@/types/mission";
import { useMissionProgress, useMissionTimeline } from "../hooks/use-missions";
import { cn } from "@/lib/utils";

interface MissionDetailsProps {
  mission: Mission | null;
  onClose: () => void;
}

function formatDate(dateStr: string | null): string {
  if (!dateStr) return "—";
  return new Date(dateStr).toLocaleDateString("en-US", { month: "short", day: "numeric", hour: "2-digit", minute: "2-digit" });
}

export function MissionDetails({ mission, onClose }: MissionDetailsProps) {
  const { data: progress } = useMissionProgress(mission?.id || "");
  const { data: timeline } = useMissionTimeline(mission?.id || "");

  return (
    <AnimatePresence>
      {mission && (
        <>
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50"
            onClick={onClose}
          />
          <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: 100 }}
            transition={{ type: "spring", damping: 25, stiffness: 300 }}
            className="fixed right-0 top-0 bottom-0 w-full max-w-lg bg-card border-l border-border shadow-xl z-50 overflow-y-auto"
          >
            <div className="p-6 space-y-6">
              {/* Header */}
              <div className="flex items-center justify-between">
                <div>
                  <h2 className="text-lg font-bold">{mission.name}</h2>
                  <p className="text-sm text-muted-foreground">{mission.keywords}</p>
                </div>
                <Button variant="ghost" size="icon" onClick={onClose}>
                  <X className="h-4 w-4" />
                </Button>
              </div>

              {/* Stats */}
              <div className="grid grid-cols-2 gap-3">
                <div className="rounded-lg border border-border p-3 text-center">
                  <Briefcase className="h-4 w-4 text-blue-500 mx-auto mb-1" />
                  <p className="text-xl font-bold">{mission.jobsFound}</p>
                  <p className="text-[10px] text-muted-foreground">Jobs Found</p>
                </div>
                <div className="rounded-lg border border-border p-3 text-center">
                  <Send className="h-4 w-4 text-green-500 mx-auto mb-1" />
                  <p className="text-xl font-bold">{mission.applicationsSubmitted}</p>
                  <p className="text-[10px] text-muted-foreground">Applied</p>
                </div>
                <div className="rounded-lg border border-border p-3 text-center">
                  <Target className="h-4 w-4 text-violet-500 mx-auto mb-1" />
                  <p className="text-xl font-bold">{mission.successRate}%</p>
                  <p className="text-[10px] text-muted-foreground">Success Rate</p>
                </div>
                <div className="rounded-lg border border-border p-3 text-center">
                  <Clock className="h-4 w-4 text-amber-500 mx-auto mb-1" />
                  <p className="text-xl font-bold">{mission.platforms.length}</p>
                  <p className="text-[10px] text-muted-foreground">Platforms</p>
                </div>
              </div>

              {/* Live Progress */}
              {progress && (
                <div className="space-y-4">
                  <h3 className="font-semibold text-sm">Live Progress</h3>
                  <div className="space-y-3">
                    {progress.platforms.map((p) => (
                      <div key={p.name} className="space-y-1">
                        <div className="flex items-center justify-between text-xs">
                          <span className="font-medium">{p.name}</span>
                          <span className="text-muted-foreground">{p.jobsFound} jobs</span>
                        </div>
                        <div className="h-2 rounded-full bg-border overflow-hidden">
                          <motion.div
                            className="h-full bg-gradient-to-r from-primary to-violet-500 rounded-full"
                            initial={{ width: 0 }}
                            animate={{ width: `${p.progress}%` }}
                            transition={{ duration: 0.8, delay: 0.2 }}
                          />
                        </div>
                      </div>
                    ))}
                  </div>
                  <div className="flex items-center justify-between text-sm mt-4 p-3 rounded-lg bg-primary/5 border border-primary/20">
                    <span className="font-semibold">Overall</span>
                    <span className="text-primary font-bold">{progress.overall}%</span>
                  </div>
                  {progress.currentActivity && (
                    <p className="text-xs text-primary flex items-center gap-1">
                      <Circle className="h-2 w-2 fill-primary animate-pulse" />
                      {progress.currentActivity}
                    </p>
                  )}
                </div>
              )}

              {/* Timeline */}
              {timeline && timeline.length > 0 && (
                <div className="space-y-3">
                  <h3 className="font-semibold text-sm">Mission Timeline</h3>
                  <div className="space-y-3">
                    {timeline.map((item, i) => (
                      <div key={item.id} className="flex items-start gap-3">
                        <div className="relative mt-0.5">
                          <div className={cn(
                            "h-5 w-5 rounded-full flex items-center justify-center",
                            item.status === "completed" && "bg-emerald-500/10",
                            item.status === "active" && "bg-primary/10",
                            item.status === "pending" && "bg-muted",
                          )}>
                            {item.status === "completed" ? (
                              <CheckCircle2 className="h-3 w-3 text-emerald-500" />
                            ) : item.status === "active" ? (
                              <Circle className="h-2.5 w-2.5 fill-primary text-primary animate-pulse" />
                            ) : (
                              <Circle className="h-2.5 w-2.5 text-muted-foreground" />
                            )}
                          </div>
                          {i < timeline.length - 1 && (
                            <div className="absolute left-1/2 top-5 h-6 w-px -translate-x-1/2 bg-border" />
                          )}
                        </div>
                        <div>
                          <p className="text-sm font-medium">{item.event}</p>
                          <p className="text-[10px] text-muted-foreground">{formatDate(item.timestamp)}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Mission Info */}
              <div className="space-y-3">
                <h3 className="font-semibold text-sm">Configuration</h3>
                <div className="rounded-lg border border-border p-4 space-y-2 text-sm">
                  <div className="flex justify-between"><span className="text-muted-foreground">Schedule</span><span className="font-medium">{mission.schedule}</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Level</span><span className="font-medium">{mission.experienceLevel}</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Location</span><span className="font-medium">{mission.location}{mission.remote ? " (Remote)" : ""}</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Resume</span><span className="font-medium">{mission.resumeTitle || "None"}</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Last Run</span><span className="font-medium">{formatDate(mission.lastRun)}</span></div>
                  <div className="flex justify-between"><span className="text-muted-foreground">Next Run</span><span className="font-medium">{formatDate(mission.nextRun)}</span></div>
                </div>
              </div>
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  );
}
