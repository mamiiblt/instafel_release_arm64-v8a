"use client";

import { motion } from "framer-motion";
import Footer from "@/components/Footer";
import { BookOpen, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Card, CardContent } from "@/components/ui/card";
import { useBlogs } from "@/hooks/useBlog";

export default function GuidePage() {
  const { guides, loading, error } = useBlogs();

  const getColorClasses = (color: string) => {
    const classes = {
      indigo:
        "bg-indigo-50 text-indigo-600 border-indigo-100 dark:bg-indigo-900/20 dark:text-indigo-300 dark:border-indigo-800",
      rose: "bg-rose-50 text-rose-600 border-rose-100 dark:bg-rose-900/20 dark:text-rose-300 dark:border-rose-800",
      sky: "bg-sky-50 text-sky-600 border-sky-100 dark:bg-sky-900/20 dark:text-sky-300 dark:border-sky-800",
      purple:
        "bg-purple-50 text-purple-600 border-purple-100 dark:bg-purple-900/20 dark:text-purple-300 dark:border-purple-800",
      orange:
        "bg-orange-50 text-orange-600 border-orange-100 dark:bg-orange-900/20 dark:text-orange-300 dark:border-orange-800",
    };
    return classes[color as keyof typeof classes] || classes.indigo;
  };

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
              Instafel <span className="text-primary">Guides</span>
            </h1>

            <p className="text-lg text-muted-foreground max-w-2xl mx-auto mb-8">
              Learn how to get the most out of Instafel with our comprehensive
              guides and step-by-step tutorials.
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
                <p className="text-red-500">Error loading guides: {error}</p>
              </div>
            ) : (
              <div className="grid md:grid-cols-2 gap-6">
                {guides.length > 0 ? (
                  guides.map((guide, index) => (
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
                              <div className="mb-3">
                                <span
                                  className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getColorClasses(guide.color)}`}
                                >
                                  {guide.subtitle}
                                </span>
                              </div>

                              <h2 className="text-xl font-bold mb-2">
                                {guide.title}
                              </h2>
                              <p className="text-muted-foreground mb-4">
                                {guide.description}
                              </p>

                              <div className="flex items-center text-primary font-medium">
                                <span>Read guide</span>
                                <ChevronRight className="ml-1 h-4 w-4 transition-transform group-hover:translate-x-1" />
                              </div>
                            </div>
                          </CardContent>
                        </Card>
                      </Link>
                    </motion.div>
                  ))
                ) : (
                  <div className="text-center p-12 border rounded-lg col-span-2">
                    <p className="text-muted-foreground">
                      No guides available yet. Check back soon!
                    </p>
                  </div>
                )}
              </div>
            )}
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.4 }}
            className="text-center max-w-xl mx-auto mt-12 pt-8 border-t"
          >
            <p className="text-muted-foreground mb-6">
              Need help or have suggestions for new guides? Join our community
              on Telegram.
            </p>
            <Button asChild>
              <a
                href="https://t.me/instafel"
                target="_blank"
                rel="noopener noreferrer"
              >
                Join Telegram Community
              </a>
            </Button>
          </motion.div>
        </div>
      </div>
      <Footer />
    </>
  );
}
