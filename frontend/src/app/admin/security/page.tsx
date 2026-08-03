"use client";

import { useEffect, useState } from "react";
import { apiClient } from "@/services/api";
import { Shield } from "lucide-react";

interface SecurityConfigItem {
  id: string;
  configKey: string;
  configValue: string;
  enabled: boolean;
  updatedAt: string;
}

export default function AdminSecurityPage() {
  const [configs, setConfigs] = useState<SecurityConfigItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadConfigs();
  }, []);

  async function loadConfigs() {
    try {
      const resp = await apiClient.get<{ data: SecurityConfigItem[] }>("/api/v1/admin/security");
      setConfigs(resp.data.data);
    } catch (e) {
      console.error("Failed to load security configs:", e);
    } finally {
      setLoading(false);
    }
  }

  async function toggleEnabled(key: string, current: boolean) {
    try {
      await apiClient.put(`/api/v1/admin/security/${key}`, { enabled: !current });
      loadConfigs();
    } catch (e) {
      console.error("Failed to update:", e);
    }
  }

  if (loading) {
    return <div className="p-8"><p className="text-muted-foreground">Loading security configs...</p></div>;
  }

  return (
    <div className="p-8 space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Security Configuration</h1>
        <p className="text-muted-foreground">Manage rate limits, session rules, and access controls</p>
      </div>

      {configs.length === 0 ? (
        <div className="text-center py-12">
          <Shield className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
          <p className="text-muted-foreground">No security configs defined yet.</p>
          <p className="text-xs text-muted-foreground mt-1">Security configs will appear here once created via the API.</p>
        </div>
      ) : (
        <div className="rounded-xl border border-border divide-y divide-border">
          {configs.map((config) => (
            <div key={config.id} className="flex items-center justify-between px-4 py-3">
              <div>
                <p className="text-sm font-medium font-mono">{config.configKey}</p>
                <p className="text-xs text-muted-foreground">Value: {config.configValue}</p>
              </div>
              <button
                onClick={() => toggleEnabled(config.configKey, config.enabled)}
                className={`px-3 py-1 rounded-full text-xs font-medium transition-colors ${
                  config.enabled
                    ? "bg-green-500/10 text-green-400 hover:bg-green-500/20"
                    : "bg-red-500/10 text-red-400 hover:bg-red-500/20"
                }`}
              >
                {config.enabled ? "Enabled" : "Disabled"}
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
