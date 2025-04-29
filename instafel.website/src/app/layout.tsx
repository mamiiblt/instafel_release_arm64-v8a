"use client";

import { Geist } from "next/font/google";
import "./globals.css";
import { ThemeProvider } from "@/components/providers/theme-provider";
import Navbar from "@/components/Navbar";
import { SITE_CONFIG } from "@/config/config";
import { Toaster } from "@/components/ui/toaster";
import { Suspense, useEffect, useState } from "react";
const appleTitle = SITE_CONFIG.siteName;
const geist = Geist({
  subsets: ["latin"],
});

import "@/lib/i18n"; // we need to include it in RootLayout
import i18n from "i18next";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [currentLang, setCurrentLang] = useState("en");

  useEffect(() => {
    setCurrentLang(i18n.language);

    const handleLanguageChange = (lng: string) => {
      setCurrentLang(lng);
    };

    i18n.on("languageChanged", handleLanguageChange);

    return () => {
      i18n.off("languageChanged", handleLanguageChange);
    };
  }, []);

  return (
    <html lang={currentLang} suppressHydrationWarning>
      <head>
        <meta name="apple-mobile-web-app-title" content={appleTitle} />
      </head>
      <body
        className={`${geist.className} flex min-h-screen flex-col bg-background text-foreground`}
      >
        <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
          <Navbar />
          <Toaster />
          <main className="flex-1">{children}</main>
        </ThemeProvider>
      </body>
    </html>
  );
}
