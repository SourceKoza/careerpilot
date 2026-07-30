"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/stores/auth.store";
import { ROUTES } from "@/lib/constants";

interface AuthGuardProps {
  children: React.ReactNode;
}

export function AuthGuard({ children }: AuthGuardProps) {
  const router = useRouter();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const token = useAuthStore((state) => state.token);

  useEffect(() => {
    const storedToken = localStorage.getItem("careerpilot_token");
    if (!isAuthenticated && !storedToken) {
      router.push(ROUTES.LOGIN);
    }
  }, [isAuthenticated, token, router]);

  if (!isAuthenticated && typeof window !== "undefined" && !localStorage.getItem("careerpilot_token")) {
    return null;
  }

  return <>{children}</>;
}
