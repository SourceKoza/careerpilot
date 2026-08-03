import type { Metadata } from "next";
import { RegisterForm } from "@/features/auth/components/register-form";

export const metadata: Metadata = {
  title: "Create Account - CareerPilot AI",
  description: "Create your CareerPilot AI account and start your AI-powered job search",
};

export default function RegisterPage() {
  return (
    <div className="space-y-6">
      <div className="text-center space-y-2">
        <h1 className="text-2xl font-bold">Create your account</h1>
        <p className="text-sm text-muted-foreground">
          Start your AI-powered job search journey
        </p>
      </div>
      <RegisterForm />
    </div>
  );
}
