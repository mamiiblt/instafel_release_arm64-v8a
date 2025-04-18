"use client";

import React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";
import { Button } from "./ui/button";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import {
  NavigationMenu,
  NavigationMenuList,
} from "@/components/ui/navigation-menu";
import { SITE_CONFIG } from "@/config/config";
import { NavbarLoading } from "./loading";
import { NavigationItem } from "./NavigationItem";
import {
  BookOpenText,
  FileCog2Icon,
  LucideFlag,
  LucideInstagram,
  Monitor,
  RefreshCcwDot,
  Github,
  Menu,
  X,
  Sun,
  Moon,
} from "lucide-react";
import ThemeSwitcher from "./ThemeSwitcher";
import { useTheme } from "next-themes";

export default function Navbar() {
  const [open, setOpen] = React.useState(false);
  const [loading, setLoading] = React.useState(true);
  const [scrolled, setScrolled] = React.useState(false);
  const pathname = usePathname();
  const { theme, setTheme } = useTheme();

  // Track scroll position to add background opacity
  React.useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 10) {
        setScrolled(true);
      } else {
        setScrolled(false);
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  React.useEffect(() => {
    setLoading(false);
  }, []);

  // Close mobile menu when route changes
  React.useEffect(() => {
    setOpen(false);
  }, [pathname]);

  if (loading) {
    return <NavbarLoading />;
  }

  return (
    <motion.header
      initial={{ y: -100 }}
      animate={{ y: 0 }}
      transition={{ duration: 0.5, ease: "easeOut" }}
      className={`sticky top-0 z-50 border-b border-border transition-all duration-300 ${
        scrolled
          ? "bg-background/95 backdrop-blur-sm supports-backdrop-filter:bg-background/60"
          : "bg-background"
      }`}
    >
      <div className="container mx-auto flex h-16 items-center justify-between px-4 sm:px-6">
        <Link
          href="/"
          className="flex items-center space-x-2 transition-transform duration-200 hover:scale-105"
        >
          <motion.div
            initial={{ opacity: 0, rotate: -10 }}
            animate={{ opacity: 1, rotate: 0 }}
            transition={{ duration: 0.5 }}
            whileHover={{ rotate: -10 }}
          >
            <LucideInstagram className="h-6 w-6" />
          </motion.div>
          <motion.span
            initial={{ opacity: 0, x: -10 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.1, duration: 0.4 }}
            className="text-lg font-medium text-foreground"
          >
            Instafel
          </motion.span>
        </Link>

        <div className="hidden md:flex flex-1 justify-center">
          <NavigationMenu className="relative">
            <NavigationMenuList className="flex gap-1 md:gap-2">
              <AnimatePresence>
                {SITE_CONFIG.navItems.map((link, index) => {
                  const isActive = pathname === link.href;

                  // Skip rendering Source Code in desktop nav
                  if (link.title === "Source Code") {
                    return null;
                  }

                  // Change "Backup Library" to "Backups"
                  if (link.title === "Backup Library") {
                    return (
                      <motion.div
                        key={link.href}
                        initial={{ opacity: 0, y: -10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -10 }}
                        transition={{ duration: 0.3, delay: index * 0.05 }}
                      >
                        <NavigationItem
                          href={link.href}
                          title="Backups"
                          isActive={isActive}
                        />
                      </motion.div>
                    );
                  }

                  return (
                    <motion.div
                      key={link.href}
                      initial={{ opacity: 0, y: -10 }}
                      animate={{ opacity: 1, y: 0 }}
                      exit={{ opacity: 0, y: -10 }}
                      transition={{ duration: 0.3, delay: index * 0.05 }}
                    >
                      <NavigationItem
                        href={link.href}
                        title={link.title}
                        isActive={isActive}
                      />
                    </motion.div>
                  );
                })}
              </AnimatePresence>
            </NavigationMenuList>
          </NavigationMenu>
        </div>

        <div className="flex items-center gap-2">
          <motion.div
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.4, delay: 0.3 }}
          >
            <ThemeSwitcher />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.4, delay: 0.3 }}
            className="hidden md:block"
          >
            <Button
              asChild
              size="icon"
              variant="outline"
              className="transition-transform hover:scale-105"
            >
              <a
                href="https://github.com/mamiiblt/instafel"
                target="_blank"
                rel="noopener noreferrer"
              >
                <Github className="h-4 w-4" />
              </a>
            </Button>
          </motion.div>

          <div className="md:hidden">
            <Sheet open={open} onOpenChange={setOpen}>
              <SheetTrigger asChild>
                <Button
                  variant="ghost"
                  size="icon"
                  className="relative transition-all duration-200 hover:bg-accent hover:scale-105"
                  aria-label="Toggle menu"
                >
                  <Menu className="h-5 w-5" />
                </Button>
              </SheetTrigger>
              <SheetContent side="left" className="w-[300px] p-0 border-r-2">
                <div className="flex flex-col h-full">
                  <div className="border-b border-border p-5 bg-accent/10">
                    <div className="flex items-center space-x-3">
                      <motion.div
                        whileHover={{ rotate: -10 }}
                        transition={{ duration: 0.2 }}
                      >
                        <LucideInstagram className="h-7 w-7" />
                      </motion.div>
                      <span className="text-xl font-semibold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                        Instafel
                      </span>
                    </div>
                  </div>

                  <nav className="flex flex-col p-5 gap-2">
                    {SITE_CONFIG.navItems.map((link, index) => {
                      const isActive = pathname === link.href;
                      const MobileIcon = getMobileIcon(link.title);

                      return (
                        <motion.div
                          key={link.href}
                          initial={{ opacity: 0, x: -20 }}
                          animate={{ opacity: 1, x: 0 }}
                          transition={{ duration: 0.3, delay: index * 0.05 }}
                        >
                          <Link
                            href={link.href}
                            onClick={() => setOpen(false)}
                            className={`flex items-center space-x-4 rounded-lg px-4 py-3 text-sm font-medium transition-all duration-200 group
                              ${
                                isActive
                                  ? "bg-primary/10 text-primary border-l-4 border-primary"
                                  : "text-foreground hover:bg-accent/40 hover:text-accent-foreground hover:border-l-4 hover:border-primary/50"
                              }`}
                          >
                            <span
                              className={`flex items-center justify-center w-9 h-9 rounded-full ${isActive ? "bg-primary/15" : "bg-background/90"} text-foreground group-hover:scale-110 transition-transform shadow-sm`}
                            >
                              <MobileIcon
                                className={`h-5 w-5 ${isActive ? "text-primary" : ""}`}
                              />
                            </span>
                            <span className={isActive ? "font-semibold" : ""}>
                              {link.title}
                            </span>
                          </Link>
                        </motion.div>
                      );
                    })}
                  </nav>

                  <div className="mt-auto border-t border-border p-5">
                    <p className="mb-3 text-xs uppercase tracking-wider text-muted-foreground font-semibold pl-1">
                      Appearance
                    </p>
                    <div className="grid grid-cols-3 gap-2 bg-accent/10 p-2 rounded-lg">
                      <Button
                        variant={theme === "light" ? "default" : "ghost"}
                        size="sm"
                        onClick={() => setTheme("light")}
                        className={`flex-1 ${theme === "light" ? "shadow-md" : "hover:bg-background/80"}`}
                      >
                        <Sun className="mr-1.5 h-4 w-4" /> Light
                      </Button>
                      <Button
                        variant={theme === "dark" ? "default" : "ghost"}
                        size="sm"
                        onClick={() => setTheme("dark")}
                        className={`flex-1 ${theme === "dark" ? "shadow-md" : "hover:bg-background/80"}`}
                      >
                        <Moon className="mr-1.5 h-4 w-4" /> Dark
                      </Button>
                      <Button
                        variant={theme === "system" ? "default" : "ghost"}
                        size="sm"
                        onClick={() => setTheme("system")}
                        className={`flex-1 ${theme === "system" ? "shadow-md" : "hover:bg-background/80"}`}
                      >
                        <Monitor className="mr-1.5 h-4 w-4" /> Auto
                      </Button>
                    </div>
                  </div>
                </div>
              </SheetContent>
            </Sheet>
          </div>
        </div>
      </div>
    </motion.header>
  );
}

// Helper function to get the appropriate icon for mobile navigation
function getMobileIcon(title: string) {
  switch (title) {
    case "Guide":
      return BookOpenText;
    case "Backup Library":
      return FileCog2Icon;
    case "Flag Library":
      return LucideFlag;
    case "Updater":
      return RefreshCcwDot;
    case "Source Code":
      return Github;
    default:
      return BookOpenText;
  }
}
