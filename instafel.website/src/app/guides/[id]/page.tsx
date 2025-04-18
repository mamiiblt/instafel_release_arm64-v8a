"use client";

import { useParams, useRouter } from "next/navigation";
import { motion } from "framer-motion";
import Footer from "@/components/Footer";
import { ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import Link from "next/link";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import rehypeRaw from "rehype-raw";
import rehypeSanitize from "rehype-sanitize";
import { useBlogPost } from "@/hooks/useBlog";

export default function GuidePage() {
  const router = useRouter();
  const { id } = useParams<{ id: string }>();
  const { guide, loading, error } = useBlogPost(id as string);

  // Redirect if there's an error
  if (error) {
    router.push("/guides");
    return null;
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin h-8 w-8 border-4 border-primary border-t-transparent rounded-full"></div>
      </div>
    );
  }

  if (!guide) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div>Guide not found</div>
      </div>
    );
  }

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
    return classes[guide.color as keyof typeof classes] || classes.indigo;
  };

  return (
    <>
      <div className="min-h-screen bg-background py-16">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="max-w-3xl mx-auto mb-8"
          >
            <Link
              href="/guides"
              className="inline-flex items-center text-muted-foreground hover:text-primary mb-6 transition-colors"
            >
              <ArrowLeft className="mr-2 h-4 w-4" />
              Back to guides
            </Link>

            <div className="mb-3">
              <span
                className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getColorClasses(guide.color)}`}
              >
                {guide.subtitle}
              </span>
            </div>

            <h1 className="text-3xl md:text-4xl font-bold mb-4">
              {guide.title}
            </h1>
            <p className="text-muted-foreground">{guide.description}</p>
          </motion.div>

          <Separator className="max-w-3xl mx-auto mb-8" />

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
            className="prose dark:prose-invert prose-headings:scroll-mt-20 max-w-3xl mx-auto"
          >
            <ReactMarkdown
              remarkPlugins={[remarkGfm]}
              rehypePlugins={[rehypeRaw, rehypeSanitize]}
              components={{
                h1: ({ node, ...props }) => (
                  <h1 className="text-3xl font-bold mt-8 mb-4" {...props} />
                ),
                h2: ({ node, ...props }) => (
                  <h2 className="text-2xl font-bold mt-6 mb-3" {...props} />
                ),
                h3: ({ node, ...props }) => (
                  <h3 className="text-xl font-bold mt-5 mb-3" {...props} />
                ),
                p: ({ node, ...props }) => <p className="my-3" {...props} />,
                ul: ({ node, ...props }) => (
                  <ul className="list-disc pl-6 my-4" {...props} />
                ),
                ol: ({ node, ...props }) => (
                  <ol className="list-decimal pl-6 my-4" {...props} />
                ),
                li: ({ node, ...props }) => <li className="mb-2" {...props} />,
                a: ({ node, ...props }) => (
                  <a
                    className="text-primary hover:underline"
                    target="_blank"
                    rel="noopener noreferrer"
                    {...props}
                  />
                ),
                code: ({ node, ...props }) => (
                  <code
                    className="bg-muted text-primary rounded-md px-1 py-0.5"
                    {...props}
                  />
                ),
                blockquote: ({ node, ...props }) => (
                  <blockquote
                    className="border-l-4 border-muted pl-4 italic my-4"
                    {...props}
                  />
                ),
                strong: ({ node, ...props }) => (
                  <strong className="font-bold" {...props} />
                ),
              }}
            >
              {guide.content}
            </ReactMarkdown>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.3 }}
            className="max-w-3xl mx-auto mt-16 pt-8 border-t text-center"
          >
            <p className="text-muted-foreground mb-6">
              Was this guide helpful? Have questions or suggestions?
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button asChild>
                <a
                  href="https://t.me/instafel"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  Join the Discussion
                </a>
              </Button>
              <Button variant="outline" asChild>
                <Link href="/guides">View All Guides</Link>
              </Button>
            </div>
          </motion.div>
        </div>
      </div>
      <Footer />
    </>
  );
}
