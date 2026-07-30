"use client";

import { motion } from "framer-motion";
import {
  Search,
  FileText,
  Zap,
  Bot,
  BarChart3,
  MessageSquare,
} from "lucide-react";

const features = [
  {
    icon: Search,
    title: "AI Job Search",
    description:
      "Intelligent job matching across multiple platforms. Find roles that align with your skills and career goals.",
  },
  {
    icon: FileText,
    title: "Resume Optimizer",
    description:
      "AI-powered resume tailoring for each application. Maximize your chances with keyword optimization.",
  },
  {
    icon: Zap,
    title: "Auto Apply",
    description:
      "Automated job applications with personalized cover letters. Apply to hundreds of jobs effortlessly.",
  },
  {
    icon: Bot,
    title: "AI Agents",
    description:
      "Multi-agent system that works 24/7 to find, analyze, and apply to the best opportunities for you.",
  },
  {
    icon: BarChart3,
    title: "Analytics Dashboard",
    description:
      "Track applications, response rates, and insights to optimize your job search strategy.",
  },
  {
    icon: MessageSquare,
    title: "Interview Assistant",
    description:
      "AI-powered interview preparation with company-specific questions and mock sessions.",
  },
];

const containerVariants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: { staggerChildren: 0.1 },
  },
};

const itemVariants = {
  hidden: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.5 } },
};

export function FeaturesSection() {
  return (
    <section id="features" className="py-24 relative">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-16"
        >
          <h2 className="text-3xl sm:text-4xl font-bold">
            Everything You Need to{" "}
            <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              Land Your Dream Job
            </span>
          </h2>
          <p className="mt-4 text-lg text-muted-foreground max-w-2xl mx-auto">
            A complete AI-powered platform designed to automate every step of
            your job search journey.
          </p>
        </motion.div>

        <motion.div
          variants={containerVariants}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true }}
          className="grid gap-6 md:grid-cols-2 lg:grid-cols-3"
        >
          {features.map((feature) => (
            <motion.div
              key={feature.title}
              variants={itemVariants}
              className="group relative rounded-2xl border border-border/50 bg-card/50 p-6 backdrop-blur-sm transition-all duration-300 hover:border-primary/30 hover:shadow-lg hover:shadow-primary/5"
            >
              <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-primary/10 transition-colors group-hover:bg-primary/20">
                <feature.icon className="h-6 w-6 text-primary" />
              </div>
              <h3 className="mt-4 text-lg font-semibold">{feature.title}</h3>
              <p className="mt-2 text-sm text-muted-foreground leading-relaxed">
                {feature.description}
              </p>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
}
