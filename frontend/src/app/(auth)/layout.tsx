"use client";

import Link from "next/link";
import { motion } from "framer-motion";
import { Rocket } from "lucide-react";
import { APP_NAME, ROUTES } from "@/lib/constants";
import { PageTransition } from "@/components/shared/page-transition";

export default function AuthLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative min-h-screen flex items-center justify-center p-4">
      {/* Background effects */}
      <div className="absolute inset-0 -z-10">
        <div className="absolute top-1/3 left-1/4 h-72 w-72 rounded-full bg-primary/15 blur-3xl" />
        <div className="absolute bottom-1/3 right-1/4 h-72 w-72 rounded-full bg-accent/15 blur-3xl" />
      </div>

      <div className="w-full max-w-md">
        <div className="mb-8 text-center">
          <Link href={ROUTES.HOME} className="inline-flex items-center gap-2">
            <motion.div
              className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary"
              initial={{ scale: 0, rotate: -180 }}
              animate={{ scale: 1, rotate: 0 }}
              transition={{ type: "spring", stiffness: 200, damping: 15 }}
            >
              <Rocket className="h-5 w-5 text-primary-foreground" />
            </motion.div>
            <motion.span
              className="text-xl font-bold"
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: 0.2 }}
            >
              {APP_NAME}
            </motion.span>
          </Link>
        </div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1, duration: 0.4 }}
          className="rounded-2xl border border-border/50 bg-card/80 backdrop-blur-xl p-8 shadow-xl"
        >
          <PageTransition>{children}</PageTransition>
        </motion.div>
      </div>
    </div>
  );
}
