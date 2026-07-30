import { AxiosError } from "axios";

interface ApiError {
  message?: string;
  error?: string;
}

export function getErrorMessage(error: unknown): string {
  if (error instanceof AxiosError) {
    const data = error.response?.data as ApiError | undefined;
    if (data?.message) return data.message;
    if (data?.error) return data.error;
    if (error.response?.status === 401) return "Invalid email or password";
    if (error.response?.status === 409) return "An account with this email already exists";
    if (error.response?.status === 400) return "Please check your input and try again";
    return "Something went wrong. Please try again.";
  }
  if (error instanceof Error) return error.message;
  return "An unexpected error occurred";
}
