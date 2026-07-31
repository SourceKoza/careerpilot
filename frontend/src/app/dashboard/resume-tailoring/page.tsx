import type { Metadata } from "next";
import { ResumeTailoringDashboard } from "@/features/resume-tailoring/components/resume-tailoring-dashboard";

export const metadata: Metadata = {
  title: "AI Resume Tailoring - CareerPilot AI",
  description: "Generate job-specific resume versions with AI optimization",
};

export default function ResumeTailoringRoute() {
  return <ResumeTailoringDashboard />;
}
