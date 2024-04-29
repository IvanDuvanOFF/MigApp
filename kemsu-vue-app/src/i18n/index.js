import { VITE_DEFAULT_LOCALE, VITE_FALLBACK_LOCALE } from "@/urls";
import { createI18n } from "vue-i18n";
import ru from "./locales/ru.json";
import en from "./locales/en.json";

export default createI18n({
    locale: VITE_DEFAULT_LOCALE,
    fallbackLocale: VITE_FALLBACK_LOCALE,
    legacy: false,    
    globalInjection: true,
    messages: {ru, en}
})