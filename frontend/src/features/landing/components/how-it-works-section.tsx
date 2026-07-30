"use client";

import { motion } from "framer-motion";
import { Upload, Cpu, Briefcase } from "lucide-react";

const steps = [
  {
    icon: Upload,
    title: "Upload Your Resume",
    description:
      "Upload your resume and set your job preferences. Our AI analyzes your skills and experience.",
    step: "01",
  },
  {
    icon: Cpu,
    title: "AI Goes to Work",
    description:
      "Our multi-agent system searches jobs, optimizes your resume, and prepares personalized applications.",
    step: "02",
  },
  {
    icon: Briefcase,
    title: "Land Interviews",
    description:
      "Receive tailored applications, track responses, and prepare for interviews with AI coaching.",
    step: "03",
  },
];

export function HowItWorksSection() {
  return (
    <section id="how-it-works" className="py-24 bg-muted/30">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="text-3xl sm:text-4xl font-bold">
            How It{" "}
            <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              Works
            </span>
          </h2>
          <p className="mt-4 text-lg text-muted-foreground max-w-2xl mx-auto">
            Three simple steps to transform your job search experience.
          </p>
        </motion.div>

        <div className="grid gap-8 md:grid-cols-3">
          {steps.map((step, index) => (
            <motion.div
              key={step.title}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: index * 0.2 }}
              className="relative text-center"
            >
              <div className="mx-auto mb-6 relative">
                <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-primary/10 mx-auto">
                  <step.icon className="h-8 w-8 text-primary" />
                </div>
                <span className="absolute -top-2 -right-2 flex h-7 w-7 items-center justify-center rounded-full bg-primary text-xs font-bold text-primary-foreground">
                  {step.step}
                </span>
              </div>
              <h3 className="text-xl font-semibold">{step.title}</h3>
              <p className="mt-3 text-sm text-muted-foreground leading-relaxed max-w-xs mx-auto">
                {step.description}
              </p>
              {index < steps.length - 1 && (
                <div className="hidden md:block absolute top-8 left-[60%] w-[80%] border-t border-dashed border-border" />
              )}
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
