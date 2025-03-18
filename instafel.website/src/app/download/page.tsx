"use client";

import { AnimatePresence, motion } from "framer-motion";
import Link from "next/link";
import { Suspense, useEffect, useState } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import {
  Check,
  ChevronRight,
  Clock,
  Cpu,
  HardDrive,
  Laptop,
  LucideRefreshCw,
  LucideRocket,
  Server,
  Smartphone,
  Zap,
} from "lucide-react";
import { RadioGroup } from "@radix-ui/react-radio-group";
import { useSearchParams } from "next/navigation";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";

export default function DownloadPage() {
  return (
    <Suspense fallback={<LoadingBar />}>
      <DownloadPageContent />
    </Suspense>
  );
}

function DownloadPageContent() {
  const searchParams = useSearchParams();
  const version = searchParams.get("version");

  const [data, setData] = useState(null);
  useEffect(() => {
    if (version != null) {
      if (version != "latest") {
        setData(version);
      } else {
        const fetchData = async () => {
          const res = await fetch(
            "https://api.github.com/repos/mamiiblt/instafel_release_arm64-v8a/releases/latest"
          );
          const result = await res.json();
          setData(result.tag_name);
        };
        fetchData();
      }
    } else {
      setData(0);
    }
  }, []);

  return (
    <AnimatePresence>
      {data ? (
        <div>
          <div className="w-full">
            <CardHeader className="text-center pt-6 pb-2">
              <motion.div
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  duration: 0.8,
                  ease: "easeInOut",
                }}
              >
                <div className="mx-auto mb-4 p-3 rounded-full bg-primary/10 w-16 h-16 flex items-center justify-center">
                  <Cpu className="h-8 w-8 text-primary" />
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.3,
                  duration: 0.8,
                  ease: "easeInOut",
                }}
              >
                <CardTitle className="text-3xl font-bold">
                  Select Architecture
                </CardTitle>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.5,
                  duration: 0.8,
                  ease: "easeInOut",
                }}
              >
                <CardDescription className="text-lg max-w-xl mx-auto">
                  Instafel provides full support for new and old devices, you
                  need to select an architecture for download Instafel APKs
                </CardDescription>
              </motion.div>
            </CardHeader>

            <CardContent className="pt-6">
              <RadioGroup className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <motion.div
                  initial={{ opacity: 0, y: 50 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.8,
                    duration: 1,
                    ease: "easeInOut",
                  }}
                >
                  <Link
                    href={{
                      pathname: "/download_instafel",
                      query: {
                        version: data,
                        arch: "arm64",
                      },
                    }}
                  >
                    <div className="relative group">
                      <RadioGroupItem
                        value="64bit"
                        id="64bit"
                        className="peer sr-only"
                      />
                      <Label
                        htmlFor="64bit"
                        className="flex flex-col h-full rounded-xl border-2 border-muted bg-card p-6 transition-all duration-200 peer-data-[state=checked]:border-primary peer-data-[state=checked]:bg-primary/5 [&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-primary/5 cursor-pointer"
                      >
                        <div className="flex items-center gap-4 mb-4">
                          <div className="bg-primary/10 p-3 rounded-lg">
                            <LucideRocket className="h-8 w-8 text-primary" />
                          </div>
                          <div>
                            <h3 className="text-2xl font-bold">64-bit</h3>
                            <p className="text-muted-foreground">
                              Recommended for new phones
                            </p>
                          </div>
                        </div>

                        <div className="grid gap-4 mt-2">
                          <div className="flex items-center gap-3">
                            <LucideRefreshCw className="h-5 w-5 text-primary" />
                            <span>Gets all updates</span>
                          </div>
                          <div className="flex items-center gap-3">
                            <Zap className="h-5 w-5 text-primary" />
                            <span>Includes all features</span>
                          </div>
                          <div className="flex items-center gap-3">
                            <Server className="h-5 w-5 text-primary" />
                            <span>Compatible with new phones</span>
                          </div>
                        </div>
                      </Label>
                    </div>
                  </Link>
                </motion.div>

                <motion.div
                  initial={{ opacity: 0, y: 50 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 1.1,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                >
                  <Link
                    href={{
                      pathname: "/download_instafel",
                      query: {
                        version: data,
                        arch: "arm32",
                      },
                    }}
                  >
                    <div className="relative group">
                      <RadioGroupItem
                        value="32bit"
                        id="32bit"
                        className="peer sr-only"
                      />
                      <Label
                        htmlFor="32bit"
                        className="flex flex-col h-full rounded-xl border-2 border-muted bg-card p-6 transition-all duration-200 peer-data-[state=checked]:border-primary peer-data-[state=checked]:bg-primary/5 [&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-primary/5 cursor-pointer"
                      >
                        <div className="flex items-center gap-4 mb-4">
                          <div className="bg-primary/10 p-3 rounded-lg">
                            <Clock className="h-8 w-8 text-primary" />
                          </div>
                          <div>
                            <h3 className="text-2xl font-bold">32-bit</h3>
                            <p className="text-muted-foreground">
                              Recommended for old phones
                            </p>
                          </div>
                        </div>

                        <div className="grid gap-4 mt-2">
                          <div className="flex items-center gap-3">
                            <LucideRefreshCw className="h-5 w-5 text-primary" />
                            <span>Gets all updates</span>
                          </div>
                          <div className="flex items-center gap-3">
                            <Zap className="h-5 w-5 text-primary" />
                            <span>Includes all features</span>
                          </div>
                          <div className="flex items-center gap-3">
                            <Smartphone className="h-5 w-5 text-primary" />
                            <span>Competible with old phones</span>
                          </div>
                        </div>
                      </Label>
                    </div>
                  </Link>
                </motion.div>
              </RadioGroup>

              <motion.div
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 1.2,
                  duration: 1,
                  ease: "easeInOut",
                }}
              >
                <div className="space-y-3 text-sm mt-6">
                  <div className="bg-card p-3  rounded-xl border-2 border-muted bg-card ">
                    <h5 className="font-medium mb-1">
                      How do I find out which architecture I have?
                    </h5>
                    <ol className="list-decimal pl-5 space-y-1">
                      <li>Download CPU-Z from the Play Store</li>
                      <li>Go to the System tab</li>
                      <li>
                        You can find your system ABIs (Architecture) from here
                      </li>
                    </ol>
                  </div>
                </div>{" "}
              </motion.div>
            </CardContent>
          </div>
          <Footer />
        </div>
      ) : (
        <LoadingBar />
      )}
    </AnimatePresence>
  );
}
