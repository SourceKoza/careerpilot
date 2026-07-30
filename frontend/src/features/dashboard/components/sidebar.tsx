"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import {
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
  { href: ROUTES.DASHBOARD, icon: LayoutDashboard, label: "Dashboard" },
  { href: ROUTES.JOBS, icon: Briefcase, label: "Jobs" },
  { href: ROUTES.RESUME, icon: FileText, label: "Resume" },
  { href: ROUTES.APPLICATIONS, icon: Send, label: "Applications" },
  { href: ROUTES.AGENTS, icon: Bot, label: "AI Agents" },
  { href: ROUTES.SETTINGS, icon: Settings, label: "Settings" },
];

export function DashboardSidebar() {
  const pathname = usePathname();

  return (
    <aside className="hidden lg:flex w-64 flex-col border-r border-border bg-card">
      {/* Logo */}
      <div className="flex h-16 items-center gap-2 px-6 border-b border-border">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary">
          <Rocket className="h-4 w-4 text-primary-foreground" />
        </div>
        <span className="text-lg font-bold">{APP_NAME}</span>
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1 px-3 py-4">
        {navItems.map((item) => {
          const isActive =
            pathname === item.href ||
            (item.href !== ROUTES.DASHBOARD &&
              pathname.startsWith(item.href));
          return (
            <Link
              key={item.href}
              href={item.href}
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

      {/* Bottom section */}
      <div className="border-t border-border p-4">
        <div className="rounded-lg bg-primary/5 p-3">
          <p className="text-xs font-medium text-primary">Free Plan</p>
          <p className="text-xs text-muted-foreground mt-1">
            5 applications remaining
          </p>
          <div className="mt-2 h-1.5 rounded-full bg-primary/20">
            <div className="h-full w-1/3 rounded-full bg-primary" />
          </div>
        </div>
      </div>
    </aside>
  );
}
