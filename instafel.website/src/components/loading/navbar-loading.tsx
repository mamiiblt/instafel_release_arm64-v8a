"use client";

import { Skeleton } from "@/components/ui/skeleton";

export function NavbarLoading() {
  return (
    <div className="sticky top-0 z-50 border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
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

        {/* Right section - Theme toggle + menu */}
        <div className="flex w-1/4 justify-end items-center space-x-4">
          <div className="flex items-center space-x-2">
            <Skeleton className="h-5 w-5 rounded-full" />
            <Skeleton className="h-5 w-9 rounded-full" />
            <Skeleton className="h-5 w-5 rounded-full" />
          </div>
          <Skeleton className="h-9 w-9 md:hidden" /> {/* Mobile menu button */}
        </div>
      </div>
    </div>
  );
}
