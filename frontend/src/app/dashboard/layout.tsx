"use client";

import { AuthGuard } from "@/features/auth/components/auth-guard";
import { DashboardSidebar } from "@/features/dashboard/components/sidebar";
import { DashboardTopbar } from "@/features/dashboard/components/topbar";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <AuthGuard>
      <div className="flex h-screen overflow-hidden">
        <DashboardSidebar />
        <div className="flex flex-1 flex-col overflow-hidden">
          <DashboardTopbar />
          <main className="flex-1 overflow-y-auto p-6">{children}</main>
        </div>
      </div>
    </AuthGuard>
  );
}
