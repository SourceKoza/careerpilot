"use client";

import { useEffect, useState } from "react";
import { apiClient } from "@/services/api";
import { Shield, UserX, UserCheck } from "lucide-react";

interface AdminUser {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  enabled: boolean;
  createdAt: string;
}

export default function AdminUsersPage() {
  const [users, setUsers] = useState<AdminUser[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUsers();
  }, []);

  async function loadUsers() {
    try {
      const resp = await apiClient.get<{ data: { content: AdminUser[] } }>("/api/v1/admin/users?page=0&size=50");
      setUsers(resp.data.data.content);
    } catch (e) {
      console.error("Failed to load users:", e);
    } finally {
      setLoading(false);
    }
  }

  async function toggleRole(userId: string, currentRole: string) {
    const newRole = currentRole === "ROLE_ADMIN" ? "ROLE_USER" : "ROLE_ADMIN";
    try {
      await apiClient.put(`/api/v1/admin/users/${userId}/role`, { role: newRole });
      loadUsers();
    } catch (e) {
      console.error("Failed to change role:", e);
    }
  }

  async function toggleEnabled(userId: string, currentEnabled: boolean) {
    const endpoint = currentEnabled ? "disable" : "enable";
    try {
      await apiClient.put(`/api/v1/admin/users/${userId}/${endpoint}`);
      loadUsers();
    } catch (e) {
      console.error("Failed to toggle user:", e);
    }
  }

  if (loading) {
    return <div className="p-8"><p className="text-muted-foreground">Loading users...</p></div>;
  }

  return (
    <div className="p-8 space-y-6">
      <div>
        <h1 className="text-2xl font-bold">User Management</h1>
        <p className="text-muted-foreground">{users.length} registered users</p>
      </div>

      <div className="rounded-xl border border-border overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-muted/50 border-b border-border">
            <tr>
              <th className="text-left px-4 py-3 font-medium">Name</th>
              <th className="text-left px-4 py-3 font-medium">Email</th>
              <th className="text-left px-4 py-3 font-medium">Role</th>
              <th className="text-left px-4 py-3 font-medium">Status</th>
              <th className="text-left px-4 py-3 font-medium">Joined</th>
              <th className="text-right px-4 py-3 font-medium">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="border-b border-border last:border-0 hover:bg-muted/30">
                <td className="px-4 py-3 font-medium">{user.firstName} {user.lastName}</td>
                <td className="px-4 py-3 text-muted-foreground">{user.email}</td>
                <td className="px-4 py-3">
                  <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                    user.role === "ROLE_ADMIN"
                      ? "bg-red-500/10 text-red-400 border border-red-500/30"
                      : "bg-blue-500/10 text-blue-400 border border-blue-500/30"
                  }`}>
                    {user.role === "ROLE_ADMIN" ? "Admin" : "User"}
                  </span>
                </td>
                <td className="px-4 py-3">
                  <span className={`px-2 py-0.5 rounded-full text-xs ${
                    user.enabled
                      ? "bg-green-500/10 text-green-400"
                      : "bg-red-500/10 text-red-400"
                  }`}>
                    {user.enabled ? "Active" : "Disabled"}
                  </span>
                </td>
                <td className="px-4 py-3 text-muted-foreground">
                  {new Date(user.createdAt).toLocaleDateString()}
                </td>
                <td className="px-4 py-3 text-right">
                  <div className="flex items-center justify-end gap-2">
                    <button
                      onClick={() => toggleRole(user.id, user.role)}
                      title={user.role === "ROLE_ADMIN" ? "Demote to User" : "Promote to Admin"}
                      className="p-1.5 rounded-lg hover:bg-muted transition-colors"
                    >
                      <Shield className="h-4 w-4 text-muted-foreground" />
                    </button>
                    <button
                      onClick={() => toggleEnabled(user.id, user.enabled)}
                      title={user.enabled ? "Disable User" : "Enable User"}
                      className="p-1.5 rounded-lg hover:bg-muted transition-colors"
                    >
                      {user.enabled ? (
                        <UserX className="h-4 w-4 text-red-400" />
                      ) : (
                        <UserCheck className="h-4 w-4 text-green-400" />
                      )}
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
