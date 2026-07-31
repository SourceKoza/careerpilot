"use client";

import { CheckCircle2, AlertCircle, XCircle, MinusCircle } from "lucide-react";
import type { ApplyReadiness } from "../types";
import { cn } from "@/lib/utils";

interface ApplyReadinessBadgeProps {
  readiness: ApplyReadiness;
}

const config = {
  ready: { icon: CheckCircle2, label: "Ready to Apply", color: "text-emerald-500 bg-emerald-500/10 border-emerald-500/20" },
  "minor-improvements": { icon: AlertCircle, label: "Needs Minor Improvements", color: "text-amber-500 bg-amber-500/10 border-amber-500/20" },
  "needs-updates": { icon: MinusCircle, label: "Needs Resume Updates", color: "text-orange-500 bg-orange-500/10 border-orange-500/20" },
  "not-recommended": { icon: XCircle, label: "Not Recommended Yet", color: "text-red-500 bg-red-500/10 border-red-500/20" },
};

export function ApplyReadinessBadge({ readiness }: ApplyReadinessBadgeProps) {
  const { icon: Icon, label, color } = config[readiness];
  return (
    <div className={cn("inline-flex items-center gap-2 rounded-full border px-4 py-2 text-sm font-medium", color)}>
      <Icon className="h-4 w-4" />
      {label}
    </div>
  );
}
