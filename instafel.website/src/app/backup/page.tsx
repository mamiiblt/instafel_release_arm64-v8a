"use client";

import { AnimatePresence, motion } from "framer-motion";
import { IflDownload, IflLibrary } from "@/components/Icons";
import { Suspense, useEffect, useState } from "react";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useSearchParams } from "next/navigation";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";

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

  useEffect(() => {
    const fetchData = async () => {
      const requestUrl = `https://raw.githubusercontent.com/instafel/backups/refs/heads/main/${id}/manifest.json`;
      const res = await fetch(requestUrl);
      const result: Resp = await res.json();
      setData(result);
    };
    fetchData();
  }, [id]);

  const handleDownloadBackup = (id: string, version: string) => {
    const link = document.createElement("a");
    link.href = `https://api.mamiiblt.me/ifl/dw_backup?id=${id}&version=${version}`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const handleImportInstafel = () => {
    // Implement import functionality
    console.log("Importing to Instafel...");
  };

  return (
    <AnimatePresence>
      {data ? (
        <div>
          <div className="container mx-auto py-4 px-4">
            <div>
              <Card className="border-2 shadow-lg">
                <CardHeader className="">
                  <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                    <div>
                      <motion.h1
                        initial={{ opacity: 0, y: 30 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{
                          duration: 0.8,
                          ease: "easeOut",
                        }}
                        className="text-3xl font-bold"
                      >
                        {data.manifest.name}
                      </motion.h1>
                      <motion.div
                        initial={{ opacity: 0, y: 30 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{
                          delay: 0.3,
                          duration: 0.8,
                          animate: "show",
                        }}
                        className="flex flex-wrap items-center gap-2 mt-2"
                      >
                        <Badge
                          variant="outline"
                          className="text-sm font-medium"
                        >
                          Created by {data.manifest.author}
                        </Badge>
                        <Badge
                          variant="secondary"
                          className="text-sm font-medium"
                        >
                          {data.manifest.version_name}
                        </Badge>
                      </motion.div>
                    </div>
                    <motion.div
                      initial={{ opacity: 0, y: 30 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{
                        delay: 0.5,
                        duration: 0.8,
                        ease: "easeOut",
                      }}
                    >
                      <Button
                        onClick={() =>
                          handleDownloadBackup(id, data.manifest.version_name)
                        }
                        className="bg-primary w-full mb-4 hover:bg-primary/90"
                      >
                        <IflDownload className="mr-2 h-4 w-4" />
                        Download
                      </Button>
                      <Button
                        onClick={handleImportInstafel}
                        variant="outline"
                        className="border-primary w-full text-primary hover:bg-primary/10"
                      >
                        <IflLibrary className="mr-2 h-4 w-4" />
                        Import in Instafel
                      </Button>
                    </motion.div>
                  </div>
                </CardHeader>
                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.7,
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                >
                  <CardContent className="">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div>
                        <h3 className="text-sm font-medium text-muted-foreground">
                          Description
                        </h3>
                        <p className="font-medium">
                          {data.manifest.description}
                        </p>
                      </div>
                      <div>
                        <h3 className="text-sm font-medium text-muted-foreground">
                          Version
                        </h3>
                        <p className="font-medium">
                          {data.manifest.version_name}
                        </p>
                      </div>
                      <div>
                        <h3 className="text-sm font-medium text-muted-foreground">
                          Last Update
                        </h3>
                        <p className="font-medium">
                          {data.manifest.last_updated}
                        </p>
                      </div>
                      <div>
                        <h3 className="text-sm font-medium text-muted-foreground">
                          Changelog
                        </h3>
                        <p className="font-medium whitespace-pre-wrap">
                          {data.manifest.changelog}
                        </p>
                      </div>
                    </div>
                  </CardContent>
                </motion.div>
              </Card>
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
