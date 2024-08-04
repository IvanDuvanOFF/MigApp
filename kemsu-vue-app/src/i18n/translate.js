import i18n from '@/i18n/index.js';
import { VITE_DEFAULT_LOCALE, VITE_SUPPORTED_LOCALES } from "@/urls";

const Trans = {
    // Получить список поддериживаемых языков
    get supportedLocales() {
        return VITE_SUPPORTED_LOCALES.split(",");
    },

    // Получить язык по умолчанию
    get defaultLocale() {
        return VITE_DEFAULT_LOCALE;
    },

    // Поставить текущий язык
    set currentLocale(newLocale) {
        i18n.global.locale.value = newLocale;
    },

    // Получить информацию, поддерживается ли язык
    isLocaleSupported(locale) {
        return Trans.supportedLocales.includes(locale);
    },

    // Получить язык зарегистрированного пользователя
    getUserLocale() {
        const locale = window.navigator.language ||
            window.navigator.userLanguage ||
            Trans.defaulLocale

        return {
            locale: locale,
            localeNoRegion: locale.split('-')[0]
        }
    },

    // Получить сохраненный язык пользователя
    getPersistedLocale() {
        const persistedLocale = localStorage.getItem("user-locale")
        console.log(persistedLocale);
        if (Trans.isLocaleSupported(persistedLocale)) {
            return persistedLocale
        } else {
            return this.defaultLocale;
        }
    },

    // Поменять язык
    async switchLanguage(newLocale) {
        Trans.currentLocale = newLocale;
        document.querySelector("html").setAttribute("lang", newLocale);
        localStorage.setItem("user-locale", newLocale);
    }
}

export default Trans;