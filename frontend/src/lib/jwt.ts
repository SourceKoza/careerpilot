/**
 * Check if a JWT token is expired.
 */
export function isTokenExpired(token: string): boolean {
  try {
    const payload = token.split(".")[1];
    const decoded = JSON.parse(atob(payload));
    if (!decoded.exp) return false;
    const expirationMs = decoded.exp * 1000;
    return Date.now() >= expirationMs;
  } catch {
    return true;
  }
}

/**
 * Decode basic payload from JWT.
 */
export function decodeToken(token: string): Record<string, unknown> | null {
  try {
    const payload = token.split(".")[1];
    return JSON.parse(atob(payload));
  } catch {
    return null;
  }
}
