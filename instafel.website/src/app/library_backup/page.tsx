"use client";

import { AnimatePresence, motion } from "framer-motion";
import {
  FileCog2Icon,
  FileSpreadsheet,
  Package,
  ChevronRight,
  FileText,
} from "lucide-react";
import { useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

interface Backup {
  id: string;
  name: string;
  author: string;
}

interface BackupInfo {
  tag_name: string;
  backups: Backup[];
}

export default function LibraryBackupPage() {
  const [data, setData] = useState<BackupInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const requestUrl =
          "https://raw.githubusercontent.com/instafel/backups/refs/heads/main/backups.json";
        const res = await fetch(requestUrl);
        const result: BackupInfo = await res.json();
        setData(result);
      } catch (error) {
        console.error("Failed to fetch backups:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  if (isLoading) {
    return <LoadingBar />;
  }

  return (
    <AnimatePresence>
      {data ? (
        <div className="flex flex-col min-h-screen">
          <section className="flex-grow py-12 px-4 md:px-8 lg:px-12">
            <div className="max-w-6xl mx-auto">
              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6 }}
                className="text-center mb-12"
              >
                <div className="flex justify-center mb-6">
                  <div className="relative">
                    <div
                      className="absolute inset-0 rounded-full bg-primary/20 animate-ping"
                      style={{ animationDuration: "3s" }}
                    ></div>
                    <div className="relative bg-primary/30 p-5 rounded-full">
                      <FileSpreadsheet className="h-12 w-12 text-primary" />
                    </div>
                  </div>
                </div>
                <h1 className="text-4xl font-bold mb-4">
                  Instafel <span className="text-primary">Backup Library</span>
                </h1>
                <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                  Browse community-created backups that you can import directly
                  into Instafel. Save your favorite settings and easily restore
                  them anytime.
                </p>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3, duration: 0.6 }}
                className="mb-12"
              >
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-3xl mx-auto">
                  <Card className="border-2 hover:border-primary/40 transition-all duration-300">
                    <CardContent className="p-6">
                      <div className="mb-4">
                        <Package className="h-10 w-10 text-primary" />
                      </div>
                      <h3 className="text-xl font-semibold mb-2">
                        Import Backup
                      </h3>
                      <p className="text-muted-foreground mb-4">
                        Import backups directly from this library into your
                        Instafel app. All your settings will be instantly
                        applied.
                      </p>
                      <Badge variant="outline" className="mb-2">
                        Available in Instafel v1.5+
                      </Badge>
                    </CardContent>
                  </Card>

                  <Card className="border-2 hover:border-primary/40 transition-all duration-300">
                    <CardContent className="p-6">
                      <div className="mb-4">
                        <FileCog2Icon className="h-10 w-10 text-primary" />
                      </div>
                      <h3 className="text-xl font-semibold mb-2">
                        Create Backup
                      </h3>
                      <p className="text-muted-foreground mb-4">
                        Share your perfect setup with the community by creating
                        and uploading your own backups through the app.
                      </p>
                      <Badge variant="outline" className="mb-2">
                        Managed via Telegram
                      </Badge>
                    </CardContent>
                  </Card>
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.6, duration: 0.6 }}
              >
                <h2 className="text-2xl font-bold text-center mb-8">
                  Available Backups
                </h2>

                <div className="bg-white dark:bg-gray-800/50 rounded-xl shadow-md overflow-hidden border-2">
                  <div className="px-4 py-5 sm:p-6">
                    {data.backups.length > 0 ? (
                      <div className="grid gap-4">
                        {data.backups.map((backup, index) => (
                          <motion.div
                            key={index}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{
                              delay: 0.7 + index * 0.1,
                              duration: 0.5,
                            }}
                            whileHover={{ x: 5 }}
                            className="transition-all duration-300"
                          >
                            <Link
                              href={`/backup?id=${backup.id}`}
                              className="block p-4 hover:bg-gray-50 dark:hover:bg-gray-700/50 rounded-lg transition-colors"
                            >
                              <div className="flex items-start">
                                <div className="bg-gradient-to-r from-gray-100 to-gray-200 dark:from-gray-700 dark:to-gray-800 p-3 rounded-lg mr-4">
                                  <FileCog2Icon className="h-6 w-6 text-primary" />
                                </div>
                                <div className="flex-grow">
                                  <div className="flex items-center justify-between">
                                    <h3 className="font-semibold text-lg">
                                      {backup.name}
                                    </h3>
                                    <ChevronRight className="h-5 w-5 text-muted-foreground" />
                                  </div>
                                  <p className="text-sm text-muted-foreground">
                                    Created by{" "}
                                    <span className="font-medium">
                                      {backup.author}
                                    </span>
                                  </p>
                                </div>
                              </div>
                            </Link>
                          </motion.div>
                        ))}
                      </div>
                    ) : (
                      <div className="py-12 text-center">
                        <div className="inline-flex items-center justify-center rounded-full bg-gray-100 dark:bg-gray-800 p-4 mb-4">
                          <FileText className="h-8 w-8 text-muted-foreground" />
                        </div>
                        <h3 className="text-lg font-medium mb-2">
                          No backups available
                        </h3>
                        <p className="text-muted-foreground mb-6">
                          No backups have been uploaded to the library yet.
                        </p>
                      </div>
                    )}
                  </div>
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 1, duration: 0.6 }}
                className="mt-12 text-center"
              >
                <div className="bg-gradient-to-r from-primary/5 to-primary/10 rounded-xl p-8">
                  <h3 className="text-xl font-bold mb-3">
                    Want to Contribute?
                  </h3>
                  <p className="text-muted-foreground mb-6 max-w-lg mx-auto">
                    Share your Instafel settings with the community and help
                    others discover optimal configurations.
                  </p>
                  <Button asChild>
                    <Link href="https://t.me/instafel">Join Our Community</Link>
                  </Button>
                </div>
              </motion.div>
            </div>
          </section>
          <Footer />
        </div>
      ) : (
        <div className="py-12 px-6 text-center">
          <Card className="max-w-md mx-auto p-6">
            <h2 className="text-xl font-bold mb-4">Failed to Load Backups</h2>
            <p className="text-muted-foreground mb-6">
              We couldn&apos;t retrieve the backup information. Please check
              your connection or try again later.
            </p>
            <Button onClick={() => window.location.reload()}>Retry</Button>
          </Card>
        </div>
      )}
    </AnimatePresence>
  );
}
