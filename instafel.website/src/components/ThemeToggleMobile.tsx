import * as React from "react";
import { Moon, Sun } from "@/components/Icons";
import { useTheme as useNextTheme } from "next-themes";
import { Switch } from "@/components/ui/switch";
import { ThemeToggleLoading } from "@/components/loading";
import { motion } from "framer-motion";

export function ThemeToggle() {
  const { theme, setTheme } = useNextTheme();
  const [mounted, setMounted] = React.useState(false);
  const [isLoading, setIsLoading] = React.useState(false);

  React.useEffect(() => {
    setMounted(true);
    if (!theme) {
      setTheme("light");
    }
  }, [theme, setTheme]);

  if (!mounted) return <ThemeToggleLoading />;

  const isDark = theme === "dark";

  const handleThemeChange = (checked: boolean) => {
    if (!isLoading) {
      setIsLoading(true);
      setTheme(checked ? "dark" : "light");
      setTimeout(() => setIsLoading(false), 250);
    }
  };

  return (
    <motion.div
      className="flex items-center space-x-2"
      whileHover={{ scale: 1.05 }}
      whileTap={{ scale: 0.95 }}
    >
    
      <Sun
        className={`hidden md:block h-[1.2rem] w-[1.2rem] transition-colors ${!isDark ? "text-primary" : "text-muted-foreground"}`}
      />
      <Switch
        checked={isDark}
        className="hidden md:block"
        onCheckedChange={handleThemeChange}
        disabled={isLoading}
        aria-label="Toggle theme"
      />
      <Moon
        className={`hidden md:block h-[1.2rem] w-[1.2rem] transition-colors ${isDark ? "text-primary" : "text-muted-foreground"}`}
      />
    </motion.div>
  );
}
