export const fallbackLng = "en";
export const navLanguages = [
  { code: "en", name: "English", flag: "🇬🇧" },
  { code: "tr", name: "Türkçe", flag: "🇹🇷" },
  { code: "de", name: "Deutsch", flag: "🇩🇪" },
  { code: "es", name: "Español", flag: "🇪🇸" },
  { code: "hi", name: "हिन्दी", flag: "🇮🇳" },
];
export const languages = navLanguages.map((lang) => lang.code);
export const defaultNS = "common";
export const namespaces = [
  "backup",
  "common",
  "download",
  "guides",
  "home",
  "library_backup",
  "updater",
];
export const cookieName = "i18next";
export const headerName = "x-i18next-current-language";
