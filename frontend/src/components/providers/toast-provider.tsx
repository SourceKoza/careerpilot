"use client";

import {
  ToastProvider as RadixToastProvider,
  ToastViewport,
} from "@/components/ui/toast";
import type { ReactNode } from "react";

interface ToastProviderProps {
  children: ReactNode;
}

export function ToastProvider({ children }: ToastProviderProps) {
  return (
    <RadixToastProvider>
      {children}
      <ToastViewport />
    </RadixToastProvider>
  );
}
