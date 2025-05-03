/*import i18n from "i18next";
import FsBackend from "i18next-fs-backend";
import LanguageDetector from "i18next-browser-languagedetector";
import { initReactI18next } from "react-i18next";
import { locales } from "./i18n-config";

i18n
  .use(FsBackend)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    supportedLngs: locales,
    fallbackLng: "en",
    debug: process.env.NODE_ENV === "development",
    ns: ["common", "backup", "download", "guides", "home", "library_backup", "updater"],
    backend: {
      loadPath: "./src/locales/{{lng}}/{{ns}}.json",
    },
    detection: {
      order: ['cookie', 'navigator', 'htmlTag', 'localStorage'],
      caches: ['cookie'],
    },
  });

export default i18n;
*/