export default function Home() {
  return (
    <main
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        minHeight: "100vh",
        fontFamily: "system-ui, sans-serif",
      }}
    >
      <h1>CareerPilot AI</h1>
      <p>AI-powered Job Search and Application Automation Platform</p>
      <p style={{ color: "#666", fontSize: "0.875rem" }}>
        SourceKoza Labs &mdash; v0.1.0
      </p>
    </main>
  );
}
