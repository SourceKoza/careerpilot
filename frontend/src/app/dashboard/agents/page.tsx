import type { Metadata } from "next";
import { MissionsPage } from "@/features/missions/components/missions-page";

export const metadata: Metadata = {
  title: "AI Missions - CareerPilot AI",
  description: "Manage your AI search missions",
};

export default function AgentsRoute() {
  return <MissionsPage />;
}
