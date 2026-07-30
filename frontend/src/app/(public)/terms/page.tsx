import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Terms of Service - CareerPilot AI",
  description: "Terms of Service for CareerPilot AI",
};

export default function TermsPage() {
  return (
    <div className="mx-auto max-w-3xl px-4 py-16 sm:px-6 lg:px-8">
      <h1 className="text-3xl font-bold mb-8">Terms of Service</h1>
      <div className="prose prose-sm dark:prose-invert max-w-none space-y-6">
        <p className="text-muted-foreground">
          Last updated: July 2026
        </p>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">1. Acceptance of Terms</h2>
          <p className="text-muted-foreground">
            By accessing or using CareerPilot AI, you agree to be bound by these Terms of Service.
            If you do not agree to these terms, please do not use our services.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">2. Description of Service</h2>
          <p className="text-muted-foreground">
            CareerPilot AI provides AI-powered job search automation, resume optimization,
            and application management services. We use artificial intelligence to assist
            users in their career advancement efforts.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">3. User Accounts</h2>
          <p className="text-muted-foreground">
            You are responsible for maintaining the confidentiality of your account credentials.
            You agree to notify us immediately of any unauthorized use of your account.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">4. Acceptable Use</h2>
          <p className="text-muted-foreground">
            You agree to use our services only for lawful purposes. You must not use the platform
            to submit fraudulent applications or misrepresent your qualifications.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">5. Intellectual Property</h2>
          <p className="text-muted-foreground">
            All content, features, and functionality of CareerPilot AI are owned by SourceKoza Labs
            and are protected by copyright, trademark, and other intellectual property laws.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">6. Limitation of Liability</h2>
          <p className="text-muted-foreground">
            CareerPilot AI is provided &ldquo;as is&rdquo; without warranties of any kind.
            We do not guarantee job placement or interview outcomes. Use of our service does
            not constitute employment advice.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">7. Contact</h2>
          <p className="text-muted-foreground">
            For questions about these Terms, contact us at legal@sourcekoza.com.
          </p>
        </section>
      </div>
    </div>
  );
}
