"use client";

import { AnimatePresence, motion } from "framer-motion";
import React, { Suspense, useEffect, useState } from "react";
import { LoadingBar } from "@/components/ifl";
import Footer from "@/components/Footer";
import { useRouter, useSearchParams } from "next/navigation";
import { Flag, Search, Trash, User, X } from "lucide-react";
import Link from "next/link";
import { Button } from "@/components/ui/button";

export default function FlagListPage() {
  return (
    <Suspense fallback={<LoadingBar />}>
      <FlagListPageContent />
    </Suspense>
  );
}

const categories = [
  "All Flags",
  "Direct",
  "Reels",
  "Stories",
  "Feed",
  "Interface",
  "Notes",
  "Quality",
  "Camera",
  "Call",
  "Fixes",
  "Other",
];

interface Info {
  page_size: number;
  fl_enabled: boolean;
  total_flag_size: number;
  filtered_flag_size: number;
  authors_of_all_flags: string[];
}

interface Flag {
  id: number;
  name: string;
  author: string;
  category_id: number;
  fnames: string[];
  addate: string;
  rv: number;
  rv_at: string;
}

interface ResponseScheme {
  manifest_version: number;
  info: Info;
  flags: Flag[];
}

function FlagListPageContent() {
  const router = useRouter();

  const [hoveredId, setHoveredId] = useState<number | null>(null);

  const searchParams = useSearchParams();
  const categoryId = searchParams.get("category") ?? "0";
  const [pageNumber, setPageNumber] = useState(1);
  const [paramSelectedUser, setParamSelectedUser] = useState("");
  const [paramSearchQuery, setParamSearchQuery] = useState("");
  const [refreshData, setRefreshData] = useState(true);
  const [hasAnimated, setHasAnimated] = useState(false);
  const [recallAPI, setRecallAPI] = useState(false);
  const [data, setData] = useState<ResponseScheme | null>(null);

  useEffect(() => {
    setRefreshData(true);
    const fetchData = async () => {
      var requestUrl = `https://glorious-spoon-7vx9jpprpgpfrxqq-3040.app.github.dev/list?category=${categoryId}&page=${pageNumber}`;
      // var requestUrl = `https://iflagapi.mamiiblt.me/list?category=${categoryId}&page=${pageNumber}`;

      if (paramSelectedUser.trim() != "") {
        requestUrl = requestUrl + `&flName=${paramSelectedUser}`;
      }

      if (paramSearchQuery.toString() != "") {
        requestUrl =
          requestUrl + `&flSearch=${encodeURIComponent(paramSearchQuery)}`;
      }
      const res = await fetch(requestUrl);
      const result: ResponseScheme = await res.json();
      const sortedResult = result.flags.sort(
        (a, b) => new Date(b.addate).getTime() - new Date(a.addate).getTime()
      );
      result.flags = sortedResult;

      setData(result);
      setRefreshData(false);
    };
    fetchData();
  }, [recallAPI]);

  useEffect(() => {
    if (refreshData == false) {
      setHasAnimated(true);
    }
  }, [refreshData]);

  useEffect(() => {
    if (pageNumber == 2589) {
      setPageNumber(1);
    } else {
      setRecallAPI(!recallAPI);
    }
  }, [pageNumber]);

  const searchEvent = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      event.preventDefault();
      setPageNumber(2589);
    }
  };

  const changeSelectedUser = (newUser: string) => {
    setParamSelectedUser(newUser);
    setPageNumber(2589);
  };

  const changePageNumberFromPagination = (newPageNum: number) => {
    setPageNumber(newPageNum);
  };

  const clearFilters = () => {
    setParamSelectedUser("");
    setParamSearchQuery("");
    setPageNumber(2589);
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const input = event.target.value;
    const formattedInput = input.replace(/\s+/g, " ").replace(/ /g, "_");
    setParamSearchQuery(formattedInput);
  };

  const getTypeColor = (type: string) => {
    const colors = {
      c0: "bg-blue-50 text-blue-600 border-blue-100",
      c1: "bg-violet-50 text-violet-600 border-violet-100",
      c2: "bg-emerald-50 text-emerald-600 border-emerald-100",
      c3: "bg-orange-50 text-orange-600 border-orange-100",
      c4: "bg-indigo-50 text-indigo-600 border-indigo-100",
      c5: "bg-rose-50 text-rose-600 border-rose-100",
      c6: "bg-fuchsia-50 text-fuchsia-600 border-fuchsia-100",
      c7: "bg-amber-50 text-amber-600 border-amber-100",
      c8: "bg-cyan-50 text-cyan-600 border-cyan-100",
      c9: "bg-indigo-50 text-indigo-600 border-indigo-100",
      c10: "bg-emerald-50 text-emerald-600 border-emerald-100",
      c11: "bg-teal-50 text-teal-600 border-teal-100",
    };
    return (
      colors[type as keyof typeof colors] ||
      "bg-gray-50 text-gray-600 border-gray-100"
    );
  };
  return (
    <AnimatePresence>
      {data ? (
        <div>
          <div className="min-h-screen py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-5xl mx-auto">
              <div className="text-center mb-16">
                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="inline-flex items-center justify-center gap-2 px-4 py-1.5 rounded-full bg-gray-900/5 text-sm mb-6"
                >
                  <span className="flex items-center gap-1.5">
                    Flag Library
                  </span>
                </motion.div>

                <motion.h1
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.2,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="text-5xl font-bold tracking-tight mb-4"
                >
                  {categories[categoryId]}
                </motion.h1>

                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.3,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="flex items-center justify-center gap-3 text-muted-foreground"
                >
                  <div className="h-[1px] w-10" />
                  {data.info.fl_enabled === true ? (
                    <div>
                      {parseInt(categoryId) !== 0 ? (
                        <p className="text-lg">
                          There are a total of
                          <span className="font-semibold text-gray-900">
                            {" " + data.info.filtered_flag_size + " "}
                          </span>
                          flags in this category with filters.
                        </p>
                      ) : (
                        <p className="text-lg">
                          There are a total of
                          <span className="font-semibold text-gray-900">
                            {" " + data.info.filtered_flag_size + " "}
                          </span>
                          in flag library with filters.
                        </p>
                      )}
                    </div>
                  ) : (
                    <div>
                      {parseInt(categoryId) !== 0 ? (
                        <p className="text-lg">
                          There are a total of
                          <span className="font-semibold text-gray-900">
                            {" " + data.info.total_flag_size + " "}
                          </span>
                          flags in this category
                        </p>
                      ) : (
                        <p className="text-lg">
                          There are a total of
                          <span className="font-semibold text-gray-900">
                            {" " + data.info.total_flag_size + " "}
                          </span>
                          in flag library
                        </p>
                      )}
                    </div>
                  )}
                  <div className="h-[1px] w-10" />
                </motion.div>
              </div>

              <div className="mb-6 space-y-4">
                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.4,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="flex gap-4"
                >
                  <div className="flex-1 relative">
                    <input
                      type="text"
                      placeholder="Search by flag name..."
                      value={paramSearchQuery}
                      onChange={handleChange}
                      onKeyDown={searchEvent}
                      className="w-full pl-10 pr-4 py-3 rounded-lg border border-gray-200 focus:border-gray-300 focus:ring-0 focus:outline-none"
                    />
                    <svg
                      className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={1.5}
                        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                      />
                    </svg>
                  </div>
                  <button
                    onClick={() => setPageNumber(2589)}
                    className="px-4 py-3 bg-gray-900 text-white rounded-lg hover:bg-gray-800 transition-colors flex items-center"
                  >
                    <Search className="w-5 h-5" />
                  </button>
                </motion.div>

                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.4,
                    duration: 0.8,
                    ease: "easeInOut",
                  }}
                  className="flex gap-4 items-center"
                >
                  <div className="relative">
                    <select
                      value={paramSelectedUser || ""}
                      onChange={(e) => changeSelectedUser(e.target.value)}
                      className="appearance-none pl-8 pr-2 py-2 rounded-lg border border-gray-200 focus:border-gray-300 focus:ring-0 focus:outline-none text-sm font-medium bg-white text-gray-700"
                    >
                      <option value="">All Users</option>
                      {data.info.authors_of_all_flags.map((user, index) => (
                        <option key={index} value={user}>
                          {user}
                        </option>
                      ))}
                    </select>

                    <User className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                  </div>

                  {data.info.fl_enabled === true && (
                    <div className="relative">
                      <Button
                        onClick={clearFilters}
                        className="appearance-none pl-8 pr-2 py-2 rounded-lg border border-gray-200 text-sm font-medium bg-white text-gray-700"
                      >
                        Clear Filters
                      </Button>

                      <X className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                    </div>
                  )}
                </motion.div>
              </div>

              {refreshData === false ? (
                <motion.div
                  initial={{ opacity: 0, y: hasAnimated ? 0 : 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={
                    hasAnimated
                      ? {}
                      : { delay: 0.6, duration: 0.8, ease: "easeInOut" }
                  }
                  className="grid gap-4 mb-6"
                >
                  <div className="grid gap-4 mb-6">
                    {data.flags.map((flag, index) => (
                      <Link
                        key={index}
                        href={"/flag?id=" + flag.id}
                        className="group relative"
                      >
                        <div
                          className={`
          relative overflow-hidden bg-white rounded-xl border border-gray-200
          transition-all duration-300
          ${hoveredId === index ? "shadow-lg scale-[1.01]" : "hover:shadow"}
        `}
                        >
                          <div className="p-4 sm:p-6">
                            <div className="flex items-start justify-between">
                              <div className="flex-12">
                                <div className="flex items-center gap-3">
                                  <h3 className="text-lg font-semibold text-gray-900">
                                    {flag.name}
                                  </h3>
                                </div>
                                <div className="mb-2">
                                  {flag.fnames.map((fname, index) => (
                                    <div
                                      key={index}
                                      className="flex items-center gap-3"
                                    >
                                      <h3 className="text-sm font-regular text-gray-900">
                                        {fname}
                                      </h3>
                                    </div>
                                  ))}
                                </div>

                                <div className="items-center gap-4 text-sm text-gray-500">
                                  <div className="flex items-center gap-2">
                                    <svg
                                      className="w-4 h-4"
                                      fill="none"
                                      stroke="currentColor"
                                      viewBox="0 0 24 24"
                                    >
                                      <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={1.5}
                                        d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                                      />
                                    </svg>
                                    Added by {flag.author}
                                  </div>
                                  <div className="flex items-center gap-2">
                                    <svg
                                      className="w-4 h-4"
                                      fill="none"
                                      stroke="currentColor"
                                      viewBox="0 0 24 24"
                                    >
                                      <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={1.5}
                                        d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                                      />
                                    </svg>
                                    Uploaded at{" "}
                                    {new Date(flag.addate).toLocaleDateString(
                                      "en-US"
                                    )}
                                  </div>
                                  {flag.rv === 1 && (
                                    <div className="flex items-center gap-2">
                                      <Trash className="w-4 h-4" />
                                      Removed with v{flag.rv_at}
                                    </div>
                                  )}
                                </div>
                              </div>

                              <button
                                className={`
                  p-2 rounded-lg text-gray-400
                  transition-all duration-300 ease-in-out
                  hover:text-gray-900 hover:bg-gray-100`}
                              >
                                {categoryId === "0" && (
                                  <span
                                    className={`
                            px-2.5 py-1 rounded-full text-xs font-medium border
                            ${getTypeColor("c" + flag.category_id)}
                          `}
                                  >
                                    {categories[flag.category_id]}
                                  </span>
                                )}
                              </button>
                            </div>
                          </div>

                          <div
                            className={`
              absolute inset-0 
              bg-gradient-to-tr from-gray-100/0 via-gray-100/0 to-gray-100/50
              transition-opacity duration-300`}
                          />
                        </div>
                      </Link>
                    ))}
                  </div>
                </motion.div>
              ) : (
                <div className="grid gap-4 mb-6 min-h-[400px] relative">
                  <div className="absolute inset-0 flex flex-col items-center justify-center">
                    <div className="relative">
                      <div className="h-24 w-24 rounded-full border-t-2 border-b-2 border-gray-900 animate-spin"></div>
                      <div className="absolute inset-0 flex items-center justify-center">
                        <Flag className="w-10 h-10 text-gray900" />
                      </div>
                    </div>
                    <p className="mt-4 text-gray-600 font-medium">Loading...</p>
                  </div>
                </div>
              )}

              <div className="flex justify-center border-t border-gray-200 pt-4">
                <div className="flex gap-2">
                  <button
                    onClick={() =>
                      changePageNumberFromPagination(pageNumber - 1)
                    }
                    disabled={pageNumber === 1}
                    className={`
                  p-2 rounded-lg border text-sm font-medium
                  ${
                    pageNumber === 1
                      ? "bg-gray-50 text-gray-400 border-gray-200 cursor-not-allowed"
                      : "bg-white text-gray-700 border-gray-200 hover:border-gray-300"
                  }
                `}
                  >
                    <svg
                      className="w-5 h-5"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M15 19l-7-7 7-7"
                      />
                    </svg>
                  </button>
                  {[...Array(data.info.page_size)].map((_, i) => (
                    <button
                      onClick={() => changePageNumberFromPagination(i + 1)}
                      key={i + 1}
                      className={`
                    w-10 h-10 rounded-lg border text-sm font-medium
                    transition-colors
                    ${
                      pageNumber === i + 1
                        ? "bg-gray-900 text-white border-gray-900"
                        : "bg-white text-gray-700 border-gray-200 hover:border-gray-300"
                    }
                  `}
                    >
                      {i + 1}
                    </button>
                  ))}
                  <button
                    onClick={() =>
                      changePageNumberFromPagination(pageNumber + 1)
                    }
                    disabled={pageNumber === data.info.page_size}
                    className={`
                  p-2 rounded-lg border text-sm font-medium
                  ${
                    pageNumber === data.info.page_size
                      ? "bg-gray-50 text-gray-400 border-gray-200 cursor-not-allowed"
                      : "bg-white text-gray-700 border-gray-200 hover:border-gray-300"
                  }
                `}
                  >
                    <svg
                      className="w-5 h-5"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M9 5l7 7-7 7"
                      />
                    </svg>
                  </button>
                </div>
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
