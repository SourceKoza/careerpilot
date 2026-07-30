"use client";

import type { ReactNode } from "react";
import { ThemeProvider } from "./theme-provider";
import { QueryProvider } from "./query-provider";
import { ToastProvider } from "./toast-provider";
import { AuthHydration } from "@/features/auth/components/auth-hydration";

interface ProvidersProps {
  children: ReactNode;
}

export function Providers({ children }: ProvidersProps) {
  return (
    <ThemeProvider>
      <QueryProvider>
        <ToastProvider>
          <AuthHydration />
          {children}
        </ToastProvider>
      </QueryProvider>
    </ThemeProvider>
  );
}
