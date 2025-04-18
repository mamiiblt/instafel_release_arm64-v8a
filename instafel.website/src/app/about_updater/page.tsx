"use client";

import { AnimatePresence, motion } from "framer-motion";
import Link from "next/link";
import { Card, CardContent } from "@/components/ui/card";
import {
  AudioLines,
  CheckCircle,
  Download,
  GitBranch,
  PawPrint,
  Shield,
  Smartphone,
  Zap,
  RefreshCcwDot,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";
import Image from "next/image";

interface Asset {
  name: string;
  browser_download_url: string;
}

interface Release {
  tag_name: string;
  assets: Asset[];
}

export default function UpdaterPage() {
  const [apkUrl, setApkUrl] = useState<string | null>(null);
  const [versionTag, setVersionTag] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchApkUrl = async () => {
      try {
        const response = await fetch(
          "https://api.github.com/repos/mamiiblt/instafel-updater/releases/latest",
        );
        const data: Release = await response.json();
        setVersionTag(data.tag_name);

        const apkAsset = data.assets.find((asset) =>
          asset.name.endsWith(".apk"),
        );
        setApkUrl(apkAsset ? apkAsset.browser_download_url : null);
      } catch (error) {
        console.error("Failed to fetch updater information:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchApkUrl();
  }, []);

  if (isLoading) {
    return <LoadingBar />;
  }

  return (
    <AnimatePresence>
      {versionTag ? (
        <div>
          <div className="min-h-screen py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-4xl mx-auto">
              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6 }}
                className="text-center mb-16"
              >
                <div className="flex justify-center mb-6">
                  <div className="relative">
                    <div
                      className="absolute inset-0 rounded-full bg-primary/20 animate-ping"
                      style={{ animationDuration: "3s" }}
                    ></div>
                    <div className="relative bg-primary/30 p-5 rounded-full">
                      <RefreshCcwDot className="h-12 w-12 text-primary" />
                    </div>
                  </div>
                </div>

                <h1 className="text-4xl font-extrabold tracking-tight sm:text-5xl md:text-6xl">
                  Instafel <span className="text-primary">Updater</span>
                </h1>
                <h2 className="text-2xl font-bold mt-2 text-foreground/80">
                  Automatic updates, zero effort
                </h2>
                <p className="mt-6 max-w-2xl mx-auto text-lg text-muted-foreground">
                  Keep Instafel always up-to-date with this powerful companion
                  app that handles updates automatically through Shizuku or
                  Root.
                </p>

                <div className="mt-10 flex flex-col sm:flex-row gap-4 justify-center">
                  <Button
                    asChild
                    size={"lg"}
                    variant="default"
                    className="relative overflow-hidden group"
                  >
                    <Link href={apkUrl || "#"}>
                      <span className="absolute inset-0 bg-gradient-to-r from-primary/30 to-primary/0 group-hover:opacity-50 opacity-0 transition-opacity" />
                      <Download className="mr-2" />
                      Download {versionTag}
                    </Link>
                  </Button>
                  <Button
                    asChild
                    size={"lg"}
                    variant="outline-gradient"
                    className="group"
                  >
                    <Link href="https://github.com/mamiiblt/instafel-updater">
                      <GitBranch className="mr-2 group-hover:rotate-12 transition-transform duration-300" />
                      Source Code
                    </Link>
                  </Button>
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3, duration: 0.6 }}
                className="mb-16"
              >
                <h2 className="text-2xl font-bold text-center mb-8">
                  How It Works
                </h2>

                <div className="grid md:grid-cols-3 gap-4">
                  {[
                    {
                      step: "1",
                      title: "Install & Grant Permissions",
                      description:
                        "Install the app and grant Shizuku or Root permissions.",
                      icon: <Shield className="h-6 w-6 text-emerald-500" />,
                    },
                    {
                      step: "2",
                      title: "Configure Settings",
                      description:
                        "Choose your update preferences and check frequency.",
                      icon: <Smartphone className="h-6 w-6 text-blue-500" />,
                    },
                    {
                      step: "3",
                      title: "Automatic Updates",
                      description:
                        "Relax and let Updater handle everything for you.",
                      icon: <Zap className="h-6 w-6 text-amber-500" />,
                    },
                  ].map((item, index) => (
                    <Card
                      key={index}
                      className="relative overflow-hidden border-2 hover:border-primary/50 transition-colors group"
                    >
                      <div className="absolute -right-4 -top-4 bg-gray-100 dark:bg-gray-800 rounded-full w-12 h-12 flex items-center justify-center text-lg font-bold opacity-70">
                        {item.step}
                      </div>
                      <CardContent className="pt-6">
                        <div className="mb-4 rounded-full bg-primary/10 p-3 w-fit group-hover:bg-primary/20 transition-colors">
                          {item.icon}
                        </div>
                        <h3 className="text-lg font-semibold mb-2">
                          {item.title}
                        </h3>
                        <p className="text-muted-foreground">
                          {item.description}
                        </p>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.5, duration: 0.6 }}
              >
                <h2 className="text-2xl font-bold text-center mb-8">
                  Key Features
                </h2>
                <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3 mb-16">
                  {[
                    {
                      icon: <AudioLines className="h-8 w-8 text-primary" />,
                      title: "Silent Installation",
                      description:
                        "Runs in the background to automatically download and install the latest versions from our API.",
                    },
                    {
                      icon: <PawPrint className="h-8 w-8 text-primary" />,
                      title: "Shizuku Support",
                      description:
                        "Works on non-rooted phones with Shizuku without requiring any additional root managers.",
                    },
                    {
                      icon: <GitBranch className="h-8 w-8 text-primary" />,
                      title: "Open Source",
                      description:
                        "Completely transparent code so you can trust the app with the necessary permissions.",
                    },
                  ].map((feature, index) => (
                    <motion.div
                      key={index}
                      initial={{ opacity: 0, y: 30 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: 0.6 + index * 0.2, duration: 0.6 }}
                      whileHover={{ y: -5 }}
                    >
                      <Card className="flex flex-col items-center text-center p-6 h-full border-2 hover:border-primary/40 transition-colors">
                        <div className="mb-4 rounded-full bg-primary/10 p-4">
                          {feature.icon}
                        </div>
                        <h3 className="text-lg font-semibold mb-3">
                          {feature.title}
                        </h3>
                        <p className="text-muted-foreground">
                          {feature.description}
                        </p>
                      </Card>
                    </motion.div>
                  ))}
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.7, duration: 0.6 }}
                className="mb-16"
              >
                <div className="bg-gradient-to-r from-gray-100/50 to-gray-50/50 dark:from-gray-800/50 dark:to-gray-900/50 rounded-2xl p-8">
                  <div className="flex flex-col md:flex-row items-center">
                    <div className="md:w-1/2 mb-6 md:mb-0">
                      <h2 className="text-2xl font-bold mb-4">
                        Why Use Updater?
                      </h2>
                      <ul className="space-y-4">
                        {[
                          "Always have the latest patches and features automatically",
                          "Save time with background updates instead of manual OTA checks",
                          "Lightweight and efficient with minimal resource usage",
                          "Get notified only when updates are successfully installed",
                          "Works seamlessly with both Shizuku and Root methods",
                        ].map((benefit, index) => (
                          <li key={index} className="flex items-start">
                            <CheckCircle className="h-5 w-5 text-green-500 mr-2 shrink-0 mt-0.5" />
                            <span>{benefit}</span>
                          </li>
                        ))}
                      </ul>
                    </div>
                    {/* <div className="md:w-1/2 flex justify-center">
                      <div className="relative max-w-[220px]">
                        <div className="absolute -inset-4 bg-gradient-to-r from-primary/20 via-primary/10 to-primary/20 rounded-3xl blur-xl opacity-70"></div>
                        <div className="relative rounded-3xl overflow-hidden border-8 border-gray-800 shadow-lg">
                          <Image
                            src="/updater-mockup.jpg"
                            alt="Instafel Updater app screenshot"
                            width={220}
                            height={440}
                            className="w-full"
                          />
                        </div>
                      </div>
                    </div> */}
                  </div>
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.9, duration: 0.6 }}
                className="text-center bg-gradient-to-r from-primary/5 to-primary/10 p-8 rounded-2xl"
              >
                <h2 className="text-2xl font-bold mb-4">Get Started Now</h2>
                <p className="text-lg text-muted-foreground mb-6">
                  Keep your Instafel experience up-to-date with minimum effort.
                  Download the updater now.
                </p>
                <Button
                  asChild
                  size="lg"
                  variant="default"
                  className="px-8 py-6 text-lg shadow-lg hover:shadow-xl transition-shadow"
                >
                  <Link href={apkUrl || "#"}>
                    <Download className="mr-2 h-5 w-5" />
                    Download Updater {versionTag}
                  </Link>
                </Button>
              </motion.div>
            </div>
          </div>
          <Footer />
        </div>
      ) : (
        <div className="py-12 px-6 text-center">
          <Card className="max-w-md mx-auto p-6">
            <h2 className="text-xl font-bold mb-4">Download Error</h2>
            <p className="text-muted-foreground mb-6">
              We couldn&apos;t retrieve the latest version information. Please
              check your connection or try again later.
            </p>
            <Button onClick={() => window.location.reload()}>Retry</Button>
          </Card>
        </div>
      )}
    </AnimatePresence>
  );
}
