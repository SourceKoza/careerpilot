"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { motion } from "framer-motion";
import {
  LayoutDashboard,
  Briefcase,
  FileText,
  Send,
  Bot,
  Settings,
  Rocket,
  ChevronsLeft,
  ChevronsRight,
} from "lucide-react";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { cn } from "@/lib/utils";
import { APP_NAME, ROUTES } from "@/lib/constants";
import { useSidebarStore } from "@/stores/sidebar.store";

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
  const { collapsed, toggle } = useSidebarStore();

  return (
    <TooltipProvider delayDuration={0}>
      <motion.aside
        animate={{ width: collapsed ? 72 : 256 }}
        transition={{ duration: 0.2 }}
        className="hidden lg:flex flex-col border-r border-border bg-card overflow-hidden"
      >
        {/* Logo */}
        <div className="flex h-16 items-center gap-2 px-4 border-b border-border">
          <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-primary">
            <Rocket className="h-4 w-4 text-primary-foreground" />
          </div>
          {!collapsed && (
            <motion.span
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="text-lg font-bold whitespace-nowrap"
            >
              {APP_NAME}
            </motion.span>
          )}
        </div>

        {/* Navigation */}
        <nav className="flex-1 space-y-1 px-2 py-4">
          {navItems.map((item) => {
            const isActive =
              pathname === item.href ||
              (item.href !== ROUTES.DASHBOARD && pathname.startsWith(item.href));

            const linkContent = (
              <Link
                href={item.href}
                className={cn(
                  "relative flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-all duration-200",
                  collapsed && "justify-center px-2",
                  isActive
                    ? "bg-primary/10 text-primary"
                    : "text-muted-foreground hover:bg-secondary hover:text-foreground"
                )}
              >
                {isActive && (
                  <motion.div
                    layoutId="sidebar-active"
                    className="absolute inset-0 rounded-lg bg-primary/10"
                    transition={{ type: "spring", duration: 0.4 }}
                  />
                )}
                <item.icon className="h-4 w-4 shrink-0 relative z-10" />
                {!collapsed && (
                  <span className="relative z-10 whitespace-nowrap">{item.label}</span>
                )}
              </Link>
            );

            if (collapsed) {
              return (
                <Tooltip key={item.href}>
                  <TooltipTrigger asChild>{linkContent}</TooltipTrigger>
                  <TooltipContent side="right">{item.label}</TooltipContent>
                </Tooltip>
              );
            }

            return <div key={item.href}>{linkContent}</div>;
          })}
        </nav>

        {/* Collapse toggle */}
        <div className="border-t border-border p-2">
          <button
            onClick={toggle}
            className="flex w-full items-center justify-center gap-2 rounded-lg px-3 py-2 text-sm text-muted-foreground hover:bg-secondary hover:text-foreground transition-colors"
            aria-label={collapsed ? "Expand sidebar" : "Collapse sidebar"}
          >
            {collapsed ? (
              <ChevronsRight className="h-4 w-4" />
            ) : (
              <>
                <ChevronsLeft className="h-4 w-4" />
                <span>Collapse</span>
              </>
            )}
          </button>
        </div>

        {/* Plan badge - only when expanded */}
        {!collapsed && (
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
        )}
      </motion.aside>
    </TooltipProvider>
  );
}
