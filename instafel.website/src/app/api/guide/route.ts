import { NextResponse } from "next/server";
import { getAllPostsSync, getPostByIdSync } from "@/lib/guide";

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const id = searchParams.get("id");

  if (id) {
    const post = getPostByIdSync(id);
    return NextResponse.json(post || { error: "Post not found" });
  } else {
    const posts = getAllPostsSync();
    return NextResponse.json(posts);
  }
}
