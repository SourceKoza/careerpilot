"use client";

import { useEffect, useState } from "react";
import { apiClient } from "@/services/api";
import { ScrollText } from "lucide-react";

interface AuditEntry {
  id: string;
  adminId: string;
  action: string;
  targetType: string;
  targetId: string;
  details: string | null;
  ipAddress: string;
  createdAt: string;
}

export default function AdminAuditPage() {
  const [logs, setLogs] = useState<AuditEntry[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const resp = await apiClient.get<{ data: { content: AuditEntry[] } }>("/api/v1/admin/audit?page=0&size=50");
        setLogs(resp.data.data.content);
      } catch (e) {
        console.error("Failed to load audit logs:", e);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  if (loading) {
    return <div className="p-8"><p className="text-muted-foreground">Loading audit logs...</p></div>;
  }

  return (
    <div className="p-8 space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Audit Logs</h1>
        <p className="text-muted-foreground">All admin actions are logged here</p>
      </div>

      {logs.length === 0 ? (
        <div className="text-center py-12">
          <ScrollText className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
          <p className="text-muted-foreground">No audit entries yet.</p>
          <p className="text-xs text-muted-foreground mt-1">Actions will appear here as admins make changes.</p>
        </div>
      ) : (
        <div className="rounded-xl border border-border overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-muted/50 border-b border-border">
              <tr>
                <th className="text-left px-4 py-3 font-medium">Action</th>
                <th className="text-left px-4 py-3 font-medium">Target</th>
                <th className="text-left px-4 py-3 font-medium">Details</th>
                <th className="text-left px-4 py-3 font-medium">IP</th>
                <th className="text-left px-4 py-3 font-medium">Time</th>
              </tr>
            </thead>
            <tbody>
              {logs.map((log) => (
                <tr key={log.id} className="border-b border-border last:border-0 hover:bg-muted/30">
                  <td className="px-4 py-3">
                    <span className="px-2 py-0.5 rounded text-xs font-mono bg-muted">
                      {log.action}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-muted-foreground">
                    {log.targetType} / {log.targetId?.substring(0, 8)}...
                  </td>
                  <td className="px-4 py-3 text-xs text-muted-foreground max-w-[200px] truncate">
                    {log.details || "—"}
                  </td>
                  <td className="px-4 py-3 text-xs text-muted-foreground font-mono">{log.ipAddress}</td>
                  <td className="px-4 py-3 text-xs text-muted-foreground">
                    {new Date(log.createdAt).toLocaleString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
