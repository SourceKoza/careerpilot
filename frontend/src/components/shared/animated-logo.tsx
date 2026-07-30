"use client";

import { motion } from "framer-motion";
import { Rocket } from "lucide-react";
import Link from "next/link";
import { APP_NAME, ROUTES } from "@/lib/constants";

export function AnimatedLogo() {
  return (
    <Link href={ROUTES.HOME} className="flex items-center gap-2">
      <motion.div
        className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary"
        whileHover={{ scale: 1.1, rotate: -5 }}
        whileTap={{ scale: 0.95 }}
        initial={{ rotate: -180, opacity: 0 }}
        animate={{ rotate: 0, opacity: 1 }}
        transition={{ type: "spring", stiffness: 200, damping: 15 }}
      >
        <motion.div
          animate={{ y: [0, -2, 0] }}
          transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
        >
          <Rocket className="h-4 w-4 text-primary-foreground" />
        </motion.div>
      </motion.div>
      <motion.span
        className="text-lg font-bold"
        initial={{ opacity: 0, x: -10 }}
        animate={{ opacity: 1, x: 0 }}
        transition={{ delay: 0.2 }}
      >
        {APP_NAME}
      </motion.span>
    </Link>
  );
}
