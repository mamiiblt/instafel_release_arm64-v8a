"use client";
import React from "react";
import * as Icons from "@/components/Icons";
import { FooterLoading } from "./loading";
import { usePathname } from "next/navigation";
import {
  Book,
  Download,
  FileCog2Icon,
  GithubIcon,
  RefreshCcwDot,
  Send,
  User,
} from "lucide-react";
import Link from "next/link";
import { motion } from "framer-motion";

export default function Footer() {
  const [loading, setLoading] = React.useState(true);
  const pathname = usePathname();

  React.useEffect(() => {
    // Simulating loading for a very short time
    const timer = setTimeout(() => {
      setLoading(false);
    }, 300);
    return () => clearTimeout(timer);
  }, []);

  if (loading) {
    return <FooterLoading />;
  }

  return (
    <motion.footer
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="relative border-t border-border bg-background"
    >
      <div className="absolute inset-0 pointer-events-none" />

      <div className="relative container mx-auto px-4 py-8">
        <div className="max-w-6xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            <div className="space-y-4 lg:col-span-2">
              <div className="flex items-center gap-2">
                <div className="w-10 h-10 rounded-xl bg-instafelcolor flex items-center justify-center">
                  <Icons.Instagram className="text-black font-bold text-xl" />
                </div>
                <span className="font-bold text-xl text-foreground">
                  Instafel
                </span>
              </div>
              <p className="text-muted-foreground">
                Provides fast & up-to-date Instagram Alpha experience with
                enhanced features and community-driven development
              </p>
            </div>

            <div>
              <h3 className="font-semibold mb-4 text-lg">Resources</h3>
              <ul className="space-y-3">
                <motion.li
                  whileHover={{ x: 5 }}
                  transition={{ type: "spring", stiffness: 400 }}
                >
                  <Link
                    href="/guides"
                    className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors"
                  >
                    <Book className="w-4 h-4" />
                    Guides
                  </Link>
                </motion.li>
                <motion.li
                  whileHover={{ x: 5 }}
                  transition={{ type: "spring", stiffness: 400 }}
                >
                  <Link
                    href="/library_backup"
                    className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors"
                  >
                    <FileCog2Icon className="w-4 h-4" />
                    Backup Library
                  </Link>
                </motion.li>
              </ul>
            </div>

            <div>
              <h3 className="font-semibold mb-4 text-lg">Links</h3>
              <ul className="space-y-3">
                <motion.li
                  whileHover={{ x: 5 }}
                  transition={{ type: "spring", stiffness: 400 }}
                >
                  <Link
                    href="/about_updater"
                    className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors"
                  >
                    <RefreshCcwDot className="w-4 h-4" />
                    Updater
                  </Link>
                </motion.li>
                <motion.li
                  whileHover={{ x: 5 }}
                  transition={{ type: "spring", stiffness: 400 }}
                >
                  <Link
                    href="/download?version=latest"
                    className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors"
                  >
                    <Download className="w-4 h-4" />
                    Download
                  </Link>
                </motion.li>
                <motion.li
                  whileHover={{ x: 5 }}
                  transition={{ type: "spring", stiffness: 400 }}
                >
                  <a
                    href="https://github.com/mamiiblt/instafel"
                    className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors"
                    target="_blank"
                  >
                    <GithubIcon className="w-4 h-4" />
                    Source Code
                  </a>
                </motion.li>
              </ul>
            </div>
          </div>

          <div className="mt-8 pt-8 border-t border-border">
            <div className="flex flex-col md:flex-row justify-between items-center gap-4">
              <p className="text-sm text-muted-foreground">
                Developed with ❤️ by{" "}
                <a
                  href="https://github.com/mamiiblt"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:underline text-foreground"
                >
                  mamiiblt
                </a>
              </p>
              <div className="flex items-center gap-4">
                <motion.a
                  whileHover={{ y: -3 }}
                  href="https://mamiiblt.me/about"
                  className="text-muted-foreground hover:text-foreground transition-colors"
                  aria-label="About Developer"
                  target="_blank"
                >
                  <User className="w-5 h-5" />
                </motion.a>
                <motion.a
                  whileHover={{ y: -3 }}
                  href="https://github.com/mamiiblt"
                  className="text-muted-foreground hover:text-foreground transition-colors"
                  aria-label="GitHub"
                  target="_blank"
                >
                  <GithubIcon className="w-5 h-5" />
                </motion.a>
                <motion.a
                  whileHover={{ y: -3 }}
                  href="https://t.me/mamiiblt"
                  className="text-muted-foreground hover:text-foreground transition-colors"
                  aria-label="Telegram"
                  target="_blank"
                >
                  <Send className="w-5 h-5" />
                </motion.a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </motion.footer>
  );
}
