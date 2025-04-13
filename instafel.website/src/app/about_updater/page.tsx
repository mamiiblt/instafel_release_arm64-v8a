"use client";

import { AnimatePresence, motion } from "framer-motion";
import Link from "next/link";
import { Card } from "@/components/ui/card";
import {
  AudioLines,
  CheckCircle,
  Download,
  GitBranch,
  PawPrint,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";

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

  useEffect(() => {
    const fetchApkUrl = async () => {
      const response = await fetch(
        "https://api.github.com/repos/mamiiblt/instafel-updater/releases/latest"
      );
      const data: Release = await response.json();
      setVersionTag(data.tag_name);

      const apkAsset = data.assets.find((asset) => asset.name.endsWith(".apk"));
      setApkUrl(apkAsset ? apkAsset.browser_download_url : null);
    };

    fetchApkUrl();
  }, []);

  if (!apkUrl) {
    return <LoadingBar />;
  }
  return (
    <AnimatePresence>
      {versionTag ? (
        <div>
          <div className="min-h-screen bg-primary-foreground dark:bg-primary-background py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-4xl mx-auto">
              <div className="text-center mb-16">
                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                >
                  <h1 className="text-4xl font-extrabold tracking-tight sm:text-5xl md:text-6xl">
                    Stay updated
                  </h1>
                  <h2 className="text-3xl font-bold tracking-tight sm:text-3xl md:text-4xl">
                    with Updater
                  </h2>
                </motion.div>
                <motion.p
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.3,
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                  className="mt-3 max-w-md mx-auto text-xl text-muted-foreground sm:text-2xl md:mt-5 md:max-w-3xl"
                >
                  Automatically update Instafel with Shizuku and Root as an
                  additional alternative to the in-app OTA system
                </motion.p>
                <div className="mt-10">
                  <motion.div
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{
                      delay: 0.6,
                      duration: 0.8,
                      ease: "easeOut",
                    }}
                  >
                    <div className="mb-16">
                      <Button
                        asChild
                        size={"lg"}
                        variant="default"
                        className="w-full mb-4 md:mr-5 sm:w-auto transform hover:scale-105 transition-transform duration-200"
                      >
                        <Link href={apkUrl}>
                          <Download className="mr-2" />
                          Download {versionTag}
                        </Link>
                      </Button>
                      <Button
                        asChild
                        size={"lg"}
                        variant="outline-gradient"
                        className="w-full sm:w-auto transform hover:scale-105 transition-transform duration-200"
                      >
                        <Link href="https://github.com/mamiiblt/instafel-updater">
                          <GitBranch className="mr-2" />
                          Source Code
                        </Link>
                      </Button>
                    </div>
                  </motion.div>
                </div>
              </div>

              {/* Key Features */}
              <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3 mb-16">
                {[
                  {
                    delay: 0.8,
                    icon: <AudioLines className="h-8 w-8 text-primary" />,
                    title: "Slient Installation",
                    description:
                      "Updater runs full-time in the background for automatically downloading and installing the latest versions from API.",
                  },
                  {
                    delay: 0.9,
                    icon: <PawPrint className="h-8 w-8 text-primary" />,
                    title: "Shizuku Support",
                    description:
                      "Non-rooted phones can also use Instafel Updater with Shizuku without any Root managers!",
                  },
                  {
                    delay: 1,
                    icon: <GitBranch className="h-8 w-8 text-primary" />,
                    title: "Open Source",
                    description:
                      "Instafel Updater is completely open source, so you don't have to worry about granting the necessary permissions!",
                  },
                ].map((feature, index) => (
                  <motion.div
                    key={index}
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{
                      delay: feature.delay,
                      duration: 0.8,
                      ease: "easeOut",
                    }}
                  >
                    <Card className="flex flex-col items-center text-center p-6">
                      <div className="mb-4 rounded-full bg-primary/10 p-3">
                        {feature.icon}
                      </div>
                      <h3 className="text-lg font-semibold mb-2">
                        {feature.title}
                      </h3>
                      <p className="text-muted-foreground">
                        {feature.description}
                      </p>
                    </Card>
                  </motion.div>
                ))}
              </div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 1.2,
                  duration: 0.8,
                  ease: "easeOut",
                }}
              >
                <div className="mb-16">
                  <h2 className="text-3xl font-bold text-center mb-8">
                    Advantages of Updater
                  </h2>
                  <ul className="space-y-4">
                    {[
                      "Always have the latest patches and featues!",
                      "Save time - no need to manually update with in-app OTA",
                      "A lightweight app that is efficient with system resources",
                    ].map((benefit, index) => (
                      <li key={index} className="flex items-start">
                        <CheckCircle className="h-6 w-6 text-primary mr-2 flex-shrink-0" />
                        <span>{benefit}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 1.4,
                  duration: 0.8,
                  ease: "easeOut",
                }}
              >
                {" "}
                <div className="text-center">
                  <h2 className="text-3xl font-bold mb-4">Download Now</h2>
                  <p className="text-xl text-muted-foreground mb-8">
                    Install Instafel Updater now and ensure Instafel is always
                    up-to-date.
                  </p>
                  <Link href={apkUrl}>
                    <Button
                      variant="outline-gradient"
                      size="lg"
                      className="px-8 py-3 text-lg"
                    >
                      <Download className="mr-2 h-5 w-5" />
                      Download {versionTag}
                    </Button>
                  </Link>
                </div>
              </motion.div>
            </div>
          </div>
          <Footer />
        </div>
      ) : (
        <LoadingBar />
      )}
    </AnimatePresence>
  );
}
