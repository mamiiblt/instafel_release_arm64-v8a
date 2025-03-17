"use client";

import { AnimatePresence, motion } from "framer-motion";
import React, { Suspense, useEffect, useState } from "react";
import { LoadingBar } from "../../components/ifl";
import Footer from "../../components/Footer";
import { useRouter, useSearchParams } from "next/navigation";
import {
  AlertTriangle,
  ArrowRight,
  Calendar,
  Check,
  CirclePlus,
  Clock,
  Copy,
  Delete,
  Download,
  FileCog2Icon,
  FileText,
  Flag,
  GitPullRequestCreateArrow,
  IdCard,
  Info,
  Plus,
  Search,
  Shapes,
  ShoppingCart,
  Tag,
  TagIcon,
  ToggleLeft,
  Trash,
  User,
  X,
} from "lucide-react";
import Link from "next/link";
import { Button } from "../../components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../components/ui/card";
import { Badge } from "../../components/ui/badge";
import { FlagLibrartCarousel } from "../../components/carousel";
import { Switch } from "../../components/ui/switch";
import { Separator } from "../../components/ui/separator";

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

interface Note {
  text: string;
  type: number; // 0 = info ve 1 = warning olabiler
}

interface FlagProp {
  name: string;
  desc: string;
  value_type: number; // 0 ise bool, 1 ise number, 3 ise text
}

interface Flag {
  name: string;
  props: FlagProp;
}

interface Changelog {
  version: string;
  date: string;
  logs: string[];
}

interface Data {
  id: number;
  name: string;
  fnames: string[];
  author: string;
  description: string;
  imgs: string[];
  notes: Note[];
  changelogs: Changelog[];
  uncompitable_flags: string[];
  flag_data: Flag[];
  category_id: number;
  addate: string;
  rv: boolean;
  rv_at: string;
  ad_at: string;
}

interface ResponseScheme {
  manifest_version: number;
  status: string;
  data: Data;
}

