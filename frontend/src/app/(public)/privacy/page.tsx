import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Privacy Policy - CareerPilot AI",
  description: "Privacy Policy for CareerPilot AI",
};

export default function PrivacyPage() {
  return (
    <div className="mx-auto max-w-3xl px-4 py-16 sm:px-6 lg:px-8">
      <h1 className="text-3xl font-bold mb-8">Privacy Policy</h1>
      <div className="prose prose-sm dark:prose-invert max-w-none space-y-6">
        <p className="text-muted-foreground">
          Last updated: July 2026
        </p>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">1. Information We Collect</h2>
          <p className="text-muted-foreground">
            We collect information you provide directly, including your name, email address,
            resume content, job preferences, and account credentials. We also collect usage
            data to improve our services.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">2. How We Use Your Information</h2>
          <p className="text-muted-foreground">
            Your information is used to provide our AI-powered job search and application
            services, personalize your experience, communicate with you, and improve our platform.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">3. Data Security</h2>
          <p className="text-muted-foreground">
            We implement industry-standard security measures to protect your personal information.
            All data is encrypted in transit and at rest. We regularly audit our security practices.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">4. Data Sharing</h2>
          <p className="text-muted-foreground">
            We do not sell your personal information. We may share data with service providers
            who assist in operating our platform, subject to strict confidentiality agreements.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">5. Your Rights</h2>
          <p className="text-muted-foreground">
            You have the right to access, correct, or delete your personal information at any time.
            You may also request data portability or opt out of certain data processing activities.
          </p>
        </section>

        <section className="space-y-3">
          <h2 className="text-xl font-semibold">6. Contact Us</h2>
          <p className="text-muted-foreground">
            If you have questions about this Privacy Policy, please contact us at
            privacy@sourcekoza.com.
          </p>
        </section>
      </div>
    </div>
  );
}
