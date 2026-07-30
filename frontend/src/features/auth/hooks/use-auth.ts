"use client";

import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { authService } from "@/services/auth.service";
import { useAuthStore } from "@/stores/auth.store";
import { ROUTES } from "@/lib/constants";
import type { LoginRequest, RegisterRequest, User } from "@/types/auth";

function decodeUserFromToken(token: string): User {
  try {
    const payload = token.split(".")[1];
    const decoded = JSON.parse(atob(payload));
    return {
      email: decoded.sub || decoded.email || "",
      firstName: decoded.firstName || "",
      lastName: decoded.lastName || "",
    };
  } catch {
    return { email: "", firstName: "", lastName: "" };
  }
}

export function useLogin() {
  const router = useRouter();
  const setAuth = useAuthStore((state) => state.setAuth);

  return useMutation({
    mutationFn: (data: LoginRequest) => authService.login(data),
    onSuccess: (response) => {
      const user = decodeUserFromToken(response.token);
      setAuth(response.token, user);
      router.push(ROUTES.DASHBOARD);
    },
  });
}

export function useRegister() {
  const router = useRouter();
  const setAuth = useAuthStore((state) => state.setAuth);

  return useMutation({
    mutationFn: (data: RegisterRequest) => authService.register(data),
    onSuccess: (response) => {
      const user = decodeUserFromToken(response.token);
      setAuth(response.token, user);
      router.push(ROUTES.DASHBOARD);
    },
  });
}

export function useLogout() {
  const router = useRouter();
  const logout = useAuthStore((state) => state.logout);

  return () => {
    logout();
    router.replace(ROUTES.LOGIN);
  };
}
