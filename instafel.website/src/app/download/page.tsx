"use client";

import React from "react";
import { AnimatePresence, motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Suspense, useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

import {
  ChevronRight,
  Download,
  FileText,
  Info,
  Shapes,
  Calendar,
  Code,
  CheckCircle,
  ShieldCheck,
  HardDrive,
  Smartphone,
} from "lucide-react";
import { useSearchParams } from "next/navigation";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";

interface InstafelData {
  build_date: string | null;
  gen_id: string | null;
  app: {
    ifl_version: string | null;
    version_name: string | null;
    version_code: string | null;
  };
  hash: {
    uc: string | null;
    c: string | null;
  };
  download_urls: {
    unclone: string | null;
    clone: string | null;
  };
  patcher: {
    version: string | null;
    commit: string | null;
  };
  changelogs: string[] | null;
}

interface GithubAsset {
  name: string;
  browser_download_url: string;
}

interface GithubRelease {
  body: string;
  status?: string;
  assets: GithubAsset[];
}

export default function DownloadInstafelPage() {
  return (
    <Suspense fallback={<LoadingBar />}>
      <DownloadIflContent />
    </Suspense>
  );
}

function DownloadIflContent() {
  const [activeTab, setActiveTab] = useState<string>("download");

  const searchParams = useSearchParams();
  const version = searchParams.get("version");

  const [data, setData] = useState<InstafelData | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [downloadStarted, setDownloadStarted] = useState<{
    unclone: boolean;
    clone: boolean;
  }>({ unclone: false, clone: false });

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      let requestUrl = "";
      if (version === "latest") {
        requestUrl =
          "https://api.github.com/repos/mamiiblt/instafel/releases/latest";
      } else {
        requestUrl =
          "https://api.github.com/repos/mamiiblt/instafel/releases/tags/" +
          version;
      }
      try {
        const res = await fetch(requestUrl);
        const result: GithubRelease = await res.json();

        const values: InstafelData = {
          build_date: null,
          gen_id: null,
          app: {
            ifl_version: null,
            version_name: null,
            version_code: null,
          },
          hash: {
            uc: null,
            c: null,
          },
          download_urls: {
            unclone: null,
            clone: null,
          },
          patcher: {
            version: null,
            commit: null,
          },
          changelogs: null,
        };

        if (result.status !== "Not Found") {
          const releaseBody = result.body.split("\n");
          let changeLogs: string[] = [];

          releaseBody.forEach((line: string) => {
            if (
              !line.startsWith("|") &&
              line !== "" &&
              line !== "# Changelog"
            ) {
              changeLogs.push(line.trim().substring(2));
            } else {
              const lineParts = line.split("|");
              for (let i = 0; i < lineParts.length; i++) {
                const part = lineParts[i].trim();
                if (
                  !part.includes("PROPERTY") &&
                  !part.includes("VALUE") &&
                  !part.includes("Changelog") &&
                  !part.includes("-------------") &&
                  !(part.length === 1)
                ) {
                  const nextValue = lineParts[i + 1]?.trim();
                  switch (part) {
                    case "build_date":
                      values.build_date = nextValue;
                      break;
                    case "gen_id":
                      values.gen_id = nextValue;
                      break;
                    case "app.ifl_version":
                      values.app.ifl_version = nextValue;
                      break;
                    case "app.version_name":
                      values.app.version_name = nextValue;
                      break;
                    case "app.version_code":
                      values.app.version_code = nextValue;
                      break;
                    case "hash.uc":
                      values.hash.uc = nextValue;
                      break;
                    case "hash.c":
                      values.hash.c = nextValue;
                      break;
                    case "patcher.version":
                      values.patcher.version = nextValue;
                      break;
                    case "patcher.commit":
                      values.patcher.commit = nextValue;
                      break;
                  }
                }
              }
            }
          });

          result.assets.forEach((asset: GithubAsset) => {
            if (asset.name.includes("instafel_uc")) {
              values.download_urls.unclone = asset.browser_download_url;
            }

            if (asset.name.includes("instafel_c")) {
              values.download_urls.clone = asset.browser_download_url;
            }
          });
          values.changelogs = changeLogs;
          setData(values);
        } else {
          setData(null);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        setData(null);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [version]);

  const download = (url: string | null, type: "unclone" | "clone"): void => {
    if (!url) return;

    // Set the download state
    setDownloadStarted((prev) => ({ ...prev, [type]: true }));

    // Create a temporary link
    const link = document.createElement("a");
    link.href = url;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    // Reset the download state after animation completes
    setTimeout(() => {
      setDownloadStarted((prev) => ({ ...prev, [type]: false }));
    }, 2000);
  };

  return (
    <AnimatePresence>
      {loading ? (
        <LoadingBar />
      ) : (
        <div className="min-h-screen flex flex-col bg-background">
          <div className="container mx-auto px-2 sm:px-4 md:px-8 pt-8 sm:pt-12 md:pt-16 lg:pt-20 pb-32 sm:pb-12 flex-grow">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{
                duration: 0.7,
                ease: "easeOut",
              }}
              className="text-center mb-8 sm:mb-12"
            >
              <div className="flex justify-center mb-4 sm:mb-6">
                <div className="relative">
                  <div
                    className="absolute inset-0 rounded-full bg-primary/20 animate-ping"
                    style={{ animationDuration: "3s" }}
                  ></div>
                  <div className="relative bg-primary/30 p-3 sm:p-4 md:p-5 rounded-full">
                    <Download className="h-6 w-6 sm:h-8 sm:w-8 md:h-12 md:w-12 text-primary" />
                  </div>
                </div>
              </div>
              <h1 className="text-2xl xs:text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-bold tracking-tight mb-3 sm:mb-4">
                Download <span className="text-primary">Instafel</span>
              </h1>
              <motion.p
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.2, duration: 0.7 }}
                className="text-base xs:text-lg sm:text-xl md:text-2xl text-muted-foreground max-w-2xl mx-auto"
              >
                {data
                  ? `Version ${data.app.ifl_version}`
                  : "Choose your version"}
              </motion.p>
            </motion.div>

            {data && (
              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.4, duration: 0.7 }}
                className="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-6 lg:gap-8 mb-8 sm:mb-12 "
              >
                {[
                  {
                    title: "Standard Version",
                    subtitle: "Unclone Variant",
                    description:
                      "Replace the standard Instagram app with Instafel. Original app must be uninstalled.",
                    icon: (
                      <Smartphone className="h-5 w-5 sm:h-6 sm:w-6 text-primary" />
                    ),
                    downloadUrl: data.download_urls.unclone,
                    type: "unclone" as const,
                    benefits: [
                      "Full feature set",
                      "Better performance",
                      "Recommended for most users",
                    ],
                    delay: 0.5,
                  },
                  {
                    title: "Clone Version",
                    subtitle: "Clone Variant",
                    description:
                      "Install alongside the original Instagram app. Perfect for testing while keeping your original app.",
                    icon: (
                      <Shapes className="h-5 w-5 sm:h-6 sm:w-6 text-primary" />
                    ),
                    downloadUrl: data.download_urls.clone,
                    type: "clone" as const,
                    benefits: [
                      "Keep original Instagram",
                      "Test new features",
                      "Separate accounts",
                    ],
                    delay: 0.6,
                  },
                ].map((variant) => (
                  <motion.div
                    key={variant.title}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: variant.delay, duration: 0.5 }}
                    whileHover={{ scale: 1.025 }}
                    className="relative"
                  >
                    <Card className="h-full border-2 hover:border-primary/70 transition-all duration-300 overflow-hidden shadow-lg bg-card/80">
                      <CardContent className="p-0">
                        <div className="p-4 sm:p-6 pb-0">
                          <div className="flex items-center gap-3 sm:gap-4 mb-3 sm:mb-4">
                            <div className="p-2 sm:p-3 rounded-full bg-gradient-to-r from-primary/20 to-primary/5">
                              {variant.icon}
                            </div>
                            <div>
                              <h3 className="text-lg sm:text-xl font-bold">
                                {variant.title}
                              </h3>
                              <p className="text-primary text-xs sm:text-sm font-medium">
                                {variant.subtitle}
                              </p>
                            </div>
                          </div>

                          <p className="text-xs sm:text-sm md:text-base text-muted-foreground mb-4 sm:mb-5">
                            {variant.description}
                          </p>
                        </div>

                        <div className="px-4 sm:px-6 pb-3 sm:pb-4">
                          <h4 className="text-xs sm:text-sm font-medium mb-2 flex items-center">
                            <CheckCircle className="h-3.5 w-3.5 sm:h-4 sm:w-4 mr-1.5 text-primary" />
                            Benefits
                          </h4>
                          <ul className="space-y-0.5 sm:space-y-1 mb-5 sm:mb-6">
                            {variant.benefits.map((benefit, benefitIndex) => (
                              <li
                                key={benefitIndex}
                                className="text-xs sm:text-sm flex items-center"
                              >
                                <ChevronRight className="h-3 w-3 sm:h-3.5 sm:w-3.5 text-primary/70 mr-1" />
                                {benefit}
                              </li>
                            ))}
                          </ul>
                        </div>

                        <div className="bg-gradient-to-r from-primary/5 to-primary/10 p-4 sm:p-6">
                          <Button
                            size="default"
                            className={`w-full relative overflow-hidden ${downloadStarted[variant.type] ? "bg-green-600 hover:bg-green-700" : ""}`}
                            onClick={() =>
                              download(variant.downloadUrl, variant.type)
                            }
                            disabled={!variant.downloadUrl}
                          >
                            {downloadStarted[variant.type] ? (
                              <>
                                <motion.div
                                  initial={{ width: 0 }}
                                  animate={{ width: "100%" }}
                                  transition={{ duration: 1.5 }}
                                  className="absolute left-0 top-0 bottom-0 bg-green-500/20"
                                />
                                <span className="relative z-10 flex items-center text-xs sm:text-sm md:text-base">
                                  <svg
                                    className="w-3 h-3 sm:w-4 sm:h-4 md:w-5 md:h-5 mr-1 sm:mr-1.5 md:mr-2"
                                    viewBox="0 0 24 24"
                                  >
                                    <path
                                      fill="currentColor"
                                      d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
                                    />
                                  </svg>
                                  Download Started
                                </span>
                              </>
                            ) : (
                              <span className="flex items-center text-xs sm:text-sm md:text-base">
                                <Download className="mr-1 sm:mr-1.5 md:mr-2 h-3 w-3 sm:h-4 sm:w-4 md:h-5 md:w-5" />
                                Download {variant.subtitle}
                              </span>
                            )}
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  </motion.div>
                ))}
              </motion.div>
            )}

            {data && (
              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.7, duration: 0.7 }}
                className="mt-8 sm:mt-10"
              >
                <Card className="shadow-md border-2 bg-card/90 transition-all duration-300">
                  <CardContent className="p-0">
                    <Tabs
                      defaultValue="download"
                      value={activeTab}
                      onValueChange={setActiveTab}
                      className="w-full"
                    >
                      <div className="border-b p-4 sm:p-5">
                        <h2 className="text-lg sm:text-xl md:text-2xl font-semibold mb-4 flex items-center gap-2">
                          <Info className="h-5 w-5 sm:h-6 sm:w-6 text-primary" />
                          Release Information
                        </h2>
                        <TabsList className="grid w-full grid-cols-3 h-auto">
                          <TabsTrigger
                            value="download"
                            className="py-2.5 text-xs sm:text-sm md:text-base"
                          >
                            <span className="flex items-center gap-1.5">
                              <Download className="h-3.5 w-3.5 sm:h-4 sm:w-4" />
                              <span className="hidden xs:inline">
                                Download
                              </span>{" "}
                              Details
                            </span>
                          </TabsTrigger>
                          <TabsTrigger
                            value="changelog"
                            className="py-2.5 text-xs sm:text-sm md:text-base"
                          >
                            <span className="flex items-center gap-1.5">
                              <FileText className="h-3.5 w-3.5 sm:h-4 sm:w-4" />
                              Changelog
                            </span>
                          </TabsTrigger>
                          <TabsTrigger
                            value="build"
                            className="py-2.5 text-xs sm:text-sm md:text-base"
                          >
                            <span className="flex items-center gap-1.5">
                              <Code className="h-3.5 w-3.5 sm:h-4 sm:w-4" />
                              Build Info
                            </span>
                          </TabsTrigger>
                        </TabsList>
                      </div>

                      <div className="p-4 sm:p-5 md:p-6">
                        <TabsContent
                          value="download"
                          className="mt-0 space-y-4"
                        >
                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
                            {[
                              {
                                title: "Standard Version",
                                badge: "Recommended",
                                badgeVariant: "default",
                                description:
                                  "Replace the original Instagram app with Instafel for the best experience.",
                                steps: [
                                  "Uninstall the original Instagram app",
                                  "Install this APK file",
                                  "Login with your Instagram credentials",
                                ],
                                buttonText: "Download Standard Version",
                                onClick: () =>
                                  download(
                                    data.download_urls.unclone,
                                    "unclone",
                                  ),
                                icon: <Smartphone className="h-5 w-5" />,
                              },
                              {
                                title: "Clone Version",
                                badge: "Alternative",
                                badgeVariant: "outline",
                                description:
                                  "Install alongside the original Instagram app. Great for testing.",
                                steps: [
                                  "Keep your original Instagram app",
                                  "Install this APK file",
                                  "Both apps will work independently",
                                ],
                                buttonText: "Download Clone Version",
                                onClick: () =>
                                  download(data.download_urls.clone, "clone"),
                                icon: <Shapes className="h-5 w-5" />,
                              },
                            ].map((variant, idx) => (
                              <div
                                key={idx}
                                className="border rounded-xl overflow-hidden transition-all bg-card/50 hover:shadow-md hover:border-primary/30"
                              >
                                <div className="bg-muted/50 p-4">
                                  <div className="flex justify-between items-center">
                                    <div className="flex items-center gap-2.5">
                                      <div className="p-2 rounded-md bg-primary/10 text-primary">
                                        {variant.icon}
                                      </div>
                                      <h3 className="font-bold text-base sm:text-lg">
                                        {variant.title}
                                      </h3>
                                    </div>
                                    <Badge
                                      variant={
                                        variant.badgeVariant as
                                          | "default"
                                          | "outline"
                                      }
                                      className={`${variant.badgeVariant === "outline" ? "border-muted-foreground/50" : ""}`}
                                    >
                                      {variant.badge}
                                    </Badge>
                                  </div>
                                </div>

                                <div className="p-4">
                                  <p className="text-sm text-muted-foreground mb-4">
                                    {variant.description}
                                  </p>

                                  <div className="mb-4">
                                    <h4 className="text-sm font-medium mb-2">
                                      Installation:
                                    </h4>
                                    <ol className="list-decimal list-inside space-y-1">
                                      {variant.steps.map((step, stepIdx) => (
                                        <li
                                          key={stepIdx}
                                          className="text-xs sm:text-sm text-muted-foreground"
                                        >
                                          {step}
                                        </li>
                                      ))}
                                    </ol>
                                  </div>

                                  <Button
                                    variant={idx === 0 ? "default" : "outline"}
                                    className="w-full group"
                                    onClick={variant.onClick}
                                  >
                                    <Download className="mr-2 h-4 w-4 group-hover:animate-bounce" />
                                    {variant.buttonText}
                                  </Button>
                                </div>
                              </div>
                            ))}
                          </div>
                        </TabsContent>

                        <TabsContent value="changelog" className="mt-0">
                          <div className="bg-card/50 rounded-xl border p-4">
                            <div className="flex items-center justify-between mb-4">
                              <div className="flex items-center gap-2">
                                <Badge
                                  variant="secondary"
                                  className="px-2.5 py-1"
                                >
                                  v{data.app.ifl_version || "Unknown"}
                                </Badge>
                                <span className="text-sm text-muted-foreground">
                                  {data.app.version_name &&
                                    `Based on Instagram v${data.app.version_name}`}
                                </span>
                              </div>

                              <div className="text-xs text-muted-foreground flex items-center gap-1.5">
                                <Calendar className="h-3.5 w-3.5" />
                                {data.build_date
                                  ? new Date(
                                      parseInt(data.build_date),
                                    ).toLocaleString("en-US", {
                                      day: "2-digit",
                                      month: "2-digit",
                                      year: "numeric",
                                    })
                                  : "Unknown date"}
                              </div>
                            </div>

                            {data.changelogs && data.changelogs.length > 0 ? (
                              <div className="space-y-1 max-h-[320px] overflow-y-auto pr-2 custom-scrollbar">
                                {data.changelogs.map((item, idx) => (
                                  <motion.div
                                    key={idx}
                                    initial={{ opacity: 0, y: 5 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    transition={{
                                      delay: 0.05 * idx,
                                      duration: 0.3,
                                    }}
                                  >
                                    <div className="flex group items-start gap-2 py-2 px-3 hover:bg-muted/40 rounded-md transition-colors">
                                      <ChevronRight className="h-4 w-4 shrink-0 text-primary mt-0.5 group-hover:translate-x-0.5 transition-transform" />
                                      <span className="text-sm group-hover:text-foreground/90 transition-colors">
                                        {item}
                                      </span>
                                    </div>
                                  </motion.div>
                                ))}
                              </div>
                            ) : (
                              <div className="bg-muted/30 rounded-lg py-10 text-center">
                                <FileText className="h-10 w-10 mx-auto text-muted-foreground/40 mb-3" />
                                <p className="text-sm text-muted-foreground">
                                  No changelog entries available for this
                                  version.
                                </p>
                              </div>
                            )}
                          </div>
                        </TabsContent>

                        <TabsContent value="build" className="mt-0">
                          <div className="bg-card/50 rounded-xl border p-4">
                            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                              <InfoCard
                                icon={
                                  <Calendar className="h-5 w-5 text-primary" />
                                }
                                title="Build Date"
                                value={
                                  data.build_date
                                    ? new Date(parseInt(data.build_date))
                                        .toLocaleString("en-US", {
                                          day: "2-digit",
                                          month: "2-digit",
                                          year: "numeric",
                                          hour: "2-digit",
                                          minute: "2-digit",
                                        })
                                        .replace(",", "")
                                    : "Not available"
                                }
                              />

                              <InfoCard
                                icon={<Code className="h-5 w-5 text-primary" />}
                                title="Instafel Version"
                                value={
                                  <Badge
                                    variant="outline"
                                    className="font-medium"
                                  >
                                    v{data.app.ifl_version || "Not available"}
                                  </Badge>
                                }
                              />

                              <InfoCard
                                icon={
                                  <Smartphone className="h-5 w-5 text-primary" />
                                }
                                title="Instagram Version"
                                value={`v${data.app.version_name || "Not available"}`}
                              />

                              <InfoCard
                                icon={
                                  <HardDrive className="h-5 w-5 text-primary" />
                                }
                                title="Version Code"
                                value={data.app.version_code || "Not available"}
                              />

                              <InfoCard
                                icon={
                                  <ShieldCheck className="h-5 w-5 text-primary" />
                                }
                                title="Patcher Version"
                                value={
                                  data.patcher.version != null
                                    ? `v${data.patcher.version}`
                                    : "Uses older patcher"
                                }
                                subtitle={
                                  data.patcher.commit
                                    ? `Commit: ${data.patcher.commit}`
                                    : null
                                }
                              />

                              <InfoCard
                                icon={
                                  <Shapes className="h-5 w-5 text-primary" />
                                }
                                title="Generation ID"
                                value={
                                  <code className="text-xs bg-muted px-1.5 py-0.5 rounded">
                                    {data.gen_id || "Not available"}
                                  </code>
                                }
                              />
                            </div>

                            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-4 pt-4 border-t">
                              <div className="bg-muted/30 rounded-lg p-3">
                                <h4 className="text-sm font-medium flex items-center gap-2 mb-2 text-primary">
                                  <ShieldCheck className="h-4 w-4" />
                                  MD5 Hash (Standard Version)
                                </h4>
                                <code className="text-xs block bg-background/80 p-2 rounded overflow-x-auto whitespace-nowrap">
                                  {data.hash.uc || "Not available"}
                                </code>
                              </div>

                              <div className="bg-muted/30 rounded-lg p-3">
                                <h4 className="text-sm font-medium flex items-center gap-2 mb-2 text-primary">
                                  <ShieldCheck className="h-4 w-4" />
                                  MD5 Hash (Clone Version)
                                </h4>
                                <code className="text-xs block bg-background/80 p-2 rounded overflow-x-auto whitespace-nowrap">
                                  {data.hash.c || "Not available"}
                                </code>
                              </div>
                            </div>
                          </div>
                        </TabsContent>
                      </div>
                    </Tabs>
                  </CardContent>
                </Card>
              </motion.div>
            )}

            {!data && !loading && (
              <div className="text-center p-4 xs:p-6 sm:p-10 md:p-12 border rounded-lg bg-card/90">
                <FileText className="mx-auto h-12 w-12 sm:h-16 sm:w-16 text-muted-foreground mb-3 sm:mb-4 opacity-50" />
                <h3 className="text-xl sm:text-2xl font-medium mb-2">
                  Release Not Found
                </h3>
                <p className="text-sm sm:text-base text-muted-foreground mb-4 sm:mb-6">
                  We couldn&apos;t find the version you&apos;re looking for. It
                  may have been removed or doesn&apos;t exist.
                </p>
                <Button asChild size="default">
                  <a href="/download?version=latest">Get Latest Version</a>
                </Button>
              </div>
            )}
          </div>
          <Footer />
        </div>
      )}
    </AnimatePresence>
  );
}

// Helper function to render info cards
function InfoCard({ icon, title, value, subtitle = null }) {
  return (
    <div className="bg-muted/30 rounded-lg p-3 flex flex-col">
      <div className="flex items-center gap-2 mb-1">
        {icon}
        <h3 className="text-sm font-medium">{title}</h3>
      </div>
      <div className="mt-1">
        <div className="font-medium text-sm">{value}</div>
        {subtitle && (
          <div className="text-xs text-muted-foreground mt-0.5">{subtitle}</div>
        )}
      </div>
    </div>
  );
}
