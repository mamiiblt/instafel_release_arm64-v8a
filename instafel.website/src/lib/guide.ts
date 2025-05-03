import * as fs from "fs";
import path from "path";
import matter from "gray-matter";

const BLOG_DIR = path.join(process.cwd(), "public", "content", "guides");

export interface Post {
  slug: string;
  title: string;
  description: string;
  content: string;
  id: number;
}

interface FrontMatter {
  title: string;
  date?: string;
  description: string;
  tags?: string[];
}

export function getAllPostsSync() {
  try {
    try {
      fs.accessSync(BLOG_DIR, fs.constants.F_OK);
    } catch (error) {
      console.warn(`Guide directory not found: ${BLOG_DIR}`, error);
      return [];
    }

    const fileNames = fs.readdirSync(BLOG_DIR);

    const posts = fileNames
      .filter((fileName) => fileName.endsWith(".md"))
      .map((fileName) => {
        const id = fileName.split(".")[0];

        const fullPath = path.join(BLOG_DIR, fileName);
        const fileContents = fs.readFileSync(fullPath, "utf8");

        const { data, content } = matter(fileContents);
        const frontmatter = data as FrontMatter;

        return {
          id: parseInt(id),
          title: frontmatter.title || "Untitled",
          subtitle: fileName.split(".")[1]?.replace(/-/g, " ") || "Guide",
          description: frontmatter.description || "",
          color: ["indigo", "rose", "sky", "purple", "orange"][
            Math.floor(Math.random() * 5)
          ],
          slug: fileName.replace(/\.md$/, ""),
          content,
        };
      })
      .sort((a, b) => a.id - b.id);

    return posts;
  } catch (error) {
    console.error("Error loading guide posts:", error);
    return [];
  }
}

export function getPostByIdSync(id: string) {
  const posts = getAllPostsSync();
  return posts.find((post) => post.id.toString() === id);
}

export function extractHeadings(
  content: string
): Array<{ id: string; text: string; level: number }> {
  const headingRegex = /^(#{1,6})\s+(.+)$/gm;
  const headings: Array<{ id: string; text: string; level: number }> = [];
  const usedIds = new Set<string>();

  let match: RegExpExecArray | null;
  while ((match = headingRegex.exec(content)) !== null) {
    const level = match[1].length;
    const text = match[2].trim();
    let id = text
      .toLowerCase()
      .replace(/[^\w\s-]/g, "")
      .replace(/\s+/g, "-")
      .replace(/-+/g, "-");

    if (usedIds.has(id)) {
      let counter = 1;
      while (usedIds.has(`${id}-${counter}`)) {
        counter++;
      }
      id = `${id}-${counter}`;
    }

    usedIds.add(id);
    headings.push({ id, text, level });
  }
  return headings;
}
