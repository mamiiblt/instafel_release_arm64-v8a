"use client";

import React from "react";
import { cn } from "@/lib/utils";

export function LoadingSpinner({ className }: { className?: string }) {
  return (
    <div
      className={cn(
        "h-4 w-4 animate-spin rounded-full border-2 border-primary border-t-transparent",
        className
      )}
    />
  );
}
