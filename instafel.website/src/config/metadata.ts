import { Metadata } from "next";
import { SITE_CONFIG } from "./config";

const siteUrl = process.env.NEXT_PUBLIC_SITE_URL || "";

if (!siteUrl) {
  throw new Error("NEXT_PUBLIC_SITE_URL is not defined");
}

export const defaultMetadata: Metadata = {
  metadataBase: new URL(siteUrl),
  title: {
    default: `${SITE_CONFIG.name}`,
    template: `%s`,
  },
  description: SITE_CONFIG.description,
  keywords: [
    "Next.js",
    "React",
    "JavaScript",
    "TypeScript",
    "Web Development",
    "Blog",
    "Portfolio",
  ],
  authors: [
    {
      name: SITE_CONFIG.name,
      url: siteUrl,
    },
  ],
  creator: SITE_CONFIG.name,
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-video-preview": -1,
      "max-image-preview": "large",
      "max-snippet": -1,
    },
  },
  icons: {
    icon: "/favicon.ico",
    shortcut: "/favicon-16x16.png",
    apple: "/apple-touch-icon.png",
    other: [
      {
        rel: "icon",
        type: "image/png",
        sizes: "32x32",
        url: "/favicon-32x32.png",
      },
      {
        rel: "icon",
        type: "image/png",
        sizes: "16x16",
        url: "/favicon-16x16.png",
      },
      {
        rel: "manifest",
        url: "/site.webmanifest",
      },
    ],
  },
  manifest: "/site.webmanifest",
};
