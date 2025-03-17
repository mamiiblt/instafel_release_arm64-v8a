"use client";
import React from "react";
import * as Icons from "@/components/Icons";
import Link from "next/link";
import { SITE_CONFIG } from "@/config/config";
import CustomSocialLinks from "./CustomSocialLinks";
import { FooterLoading } from "./loading";
import { motion } from "framer-motion";
import { usePathname } from "next/navigation";
import Image from "next/image";
import {
  Book,
  Download,
  FileCog2Icon,
  Github,
  GithubIcon,
  Link2,
  LinkIcon,
  LucideFlag,
  RefreshCcwDot,
  Send,
  User,
} from "lucide-react";

export default function Footer() {
  const [loading, setLoading] = React.useState(true);
  const pathname = usePathname();

  React.useEffect(() => {
    setLoading(false);
  }, []);

  if (loading) {
    return <FooterLoading />;
  }

  const container = {
    hidden: { opacity: 0 },
    show: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const item = {
    hidden: { opacity: 0, y: 20 },
    show: { opacity: 1, y: 0 },
  };

  return (
    <div>
      {pathname !== "/about" && (
        <footer className="relative border-t border-border g-gradient-to-b from-background to-background/50 backdrop-blur-sm">
          <div className="absolute inset-0 pointer-events-none" />

          <div className="relative container mx-auto px-4 pt-8 md:pt-8 pb-8 md:pb-8">
            <div className="max-w-6xl mx-auto">
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="space-y-4">
                  <div className="flex items-center gap-2">
                    <div className="w-10 h-10 rounded-xl bg-instafelcolor flex items-center justify-center">
                      <Icons.Instagram className="text-black font-bold text-xl" />
                    </div>
                    <span className="font-bold text-xl text-black">
                      Instafel
                    </span>
                  </div>
                  <p className="text-muted-foreground text-sm">
                    Provides fast & up-to-date Instagram Alpha experience
                  </p>
                </div>

                <div>
                  <h3 className="font-semibold mb-4 text-lg">Quick Links</h3>
                  <ul className="space-y-3">
                    <li>
                      <a
                        href="/guides"
                        className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors group"
                      >
                        <Book className="w-4 h-4 transition-colors" />
                        Guide
                      </a>
                    </li>
                    <li>
                      <a
                        href="/about_updater"
                        className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors group"
                      >
                        <RefreshCcwDot className="w-4 h-4 transition-colors" />
                        Updater
                      </a>
                    </li>
                    <li>
                      <a
                        href="/library_backup"
                        className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors group"
                      >
                        <FileCog2Icon className="w-4 h-4 transition-colors" />
                        Backup Library
                      </a>
                    </li>
                    <li>
                      <a
                        href="/library_flag"
                        className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors group"
                      >
                        <LucideFlag className="w-4 h-4 transition-colors" />
                        Flag Library
                      </a>
                    </li>
                    <li>
                      <a
                        href="/download?version=latest"
                        className="text-muted-foreground hover:text-foreground flex items-center gap-2 transition-colors group"
                      >
                        <Download className="w-4 h-4 transition-colors" />
                        Download
                      </a>
                    </li>
                  </ul>
                </div>
              </div>

              <div className="mt-8 pt-8 border-t border-t border-border ">
                <div className="flex flex-col md:flex-row justify-between items-center gap-4">
                  <p className="text-sm">Developed with ❤️ by mamiiblt</p>
                  <div className="flex items-center gap-4">
                    <a
                      href="https://mamiiblt.me/about"
                      className="text-gray-400 "
                      aria-label="GitHub"
                    >
                      <User className="w-5 h-5" />
                    </a>
                    <a
                      href="https://github.com/mamiiblt"
                      className="text-gray-400 "
                      aria-label="GitHub"
                    >
                      <GithubIcon className="w-5 h-5" />
                    </a>
                    <a
                      href="https://t.me/mamiiblt"
                      className="text-gray-400"
                      aria-label="Telegram"
                    >
                      <Send className="w-5 h-5" />
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </footer>
      )}
    </div>
  );
}
