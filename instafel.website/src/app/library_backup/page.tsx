"use client";

import { AnimatePresence, motion } from "framer-motion";
import Link from "next/link";
import { Book, IflLibraryBackup } from "@/components/Icons";
import { getAllPostsSync, getInstafelBackups } from "@/lib/blog";
import { useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import { Separator } from "@radix-ui/react-dropdown-menu";
import Footer from "@/components/Footer";

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
  useEffect(() => {
    const fetchData = async () => {
      var requestUrl =
        "https://raw.githubusercontent.com/instafel/backups/refs/heads/main/backups.json";
      const res = await fetch(requestUrl);
      const result: BackupInfo = await res.json();
      setData(result);
    };
    fetchData();
  }, []);

  return (
    <AnimatePresence>
      {data ? (
        <div className="flex flex-col">
          <section id="backup_lib" className="py-12 px-4 md:px-12">
            <div className="max-w-6xl mx-auto">
              <div className="text-center mb-12">
                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                  className="bg-gray-600 text-white p-3 rounded-lg inline-block mb-4"
                >
                  <IflLibraryBackup size={24} />
                </motion.div>
                <motion.h2
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="text-3xl font-bold mb-4"
                >
                  Backup Library
                </motion.h2>
                <motion.p
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.5,
                    duration: 0.6,
                    ease: "easeOut",
                  }}
                  className="text-muted-foreground  max-w-2xl mx-auto"
                >
                  On this page you can view all backup files available for
                  Instafel, and also you can use features like automatic updates
                  with the library within the app!
                </motion.p>
              </div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 1,
                  duration: 0.8,
                  ease: "easeOut",
                }}
              >
                <div className="bg-white rounded-xl shadow-md overflow-hidden">
                  <div className="px-4">
                    <div>
                      <div className="mb-2" />
                      {data.backups.map((backup, index) => (
                        <motion.div
                          key={index}
                          initial={{ opacity: 0, y: 30 }}
                          animate={{ opacity: 1, y: 0 }}
                          transition={{
                            delay: index * 0.15,
                            duration: 0.6,
                            ease: "easeOut",
                          }}
                        >
                          <a
                            href={`/backup?id=${backup.id}`}
                            className="block p-3 hover:bg-gray-50 rounded-lg mb-2 flex items-start"
                          >
                            <div className="bg-gray-200 p-2 rounded-lg mr-3">
                              <IflLibraryBackup />
                            </div>
                            <div>
                              <div className="flex items-center">
                                <h4 className="font-medium ">{backup.name}</h4>
                              </div>
                              <p className="text-sm text-muted-foreground ">
                                Created by {backup.author}
                              </p>
                            </div>
                          </a>
                        </motion.div>
                      ))}
                    </div>
                  </div>
                </div>
              </motion.div>
            </div>
          </section>
          <Footer />
        </div>
      ) : (
        <LoadingBar />
      )}
    </AnimatePresence>
  );
}
