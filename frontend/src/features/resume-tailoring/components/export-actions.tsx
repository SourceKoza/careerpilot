"use client";

import { motion } from "framer-motion";
import { Download, FileDown, FileType, ClipboardCopy } from "lucide-react";
import { Button } from "@/components/ui/button";

interface ExportActionsProps {
  onExportPdf: () => void;
  onExportDocx: () => void;
  isExportingPdf?: boolean;
  isExportingDocx?: boolean;
}

export function ExportActions({
  onExportPdf,
  onExportDocx,
  isExportingPdf,
  isExportingDocx,
}: ExportActionsProps) {
  const handleCopy = () => {
    // Mock copy functionality
    void navigator.clipboard.writeText("Resume content copied to clipboard");
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.35 }}
      className="rounded-xl border border-border bg-card p-5"
    >
      <div className="flex items-center gap-2 mb-4">
        <Download className="h-5 w-5 text-primary" />
        <h3 className="font-semibold text-sm">Export Resume</h3>
      </div>

      <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-3">
        <Button
          variant="outline"
          className="justify-start gap-2"
          onClick={onExportPdf}
          disabled={isExportingPdf}
        >
          <FileDown className="h-4 w-4 text-red-500" />
          {isExportingPdf ? "Exporting..." : "Export PDF"}
        </Button>
        <Button
          variant="outline"
          className="justify-start gap-2"
          onClick={onExportDocx}
          disabled={isExportingDocx}
        >
          <FileType className="h-4 w-4 text-blue-500" />
          {isExportingDocx ? "Exporting..." : "Export DOCX"}
        </Button>
        <Button variant="outline" className="justify-start gap-2" onClick={handleCopy}>
          <ClipboardCopy className="h-4 w-4 text-emerald-500" />
          Copy Resume
        </Button>
      </div>
    </motion.div>
  );
}
