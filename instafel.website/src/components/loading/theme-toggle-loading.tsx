"use client";

import { Skeleton } from "@/components/ui/skeleton";

export function ThemeToggleLoading() {
  return (
    <div className="flex items-center space-x-2">
      <Skeleton className="h-[1.2rem] w-[1.2rem] rounded-full" />
      <Skeleton className="h-5 w-9 rounded-full" />
      <Skeleton className="h-[1.2rem] w-[1.2rem] rounded-full" />
    </div>
  );
}
