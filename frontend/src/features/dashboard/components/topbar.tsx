"use client";

import { usePathname } from "next/navigation";
import { Bell, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ThemeToggle } from "@/components/shared/theme-toggle";
import { UserMenu } from "./user-menu";
import { MobileSidebar } from "./mobile-sidebar";

function getBreadcrumbs(pathname: string): string[] {
  const parts = pathname.split("/").filter(Boolean);
  return parts.map((part) => part.charAt(0).toUpperCase() + part.slice(1));
}

export function DashboardTopbar() {
  const pathname = usePathname();
  const breadcrumbs = getBreadcrumbs(pathname);

  return (
    <header className="flex h-16 items-center justify-between border-b border-border bg-card px-4 lg:px-6">
      <div className="flex items-center gap-4">
        <MobileSidebar />
        <nav className="hidden sm:flex items-center gap-1 text-sm text-muted-foreground">
          {breadcrumbs.map((crumb, index) => (
            <span key={index} className="flex items-center gap-1">
              {index > 0 && <ChevronRight className="h-3 w-3" />}
              <span
                className={
                  index === breadcrumbs.length - 1
                    ? "text-foreground font-medium"
                    : ""
                }
              >
                {crumb}
              </span>
            </span>
          ))}
        </nav>
      </div>

      <div className="flex items-center gap-2">
        <ThemeToggle />
        <Button variant="ghost" size="icon" className="relative" aria-label="Notifications">
          <Bell className="h-4 w-4" />
          <span className="absolute top-2 right-2 h-2 w-2 rounded-full bg-primary" />
        </Button>
        <UserMenu />
      </div>
    </header>
  );
}
