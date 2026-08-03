import type { Metadata } from "next";
import { ResumeIntelligencePage } from "@/features/resume/components/resume-intelligence-page";

export const metadata: Metadata = {
  title: "Resume Intelligence - CareerPilot AI",
  description: "AI-powered resume analysis and optimization",
};

export default function ResumeRoute() {
  return <ResumeIntelligencePage />;
}
