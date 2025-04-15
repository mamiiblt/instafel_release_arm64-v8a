"use client";

import { AnimatePresence, motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Suspense, useEffect, useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

import {
  ArrowRight,
  ChevronRight,
  Download,
  DownloadIcon,
  Info,
  LucideFileText,
  Shapes,
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

export default function DownloadInstafelPage() {
  return (
    <Suspense fallback={<LoadingBar />}>
      <DownloadIflContent />
    </Suspense>
  );
}

function DownloadIflContent() {
  const [activeTab, setActiveTab] = useState("download");

  const searchParams = useSearchParams();
  const version = searchParams.get("version");

  const [data, setData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      var requestUrl = "";
      if (version == "latest") {
        requestUrl =
          "https://api.github.com/repos/mamiiblt/instafel/releases/latest";
      } else {
        requestUrl =
          "https://api.github.com/repos/mamiiblt/instafel/releases/tags/" +
          version;
      }
      const res = await fetch(requestUrl);
      const result = await res.json();

      var values = {
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

      if (result.status != "Not Found") {
        const releaseBody = result.body.split("\n");
        let changeLogs = [];

        releaseBody.forEach((line) => {
          if (!line.startsWith("|") && line != "" && line != "# Changelog") {
            changeLogs.push(line.trim().substring(2));
          } else {
            const lineParts = line.split("|");
            for (let i = 0; i < lineParts.length; i++) {
              var part = lineParts[i].trim();
              if (
                !part.includes("PROPERTY") &&
                !part.includes("VALUE") &&
                !part.includes("Changelog") &&
                !part.includes("-------------") &&
                !part.length != 1
              ) {
                var nextValue = lineParts[i + 1].trim();
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

        result.assets.forEach((asset) => {
          if (asset.name.includes("instafel_uc")) {
            values.download_urls.unclone = asset.browser_download_url;
          }

          if (asset.name.includes("instafel_c")) {
            values.download_urls.clone = asset.browser_download_url;
          }
        });
        values.changelogs = changeLogs;
        console.log(changeLogs);
        setData(values);
      } else {
        setData(null);
      }
    };
    fetchData();
  }, []);

  const download = (url) => {
    const link = document.createElement("a");
    link.href = url;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <AnimatePresence>
      {data ? (
        <div>
          {" "}
          <div className="min-h-screen  font-sans">
            <div className="container mx-auto px-4 pt-20 text-center">
              <motion.h1
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  duration: 0.8,
                  ease: "easeInOut",
                }}
                className="mb-2 text-5xl font-bold tracking-tight sm:text-6xl md:text-7xl"
              >
                Download <br />
              </motion.h1>
              <motion.h1
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  duration: 0.8,
                  ease: "easeInOut",
                }}
                className="mb-2 text-3xl font-regular tracking-tight sm:text-4xl md:text-5xl"
              >
                Instafel v{data ? data.app.ifl_version : "..."}
              </motion.h1>
              <motion.p
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.5,
                  duration: 0.6,
                  ease: "easeOut",
                }}
                className="mx-auto mt-4 max-w-2xl text-xl text-muted-foreground"
              >
                You can download the APK files that suits you by selecting it
                from here!
              </motion.p>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.8,
                  duration: 0.8,
                  ease: "easeOut",
                }}
              >
                <div className="mt-12 flex flex-col items-center justify-center gap-4 sm:flex-row">
                  <Button
                    onClick={() =>
                      download(data ? data.download_urls.unclone : null)
                    }
                    size="lg"
                    className="h-14 w-full rounded-full px-8 text-lg sm:w-auto"
                  >
                    <Download className="mr-2 h-5 w-5" />
                    Download Unclone
                  </Button>

                  <Button
                    variant="outline-gradient"
                    size="lg"
                    onClick={() =>
                      download(data ? data.download_urls.clone : null)
                    }
                    className="h-14 w-full rounded-full px-8 text-lg sm:w-auto"
                  >
                    <Download className="mr-2 h-5 w-5" />
                    Download Clone
                  </Button>
                </div>
              </motion.div>
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
              <div className="container mx-auto px-4 mt-12 pb-12">
                <Tabs
                  defaultValue="download"
                  className="w-full mt-8"
                  onValueChange={setActiveTab}
                >
                  <TabsContent
                    value="download"
                    className="mt-0 animate-fade-in space-y-8"
                  >
                    <Card>
                      <CardContent className="p-6">
                        <h2 className="mb-4 flex items-center gap-2 text-2xl font-bold">
                          <Shapes className="h-5 w-5 text-primary" />
                          Variant Infos
                        </h2>
                        <div className="mb-8 flex justify-center">
                          <TabsList className="grid w-full max-w-md grid-cols-3">
                            <TabsTrigger value="download">Variants</TabsTrigger>
                            <TabsTrigger value="changelog">
                              Changelog
                            </TabsTrigger>
                            <TabsTrigger value="build">Build Info</TabsTrigger>
                          </TabsList>
                        </div>

                        <div className="overflow-x-auto">
                          <div className="grid gap-8 md:grid-cols-2">
                            <div className="flex flex-col h-full rounded-xl border-2 bg-card p-6 transition-all duration-200 peer-data-[state=checked]:border-primary peer-data-[state=checked]:bg-primary/5 [&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-primary/5 cursor-pointer">
                              <div className="flex items-center gap-4 mb-4">
                                <div className="bg-primary/10 p-3 rounded-full">
                                  <DownloadIcon className="h-6 w-6 text-primary" />
                                </div>
                                <div>
                                  <h3 className="text-xl font-bold">
                                    Unclone Variant
                                  </h3>
                                </div>
                              </div>

                              <div className="grid gap-6 mt-2">
                                <span>
                                  In order to install this variant, the original
                                  Instagram app must be uninstalled, as it will
                                  replace the standard Instagram application.
                                </span>
                              </div>
                              <Button
                                className="mt-6 w-full"
                                onClick={() =>
                                  download(
                                    data ? data.download_urls.unclone : null
                                  )
                                }
                              >
                                <Download className="mr-2 h-4 w-4" />
                                Download Unclone
                              </Button>
                            </div>

                            <div className="flex flex-col h-full rounded-xl border-2 bg-card p-6 transition-all duration-200 peer-data-[state=checked]:border-primary peer-data-[state=checked]:bg-primary/5 [&:has([data-state=checked])]:border-primary [&:has([data-state=checked])]:bg-primary/5 cursor-pointer">
                              <div className="flex items-center gap-4 mb-4">
                                <div className="bg-primary/10 p-3 rounded-full">
                                  <DownloadIcon className="h-6 w-6 text-primary" />
                                </div>
                                <div>
                                  <h3 className="text-xl font-bold">
                                    Clone Variant
                                  </h3>
                                </div>
                              </div>

                              <div className="grid gap-6 mt-2">
                                <span>
                                  In order to install this clone variant, you
                                  can keep the original Instagram app installed,
                                  as this version works alongside the standard
                                  Instagram application. Compared to the unclone
                                  version, this variant may have some stability
                                  issues.
                                </span>
                              </div>
                              <Button
                                className="mt-6 w-full"
                                onClick={() =>
                                  download(
                                    data ? data.download_urls.clone : null
                                  )
                                }
                              >
                                <Download className="mr-2 h-4 w-4" />
                                Download Clone
                              </Button>
                            </div>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  </TabsContent>

                  <TabsContent value="build" className="mt-0 animate-fade-in">
                    <Card>
                      <CardContent className="p-6">
                        <h2 className="mb-4 flex items-center gap-2 text-2xl font-bold">
                          <Info className="h-5 w-5 text-primary" />
                          Build Information
                        </h2>
                        <div className="mb-8 flex justify-center">
                          <TabsList className="grid w-full max-w-md grid-cols-3">
                            <TabsTrigger value="download">Variants</TabsTrigger>
                            <TabsTrigger value="changelog">
                              Changelog
                            </TabsTrigger>
                            <TabsTrigger value="build">Build Info</TabsTrigger>
                          </TabsList>
                        </div>

                        <div className="overflow-x-auto">
                          <Table>
                            <TableHeader>
                              <TableRow>
                                <TableHead>Property</TableHead>
                                <TableHead>Value</TableHead>
                              </TableRow>
                            </TableHeader>
                            <TableBody>
                              <TableRow>
                                <TableCell className="font-medium">
                                  Build Date
                                </TableCell>
                                <TableCell>
                                  {data
                                    ? new Date(parseInt(data.build_date))
                                        .toLocaleString("en-US", {
                                          day: "2-digit",
                                          month: "2-digit",
                                          year: "numeric",
                                          hour: "2-digit",
                                          minute: "2-digit",
                                        })
                                        .replace(",", "")
                                    : "..."}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  IFL Version
                                </TableCell>
                                <TableCell>
                                  Release v{data ? data.app.ifl_version : "..."}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  IG Version
                                </TableCell>
                                <TableCell>
                                  v{data ? data.app.version_name : "..."}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  IG Ver-code
                                </TableCell>
                                <TableCell>
                                  {data ? data.app.version_code : "..."}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  Patcher Version
                                </TableCell>
                                <TableCell>
                                  {data.patcher.version != null
                                    ? `v${data.patcher.version} (${data.patcher.commit})`
                                    : "Uses old patcher"}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  Generation ID
                                </TableCell>
                                <TableCell>
                                  {data ? data.gen_id : "..."}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  MD5 Hash (UC)
                                </TableCell>
                                <TableCell>
                                  {data ? data.hash.uc : "..."}
                                </TableCell>
                              </TableRow>
                              <TableRow>
                                <TableCell className="font-medium">
                                  MD5 Hash (C)
                                </TableCell>
                                <TableCell>
                                  {data ? data.hash.c : "..."}
                                </TableCell>
                              </TableRow>
                            </TableBody>
                          </Table>
                        </div>
                      </CardContent>
                    </Card>
                  </TabsContent>

                  <TabsContent
                    value="changelog"
                    className="mt-0 animate-fade-in"
                  >
                    <Card>
                      <CardContent className="p-6">
                        <h2 className="mb-4 flex items-center gap-2 text-2xl font-bold">
                          <LucideFileText className="h-5 w-5 text-primary" />
                          Changelog
                        </h2>

                        <div className="mb-8 flex justify-center">
                          <TabsList className="grid w-full max-w-md grid-cols-3">
                            <TabsTrigger value="download">Variants</TabsTrigger>
                            <TabsTrigger value="changelog">
                              Changelog
                            </TabsTrigger>
                            <TabsTrigger value="build">Build Info</TabsTrigger>
                          </TabsList>
                        </div>

                        <div className="space-y-8">
                          <div>
                            <div className="mb-2 flex items-center gap-2">
                              <Badge>
                                {" "}
                                v{data ? data.app.ifl_version : "..."}
                              </Badge>
                              <span className="text-sm text-muted-foreground">
                                Released on{" "}
                                {data
                                  ? new Date(parseInt(data.build_date))
                                      .toLocaleString("en-US", {
                                        day: "2-digit",
                                        month: "2-digit",
                                        year: "numeric",
                                      })
                                      .replace(",", "")
                                  : "..."}
                              </span>
                            </div>
                            <ul className="space-y-2">
                              {data ? (
                                data.changelogs.map((item, index) => (
                                  <li
                                    key="index"
                                    className="flex items-start gap-2"
                                  >
                                    <ChevronRight className="mt-1 h-4 w-4 flex-shrink-0 text-primary" />
                                    <span>{item}</span>
                                  </li>
                                ))
                              ) : (
                                <li>Loading...</li>
                              )}
                            </ul>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  </TabsContent>
                </Tabs>
              </div>
            </motion.div>
          </div>
          <Footer />
        </div>
      ) : (
        <LoadingBar />
      )}
    </AnimatePresence>
  );
}
