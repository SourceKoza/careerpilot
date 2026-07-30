import type { Metadata } from "next";
import { ResumePage } from "@/features/resume/components/resume-page";

export const metadata: Metadata = {
  title: "Resumes - CareerPilot AI",
  description: "Manage your resumes",
};

export default function ResumeRoute() {
  return <ResumePage />;
}
