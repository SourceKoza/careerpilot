"use client";

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  ArrowRight,
  ArrowLeft,
  Rocket,
  MapPin,
  DollarSign,
  Globe,
  FileText,
  Clock,
  CheckCircle2,
  Loader2,
  Zap,
  Shield,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useCreateMission } from "../hooks/use-missions";
import type { CreateMissionData } from "@/types/mission";

const PLATFORMS = [
  { id: "LinkedIn", label: "LinkedIn", color: "bg-blue-500" },
  { id: "Indeed", label: "Indeed", color: "bg-violet-500" },
  { id: "Wellfound", label: "Wellfound", color: "bg-orange-500" },
  { id: "Naukri", label: "Naukri", color: "bg-emerald-500" },
  { id: "Company Sites", label: "Company Sites", color: "bg-pink-500" },
];

const STEPS = ["Details", "Location", "Salary", "Platforms", "Resume", "Schedule", "Apply Mode", "Review"];

interface MissionWizardProps {
  onClose: () => void;
}

export function MissionWizard({ onClose }: MissionWizardProps) {
  const [step, setStep] = useState(0);
  const createMission = useCreateMission();
  const [data, setData] = useState<CreateMissionData>({
    name: "",
    keywords: "",
    preferredTitle: "",
    experienceLevel: "Senior",
    location: "",
    remote: true,
    hybrid: false,
    salaryMin: null,
    currency: "USD",
    employmentType: "Full-time",
    platforms: ["LinkedIn"],
    resumeId: null,
    schedule: "Daily",
    timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
    applyMode: "SEMI_AUTO",
  });

  const update = (partial: Partial<CreateMissionData>) => setData({ ...data, ...partial });
  const next = () => setStep((s) => Math.min(s + 1, STEPS.length - 1));
  const prev = () => setStep((s) => Math.max(s - 1, 0));

  const handleSubmit = () => {
    createMission.mutate(data, { onSuccess: onClose });
  };

  const togglePlatform = (id: string) => {
    const platforms = data.platforms.includes(id)
      ? data.platforms.filter((p) => p !== id)
      : [...data.platforms, id];
    update({ platforms });
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="absolute inset-0 bg-black/60 backdrop-blur-sm"
        onClick={onClose}
      />
      <motion.div
        initial={{ opacity: 0, scale: 0.95, y: 20 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        exit={{ opacity: 0, scale: 0.95, y: 20 }}
        className="relative w-full max-w-xl rounded-2xl border border-border bg-card shadow-2xl overflow-hidden"
      >
        {/* Progress */}
        <div className="border-b border-border p-4">
          <div className="flex items-center justify-between mb-2">
            <h2 className="text-lg font-bold">Create AI Mission</h2>
            <span className="text-xs text-muted-foreground">
              Step {step + 1} of {STEPS.length}
            </span>
          </div>
          <div className="flex gap-1">
            {STEPS.map((_, i) => (
              <div
                key={i}
                className={`h-1 flex-1 rounded-full transition-colors ${
                  i <= step ? "bg-primary" : "bg-border"
                }`}
              />
            ))}
          </div>
        </div>

        {/* Content */}
        <div className="p-6 min-h-[300px]">
          <AnimatePresence mode="wait">
            <motion.div
              key={step}
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              exit={{ opacity: 0, x: -20 }}
              transition={{ duration: 0.2 }}
            >
              {step === 0 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <Rocket className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Mission Details</h3>
                  </div>
                  <div className="space-y-2">
                    <Label>Mission Name</Label>
                    <Input placeholder="e.g. Senior Frontend – Remote US" value={data.name} onChange={(e) => update({ name: e.target.value })} />
                  </div>
                  <div className="space-y-2">
                    <Label>Keywords</Label>
                    <Input placeholder="React, Next.js, TypeScript" value={data.keywords} onChange={(e) => update({ keywords: e.target.value })} />
                  </div>
                  <div className="space-y-2">
                    <Label>Preferred Job Title</Label>
                    <Input placeholder="Senior Frontend Engineer" value={data.preferredTitle} onChange={(e) => update({ preferredTitle: e.target.value })} />
                  </div>
                  <div className="space-y-2">
                    <Label>Experience Level</Label>
                    <select value={data.experienceLevel} onChange={(e) => update({ experienceLevel: e.target.value })} className="w-full rounded-lg border border-input bg-transparent px-3 py-2 text-sm">
                      <option value="Entry">Entry</option>
                      <option value="Mid">Mid</option>
                      <option value="Senior">Senior</option>
                      <option value="Lead">Lead</option>
                      <option value="Executive">Executive</option>
                    </select>
                  </div>
                </div>
              )}

              {step === 1 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <MapPin className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Location</h3>
                  </div>
                  <div className="space-y-2">
                    <Label>Location</Label>
                    <Input placeholder="City, Country" value={data.location} onChange={(e) => update({ location: e.target.value })} />
                  </div>
                  <div className="flex flex-col gap-3 mt-4">
                    <label className="flex items-center gap-2 text-sm">
                      <input type="checkbox" checked={data.remote} onChange={(e) => update({ remote: e.target.checked })} className="rounded" />
                      Remote Only
                    </label>
                    <label className="flex items-center gap-2 text-sm">
                      <input type="checkbox" checked={data.hybrid} onChange={(e) => update({ hybrid: e.target.checked })} className="rounded" />
                      Hybrid OK
                    </label>
                  </div>
                </div>
              )}

              {step === 2 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <DollarSign className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Salary & Type</h3>
                  </div>
                  <div className="grid grid-cols-2 gap-3">
                    <div className="space-y-2">
                      <Label>Minimum Salary</Label>
                      <Input type="number" placeholder="100000" value={data.salaryMin ?? ""} onChange={(e) => update({ salaryMin: e.target.value ? Number(e.target.value) : null })} />
                    </div>
                    <div className="space-y-2">
                      <Label>Currency</Label>
                      <select value={data.currency} onChange={(e) => update({ currency: e.target.value })} className="w-full rounded-lg border border-input bg-transparent px-3 py-2 text-sm">
                        <option value="USD">USD</option>
                        <option value="GBP">GBP</option>
                        <option value="EUR">EUR</option>
                        <option value="INR">INR</option>
                      </select>
                    </div>
                  </div>
                  <div className="space-y-2">
                    <Label>Employment Type</Label>
                    <select value={data.employmentType} onChange={(e) => update({ employmentType: e.target.value })} className="w-full rounded-lg border border-input bg-transparent px-3 py-2 text-sm">
                      <option value="Full-time">Full-time</option>
                      <option value="Part-time">Part-time</option>
                      <option value="Contract">Contract</option>
                    </select>
                  </div>
                </div>
              )}

              {step === 3 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <Globe className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Platforms</h3>
                  </div>
                  <p className="text-sm text-muted-foreground">Select where the AI should search.</p>
                  <div className="grid grid-cols-2 gap-3 mt-4">
                    {PLATFORMS.map((p) => (
                      <button
                        key={p.id}
                        onClick={() => togglePlatform(p.id)}
                        className={`flex items-center gap-3 rounded-xl border p-3 transition-all ${
                          data.platforms.includes(p.id)
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/30"
                        }`}
                      >
                        <div className={`h-3 w-3 rounded-full ${p.color}`} />
                        <span className="text-sm font-medium">{p.label}</span>
                        {data.platforms.includes(p.id) && (
                          <CheckCircle2 className="h-4 w-4 text-primary ml-auto" />
                        )}
                      </button>
                    ))}
                  </div>
                </div>
              )}

              {step === 4 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <FileText className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Resume</h3>
                  </div>
                  <p className="text-sm text-muted-foreground">The AI will tailor applications using this resume.</p>
                  <div className="space-y-2 mt-4">
                    <button
                      onClick={() => update({ resumeId: "1" })}
                      className={`w-full flex items-center gap-3 rounded-xl border p-4 transition-all ${data.resumeId === "1" ? "border-primary bg-primary/5" : "border-border hover:border-primary/30"}`}
                    >
                      <FileText className="h-5 w-5 text-primary" />
                      <div className="text-left">
                        <p className="text-sm font-medium">Senior Frontend Engineer</p>
                        <p className="text-xs text-muted-foreground">Default • PDF • 240KB</p>
                      </div>
                      {data.resumeId === "1" && <CheckCircle2 className="h-4 w-4 text-primary ml-auto" />}
                    </button>
                    <button
                      onClick={() => update({ resumeId: "2" })}
                      className={`w-full flex items-center gap-3 rounded-xl border p-4 transition-all ${data.resumeId === "2" ? "border-primary bg-primary/5" : "border-border hover:border-primary/30"}`}
                    >
                      <FileText className="h-5 w-5 text-blue-500" />
                      <div className="text-left">
                        <p className="text-sm font-medium">Full Stack Developer</p>
                        <p className="text-xs text-muted-foreground">PDF • 195KB</p>
                      </div>
                      {data.resumeId === "2" && <CheckCircle2 className="h-4 w-4 text-primary ml-auto" />}
                    </button>
                  </div>
                </div>
              )}

              {step === 5 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <Clock className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Schedule</h3>
                  </div>
                  <div className="space-y-2">
                    <Label>Frequency</Label>
                    <select value={data.schedule} onChange={(e) => update({ schedule: e.target.value })} className="w-full rounded-lg border border-input bg-transparent px-3 py-2 text-sm">
                      <option value="Run Once">Run Once</option>
                      <option value="Daily">Daily</option>
                      <option value="Weekly">Weekly</option>
                      <option value="Every Morning">Every Morning</option>
                    </select>
                  </div>
                  <div className="space-y-2">
                    <Label>Timezone</Label>
                    <Input value={data.timezone} onChange={(e) => update({ timezone: e.target.value })} />
                  </div>
                </div>
              )}

              {step === 6 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <Zap className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Apply Mode</h3>
                  </div>
                  <p className="text-sm text-muted-foreground">Choose how the AI handles job applications.</p>
                  <div className="space-y-3 mt-4">
                    <button
                      onClick={() => update({ applyMode: "SEMI_AUTO" })}
                      className={`w-full flex items-start gap-3 rounded-xl border p-4 transition-all text-left ${data.applyMode === "SEMI_AUTO" ? "border-primary bg-primary/5" : "border-border hover:border-primary/30"}`}
                    >
                      <Shield className="h-5 w-5 text-primary mt-0.5" />
                      <div>
                        <p className="text-sm font-medium">Semi-Automatic</p>
                        <p className="text-xs text-muted-foreground mt-1">
                          AI tailors resumes, you review and approve before sending. Full control over every application.
                        </p>
                      </div>
                      {data.applyMode === "SEMI_AUTO" && <CheckCircle2 className="h-4 w-4 text-primary ml-auto mt-0.5" />}
                    </button>
                    <button
                      onClick={() => update({ applyMode: "FULL_AUTO" })}
                      className={`w-full flex items-start gap-3 rounded-xl border p-4 transition-all text-left ${data.applyMode === "FULL_AUTO" ? "border-primary bg-primary/5" : "border-border hover:border-primary/30"}`}
                    >
                      <Zap className="h-5 w-5 text-orange-500 mt-0.5" />
                      <div>
                        <p className="text-sm font-medium">Full Automatic</p>
                        <p className="text-xs text-muted-foreground mt-1">
                          AI sends applications automatically for jobs scoring ≥ 80%. No approval needed.
                        </p>
                      </div>
                      {data.applyMode === "FULL_AUTO" && <CheckCircle2 className="h-4 w-4 text-primary ml-auto mt-0.5" />}
                    </button>
                  </div>
                </div>
              )}

              {step === 7 && (
                <div className="space-y-4">
                  <div className="flex items-center gap-2 mb-4">
                    <CheckCircle2 className="h-5 w-5 text-primary" />
                    <h3 className="font-semibold">Review Mission</h3>
                  </div>
                  <div className="rounded-lg border border-border p-4 space-y-3 text-sm">
                    <div className="flex justify-between"><span className="text-muted-foreground">Name</span><span className="font-medium">{data.name || "—"}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Keywords</span><span className="font-medium">{data.keywords || "—"}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Title</span><span className="font-medium">{data.preferredTitle || "—"}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Level</span><span className="font-medium">{data.experienceLevel}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Location</span><span className="font-medium">{data.location || "Any"}{data.remote ? " (Remote)" : ""}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Salary</span><span className="font-medium">{data.salaryMin ? `${data.currency} ${data.salaryMin.toLocaleString()}+` : "Any"}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Platforms</span><span className="font-medium">{data.platforms.join(", ")}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Schedule</span><span className="font-medium">{data.schedule}</span></div>
                    <div className="flex justify-between"><span className="text-muted-foreground">Apply Mode</span><span className="font-medium">{data.applyMode === "FULL_AUTO" ? "Full Auto" : "Semi Auto"}</span></div>
                  </div>
                </div>
              )}
            </motion.div>
          </AnimatePresence>
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between border-t border-border p-4">
          <Button variant="ghost" onClick={step === 0 ? onClose : prev} disabled={createMission.isPending}>
            {step === 0 ? "Cancel" : <><ArrowLeft className="h-4 w-4" /> Back</>}
          </Button>
          {step < STEPS.length - 1 ? (
            <Button onClick={next}>
              Next <ArrowRight className="h-4 w-4" />
            </Button>
          ) : (
            <Button onClick={handleSubmit} disabled={createMission.isPending}>
              {createMission.isPending ? <Loader2 className="h-4 w-4 animate-spin" /> : <Rocket className="h-4 w-4" />}
              Launch Mission
            </Button>
          )}
        </div>
      </motion.div>
    </div>
  );
}
