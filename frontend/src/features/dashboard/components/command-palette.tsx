"use client";

import { useState, useEffect, useCallback } from "react";
import { useRouter } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";
import {
  Search,
  LayoutDashboard,
  Briefcase,
  FileText,
  Send,
  Bot,
  Settings,
  Command,
} from "lucide-react";
import type { LucideIcon } from "lucide-react";
import { ROUTES } from "@/lib/constants";

interface CommandItem {
  id: string;
  icon: LucideIcon;
  label: string;
  description: string;
  href: string;
  category: string;
}

const commands: CommandItem[] = [
  { id: "dashboard", icon: LayoutDashboard, label: "Dashboard", description: "Go to dashboard", href: ROUTES.DASHBOARD, category: "Pages" },
  { id: "jobs", icon: Briefcase, label: "Jobs", description: "Browse job listings", href: ROUTES.JOBS, category: "Pages" },
  { id: "resume", icon: FileText, label: "Resume", description: "Manage your resume", href: ROUTES.RESUME, category: "Pages" },
  { id: "applications", icon: Send, label: "Applications", description: "Track applications", href: ROUTES.APPLICATIONS, category: "Pages" },
  { id: "agents", icon: Bot, label: "AI Agents", description: "Manage AI agents", href: ROUTES.AGENTS, category: "Pages" },
  { id: "settings", icon: Settings, label: "Settings", description: "Account settings", href: ROUTES.SETTINGS, category: "Pages" },
  { id: "search-jobs", icon: Search, label: "Search Jobs", description: "Find new opportunities", href: ROUTES.JOBS, category: "Actions" },
  { id: "upload-resume", icon: FileText, label: "Upload Resume", description: "Import your resume", href: ROUTES.RESUME, category: "Actions" },
  { id: "launch-agent", icon: Bot, label: "Launch AI Agent", description: "Start job search automation", href: ROUTES.AGENTS, category: "Actions" },
];

export function CommandPalette() {
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState("");
  const router = useRouter();

  const filtered = query
    ? commands.filter(
        (c) =>
          c.label.toLowerCase().includes(query.toLowerCase()) ||
          c.description.toLowerCase().includes(query.toLowerCase())
      )
    : commands;

  const handleSelect = useCallback(
    (item: CommandItem) => {
      setOpen(false);
      setQuery("");
      router.push(item.href);
    },
    [router]
  );

  useEffect(() => {
    function handleKeyDown(e: KeyboardEvent) {
      if ((e.metaKey || e.ctrlKey) && e.key === "k") {
        e.preventDefault();
        setOpen((prev) => !prev);
      }
      if (e.key === "Escape") {
        setOpen(false);
      }
    }
    document.addEventListener("keydown", handleKeyDown);
    return () => document.removeEventListener("keydown", handleKeyDown);
  }, []);

  return (
    <>
      {/* Trigger button */}
      <button
        onClick={() => setOpen(true)}
        className="hidden sm:flex items-center gap-2 rounded-lg border border-border bg-secondary/50 px-3 py-1.5 text-sm text-muted-foreground hover:bg-secondary transition-colors"
      >
        <Search className="h-3.5 w-3.5" />
        <span>Search...</span>
        <kbd className="ml-2 flex items-center gap-0.5 rounded border border-border bg-card px-1.5 py-0.5 text-[10px] font-mono">
          <Command className="h-2.5 w-2.5" />K
        </kbd>
      </button>

      {/* Modal */}
      <AnimatePresence>
        {open && (
          <>
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50"
              onClick={() => setOpen(false)}
            />
            <motion.div
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.95 }}
              transition={{ duration: 0.15 }}
              className="fixed left-1/2 top-[20%] -translate-x-1/2 w-full max-w-lg z-50"
            >
              <div className="rounded-xl border border-border bg-card shadow-2xl overflow-hidden mx-4">
                <div className="flex items-center gap-3 border-b border-border px-4 py-3">
                  <Search className="h-4 w-4 text-muted-foreground shrink-0" />
                  <input
                    type="text"
                    placeholder="Search pages, actions..."
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    className="flex-1 bg-transparent text-sm outline-none placeholder:text-muted-foreground"
                    autoFocus
                  />
                  <kbd className="text-[10px] text-muted-foreground border border-border rounded px-1.5 py-0.5">
                    ESC
                  </kbd>
                </div>
                <div className="max-h-72 overflow-y-auto p-2">
                  {filtered.length === 0 ? (
                    <p className="text-sm text-muted-foreground text-center py-6">
                      No results found.
                    </p>
                  ) : (
                    <>
                      {["Pages", "Actions"].map((category) => {
                        const items = filtered.filter(
                          (c) => c.category === category
                        );
                        if (items.length === 0) return null;
                        return (
                          <div key={category}>
                            <p className="text-xs font-medium text-muted-foreground px-2 py-1.5">
                              {category}
                            </p>
                            {items.map((item) => (
                              <button
                                key={item.id}
                                onClick={() => handleSelect(item)}
                                className="flex w-full items-center gap-3 rounded-lg px-3 py-2 text-sm hover:bg-secondary transition-colors"
                              >
                                <item.icon className="h-4 w-4 text-muted-foreground" />
                                <div className="flex-1 text-left">
                                  <span className="font-medium">{item.label}</span>
                                  <span className="ml-2 text-xs text-muted-foreground">
                                    {item.description}
                                  </span>
                                </div>
                              </button>
                            ))}
                          </div>
                        );
                      })}
                    </>
                  )}
                </div>
              </div>
            </motion.div>
          </>
        )}
      </AnimatePresence>
    </>
  );
}
