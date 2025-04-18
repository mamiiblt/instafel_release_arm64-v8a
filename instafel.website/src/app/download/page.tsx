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
  Smartphone,
  Calendar,
  Code,
  CheckCircle,
  ShieldCheck,
  HardDrive,
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
        <div className="min-h-screen">
          <div className="container mx-auto px-4 pt-12 sm:pt-16 md:pt-20 pb-16">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{
                duration: 0.7,
                ease: "easeOut",
              }}
              className="text-center mb-12"
            >
              <div className="flex justify-center mb-6">
                <div className="relative">
                  <div
                    className="absolute inset-0 rounded-full bg-primary/20 animate-ping"
                    style={{ animationDuration: "3s" }}
                  ></div>
                  <div className="relative bg-primary/30 p-5 rounded-full">
                    <Download className="h-12 w-12 text-primary" />
                  </div>
                </div>
              </div>
              <h1 className="text-4xl sm:text-5xl md:text-6xl font-bold tracking-tight mb-4">
                Download <span className="text-primary">Instafel</span>
              </h1>
              <motion.p
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.2, duration: 0.7 }}
                className="text-xl text-muted-foreground max-w-2xl mx-auto"
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
                className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-12"
              >
                {[
                  {
                    title: "Standard Version",
                    subtitle: "Unclone Variant",
                    description:
                      "Replace the standard Instagram app with Instafel. Original app must be uninstalled.",
                    icon: <Smartphone className="size-6 text-primary" />,
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
                    title: "Parallel Version",
                    subtitle: "Clone Variant",
                    description:
                      "Install alongside the original Instagram app. Perfect for testing while keeping your original app.",
                    icon: <Shapes className="size-6 text-primary" />,
                    downloadUrl: data.download_urls.clone,
                    type: "clone" as const,
                    benefits: [
                      "Keep original Instagram",
                      "Test new features",
                      "Separate accounts",
                    ],
                    delay: 0.6,
                  },
                ].map((variant, index) => (
                  <motion.div
                    key={variant.title}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: variant.delay, duration: 0.5 }}
                    whileHover={{ scale: 1.02 }}
                    className="relative"
                  >
                    <Card className="h-full border-2 hover:border-primary/60 transition-all duration-300 overflow-hidden shadow-lg">
                      <CardContent className="p-0">
                        <div className="p-6 pb-0">
                          <div className="flex items-center gap-4 mb-4">
                            <div className="p-3 rounded-full bg-gradient-to-r from-primary/20 to-primary/5">
                              {variant.icon}
                            </div>
                            <div>
                              <h3 className="text-2xl font-bold">
                                {variant.title}
                              </h3>
                              <p className="text-primary font-medium">
                                {variant.subtitle}
                              </p>
                            </div>
                          </div>

                          <p className="text-muted-foreground mb-5">
                            {variant.description}
                          </p>
                        </div>

                        <div className="px-6 pb-4">
                          <h4 className="text-sm font-medium mb-2 flex items-center">
                            <CheckCircle className="h-4 w-4 mr-1.5 text-primary" />
                            Benefits
                          </h4>
                          <ul className="space-y-1 mb-6">
                            {variant.benefits.map((benefit, i) => (
                              <li key={i} className="text-sm flex items-center">
                                <ChevronRight className="h-3.5 w-3.5 text-primary/70 mr-1" />
                                {benefit}
                              </li>
                            ))}
                          </ul>
                        </div>

                        <div className="bg-gradient-to-r from-primary/5 to-primary/10 p-6">
                          <Button
                            size="lg"
                            className={`w-full ${downloadStarted[variant.type] ? "bg-green-600 hover:bg-green-700" : ""}`}
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
                                <span className="relative z-10 flex items-center">
                                  <svg
                                    className="w-5 h-5 mr-2"
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
                              <span className="flex items-center">
                                <Download className="mr-2 h-5 w-5" />
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
                className="mt-10"
              >
                <Card className="shadow-md border-2">
                  <CardContent className="p-0">
                    <Tabs
                      defaultValue="download"
                      value={activeTab}
                      onValueChange={setActiveTab}
                      className="w-full"
                    >
                      <div className="border-b p-4">
                        <h2 className="text-2xl font-semibold mb-4 flex items-center gap-2">
                          <Info className="h-6 w-6 text-primary" />
                          Release Information
                        </h2>
                        <TabsList className="grid w-full max-w-md grid-cols-3 mx-auto">
                          <TabsTrigger value="download">Details</TabsTrigger>
                          <TabsTrigger value="changelog">Changelog</TabsTrigger>
                          <TabsTrigger value="build">Build Info</TabsTrigger>
                        </TabsList>
                      </div>

                      <div className="p-6 pb-8 min-h-[450px] md:min-h-[400px] relative">
                        <TabsContent
                          value="download"
                          className="w-full space-y-6 absolute top-6 left-0 right-0 px-0 md:px-4 transition-all duration-300 data-[state=inactive]:opacity-0 data-[state=inactive]:pointer-events-none data-[state=active]:opacity-100 data-[state=active]:z-10 data-[state=active]:animate-in data-[state=inactive]:animate-out data-[state=active]:fade-in-0 data-[state=inactive]:fade-out-0 data-[state=active]:zoom-in-95 data-[state=active]:slide-in-from-bottom-2"
                        >
                          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 md:gap-8">
                            {[
                              {
                                title: "Unclone Installation",
                                description: (
                                  <>
                                    <p className="mb-4">
                                      This variant requires the original
                                      Instagram app to be uninstalled first, as
                                      it replaces the standard Instagram
                                      application with Instafel.
                                    </p>
                                    <p>
                                      After uninstalling the original app,
                                      install the Unclone variant for the full
                                      Instafel experience.
                                    </p>
                                  </>
                                ),
                                buttonText: "Download Unclone",
                                onClick: () =>
                                  download(
                                    data.download_urls.unclone,
                                    "unclone",
                                  ),
                                color: "primary",
                                icon: <Smartphone className="h-4 w-4" />,
                              },
                              {
                                title: "Clone Installation",
                                description: (
                                  <>
                                    <p className="mb-4">
                                      The Clone variant can be installed
                                      alongside the original Instagram app,
                                      allowing you to use both apps
                                      simultaneously.
                                    </p>
                                    <p>
                                      Note that compared to the Unclone version,
                                      this variant may have some stability
                                      issues in certain situations.
                                    </p>
                                  </>
                                ),
                                buttonText: "Download Clone",
                                onClick: () =>
                                  download(data.download_urls.clone, "clone"),
                                color: "secondary",
                                icon: <Shapes className="h-4 w-4" />,
                              },
                            ].map((info, i) => (
                              <div
                                key={i}
                                className="border rounded-xl p-5 hover:shadow-md transition-all duration-300 hover:border-primary/30 bg-card"
                              >
                                <h3 className="text-xl font-bold mb-3 flex items-center">
                                  <div className="mr-2 p-1.5 rounded-md bg-primary/10 text-primary">
                                    {info.icon}
                                  </div>
                                  {info.title}
                                </h3>
                                <div className="text-muted-foreground mb-5 text-sm md:text-base">
                                  {info.description}
                                </div>
                                <Button
                                  variant={i === 0 ? "default" : "outline"}
                                  className="w-full transition-all group"
                                  onClick={info.onClick}
                                >
                                  <Download className="mr-2 h-4 w-4 group-hover:animate-bounce" />
                                  {info.buttonText}
                                </Button>
                              </div>
                            ))}
                          </div>
                        </TabsContent>

                        <TabsContent
                          value="build"
                          className="w-full space-y-4 absolute top-6 left-0 right-0 px-0 md:px-4 transition-all duration-300 data-[state=inactive]:opacity-0 data-[state=inactive]:pointer-events-none data-[state=active]:opacity-100 data-[state=active]:z-10 data-[state=active]:animate-in data-[state=inactive]:animate-out data-[state=active]:fade-in-0 data-[state=inactive]:fade-out-0 data-[state=active]:zoom-in-95 data-[state=active]:slide-in-from-bottom-2"
                        >
                          <div className="overflow-x-auto rounded-lg border">
                            <Table>
                              <TableHeader>
                                <TableRow className="bg-muted/50">
                                  <TableHead className="w-1/3 font-semibold">
                                    Property
                                  </TableHead>
                                  <TableHead className="w-2/3">Value</TableHead>
                                </TableRow>
                              </TableHeader>
                              <TableBody>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <Calendar className="h-4 w-4 mr-2 text-primary/70" />
                                      Build Date
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    {data.build_date
                                      ? new Date(parseInt(data.build_date))
                                          .toLocaleString("en-US", {
                                            day: "2-digit",
                                            month: "2-digit",
                                            year: "numeric",
                                            hour: "2-digit",
                                            minute: "2-digit",
                                          })
                                          .replace(",", "")
                                      : "Not available"}
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <Code className="h-4 w-4 mr-2 text-primary/70" />
                                      IFL Version
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    <Badge
                                      variant="outline"
                                      className="font-medium"
                                    >
                                      v{data.app.ifl_version || "Not available"}
                                    </Badge>
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <Smartphone className="h-4 w-4 mr-2 text-primary/70" />
                                      Instagram Version
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    v{data.app.version_name || "Not available"}
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <Code className="h-4 w-4 mr-2 text-primary/70" />
                                      Instagram Version Code
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    {data.app.version_code || "Not available"}
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <ShieldCheck className="h-4 w-4 mr-2 text-primary/70" />
                                      Patcher Version
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    {data.patcher.version != null
                                      ? `v${data.patcher.version} (${data.patcher.commit})`
                                      : "Uses older patcher"}
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <HardDrive className="h-4 w-4 mr-2 text-primary/70" />
                                      Generation ID
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    <code className="px-2 py-1 bg-muted rounded text-sm font-mono">
                                      {data.gen_id || "Not available"}
                                    </code>
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <ShieldCheck className="h-4 w-4 mr-2 text-primary/70" />
                                      MD5 Hash (Unclone)
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    <div className="overflow-x-auto max-w-full">
                                      <code className="px-2 py-1 bg-muted rounded text-sm font-mono block whitespace-nowrap">
                                        {data.hash.uc || "Not available"}
                                      </code>
                                    </div>
                                  </TableCell>
                                </TableRow>
                                <TableRow className="hover:bg-muted/30">
                                  <TableCell className="font-medium">
                                    <div className="flex items-center">
                                      <ShieldCheck className="h-4 w-4 mr-2 text-primary/70" />
                                      MD5 Hash (Clone)
                                    </div>
                                  </TableCell>
                                  <TableCell>
                                    <div className="overflow-x-auto max-w-full">
                                      <code className="px-2 py-1 bg-muted rounded text-sm font-mono block whitespace-nowrap">
                                        {data.hash.c || "Not available"}
                                      </code>
                                    </div>
                                  </TableCell>
                                </TableRow>
                              </TableBody>
                            </Table>
                          </div>
                        </TabsContent>

                        <TabsContent
                          value="changelog"
                          className="w-full space-y-4 absolute top-6 left-0 right-0 px-0 md:px-4 transition-all duration-300 data-[state=inactive]:opacity-0 data-[state=inactive]:pointer-events-none data-[state=active]:opacity-100 data-[state=active]:z-10"
                        >
                          <div>
                            <div className="mb-6 flex flex-wrap items-center gap-3">
                              <Badge
                                variant="secondary"
                                className="py-1.5 px-3"
                              >
                                v{data.app.ifl_version || "Unknown"}
                              </Badge>
                              <span className="text-sm text-muted-foreground flex items-center gap-1.5">
                                <Calendar className="h-3.5 w-3.5" />
                                Released on{" "}
                                {data.build_date
                                  ? new Date(parseInt(data.build_date))
                                      .toLocaleString("en-US", {
                                        day: "2-digit",
                                        month: "2-digit",
                                        year: "numeric",
                                      })
                                      .replace(",", "")
                                  : "Unknown date"}
                              </span>
                            </div>

                            <div className="space-y-2 pl-2 pr-2 max-h-[280px] md:max-h-[320px] overflow-y-auto rounded-lg border bg-card/30 p-4">
                              {data.changelogs && data.changelogs.length > 0 ? (
                                data.changelogs.map((item, index) => (
                                  <motion.div
                                    key={index}
                                    initial={{ opacity: 0, x: -10 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    transition={{
                                      delay: 0.05 * index,
                                      duration: 0.3,
                                    }}
                                  >
                                    <div className="flex items-start gap-2 py-2 group hover:bg-muted/40 px-2 rounded-md transition-colors duration-150">
                                      <ChevronRight className="mt-1 h-4 w-4 shrink-0 text-primary group-hover:translate-x-1 transition-transform duration-200" />
                                      <span className="group-hover:text-primary transition-colors duration-200">
                                        {item}
                                      </span>
                                    </div>
                                  </motion.div>
                                ))
                              ) : (
                                <p className="text-muted-foreground italic flex items-center justify-center h-24">
                                  No changelog entries available for this
                                  version.
                                </p>
                              )}
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
              <div className="text-center p-12 border rounded-lg">
                <FileText className="mx-auto h-16 w-16 text-muted-foreground mb-4 opacity-50" />
                <h3 className="text-2xl font-medium mb-2">Release Not Found</h3>
                <p className="text-muted-foreground mb-6">
                  We couldn&apos;t find the version you&apos;re looking for. It
                  may have been removed or doesn&apos;t exist.
                </p>
                <Button asChild>
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
