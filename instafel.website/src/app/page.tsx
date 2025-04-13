"use client";

import { motion, AnimatePresence } from "framer-motion";
import {
  Book,
  IflDownload,
  IflFeature_32BitSupport,
  IflFeature_CrashReports,
  IflFeature_OTAUpdates,
  IflFeature_RemoveAds,
  IflLibrary,
  Instagram,
  Notification,
  Telegram,
} from "@/components/Icons";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import Link from "next/link";
import Footer from "@/components/Footer";

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col bg-primary-foreground dark:bg-primary-background">
      <div className="mx-auto w-full max-w-7xl px-4 sm:px-6 lg:px-8 overflow-hidden">
        <div className="space-y-8 sm:space-y-12 py-6 sm:py-8 md:py-12 lg:py-16">
          <div className="animate-fadeIn">
            <section className="flex flex-col items-center justify-center px-4 pt-16 md:pt-24 text-center">
              <motion.h1
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  duration: 0.8,
                  ease: "easeInOut",
                }}
                className="text-5xl md:text-6xl font-bold mb-6 max-w-3xl"
              >
                Better Alpha Experience
              </motion.h1>
              <motion.p
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.5,
                  duration: 0.8,
                  ease: "easeOut",
                }}
                className="text-muted-foreground  mb-6 text-xl max-w-xl"
              >
                You can try out latest Alpha builds as quickly as possible with
                Instafel
              </motion.p>
              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.7,
                  duration: 0.8,
                  ease: "easeOut",
                }}
              >
                <div className="grid grid-cols-2 grid-rows-1 mb-2 gap-2">
                  <Button
                    asChild
                    size={"lg"}
                    variant={"default"}
                    className="flex justify-center items-center"
                  >
                    <Link href="/download?version=latest">
                      <IflDownload className="flex-shrink-0 w-5 h-5 mr-1" />
                      Download
                    </Link>
                  </Button>

                  <Button asChild size={"lg"} variant={"outline-gradient"}>
                    <Link href="/guides">
                      <Book className="flex-shrink-0 w-4 h-4 mr-1" />
                      Read Guide
                    </Link>
                  </Button>
                </div>

                <motion.div
                  initial={{ opacity: 0, y: 30 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    delay: 0.9,
                    duration: 0.8,
                    ease: "easeOut",
                  }}
                >
                  <Button
                    asChild
                    size="lg"
                    variant={"gradient"}
                    className="flex justify-center items-center col-span-1 mb-11"
                  >
                    <Link href="https://t.me/instafel">
                      <Telegram className="flex-shrink-0 w-4 h-4 mr-1" />
                      Join Community
                    </Link>
                  </Button>
                </motion.div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.13,
                  duration: 0.8,
                  ease: "easeOut",
                }}
              >
                <Image
                  alt="Profile Picture"
                  src="/instafel_hero.png"
                  width="800"
                  height="800"
                  quality="100"
                />
              </motion.div>
            </section>
          </div>
          <div className="animate-fadeIn">
            <section
              id="features"
              className="pb-6 px-4 md:px-12 bg-primary-foreground dark:bg-primary-background"
            >
              <h2 className="text-3xl font-bold text-center mb-12">
                Instafel Features
              </h2>
              <div className="grid md:grid-cols-3 gap-8">
                {[
                  {
                    icon: <Instagram />,
                    title: "Clone Support",
                    desc: "If you don't want to install the Alpha versions instead of the Instagram, you can install it as a secondary app!",
                  },
                  {
                    icon: <IflFeature_OTAUpdates />,
                    title: "OTA Updates",
                    desc: "You can update Instafel flawlessly using the in-app updater or the automatic update plugin Instafel Updater",
                  },
                  {
                    icon: <IflFeature_RemoveAds />,
                    title: "Ads Removed",
                    desc: "Use it comfortably without any ads in the app!",
                  },
                  {
                    icon: <IflFeature_CrashReports />,
                    title: "Crash Reports",
                    desc: "It is very common for the application to crash when using Alpha versions, Instafel always keeps a crash report for such cases",
                  },
                  {
                    icon: <IflLibrary />,
                    title: "Libraries",
                    desc: "You can find useful flags, backups in Instafel Library!",
                  },
                  {
                    icon: <IflFeature_32BitSupport />,
                    title: "32-bit Support",
                    desc: "Instafel always offer support for nearly obsolete 32-bit devices!",
                  },
                ].map((feature, idx) => (
                  <motion.div
                    key={idx}
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{
                      delay: idx * 0.15,
                      duration: 0.5,
                      ease: "easeOut",
                    }}
                    className="bg-gray-100 p-6 rounded-xl"
                    whileHover={{ scale: 1.02 }}
                  >
                    <div className="bg-gray-600 text-white p-3 rounded-lg inline-block mb-4">
                      {feature.icon}
                    </div>
                    <h3 className="text-xl font-semibold mb-2">
                      {feature.title}
                    </h3>
                    <p className="text-muted-foreground">{feature.desc}</p>
                  </motion.div>
                ))}
              </div>
            </section>
          </div>
          <div className="animate-fadeIn">
            <section id="telegram" className="pb-16 px-4 md:px-12">
              <div className="max-w-4xl mx-auto flex flex-col md:flex-row items-center">
                <div className="md:w-1/2 mb-8 md:mb-0">
                  <h2 className="text-3xl font-bold mb-4 text-center md:text-left">
                    Join Telegram Group for <br />
                    News & Discussion
                  </h2>
                  <p className="text mb-6 text-center md:text-left">
                    Join our Telegram group to stay up-to-date with the latest
                    Instafel updates and news. Share your feedback, suggestions
                    and everything with the community.
                  </p>
                  <div className="flex flex-col sm:flex-row space-y-3 sm:space-y-0 sm:space-x-4">
                    <a
                      href="https://t.me/instafel"
                      rel="noopener noreferrer"
                      className="bg-black text-white px-6 py-3 rounded-lg flex items-center justify-center hover:bg-black transition-colors"
                    >
                      <Telegram size={18} className="mr-2" /> Join Instafel
                      Community
                    </a>
                  </div>
                </div>
                <div className="md:w-1/2 flex justify-center">
                  <div className="bg-white rounded-3xl shadow-lg p-6 max-w-xs">
                    <div className="flex items-center mb-4">
                      <Image
                        alt="Profile Picture"
                        src="/instafel.jpg"
                        width="70"
                        height="70"
                        quality="50"
                        className="rounded-full p-2"
                      />
                      <div className="ml-3">
                        <h3 className="font-bold ">Instafel</h3>
                        <p className="text-gray-500 text-sm">@instafel</p>
                      </div>
                    </div>
                    <div className="bg-gray-100 rounded-lg p-3 mb-3">
                      <p className="text-gray-800 text-sm">
                        🎉 Flag library is available now!
                      </p>
                      <p className="text-gray-500 text-xs mt-2">
                        Today 02:25 AM
                      </p>
                    </div>
                    <div className="bg-gray-100 rounded-lg p-3">
                      <p className="text-gray-800 text-sm">
                        Finally I solved issues related by resource
                        decompilation events :)
                      </p>
                      <p className="text-gray-500 text-xs mt-2">
                        Yesterday 08:00 PM
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </section>
          </div>
        </div>
      </div>
      <Footer />
    </main>
  );
}
