"use client";

import { AnimatePresence, motion } from "framer-motion";
import { Suspense, useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useSearchParams } from "next/navigation";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";
import {
  Calendar,
  Download,
  FileDown,
  FileSpreadsheet,
  History,
  Info,
  Smartphone,
  User,
} from "lucide-react";
import Link from "next/link";
import { Separator } from "@/components/ui/separator";

interface Manifest {
  version_name: string;
  author: string;
  changelog: string;
  last_updated: string;
  description: string;
  name: string;
}

interface Resp {
  manifest_version: number;
  manifest: Manifest;
}

export default function LibraryBackupPage() {
  return (
    <Suspense fallback={<LoadingBar />}>
      <LibraryBackupPageContent />
    </Suspense>
  );
}

function LibraryBackupPageContent() {
  const searchParams = useSearchParams();
  const id = searchParams.get("id") ?? "null";

  const [data, setData] = useState<Resp | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [downloadStarted, setDownloadStarted] = useState(false);
  const [importStarted, setImportStarted] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const requestUrl = `https://raw.githubusercontent.com/instafel/backups/refs/heads/main/${id}/manifest.json`;
        const res = await fetch(requestUrl);
        const result: Resp = await res.json();
        setData(result);
      } catch (error) {
        console.error("Failed to fetch backup data:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleDownloadBackup = (id: string, version: string) => {
    setDownloadStarted(true);

    const link = document.createElement("a");
    link.href = `https://api.mamiiblt.me/ifl/dw_backup?id=${id}&version=${version}`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    // Reset state after animation completes
    setTimeout(() => {
      setDownloadStarted(false);
    }, 2500);
  };

  const handleImportInstafel = () => {
    setImportStarted(true);

    // Reset state after animation completes
    setTimeout(() => {
      setImportStarted(false);
    }, 2500);
  };

  if (isLoading) {
    return <LoadingBar />;
  }

  if (!data) {
    return (
      <div className="container mx-auto py-12 px-4 text-center">
        <Card className="max-w-md mx-auto p-6 border-2">
          <CardTitle className="text-xl mb-4">Backup Not Found</CardTitle>
          <p className="text-muted-foreground mb-6">
            We couldn&apos;t find the backup you&apos;re looking for. It may
            have been removed or doesn&apos;t exist.
          </p>
          <Link href="/library_backup">
            <Button>Return to Backup Library</Button>
          </Link>
        </Card>
        <Footer />
      </div>
    );
  }

  // Format changelog to array if it's a string
  const changelogItems =
    typeof data.manifest.changelog === "string"
      ? data.manifest.changelog.split("\n").filter((item) => item.trim() !== "")
      : [];

  return (
    <>
      <div className="min-h-screen py-6 sm:py-8 px-3 sm:px-6">
        <div className="max-w-4xl mx-auto">
          <div className="mb-6">
            <Link
              href="/library_backup"
              className="text-primary hover:underline flex items-center"
            >
              <FileSpreadsheet className="mr-2 h-4 w-4" />
              Back to Backup Library
            </Link>
          </div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
          >
            <Card className="border-2 shadow-lg overflow-hidden">
              <div className="bg-gradient-to-r from-primary/10 to-primary/5 p-4 sm:p-6">
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 sm:gap-6">
                  <div>
                    <motion.h1
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ duration: 0.4, delay: 0.1 }}
                      className="text-xl sm:text-2xl md:text-3xl font-bold"
                    >
                      {data.manifest.name}
                    </motion.h1>

                    <motion.div
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ duration: 0.4, delay: 0.2 }}
                      className="flex flex-wrap items-center gap-2 mt-2"
                    >
                      <Badge
                        variant="outline"
                        className="bg-white/80 text-xs font-medium flex items-center gap-1 py-1 dark:text-background"
                      >
                        <User className="h-3 w-3 text-primary dark:text-background" />
                        {data.manifest.author}
                      </Badge>

                      <Badge
                        variant="secondary"
                        className="text-xs font-medium flex items-center gap-1 py-1"
                      >
                        <History className="h-3 w-3" />
                        Version {data.manifest.version_name}
                      </Badge>
                    </motion.div>
                  </div>

                  <div className="flex flex-col sm:flex-row gap-3 md:flex-col lg:flex-row">
                    <motion.div
                      initial={{ opacity: 0, scale: 0.95 }}
                      animate={{ opacity: 1, scale: 1 }}
                      transition={{ duration: 0.4, delay: 0.3 }}
                    >
                      <Button
                        onClick={() =>
                          handleDownloadBackup(id, data.manifest.version_name)
                        }
                        className={`w-full relative overflow-hidden ${
                          downloadStarted
                            ? "bg-green-600 hover:bg-green-700"
                            : "bg-primary"
                        }`}
                        disabled={downloadStarted}
                      >
                        {downloadStarted ? (
                          <>
                            <motion.div
                              initial={{ width: 0 }}
                              animate={{ width: "100%" }}
                              transition={{ duration: 2 }}
                              className="absolute inset-y-0 left-0 bg-green-500/20"
                            />
                            <span className="relative z-10 flex items-center text-xs sm:text-sm">
                              <FileDown className="mr-1.5 sm:mr-2 h-3.5 w-3.5 sm:h-4 sm:w-4" />
                              Downloading...
                            </span>
                          </>
                        ) : (
                          <>
                            <Download className="mr-1.5 sm:mr-2 h-3.5 w-3.5 sm:h-4 sm:w-4" />
                            <span className="text-xs sm:text-sm">
                              Download Backup
                            </span>
                          </>
                        )}
                      </Button>
                    </motion.div>

                    <motion.div
                      initial={{ opacity: 0, scale: 0.95 }}
                      animate={{ opacity: 1, scale: 1 }}
                      transition={{ duration: 0.4, delay: 0.4 }}
                    >
                      <Button
                        onClick={handleImportInstafel}
                        variant="outline"
                        className={`w-full border-primary relative overflow-hidden ${
                          importStarted
                            ? "bg-primary/20 text-primary"
                            : "text-primary hover:bg-primary/10"
                        }`}
                        disabled={importStarted}
                      >
                        {importStarted ? (
                          <>
                            <motion.div
                              initial={{ width: 0 }}
                              animate={{ width: "100%" }}
                              transition={{ duration: 2 }}
                              className="absolute inset-y-0 left-0 bg-primary/10"
                            />
                            <span className="relative z-10 flex items-center text-xs sm:text-sm">
                              <Smartphone className="mr-1.5 sm:mr-2 h-3.5 w-3.5 sm:h-4 sm:w-4" />
                              Opening Instafel...
                            </span>
                          </>
                        ) : (
                          <>
                            <Smartphone className="mr-1.5 sm:mr-2 h-3.5 w-3.5 sm:h-4 sm:w-4" />
                            <span className="text-xs sm:text-sm">
                              Import in Instafel
                            </span>
                          </>
                        )}
                      </Button>
                    </motion.div>
                  </div>
                </div>
              </div>

              <CardContent className="p-4 sm:p-6">
                <motion.div
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ duration: 0.5, delay: 0.3 }}
                >
                  <div className="mb-6 sm:mb-8">
                    <h2 className="text-base sm:text-lg font-semibold mb-2 sm:mb-3 flex items-center">
                      <Info className="h-4 w-4 sm:h-5 sm:w-5 mr-2 text-primary" />
                      About This Backup
                    </h2>
                    <div className="bg-gray-50 dark:bg-gray-800/50 rounded-lg p-3 sm:p-4">
                      <p className="text-sm text-foreground/90">
                        {data.manifest.description}
                      </p>
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 sm:gap-6 mb-6 sm:mb-8">
                    <div className="space-y-4">
                      <div>
                        <h3 className="text-xs sm:text-sm font-medium text-muted-foreground flex items-center mb-1">
                          <Calendar className="h-3.5 w-3.5 sm:h-4 sm:w-4 mr-1.5" />
                          Last Updated
                        </h3>
                        <p className="text-sm sm:text-base font-medium mb-3 sm:mb-4">
                          {data.manifest.last_updated}
                        </p>

                        <h3 className="text-xs sm:text-sm font-medium text-muted-foreground flex items-center mb-1 sm:mb-2">
                          <FileDown className="h-3.5 w-3.5 sm:h-4 sm:w-4 mr-1.5" />
                          Backup ID
                        </h3>
                        <code className="bg-gray-100 dark:bg-gray-800 px-2 py-1 rounded text-xs sm:text-sm font-mono break-all">
                          {id}
                        </code>
                      </div>
                    </div>
                  </div>

                  {changelogItems.length > 0 && (
                    <div className="mt-4 sm:mt-6">
                      <Separator className="my-4 sm:my-6" />

                      <h2 className="text-base sm:text-lg font-semibold mb-2 sm:mb-3 flex items-center">
                        <History className="h-4 w-4 sm:h-5 sm:w-5 mr-2 text-primary" />
                        Changelog
                      </h2>

                      <div className="bg-gray-50 dark:bg-gray-800/50 rounded-lg p-3 sm:p-4">
                        <ul className="space-y-1.5 sm:space-y-2">
                          {changelogItems.map((item, index) => (
                            <motion.li
                              key={index}
                              initial={{ opacity: 0, x: -5 }}
                              animate={{ opacity: 1, x: 0 }}
                              transition={{
                                delay: 0.5 + index * 0.1,
                                duration: 0.3,
                              }}
                              className="flex items-start"
                            >
                              <div className="mr-2 sm:mr-3 mt-1.5 h-1.5 w-1.5 rounded-full bg-primary flex-shrink-0"></div>
                              <span className="text-xs sm:text-sm">{item}</span>
                            </motion.li>
                          ))}
                        </ul>
                      </div>
                    </div>
                  )}

                  <Separator className="my-4 sm:my-6" />

                  <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-3 sm:p-4 mt-4 sm:mt-6">
                    <h3 className="text-xs sm:text-sm font-medium text-blue-700 dark:text-blue-300 flex items-center mb-1 sm:mb-2">
                      <Info className="h-3.5 w-3.5 sm:h-4 sm:w-4 mr-1.5" />
                      How to use this backup
                    </h3>
                    <p className="text-xs sm:text-sm text-blue-700/80 dark:text-blue-300/80">
                      You can either download this backup file and manually
                      import it into Instafel, or use the &quot;Import in
                      Instafel&quot; button to automatically open the app and
                      import it directly.
                    </p>
                  </div>
                </motion.div>
              </CardContent>
            </Card>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.6 }}
            className="mt-6 sm:mt-10 text-center"
          >
            <Button asChild variant="outline">
              <Link href="/library_backup" className="flex items-center">
                <FileSpreadsheet className="mr-2 h-3.5 w-3.5 sm:h-4 sm:w-4" />
                View All Backups
              </Link>
            </Button>
          </motion.div>
        </div>
      </div>
      <Footer />
    </>
  );
}
