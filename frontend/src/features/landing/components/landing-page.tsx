"use client";

import { Navbar } from "@/components/layout/navbar";
import { Footer } from "@/components/layout/footer";
import { HeroSection } from "./hero-section";
import { FeaturesSection } from "./features-section";
import { HowItWorksSection } from "./how-it-works-section";
import { WhySection } from "./why-section";
import { StatsSection } from "./stats-section";
import { TestimonialsSection } from "./testimonials-section";
import { FaqSection } from "./faq-section";
import { CtaSection } from "./cta-section";

export function LandingPage() {
  return (
    <div className="min-h-screen">
      <Navbar />
      <main>
        <HeroSection />
        <FeaturesSection />
        <HowItWorksSection />
        <WhySection />
        <StatsSection />
        <TestimonialsSection />
        <FaqSection />
        <CtaSection />
      </main>
      <Footer />
    </div>
  );
}
