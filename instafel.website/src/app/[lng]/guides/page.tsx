"use client";

import { motion } from "framer-motion";
import Footer from "@/components/Footer";
import { BookOpen, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Card, CardContent } from "@/components/ui/card";
import { useGuides } from "@/hooks/useGuides";
import { useT } from "@/i18n/client";

export default function GuidePage() {
  const { t } = useT("guides");
  const { guides, loading, error } = useGuides();

  return (
    <>
      <div className="min-h-screen bg-background py-16">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="text-center max-w-3xl mx-auto mb-16"
          >
            <div className="inline-flex items-center justify-center mb-6 relative">
              <div
                className="absolute inset-0 rounded-full bg-primary/20 animate-ping"
                style={{ animationDuration: "3s" }}
              ></div>
              <div className="relative bg-primary/10 p-5 rounded-full">
                <BookOpen className="h-12 w-12 text-primary" />
              </div>
            </div>

            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold tracking-tight mb-6">
              {t("title")}
            </h1>

            <p className="text-lg text-muted-foreground max-w-2xl mx-auto mb-8">
              {t("subtitle")}
            </p>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.2 }}
            className="max-w-4xl mx-auto"
          >
            {loading ? (
              <div className="flex justify-center py-20">
                <div className="animate-spin h-8 w-8 border-4 border-primary border-t-transparent rounded-full"></div>
              </div>
            ) : error ? (
              <div className="text-center p-12 border rounded-lg col-span-2">
                <p className="text-red-500">
                  {t("errorLoading", { errStr: error })}
                </p>
              </div>
            ) : (
              <div className="grid md:grid-cols-2 gap-6">
                {guides.map((guide, index) => (
                  <motion.div
                    key={guide.id}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: 0.3 + index * 0.1, duration: 0.5 }}
                    whileHover={{ y: -5 }}
                    className="transition-all duration-300"
                  >
                    <Link href={`/guides/${guide.id}`}>
                      <Card className="h-full overflow-hidden border-2 hover:border-primary/60 transition-colors">
                        <CardContent className="p-0">
                          <div className="p-6">
                            <h2 className="text-xl font-bold mb-2">
                              {guide.title}
                            </h2>
                            <p className="text-muted-foreground mb-4">
                              {guide.description}
                            </p>

                            <div className="flex items-center text-primary font-medium">
                              <span>{t("readGuide")}</span>
                              <ChevronRight className="ml-1 h-4 w-4 transition-transform group-hover:translate-x-1" />
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    </Link>
                  </motion.div>
                ))}
              </div>
            )}
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.4 }}
            className="text-center max-w-xl mx-auto mt-12 pt-8 border-t"
          >
            <p className="text-muted-foreground mb-6">{t("communityHelp")}</p>
            <Button asChild>
              <a
                href="https://t.me/instafel"
                target="_blank"
                rel="noopener noreferrer"
              >
                {t("joinCommunity")}
              </a>
            </Button>
          </motion.div>
        </div>
      </div>
      <Footer />
    </>
  );
}
