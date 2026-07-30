"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/stores/auth.store";
import { ROUTES } from "@/lib/constants";
import { isTokenExpired } from "@/lib/jwt";
import { Skeleton } from "@/components/ui/skeleton";

interface AuthGuardProps {
  children: React.ReactNode;
}

export function AuthGuard({ children }: AuthGuardProps) {
  const router = useRouter();
  const { isAuthenticated, token, logout } = useAuthStore();
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    const storedToken = localStorage.getItem("careerpilot_token");

    if (!storedToken) {
      router.push(ROUTES.LOGIN);
      return;
    }

    if (isTokenExpired(storedToken)) {
      logout();
      router.push(ROUTES.LOGIN);
      return;
    }

    setChecked(true);
  }, [isAuthenticated, token, logout, router]);

  if (!checked) {
    return (
      <div className="flex h-screen items-center justify-center">
        <div className="space-y-4 w-full max-w-md p-8">
          <Skeleton className="h-8 w-48" />
          <Skeleton className="h-4 w-72" />
          <div className="grid grid-cols-2 gap-4 mt-8">
            <Skeleton className="h-24" />
            <Skeleton className="h-24" />
          </div>
        </div>
      </div>
    );
  }

  return <>{children}</>;
}
