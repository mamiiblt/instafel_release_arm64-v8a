"use client";

import { AnimatePresence } from "framer-motion";
import { getAllPostsSync } from "@/lib/blog";
import Footer from "@/components/Footer";

export default function GuidePage() {
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
    <>
      <div className="flex min-h-screen flex-col items-center justify-center bg-linear-to-b from-gray-50 to-gray-100 p-4">
        <main className="flex w-full max-w-5xl flex-1 flex-col items-center justify-center text-center">
          <h1 className="text-4xl font-bold tracking-tight text-gray-900 sm:text-6xl md:text-7xl">
            Coming soon...
          </h1>
          <p className="mt-6 text-lg text-gray-600">
            This part is still not finished, so we have to wait...
          </p>
        </main>
      </div>
      <Footer />
    </>
  );
}
