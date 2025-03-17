"use client";

import { AnimatePresence, motion } from "framer-motion";
import Link from "next/link";
import { Book } from "../../components/Icons";
import { getAllPostsSync } from "../../lib/blog";
import { useState } from "react";
import { Separator } from "../../components/ui/separator";
import Footer from "../../components/Footer";

export default function GuidePage() {
  const [hoveredId, setHoveredId] = useState<number | null>(null);
  const guides = getAllPostsSync();

  const getColorClasses = (color: string) => {
    const classes = {
      indigo: "bg-indigo-50 text-indigo-600 border-indigo-100",
      rose: "bg-rose-50 text-rose-600 border-rose-100",
      sky: "bg-sky-50 text-sky-600 border-sky-100",
      purple: "bg-purple-50 text-purple-600 border-purple-100",
      orange: "bg-orange-50 text-orange-600 border-orange-100",
    };
    return classes[color as keyof typeof classes] || classes.indigo;
  };

  return (
    <AnimatePresence>
      <div className="min-h-screen py-16">
        <header className="container mx-auto px-4 mb-16">
          <div className="max-w-4xl mx-auto text-center">
            <motion.h1
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{
                duration: 0.8,
                ease: "easeOut",
              }}
              className="text-7xl font-black tracking-tight mb-6 bg-gradient-to-r from-gray-900 to-gray-600 text-transparent bg-clip-text"
            >
              Guides
            </motion.h1>
            <motion.p
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{
                duration: 0.8,
                ease: "easeOut",
              }}
              className="text-xl text-gray-600 max-w-2xl mx-auto"
            >
              You can learn everything about Instafel in here!
            </motion.p>
          </div>
        </header>

        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{
            delay: 0.15,
            duration: 0.8,
            ease: "easeOut",
          }}
          className="container mx-auto px-4 mb-24"
          whileHover={{ scale: 1.02 }}
        >
          <div className="max-w-4xl mx-auto">
            <div className="relative overflow-hidden rounded-3xl bg-gradient-to-r from-rose-100 to-teal-100 p-1">
              <div className="bg-white rounded-[1.4rem] p-8 md:p-12">
                <div className="flex flex-col md:flex-row gap-8 md:gap-16">
                  <div className="flex-1">
                    <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-rose-50 text-rose-600 text-sm font-medium mb-6">
                      <span className="w-2 h-2 rounded-full bg-rose-600"></span>
                      Suggested
                    </div>
                    <h2 className="text-4xl font-bold mb-4 text-gray-900">
                      First Look at Instafel
                    </h2>
                    <p className="text-gray-600 mb-6 text-lg">
                      With this article, you will learn about the purpose of
                      Instafel, its working methods and what it does.
                    </p>
                    <button className="group inline-flex items-center gap-2 px-6 py-3 bg-gray-900 text-white rounded-full hover:bg-gray-800 transition-colors">
                      READ NOW
                      <svg
                        className="w-4 h-4 transform transition-transform group-hover:translate-x-1"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M17 8l4 4m0 0l-4 4m4-4H3"
                        />
                      </svg>
                    </button>
                  </div>
                  <div className="hidden md:flex items-center justify-center">
                    <div className="text-[12rem] font-black leading-none text-gray-900/5">
                      01
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </motion.div>

        <div className="container mx-auto px-4">
          <div className="max-w-4xl mx-auto">
            <div className="text-sm font-medium text-gray-900 mb-8">
              ALL GUIDES
            </div>
            <div className="grid gap-6">
              {guides.map((guide) => (
                <motion.div
                  key={guide.id}
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.15,
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                  className="container mx-auto px-4 mb-24"
                >
                  <article
                    className="group relative bg-white rounded-2xl p-6 transition-all duration-300 hover:shadow-lg border border-gray-100"
                    onMouseEnter={() => setHoveredId(guide.id)}
                    onMouseLeave={() => setHoveredId(null)}
                  >
                    <div className="flex flex-col md:flex-row md:items-center gap-6">
                      <div className="flex-1">
                        <h2 className="text-xl font-bold text-gray-900 mb-1 group-hover:text-gray-600 transition-colors">
                          {guide.title}
                        </h2>
                        <h3 className="text-gray-600 mb-2">{guide.subtitle}</h3>
                        <p className="text-gray-500 line-clamp-2">
                          {guide.description}
                        </p>
                      </div>

                      <div className="hidden md:flex items-center justify-center w-16">
                        <div
                          className={`p-3 rounded-xl transition-all duration-300 ${
                            hoveredId === guide.id
                              ? getColorClasses(guide.color)
                              : "text-gray-400"
                          }`}
                        >
                          <svg
                            className="w-6 h-6"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth={2}
                              d="M17 8l4 4m0 0l-4 4m4-4H3"
                            />
                          </svg>
                        </div>
                      </div>
                    </div>
                  </article>
                </motion.div>
              ))}
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </AnimatePresence>
  );
}
