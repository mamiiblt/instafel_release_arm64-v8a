import * as fs from 'fs'
import path from 'path'
import matter from 'gray-matter'

const BLOG_DIR = path.join(process.cwd(), 'public', 'content', 'blogs')

export interface Post {
  slug: string
  title: string
  description: string
  content: string
  id: number
}

interface FrontMatter {
  title: string
  date: string
  description: string
  tags?: string[]
}

export function getInstafelBackups() {}

export function getAllPostsSync() {
  return [
    {
      id: 1,
      title: 'Example Guide 1',
      subtitle: 'Example Guide 1 Subtitle',
      description: 'Example Guide 1 Description',
      color: 'indigo',
    },
    {
      id: 2,
      title: 'Example Guide 2',
      subtitle: 'Example Guide 2 Subtitle',
      description: 'Example Guide 2 Description',
      color: 'rose',
    },
    {
      id: 2,
      title: 'Example Guide 3',
      subtitle: 'Example Guide 3 Subtitle',
      description: 'Example Guide 3 Description',
      color: 'sky',
    },
  ]
}

export function extractHeadings(
  content: string
): Array<{ id: string; text: string; level: number }> {
  const headingRegex = /^(#{1,6})\s+(.+)$/gm
  const headings: Array<{ id: string; text: string; level: number }> = []
  const usedIds = new Set<string>()

  let match
  while ((match = headingRegex.exec(content)) !== null) {
    const level = match[1].length
    const text = match[2].trim()
    let id = text
      .toLowerCase()
      .replace(/[^\w\s-]/g, '')
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-')

    // If this ID is already used, append a number
    if (usedIds.has(id)) {
      let counter = 1
      while (usedIds.has(`${id}-${counter}`)) {
        counter++
      }
      id = `${id}-${counter}`
    }

    usedIds.add(id)
    headings.push({ id, text, level })
  }
  return headings
}