function FlagListPageContent() {
  const router = useRouter();

  const [hoveredId, setHoveredId] = useState<number | null>(null);

  const searchParams = useSearchParams();
  const flagId = searchParams.get("id") ?? "0";
  const [data, setData] = useState<ResponseScheme | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      var requestUrl = `https://glorious-spoon-7vx9jpprpgpfrxqq-3040.app.github.dev/flag?id=${flagId}`;

      const res = await fetch(requestUrl);
      const result: ResponseScheme = await res.json();
      setData(result);
    };
    fetchData();
  }, []);

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
        <div className="container mx-auto max-w-4xl">
          <div className="mb-8 p-6">
            <div className="flex flex-col md:flex-row gap-6">
              <div className="flex-1">
                <div className="flex justify-between items-start">
                  <div>
                    <h1 className="text-2xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                      {data.data.name}
                    </h1>
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mt-4">
                  <div className="flex items-center gap-2">
                    <User className="h-4 w-4 text-primary" />
                    <span className="text-sm text-muted-foreground">
                      Added by {data.data.author}
                    </span>
                  </div>

                  <div className="flex items-center gap-2">
                    <Shapes className="h-4 w-4 text-primary" />
                    <span className="text-sm text-muted-foreground">
                      {categories[data.data.category_id]}
                    </span>
                  </div>

                  <div className="flex items-center gap-2">
                    <Calendar className="h-4 w-4 text-primary" />
                    <span className="text-sm text-muted-foreground">
                      Last updated at{" "}
                      {new Date(data.data.addate).toLocaleDateString("en-US")}
                    </span>
                  </div>

                  {data.data.ad_at !== null ? (
                    <div className="flex items-center gap-2">
                      <Plus className="h-4 w-4 text-primary" />
                      <span className="text-sm text-muted-foreground">
                        Added in v{data.data.ad_at}
                      </span>
                    </div>
                  ) : (
                    <div className="flex items-center gap-2">
                      <CirclePlus className="h-4 w-4 text-primary" />
                      <span className="text-sm text-muted-foreground">
                        Added in unknown version
                      </span>
                    </div>
                  )}

                  {data.data.rv === true ? (
                    <div className="flex items-center gap-2">
                      <Trash className="h-4 w-4 text-primary" />
                      <span className="text-sm text-muted-foreground">
                        Removed in v{data.data.rv_at}
                      </span>
                    </div>
                  ) : (
                    <Link href="/download?version=latest">
                      <div className="flex items-center gap-2">
                        <Check className="h-4 w-4 text-primary" />
                        <span className="text-sm text-muted-foreground">
                          Available on latest version
                        </span>
                      </div>
                    </Link>
                  )}

                  {data.data.fnames.map((fname, index) => (
                    <div key={index} className="flex items-center gap-2">
                      <Tag className="h-4 w-4 text-primary" />
                      <span className="text-sm text-muted-foreground">
                        {fname}
                      </span>
                    </div>
                  ))}
                </div>

                <div className="mt-4">
                  <h3 className="text-md font-semibold mb-2 flex items-center gap-2">
                    <span className="h-4 w-1 bg-primary rounded-full"></span>
                    Description
                  </h3>
                  <Card className="shadow-md">
                    <CardContent className="p-0">
                      <div className="p-4 rounded-lg">
                        <p className="text-sm text-foreground/80 leading-relaxed">
                          {data.data.description}
                        </p>
                      </div>
                    </CardContent>
                  </Card>
                </div>

                {data.data.imgs.length !== 0 && (
                  <div className="mt-4">
                    <h3 className="text-md font-semibold mb-2 flex items-center gap-2">
                      <span className="h-4 w-1 bg-primary rounded-full"></span>
                      Screenshots
                    </h3>
                    <FlagLibrartCarousel images={data.data.imgs} />
                  </div>
                )}

                <div className="mt-4">
                  <h3 className="text-md font-semibold mb-2 flex items-center gap-2">
                    <span className="h-4 w-1 bg-primary rounded-full"></span>
                    Flag
                  </h3>
                  <Card className="shadow-md">
                    <CardHeader className="pb-0">
                      <CardTitle className="text-md font-medium">
                        videolite configs ig
                      </CardTitle>
                    </CardHeader>

                    <CardContent className="space-y-0">
                      <div className="py-4">
                        <div className="flex justify-between items-center">
                          <div className="space-y-0.5 w-2/3">
                            <div className="flex items-center">
                              <h3 className="font-medium">
                                <base href="" />
                                locklist codec decoder start retry transcode
                              </h3>
                            </div>
                            <div className="flex items-center text-xs text-muted-foreground">
                              <Info className="h-4 w-4 mr-1.5 text-muted-foreground" />
                              <span>(default[null server value], latest)</span>
                            </div>
                          </div>
                          <div className="flex justify-end w-1/3">
                            <Switch />
                          </div>
                        </div>
                      </div>

                      <Separator className="my-0" />

                      <div className="py-4">
                        <div className="flex justify-between items-center">
                          <div className="space-y-0.5 w-2/3">
                            <div className="flex items-center">
                              <h3 className="font-medium">
                                <base href="" />
                                locklist codec decoder start retry transcode
                              </h3>
                            </div>
                            <div className="flex items-center text-xs text-muted-foreground">
                              <Info className="h-4 w-4 mr-1.5 text-muted-foreground" />
                              <span>(default[null server value], latest)</span>
                            </div>
                          </div>
                          <div className="flex justify-end ">
                            <h3 className="font-medium">
                              <base href="" />
                              clean
                            </h3>
                          </div>
                        </div>
                      </div>

                      <Separator className="my-0" />

                      <div className="pt-4 ">
                        <div className="flex justify-between items-center">
                          <div className="space-y-0.5 w-2/3">
                            <div className="flex items-center">
                              <Button
                                variant="default"
                                size="sm"
                                className="flex items-center gap-1.5"
                              >
                                <FileCog2Icon className="h-4 w-4" />
                                Add into backup
                              </Button>
                              <Button
                                variant="secondary"
                                size="sm"
                                className="ml-4 flex items-center gap-1.5"
                              >
                                <Copy className="h-4 w-4" />
                                Copy RAW Content
                              </Button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </div>

                {data.data.uncompitable_flags.length !== 0 && (
                  <div className="mt-4">
                    <h3 className="text-md font-semibold mb-2 flex items-center gap-2">
                      <span className="h-4 w-1 bg-primary rounded-full"></span>
                      Uncompitable Flags
                    </h3>
                    <Card className="shadow-md">
                      <CardContent className="p-0">
                        <div className="p-4 rounded-lg">
                          <p className="text-sm text-foreground/80 leading-relaxed">
                            This flag is not compatible with the flags listed
                            below, if you experience problems with this flag
                            please try disabling/changing flags listed in below
                            firstly.
                          </p>
                          <div className="mt-2">
                            <div className="rounded-lg bg-red-100">
                              {data.data.uncompitable_flags.map(
                                (flag, index) => (
                                  <div
                                    key={index}
                                    className={`p-3 border-0 last:border-b-0`}
                                  >
                                    <div className="flex items-start gap-3">
                                      <div>
                                        <p
                                          className={`text-xs text-red-600 font-medium`}
                                        >
                                          {flag}
                                        </p>
                                      </div>
                                    </div>
                                  </div>
                                )
                              )}
                            </div>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  </div>
                )}

                {data.data.notes.length !== 0 && (
                  <div className="mt-4">
                    <h3 className="text-md font-semibold mb-2 flex items-center gap-2">
                      <span className="h-4 w-1 bg-primary rounded-full"></span>
                      Notes
                    </h3>
                    <Card className="border border-border/50">
                      <CardContent className="p-0">
                        {data.data.notes.map((note, index) => (
                          <div
                            key={index}
                            className={`p-3 border-b last:border-b-0 rounded-lg ${
                              note.type === 1 ? "bg-amber-50" : ""
                            }`}
                          >
                            <div className="flex items-start gap-3">
                              {note.type === 0 ? (
                                <Info className="h-4 w-4 text-primary mt-0.5 flex-shrink-0" />
                              ) : (
                                <AlertTriangle className="h-4 w-4 text-amber-500 mt-0.5 flex-shrink-0" />
                              )}
                              <div>
                                <p
                                  className={`text-xs ${
                                    note.type === 1
                                      ? "text-amber-700 dark:text-amber-400 font-medium"
                                      : "text-foreground"
                                  }`}
                                >
                                  {note.text}
                                </p>
                              </div>
                            </div>
                          </div>
                        ))}
                      </CardContent>
                    </Card>
                  </div>
                )}

                <div className="mt-4">
                  <h3 className="text-md font-semibold mb-2 flex items-center gap-2">
                    <span className="h-4 w-1 bg-primary rounded-full"></span>
                    Changelogs
                  </h3>
                  <Card className="border border-border/50">
                    <CardContent className="p-0">
                      {data.data.changelogs.map((entry, index) => (
                        <div
                          key={index}
                          className="p-4 border-b last:border-b-0"
                        >
                          <div className="flex justify-between mb-2">
                            <h3 className="font-medium">v{entry.version}</h3>
                            <span className="text-sm text-muted-foreground">
                              {entry.date}
                            </span>
                          </div>
                          <ul className="space-y-1 text-sm">
                            {entry.logs.map((log, changeIndex) => (
                              <li
                                key={changeIndex}
                                className="flex items-start"
                              >
                                <span className="mr-2">•</span>
                                <span>{log}</span>
                              </li>
                            ))}
                          </ul>
                        </div>
                      ))}
                    </CardContent>
                  </Card>
                </div>
              </div>
            </div>
          </div>

          <div className="hidden mb-4">
            <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
              <span className="h-6 w-1 bg-primary rounded-full"></span>
              Sürüm Geçmişi
            </h2>
            <Card>
              <CardContent className="p-0"></CardContent>
            </Card>
          </div>
          <Footer />
        </div>
      ) : (
        <LoadingBar />
      )}
    </AnimatePresence>
  );
}
