"use client";

import Link from "next/link";
import { motion } from "framer-motion";
import { ArrowRight, Play, Search, Bot, FileText, Send, BarChart3 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ROUTES } from "@/lib/constants";

function DashboardMockup() {
  return (
    <div className="rounded-xl bg-background border border-border overflow-hidden">
      {/* Mock topbar */}
      <div className="flex items-center justify-between border-b border-border px-4 py-2">
        <div className="flex items-center gap-2">
          <div className="h-3 w-3 rounded-full bg-red-500/70" />
          <div className="h-3 w-3 rounded-full bg-yellow-500/70" />
          <div className="h-3 w-3 rounded-full bg-green-500/70" />
        </div>
        <div className="h-5 w-48 rounded-md bg-muted" />
        <div className="flex gap-1.5">
          <div className="h-5 w-5 rounded bg-muted" />
          <div className="h-5 w-5 rounded bg-muted" />
        </div>
      </div>
      <div className="flex">
        {/* Mock sidebar */}
        <div className="hidden sm:flex w-44 flex-col border-r border-border p-3 space-y-2">
          <div className="h-5 w-24 rounded bg-primary/20" />
          <div className="h-4 w-20 rounded bg-muted mt-4" />
          <div className="h-4 w-28 rounded bg-muted" />
          <div className="h-4 w-24 rounded bg-muted" />
          <div className="h-4 w-20 rounded bg-muted" />
          <div className="h-4 w-26 rounded bg-muted" />
        </div>
        {/* Mock content */}
        <div className="flex-1 p-4 space-y-3">
          <div className="h-6 w-48 rounded bg-muted" />
          <div className="h-4 w-64 rounded bg-muted/60" />
          {/* Mock stat cards */}
          <div className="grid grid-cols-4 gap-2 mt-3">
            {[Search, Bot, FileText, Send].map((Icon, i) => (
              <div key={i} className="rounded-lg border border-border p-2 space-y-1">
                <Icon className="h-3.5 w-3.5 text-primary/60" />
                <div className="h-4 w-8 rounded bg-muted" />
                <div className="h-2 w-12 rounded bg-muted/50" />
              </div>
            ))}
          </div>
          {/* Mock chart area */}
          <div className="rounded-lg border border-border p-3 mt-2">
            <div className="flex items-center gap-2 mb-2">
              <BarChart3 className="h-3.5 w-3.5 text-primary/60" />
              <div className="h-3 w-20 rounded bg-muted" />
            </div>
            <div className="flex items-end gap-1 h-16">
              {[40, 60, 35, 80, 55, 70, 90, 65, 75, 85, 50, 95].map((h, i) => (
                <motion.div
                  key={i}
                  className="flex-1 rounded-sm bg-gradient-to-t from-primary/40 to-primary/10"
                  initial={{ height: 0 }}
                  animate={{ height: `${h}%` }}
                  transition={{ delay: 0.8 + i * 0.05, duration: 0.4 }}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export function HeroSection() {
  return (
    <section className="relative min-h-screen flex items-center justify-center overflow-hidden pt-16">
      {/* Animated background gradients */}
      <div className="absolute inset-0 -z-10">
        <motion.div
          className="absolute top-1/4 left-1/4 h-96 w-96 rounded-full bg-primary/20 blur-3xl"
          animate={{ scale: [1, 1.2, 1], opacity: [0.2, 0.3, 0.2] }}
          transition={{ duration: 6, repeat: Infinity }}
        />
        <motion.div
          className="absolute bottom-1/4 right-1/4 h-96 w-96 rounded-full bg-accent/20 blur-3xl"
          animate={{ scale: [1.2, 1, 1.2], opacity: [0.3, 0.2, 0.3] }}
          transition={{ duration: 8, repeat: Infinity }}
        />
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 h-[600px] w-[600px] rounded-full bg-violet-500/10 blur-3xl" />
      </div>

      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 text-center">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="inline-flex items-center rounded-full border border-border/60 bg-secondary/50 px-4 py-1.5 text-sm text-muted-foreground mb-8 backdrop-blur-sm"
        >
          <span className="mr-2 h-2 w-2 rounded-full bg-primary animate-pulse" />
          AI-Powered Career Automation
        </motion.div>

        <motion.h1
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.1 }}
          className="text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-bold tracking-tight"
        >
          Your AI{" "}
          <span className="bg-gradient-to-r from-primary via-violet-500 to-accent bg-clip-text text-transparent">
            Career Copilot
          </span>
        </motion.h1>

        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.2 }}
          className="mt-6 text-lg sm:text-xl text-muted-foreground max-w-2xl mx-auto"
        >
          Automate your job search with AI-powered resume optimization,
          intelligent job matching, and automated applications. Land your dream
          role faster.
        </motion.p>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.3 }}
          className="mt-10 flex flex-col sm:flex-row items-center justify-center gap-4"
        >
          <Link href={ROUTES.REGISTER}>
            <Button size="lg" className="group">
              Start Free
              <ArrowRight className="h-4 w-4 transition-transform group-hover:translate-x-1" />
            </Button>
          </Link>
          <Button size="lg" variant="outline" className="group">
            <Play className="h-4 w-4" />
            Watch Demo
          </Button>
        </motion.div>

        {/* Dashboard mockup */}
        <motion.div
          initial={{ opacity: 0, y: 40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.5 }}
          className="mt-20 relative"
        >
          {/* Gradient border wrapper */}
          <div className="relative mx-auto max-w-4xl p-[1px] rounded-2xl bg-gradient-to-r from-primary via-violet-500 to-accent">
            <div className="rounded-2xl bg-card/95 backdrop-blur-xl p-2 shadow-2xl">
              <DashboardMockup />
            </div>
          </div>
          {/* Glow effect */}
          <div className="absolute -inset-4 -z-10 rounded-3xl bg-gradient-to-r from-primary/20 via-violet-500/20 to-accent/20 blur-2xl opacity-50" />
        </motion.div>
      </div>
    </section>
  );
}
