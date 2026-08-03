import type { Metadata } from "next";
import { DashboardWelcome } from "@/features/dashboard/components/dashboard-welcome";

export const metadata: Metadata = {
  title: "Dashboard - CareerPilot AI",
  description: "Your CareerPilot AI dashboard",
};

export default function DashboardPage() {
  return <DashboardWelcome />;
}
