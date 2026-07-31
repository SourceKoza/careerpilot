"use client";

import { useEffect, useState } from "react";
import { apiClient } from "@/services/api";
import { Save, Plus } from "lucide-react";

interface Setting {
  id: string;
  settingKey: string;
  settingValue: string;
  category: string;
  description: string;
  updatedAt: string;
}

export default function AdminSettingsPage() {
  const [settings, setSettings] = useState<Record<string, Setting[]>>({});
  const [loading, setLoading] = useState(true);
  const [editingKey, setEditingKey] = useState<string | null>(null);
  const [editValue, setEditValue] = useState("");

  useEffect(() => {
    loadSettings();
  }, []);

  async function loadSettings() {
    try {
      const resp = await apiClient.get<{ data: Record<string, Setting[]> }>("/api/v1/admin/settings");
      setSettings(resp.data.data);
    } catch (e) {
      console.error("Failed to load settings:", e);
    } finally {
      setLoading(false);
    }
  }

  async function saveSetting(key: string) {
    try {
      await apiClient.put(`/api/v1/admin/settings/${key}`, { value: editValue });
      setEditingKey(null);
      loadSettings();
    } catch (e) {
      console.error("Failed to save setting:", e);
    }
  }

  if (loading) {
    return <div className="p-8"><p className="text-muted-foreground">Loading settings...</p></div>;
  }

  const categories = Object.keys(settings).sort();

  return (
    <div className="p-8 space-y-8">
      <div>
        <h1 className="text-2xl font-bold">Global Settings</h1>
        <p className="text-muted-foreground">Configure platform behavior</p>
      </div>

      {categories.map((category) => (
        <div key={category} className="space-y-3">
          <h2 className="text-lg font-semibold capitalize">{category}</h2>
          <div className="rounded-xl border border-border divide-y divide-border">
            {settings[category].map((setting) => (
              <div key={setting.settingKey} className="flex items-center justify-between px-4 py-3">
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium font-mono">{setting.settingKey}</p>
                  {setting.description && (
                    <p className="text-xs text-muted-foreground mt-0.5">{setting.description}</p>
                  )}
                </div>
                <div className="flex items-center gap-2 ml-4">
                  {editingKey === setting.settingKey ? (
                    <>
                      <input
                        type="text"
                        value={editValue}
                        onChange={(e) => setEditValue(e.target.value)}
                        className="w-48 rounded-lg border border-input bg-transparent px-3 py-1.5 text-sm"
                        autoFocus
                      />
                      <button
                        onClick={() => saveSetting(setting.settingKey)}
                        className="p-1.5 rounded-lg bg-primary/10 text-primary hover:bg-primary/20"
                      >
                        <Save className="h-4 w-4" />
                      </button>
                      <button
                        onClick={() => setEditingKey(null)}
                        className="p-1.5 rounded-lg hover:bg-muted text-muted-foreground text-xs"
                      >
                        Cancel
                      </button>
                    </>
                  ) : (
                    <button
                      onClick={() => { setEditingKey(setting.settingKey); setEditValue(setting.settingValue); }}
                      className="px-3 py-1 rounded-lg border border-border text-sm font-mono hover:bg-muted transition-colors"
                    >
                      {setting.settingValue}
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}
