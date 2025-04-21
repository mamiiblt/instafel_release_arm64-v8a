"use client";

import { motion } from "framer-motion";
import {
  Book,
  LucideInstagram,
  Bug,
  Library,
  MinusCircle,
  RefreshCw,
  Download,
  Send,
  Wrench,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import Link from "next/link";
import Footer from "@/components/Footer";
import HomeMockup from "@/components/HomeMockup";

export default function Home() {
  const features = [
    {
      icon: <Wrench />,
      title: "Developer Options",
      desc: "Access the developer-exclusive tools in latest Alpha bases."
    },
    {
      icon: <LucideInstagram />,
      title: "Clone Support",
      desc: "If you don't want to install the Alpha versions instead of the Instagram, you can install it as a secondary app!",
    },
    {
      icon: <RefreshCw />,
      title: "OTA Updates",
      desc: "You can update Instafel flawlessly using the in-app updater or the automatic update plugin Instafel Updater",
    },
    {
      icon: <MinusCircle />,
      title: "Ads Removed",
      desc: "Use it comfortably without any ads in the app!",
    },
    {
      icon: <Bug />,
      title: "Crash Reports",
      desc: "It is very common for the application to crash when using Alpha versions, Instafel always keeps a crash report for such cases",
    },
    {
      icon: <Library />,
      title: "Libraries",
      desc: "You can find useful flags, backups in Instafel Library!",
    },
  ];

  return (
    <main className="flex min-h-screen flex-col bg-primary-foreground dark:bg-primary-background">
      <div className="mx-auto w-full max-w-7xl px-4 sm:px-6 lg:px-8">
        <section className="py-16 md:py-24 lg:py-32">
          <div className="container mx-auto px-4">
            <div className="flex flex-col items-center justify-center text-center">
              <motion.div
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{
                  duration: 0.8,
                  ease: "easeOut",
                }}
                className="mb-6"
              >
                <span className="inline-flex items-center rounded-full bg-primary/10 px-3 py-1 text-sm font-medium text-primary">
                  Instagram Alpha Experience
                </span>
              </motion.div>

              <motion.h1
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  duration: 0.8,
                  ease: "easeInOut",
                }}
                className="mb-6 text-5xl font-bold tracking-tight sm:text-6xl md:text-7xl bg-gradient-to-r from-gray-900 to-gray-600 bg-clip-text text-transparent dark:from-gray-100 dark:to-gray-400"
              >
                The Best Alpha Experience
              </motion.h1>

              <motion.p
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.3,
                  duration: 0.8,
                  ease: "easeOut",
                }}
                className="mb-8 text-xl max-w-xl text-muted-foreground"
              >
                You can try out latest alpha builds as quickly as possible with
                Instafel
              </motion.p>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.5,
                  duration: 0.6,
                  ease: "easeOut",
                }}
                className="flex flex-col sm:flex-row gap-4 w-full max-w-md justify-center"
              >
                <Button
                  asChild
                  size={"lg"}
                  variant={"default"}
                  className="w-full flex justify-center items-center group hover:scale-105 transition-transform duration-300"
                >
                  <Link href="/download?version=latest">
                    <Download className="shrink-0 w-5 h-5 mr-2 group-hover:animate-pulse" />
                    Download
                  </Link>
                </Button>

                <Button
                  asChild
                  size={"lg"}
                  variant={"outline-gradient"}
                  className="w-full group hover:scale-105 transition-transform duration-300"
                >
                  <Link href="/guides">
                    <Book className="shrink-0 w-4 h-4 mr-2 group-hover:rotate-12 transition-transform duration-300" />
                    Read Guide
                  </Link>
                </Button>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{
                  delay: 0.7,
                  duration: 0.6,
                  ease: "easeOut",
                }}
                className="mt-6"
              >
                <Button
                  asChild
                  size="lg"
                  variant={"gradient"}
                  className="group hover:scale-105 transition-transform duration-300"
                >
                  <Link href="https://t.me/instafel">
                    <Send className="shrink-0 w-4 h-4 mr-2 group-hover:translate-y-[-2px] transition-transform duration-300" />
                    Join Community
                  </Link>
                </Button>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, y: 50, scale: 0.95 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                transition={{
                  delay: 0.9,
                  duration: 0.8,
                  ease: "easeOut",
                }}
                className="mt-12 w-full max-w-4xl mx-auto relative"
              >
                <div className="relative overflow-hidden rounded-xl">
                  <HomeMockup />
                </div>
              </motion.div>
            </div>
          </div>
        </section>

        <section
          id="features"
          className="py-16 px-4 md:px-12 bg-gray-50 dark:bg-gray-900/20 rounded-3xl my-12"
        >
          <motion.h2
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6 }}
            className="text-3xl font-bold text-center mb-6"
          >
            Instafel Features
          </motion.h2>

          <motion.p
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ duration: 0.6, delay: 0.2 }}
            className="text-center text-muted-foreground max-w-2xl mx-auto mb-12"
          >
            Explore the powerful features that make Instafel the best choice for
            Instagram Alpha users
          </motion.p>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, idx) => (
              <motion.div
                key={idx}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{
                  delay: idx * 0.15,
                  duration: 0.5,
                  ease: "easeOut",
                }}
                whileHover={{
                  scale: 1.03,
                  boxShadow: "0 10px 30px -15px rgba(0, 0, 0, 0.15)",
                }}
                className="bg-white dark:bg-gray-800/50 p-6 rounded-xl shadow-sm border border-gray-100 dark:border-gray-700/50"
              >
                <div className="bg-gray-600 text-white p-3 rounded-lg inline-block mb-4 hover:rotate-6 transition-transform duration-300">
                  {feature.icon}
                </div>
                <h3 className="text-xl font-semibold mb-2">{feature.title}</h3>
                <p className="text-muted-foreground">{feature.desc}</p>
              </motion.div>
            ))}
          </div>
        </section>

        <section id="telegram" className="py-16 px-4 md:px-12 my-12">
          <div className="max-w-6xl mx-auto">
            <div className="flex flex-col md:flex-row items-center gap-12">
              <motion.div
                initial={{ opacity: 0, x: -30 }}
                whileInView={{ opacity: 1, x: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6 }}
                className="md:w-1/2"
              >
                <div className="max-w-lg">
                  <span className="inline-flex items-center rounded-full bg-blue-50 px-3 py-1 text-sm font-medium text-blue-700 dark:bg-blue-900/30 dark:text-blue-300 mb-4">
                    Stay Connected
                  </span>
                  <h2 className="text-3xl md:text-4xl font-bold mb-4 text-center md:text-left">
                    Join Telegram Group for <br className="hidden md:block" />
                    <span className="text-primary">News & Discussion</span>
                  </h2>
                  <p className="text-muted-foreground mb-6 text-center md:text-left">
                    Join our Telegram group to stay up-to-date with the latest
                    Instafel updates and news. Share your feedback, suggestions
                    and learn everything with the community.
                  </p>
                  <div className="flex justify-center md:justify-start">
                    <Button
                      asChild
                      size="lg"
                      className="group bg-[#0088cc] hover:bg-[#0099dd] text-white hover:scale-105 transition-all duration-300"
                    >
                      <a
                        href="https://t.me/instafel"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="flex items-center"
                      >
                        <Send
                          size={18}
                          className="mr-2 group-hover:translate-y-[-2px] transition-all duration-300"
                        />
                        Join Instafel Community
                      </a>
                    </Button>
                  </div>
                </div>
              </motion.div>

              <motion.div
                initial={{ opacity: 0, x: 30 }}
                whileInView={{ opacity: 1, x: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6, delay: 0.2 }}
                className="md:w-1/2 flex justify-center"
              >
                <div className="relative">
                  <div className="absolute -inset-1 bg-gradient-to-r from-blue-500/20 to-primary/10 rounded-2xl blur-lg opacity-70"></div>
                  <div className="bg-white dark:bg-gray-800 rounded-3xl shadow-xl p-6 relative max-w-xs">
                    <div className="flex items-center mb-4">
                      <div className="relative w-16 h-16 rounded-full overflow-hidden border-2 border-gray-200">
                        <Image
                          alt="Instafel Logo"
                          src="/instafel.jpg"
                          width={70}
                          height={70}
                          quality={80}
                          className="object-cover w-full h-full"
                        />
                      </div>
                      <div className="ml-3">
                        <h3 className="font-bold text-lg">Instafel</h3>
                        <p className="text-gray-500 text-sm">@instafel</p>
                      </div>
                    </div>

                    <div className="space-y-3">
                      <div className="bg-gray-100 dark:bg-gray-700/50 rounded-lg p-3">
                        <p className="text-gray-800 dark:text-gray-200 text-sm">
                          🎉 New patcher is available now!
                        </p>
                        <p className="text-gray-500 dark:text-gray-400 text-xs mt-2">
                          Today 02:25 AM
                        </p>
                      </div>
                      <div className="bg-gray-100 dark:bg-gray-700/50 rounded-lg p-3">
                        <p className="text-gray-800 dark:text-gray-200 text-sm">
                          Finally I solved issues related by resource
                          decompilation events :)
                        </p>
                        <p className="text-gray-500 dark:text-gray-400 text-xs mt-2">
                          Yesterday 08:00 PM
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center justify-between mt-4 text-xs text-gray-500">
                      <span>5,500+ members</span>
                      <span>325 online</span>
                    </div>
                  </div>
                </div>
              </motion.div>
            </div>
          </div>
        </section>

        <motion.section
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.6 }}
          className="py-16 px-4 my-12 text-center bg-gradient-to-r from-primary/10 to-primary/5 rounded-3xl"
        >
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Ready to Try Instafel?
          </h2>
          <p className="text-muted-foreground mb-8 max-w-xl mx-auto">
            Experience the latest Instagram Alpha features with our enhanced
            app, download now and join thousands of satisfied users
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button
              asChild
              size="lg"
              className="hover:scale-105 transition-transform duration-300"
            >
              <Link href="/download?version=latest">
                <Download className="mr-2" />
                Download Now
              </Link>
            </Button>
            <Button
              asChild
              size="lg"
              variant="outline-gradient"
              className="hover:scale-105 transition-transform duration-300"
            >
              <Link href="/about_updater">
                <RefreshCw className="mr-2" />
                View Updater
              </Link>
            </Button>
          </div>
        </motion.section>
      </div>
      <Footer />
    </main>
  );
}
