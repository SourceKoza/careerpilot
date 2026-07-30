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
    gradient: "from-blue-500 to-cyan-500",
  },
  {
    icon: FileText,
    title: "Resume Optimizer",
    description:
      "AI-powered resume tailoring for each application. Maximize your chances with keyword optimization.",
    gradient: "from-violet-500 to-purple-500",
  },
  {
    icon: Zap,
    title: "Auto Apply",
    description:
      "Automated job applications with personalized cover letters. Apply to hundreds of jobs effortlessly.",
    gradient: "from-amber-500 to-orange-500",
  },
  {
    icon: Bot,
    title: "AI Agents",
    description:
      "Multi-agent system that works 24/7 to find, analyze, and apply to the best opportunities for you.",
    gradient: "from-emerald-500 to-green-500",
  },
  {
    icon: BarChart3,
    title: "Analytics Dashboard",
    description:
      "Track applications, response rates, and insights to optimize your job search strategy.",
    gradient: "from-pink-500 to-rose-500",
  },
  {
    icon: MessageSquare,
    title: "Interview Assistant",
    description:
      "AI-powered interview preparation with company-specific questions and mock sessions.",
    gradient: "from-indigo-500 to-blue-500",
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
              whileHover={{ y: -4, transition: { duration: 0.2 } }}
              className="group relative"
            >
              {/* Gradient border that shows on hover */}
              <div className={`absolute -inset-[1px] rounded-2xl bg-gradient-to-r ${feature.gradient} opacity-0 group-hover:opacity-100 transition-opacity duration-300 blur-[0.5px]`} />
              <div className="relative rounded-2xl border border-border/50 bg-card p-6 backdrop-blur-sm transition-all duration-300 hover:shadow-lg">
                <div className={`flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br ${feature.gradient} bg-opacity-10 transition-transform duration-300 group-hover:scale-110`}>
                  <feature.icon className="h-6 w-6 text-white" />
                </div>
                <h3 className="mt-4 text-lg font-semibold">{feature.title}</h3>
                <p className="mt-2 text-sm text-muted-foreground leading-relaxed">
                  {feature.description}
                </p>
              </div>
            </motion.div>
          ))}
        </motion.div>
      </div>
    </section>
  );
}
