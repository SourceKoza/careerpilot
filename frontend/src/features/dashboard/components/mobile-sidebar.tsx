"use client";

import { useState } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";
import {
  Menu,
  X,
  LayoutDashboard,
  Briefcase,
  FileText,
  Send,
  Bot,
  Settings,
  Rocket,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { APP_NAME, ROUTES } from "@/lib/constants";

const navItems = [
  { href: ROUTES.DASHBOARD, icon: LayoutDashboard, label: "Overview" },
  { href: ROUTES.APPLICATIONS, icon: Send, label: "Discovered Jobs" },
  { href: ROUTES.RESUME, icon: FileText, label: "Resume" },
  { href: ROUTES.AGENTS, icon: Bot, label: "Missions" },
  { href: ROUTES.SETTINGS, icon: Settings, label: "Settings" },
];

export function MobileSidebar() {
  const [open, setOpen] = useState(false);
  const pathname = usePathname();

  return (
    <>
      <button
        className="lg:hidden p-2 -ml-2"
        onClick={() => setOpen(true)}
        aria-label="Open navigation menu"
      >
        <Menu className="h-5 w-5" />
      </button>

      <AnimatePresence>
        {open && (
          <>
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="fixed inset-0 bg-black/50 backdrop-blur-sm z-40 lg:hidden"
              onClick={() => setOpen(false)}
            />
            <motion.aside
              initial={{ x: -280 }}
              animate={{ x: 0 }}
              exit={{ x: -280 }}
              transition={{ type: "spring", damping: 25, stiffness: 300 }}
              className="fixed left-0 top-0 bottom-0 w-64 bg-card border-r border-border z-50 lg:hidden"
            >
              <div className="flex h-16 items-center justify-between px-6 border-b border-border">
                <div className="flex items-center gap-2">
                  <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary">
                    <Rocket className="h-4 w-4 text-primary-foreground" />
                  </div>
                  <span className="text-lg font-bold">{APP_NAME}</span>
                </div>
                <button onClick={() => setOpen(false)} aria-label="Close menu">
                  <X className="h-5 w-5" />
                </button>
              </div>

              <nav className="space-y-1 px-3 py-4">
                {navItems.map((item) => {
                  const isActive =
                    pathname === item.href ||
                    (item.href !== ROUTES.DASHBOARD &&
                      pathname.startsWith(item.href));
                  return (
                    <Link
                      key={item.href}
                      href={item.href}
                      onClick={() => setOpen(false)}
                      className={cn(
                        "flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors",
                        isActive
                          ? "bg-primary/10 text-primary"
                          : "text-muted-foreground hover:bg-secondary hover:text-foreground"
                      )}
                    >
                      <item.icon className="h-4 w-4" />
                      {item.label}
                    </Link>
                  );
                })}
              </nav>
            </motion.aside>
          </>
        )}
      </AnimatePresence>
    </>
  );
}
