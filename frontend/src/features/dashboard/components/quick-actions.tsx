"use client";

import { motion } from "framer-motion";
import { Search, Upload, Sparkles, Bot } from "lucide-react";
import type { LucideIcon } from "lucide-react";
import Link from "next/link";
import { ROUTES } from "@/lib/constants";

interface QuickAction {
  icon: LucideIcon;
  label: string;
  description: string;
  href: string;
  color: string;
}

const actions: QuickAction[] = [
  {
    icon: Search,
    label: "Search Jobs",
    description: "Find matching roles",
    href: ROUTES.JOBS,
    color: "from-blue-500/20 to-blue-600/5",
  },
  {
    icon: Upload,
    label: "Upload Resume",
    description: "Import your resume",
    href: ROUTES.RESUME,
    color: "from-violet-500/20 to-violet-600/5",
  },
  {
    icon: Sparkles,
    label: "Optimize Resume",
    description: "AI-powered tailoring",
    href: ROUTES.RESUME,
    color: "from-amber-500/20 to-amber-600/5",
  },
  {
    icon: Bot,
    label: "Launch AI Agent",
    description: "Start automation",
    href: ROUTES.AGENTS,
    color: "from-emerald-500/20 to-emerald-600/5",
  },
];

export function QuickActions() {
  return (
    <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-4">
      {actions.map((action, index) => (
        <motion.div
          key={action.label}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 + index * 0.05 }}
        >
          <Link
            href={action.href}
            className={`group flex items-center gap-3 rounded-xl border border-border p-4 transition-all duration-200 hover:border-primary/30 hover:shadow-md hover:shadow-primary/5 bg-gradient-to-br ${action.color}`}
          >
            <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-card border border-border shadow-sm transition-transform group-hover:scale-110">
              <action.icon className="h-5 w-5 text-primary" />
            </div>
            <div className="min-w-0">
              <p className="text-sm font-medium truncate">{action.label}</p>
              <p className="text-xs text-muted-foreground truncate">
                {action.description}
              </p>
            </div>
          </Link>
        </motion.div>
      ))}
    </div>
  );
}
