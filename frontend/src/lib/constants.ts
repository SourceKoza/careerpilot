export const APP_NAME = "CareerPilot AI";

export const ROUTES = {
  HOME: "/",
  LOGIN: "/login",
  REGISTER: "/register",
  FORGOT_PASSWORD: "/forgot-password",
  PRIVACY: "/privacy",
  TERMS: "/terms",
  CONTACT: "/contact",
  DASHBOARD: "/dashboard",
  RESUME: "/dashboard/resume",
  RESUME_TAILORING: "/dashboard/resume-tailoring",
  APPLICATIONS: "/dashboard/applications",
  AGENTS: "/dashboard/agents",
  SETTINGS: "/dashboard/settings",
} as const;

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: "/api/v1/auth/login",
    REGISTER: "/api/v1/auth/register",
  },
} as const;
