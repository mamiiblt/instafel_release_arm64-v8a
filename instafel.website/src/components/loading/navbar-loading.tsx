"use client";

import { Skeleton } from "@/components/ui/skeleton";

export function NavbarLoading() {
  return (
    <div className="sticky top-0 z-50 border-b border-border bg-background/95 backdrop-blur-sm supports-backdrop-filter:bg-background/60">
      <div className="container max-w-7xl mx-auto flex h-16 items-center px-4">
        {/* Left section - Title */}
        <div className="flex w-1/4">
          <div className="flex items-center space-x-2 ml-8">
            <Skeleton className="h-8 w-32" />
          </div>
        </div>

        {/* Center section - Nav items */}
        <div className="flex-1 flex justify-center">
          <div className="hidden md:flex items-center space-x-6">
            {[...Array(4)].map((_, i) => (
              <Skeleton key={i} className="h-8 w-20" />
            ))}
          </div>
        </div>

        {/* Right section - menu */}
        <div className="flex w-1/4 justify-end items-center space-x-4">
          <Skeleton className="h-9 w-9 md:hidden" /> {/* Mobile menu button */}
        </div>
      </div>
    </div>
  );
}
