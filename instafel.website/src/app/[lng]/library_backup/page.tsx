"use client";

import { AnimatePresence, motion } from "framer-motion";
import { FileCog2Icon, FileSpreadsheet, ChevronRight } from "lucide-react";
import { Suspense, useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Card } from "@/components/ui/card";
import { useT } from "@/i18n/client";

interface Backup {
  id: string;
  name: string;
  author: string;
}

interface BackupInfo {
  tag_name: string;
  backups: Backup[];
}

export default function LibraryBackup() {
  return (
    <Suspense>
      <LibraryBackupContent />
    </Suspense>
  );
}

function LibraryBackupContent() {
  const { t } = useT("library_backup");
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
        console.error(t("errors.fetchBackupsFailed"), error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [t]);

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
                <h1 className="text-4xl font-bold mb-4">{t("title")}</h1>
                <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                  {t("description")}
                </p>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3, duration: 0.6 }}
              >
                <div className="bg-white dark:bg-gray-800/50 rounded-xl shadow-md overflow-hidden border-2 mb-8">
                  <div className="px-4 py-5 sm:p-6">
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
                                  {t("createdBy", { author: backup.author })}
                                </p>
                              </div>
                            </div>
                          </Link>
                        </motion.div>
                      ))}
                    </div>
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
                    {t("contribute.title")}
                  </h3>
                  <p className="text-muted-foreground mb-6 max-w-lg mx-auto">
                    {t("contribute.description")}
                  </p>
                  <Button asChild>
                    <Link href="https://t.me/instafel">
                      {t("contribute.button")}
                    </Link>
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
            <h2 className="text-xl font-bold mb-4">{t("error.title")}</h2>
            <p className="text-muted-foreground mb-6">
              {t("error.description")}
            </p>
            <Button onClick={() => window.location.reload()}>
              {t("error.retryButton")}
            </Button>
          </Card>
        </div>
      )}
    </AnimatePresence>
  );
}
