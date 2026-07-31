import type { Metadata } from "next";
import { JobMatchDashboard } from "@/features/job-matching/components/job-match-dashboard";

export const metadata: Metadata = {
  title: "AI Job Matching - CareerPilot AI",
  description: "AI-powered resume vs. job compatibility analysis",
};

export default function JobsRoute() {
  return <JobMatchDashboard />;
}
