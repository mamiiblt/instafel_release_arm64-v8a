"use client";

import * as React from "react";
import { NavigationMenuItem, NavigationMenuLink } from "./ui/navigation-menu";
import { LoadingSpinner } from "./ui/loading-spinner";

interface NavigationItemProps {
  href: string;
  title: string;
  isActive: boolean;
}

export function NavigationItem({ href, title, isActive }: NavigationItemProps) {
  const [isLoading, setIsLoading] = React.useState(false);

  return (
    <NavigationMenuItem>
      <NavigationMenuLink
        href={href}
        onClick={() => setIsLoading(true)}
        className={`flex items-center space-x-2 rounded-lg px-3 py-2 text-m font-medium transition-colors hover:bg-accent hover:text-accent-foreground
          ${isActive ? "bg-accent text-accent-foreground" : "text-foreground"}
        `}
      >
        {isLoading ? (
          <LoadingSpinner className="mr-2 h-4 w-4" />
        ) : (
          <span>{title}</span>
        )}
      </NavigationMenuLink>
    </NavigationMenuItem>
  );
}
