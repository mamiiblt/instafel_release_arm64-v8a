"use client";

import { Skeleton } from "@/components/ui/skeleton";

export function FooterLoading() {
  return (
    <footer className="relative border-t border-border">
      <div className="absolute inset-0 pointer-events-none" />

      <div className="relative container mx-auto px-4 pt-8 md:pt-8 pb-8 md:pb-8">
        <div className="max-w-6xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <div className="space-y-4 lg:col-span-3">
              <div className="flex items-center gap-2">
                <Skeleton className="w-10 h-10 rounded-xl" />
                <Skeleton className="h-5 w-32" />
              </div>
              <Skeleton className="h-4 w-3/4" />
            </div>

            <div className="lg:col-span-1">
              <Skeleton className="h-6 w-32 mb-4" />
              <div className="space-y-3">
                {[...Array(5)].map((_, i) => (
                  <Skeleton key={i} className="h-4 w-full max-w-[180px]" />
                ))}
              </div>
            </div>
          </div>

          <div className="mt-8 pt-8 border-t border-border">
            <div className="flex flex-col md:flex-row justify-between items-center gap-4">
              <Skeleton className="h-4 w-48" />
              <div className="flex items-center gap-4">
                {[...Array(3)].map((_, i) => (
                  <Skeleton key={i} className="h-5 w-5 rounded-full" />
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}
