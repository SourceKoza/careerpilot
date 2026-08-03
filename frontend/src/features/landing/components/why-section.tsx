"use client";

import { motion } from "framer-motion";
import { Shield, Clock, Target, Sparkles } from "lucide-react";

const advantages = [
  {
    icon: Target,
    title: "Precision Matching",
    description:
      "AI analyzes job requirements against your profile for highly relevant matches only.",
  },
  {
    icon: Clock,
    title: "Save 40+ Hours/Week",
    description:
      "Automate repetitive tasks like searching, tailoring, and applying so you focus on interviews.",
  },
  {
    icon: Shield,
    title: "Privacy First",
    description:
      "Your data stays secure. We never share your information with third parties.",
  },
  {
    icon: Sparkles,
    title: "Continuously Learning",
    description:
      "Our AI improves with every application, learning what works best for your profile.",
  },
];

export function WhySection() {
  return (
    <section className="py-24">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="grid gap-12 lg:grid-cols-2 items-center">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
          >
            <h2 className="text-3xl sm:text-4xl font-bold">
              Why{" "}
              <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                CareerPilot AI
              </span>
            </h2>
            <p className="mt-4 text-lg text-muted-foreground">
              We built what we wished existed during our own job searches.
              CareerPilot AI combines cutting-edge AI with real-world job search
              expertise.
            </p>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, x: 20 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="grid gap-6 sm:grid-cols-2"
          >
            {advantages.map((item, index) => (
              <motion.div
                key={item.title}
                initial={{ opacity: 0, y: 10 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                className="space-y-2"
              >
                <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
                  <item.icon className="h-5 w-5 text-primary" />
                </div>
                <h3 className="font-semibold">{item.title}</h3>
                <p className="text-sm text-muted-foreground">
                  {item.description}
                </p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </div>
    </section>
  );
}
