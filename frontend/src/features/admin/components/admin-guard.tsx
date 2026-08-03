"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/stores/auth.store";

export function AdminGuard({ children }: { children: React.ReactNode }) {
  const user = useAuthStore((state) => state.user);
  const router = useRouter();
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    if (user && user.role !== "ROLE_ADMIN") {
      router.replace("/dashboard");
    } else {
      setChecked(true);
    }
  }, [user, router]);

  if (!checked || !user || user.role !== "ROLE_ADMIN") {
    return (
      <div className="flex items-center justify-center h-full">
        <p className="text-muted-foreground">Checking permissions...</p>
      </div>
    );
  }

  return <>{children}</>;
}
