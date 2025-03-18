"use client";

import { AnimatePresence, motion } from "framer-motion";
import { useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";
import { useRouter } from "next/navigation";
import {
  Camera,
  CircleFadingPlus,
  Ellipsis,
  Flag,
  GalleryVerticalEnd,
  PhoneCall,
  Play,
  Send,
  Shapes,
  StickyNote,
  SwatchBook,
  Wallpaper,
  Wrench,
} from "lucide-react";

interface ResponseScheme {
  manifest_version: number;
  status: string;
  flagSizes: {
    all: number;
    direct: number;
    reels: number;
    stories: number;
    feed: number;
    interface: number;
    notes: number;
    quality: number;
    camera: number;
    call: number;
    fixes: number;
    other: number;
  };
}

export default function LibraryBackupPage() {
  const router = useRouter();
  const [hoveredId, setHoveredId] = useState<number | null>(null);
  const [data, setData] = useState<ResponseScheme | null>(null);

  const categories = [
    {
      id: 0,
      name: "All",
      icon: <Shapes />,
      color: "blue",
    },
    {
      id: 1,
      name: "Direct",
      icon: <Send />,
      color: "violet",
    },
    {
      id: 2,
      name: "Reels",
      icon: <Play />,
      color: "emerald",
    },
    {
      id: 3,
      name: "Stories",
      icon: <CircleFadingPlus />,
      color: "orange",
    },
    {
      id: 4,
      name: "Feed",
      icon: <GalleryVerticalEnd />,
      color: "indigo",
    },
    {
      id: 5,
      name: "Interface",
      icon: <SwatchBook />,
      color: "rose",
    },
    {
      id: 6,
      name: "Notes",
      icon: <StickyNote />,
      color: "fuchsia",
    },
    {
      id: 7,
      name: "Quality",
      icon: <Wallpaper />,
      color: "amber",
    },
    {
      id: 8,
      name: "Camera",
      icon: <Camera />,
      color: "cyan",
    },
    {
      id: 9,
      name: "Call",
      icon: <PhoneCall />,
      color: "indigo",
    },
    {
      id: 10,
      name: "Fixes",
      icon: <Wrench />,
      color: "emerald",
    },
    {
      id: 11,
      name: "Other",
      icon: <Ellipsis />,
      color: "teal",
    },
  ];
  useEffect(() => {
    const fetchData = async () => {
      var requestUrl = `https://glorious-spoon-7vx9jpprpgpfrxqq-3040.app.github.dev/flag_sizes`;
      // var requestUrl = `https://iflagapi.mamiiblt.me/flag_sizes`;
      const res = await fetch(requestUrl);
      const result: ResponseScheme = await res.json();
      setData(result);
    };
    fetchData();
  }, []);

  const handleCategoryClick = (categoryId: number) => {
    router.push(`/flags?category=${categoryId}`);
  };

  const getColorClasses = (color: string, isHovered: boolean) => {
    const classes = {
      blue: {
        bg: isHovered ? "bg-blue-500" : "bg-white",
        text: isHovered ? "text-white" : "text-blue-500",
        border: "border-blue-100",
        badge: "bg-blue-50 text-blue-600",
        shadow: "hover:shadow-blue-100",
      },
      emerald: {
        bg: isHovered ? "bg-emerald-500" : "bg-white",
        text: isHovered ? "text-white" : "text-emerald-500",
        border: "border-emerald-100",
        badge: "bg-emerald-50 text-emerald-600",
        shadow: "hover:shadow-emerald-100",
      },
      violet: {
        bg: isHovered ? "bg-violet-500" : "bg-white",
        text: isHovered ? "text-white" : "text-violet-500",
        border: "border-violet-100",
        badge: "bg-violet-50 text-violet-600",
        shadow: "hover:shadow-violet-100",
      },
      orange: {
        bg: isHovered ? "bg-orange-500" : "bg-white",
        text: isHovered ? "text-white" : "text-orange-500",
        border: "border-orange-100",
        badge: "bg-orange-50 text-orange-600",
        shadow: "hover:shadow-orange-100",
      },
      rose: {
        bg: isHovered ? "bg-rose-500" : "bg-white",
        text: isHovered ? "text-white" : "text-rose-500",
        border: "border-rose-100",
        badge: "bg-rose-50 text-rose-600",
        shadow: "hover:shadow-rose-100",
      },
      teal: {
        bg: isHovered ? "bg-teal-500" : "bg-white",
        text: isHovered ? "text-white" : "text-teal-500",
        border: "border-teal-100",
        badge: "bg-teal-50 text-teal-600",
        shadow: "hover:shadow-teal-100",
      },
      cyan: {
        bg: isHovered ? "bg-cyan-500" : "bg-white",
        text: isHovered ? "text-white" : "text-cyan-500",
        border: "border-cyan-100",
        badge: "bg-cyan-50 text-cyan-600",
        shadow: "hover:shadow-cyan-100",
      },
      indigo: {
        bg: isHovered ? "bg-indigo-500" : "bg-white",
        text: isHovered ? "text-white" : "text-indigo-500",
        border: "border-indigo-100",
        badge: "bg-indigo-50 text-indigo-600",
        shadow: "hover:shadow-indigo-100",
      },
      fuchsia: {
        bg: isHovered ? "bg-fuchsia-500" : "bg-white",
        text: isHovered ? "text-white" : "text-fuchsia-500",
        border: "border-fuchsia-100",
        badge: "bg-fuchsia-50 text-fuchsia-600",
        shadow: "hover:shadow-fuchsia-100",
      },
      yellow: {
        bg: isHovered ? "bg-yellow-500" : "bg-white",
        text: isHovered ? "text-white" : "text-yellow-500",
        border: "border-yellow-200",
        badge: "bg-yellow-50 text-yellow-600",
        shadow: "hover:shadow-yellow-100",
      },
      amber: {
        bg: isHovered ? "bg-amber-500" : "bg-white",
        text: isHovered ? "text-white" : "text-amber-500",
        border: "border-amber-200",
        badge: "bg-amber-50 text-amber-600",
        shadow: "hover:shadow-amber-100",
      },
    };
    return classes[color as keyof typeof classes] || classes.blue;
  };

  return (
    <AnimatePresence>
      {data ? (
        <div>
          {" "}
          <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
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
                  <Flag size={24} />
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
                  Flag Library
                </motion.h2>
                <motion.p
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.2,
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                  className="text-muted-foreground  max-w-2xl mx-auto"
                >
                  You can list the flags in any category or all of them if you
                  want. Also you can filter flags in category with add date,
                  author or etc filters.
                </motion.p>
              </div>

              <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
                {categories.map((category) => {
                  const colorClasses = getColorClasses(
                    category.color,
                    hoveredId === category.id
                  );

                  return (
                    <motion.div
                      key={category.id}
                      initial={{ opacity: 0, y: 30 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{
                        delay: 0.4 + category.id * 0.15,
                        duration: 0.8,
                        ease: "easeOut",
                      }}
                    >
                      <div
                        onMouseEnter={() => setHoveredId(category.id)}
                        onMouseLeave={() => setHoveredId(null)}
                      >
                        <button
                          onClick={() => handleCategoryClick(category.id)}
                          className={`
            w-full p-4 h-[160px]
            flex flex-col items-center justify-center text-center
            rounded-xl border transition-all duration-300
            ${colorClasses.bg} ${colorClasses.text} ${colorClasses.border}
            hover:shadow-lg ${colorClasses.shadow}
          `}
                        >
                          <div
                            className={`
            mb-3 transform transition-transform duration-300
            ${hoveredId === category.id ? "scale-110" : ""}
          `}
                          >
                            {category.icon}
                          </div>

                          <h3
                            className={`
            font-medium mb-1 transition-colors duration-300
            ${hoveredId === category.id ? "text-white" : "text-gray-900"}
          `}
                          >
                            {category.name}
                          </h3>

                          <div className="mt-2 text-center">
                            <span
                              className={`
                    text-xs font-medium px-2 py-1 rounded-full
                    ${colorClasses.badge}
                  `}
                            >
                              {data.flagSizes[category.name.toLowerCase()]} Flag
                            </span>
                          </div>
                        </button>
                      </div>
                    </motion.div>
                  );
                })}
              </div>
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
