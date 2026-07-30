import Link from "next/link";
import { Rocket } from "lucide-react";
import { APP_NAME, ROUTES } from "@/lib/constants";

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
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary">
              <Rocket className="h-5 w-5 text-primary-foreground" />
            </div>
            <span className="text-xl font-bold">{APP_NAME}</span>
          </Link>
        </div>

        <div className="rounded-2xl border border-border/50 bg-card/80 backdrop-blur-xl p-8 shadow-xl">
          {children}
        </div>
      </div>
    </div>
  );
}
