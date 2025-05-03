import { NextResponse } from "next/server";
import acceptLanguage from "accept-language";
import {
  fallbackLng,
  languages,
  cookieName,
  headerName,
} from "./i18n/settings";

acceptLanguage.languages(languages);

export const config = {
  // Avoid matching for static files, API routes, etc.
  matcher: [
    "/((?!api|_next/static|_next/image|assets|favicon.ico|sw.js|site.webmanifest|apple-icon.png|globals.css|icon0.svg|icon1.png|manifest.json|sitemap.xml).*)",
  ],
};

export function middleware(req) {
  // Ignore paths with "icon" or "chrome"
  if (
    req.nextUrl.pathname.indexOf("icon") > -1 ||
    req.nextUrl.pathname.indexOf("chrome") > -1
  )
    return NextResponse.next();

  let lng;
  // Try to get language from cookie
  if (req.cookies.has(cookieName))
    lng = acceptLanguage.get(req.cookies.get(cookieName).value);
  // If no cookie, check the Accept-Language header
  if (!lng) lng = acceptLanguage.get(req.headers.get("Accept-Language"));
  // Default to fallback language if still undefined
  if (!lng) lng = fallbackLng;

  // Check if the language is already in the path
  const lngInPath = languages.find((loc) =>
    req.nextUrl.pathname.startsWith(`/${loc}`)
  );
  const headers = new Headers(req.headers);
  headers.set(headerName, lngInPath || lng);

  // If the language is not in the path, redirect to include it
  if (!lngInPath && !req.nextUrl.pathname.startsWith("/_next")) {
    return NextResponse.redirect(
      new URL(`/${lng}${req.nextUrl.pathname}${req.nextUrl.search}`, req.url)
    );
  }

  // If a referer exists, try to detect the language from there and set the cookie accordingly
  if (req.headers.has("referer")) {
    const refererUrl = new URL(req.headers.get("referer"));
    const lngInReferer = languages.find((l) =>
      refererUrl.pathname.startsWith(`/${l}`)
    );
    const response = NextResponse.next({ headers });
    if (lngInReferer) response.cookies.set(cookieName, lngInReferer);
    return response;
  }

  return NextResponse.next({ headers });
}
