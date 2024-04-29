import i18n from '@/i18n/index.js';
import { VITE_DEFAULT_LOCALE, VITE_SUPPORTED_LOCALES } from "@/urls";

const Trans = {
    get supportedLocales(){
        return VITE_SUPPORTED_LOCALES.split(",");
    },

    get defaulLocale(){
        return VITE_DEFAULT_LOCALE;
    },

    set currentLocale(newLocale){
        i18n.global.locale.value = newLocale;
    },

    isLocaleSupported(locale){
        return Trans.supportedLocales.includes(locale);
    },

    getUserLocale(){
        const locale = window.navigator.language ||
        window.navigator.userLanguage ||
        Trans.defaulLocale

        return {
            locale: locale,
            localeNoRegion: locale.split('-')[0]
        }
    },

    getPersistedLocale(){
        const persistedLocale = localStorage.getItem("user-locale")
        console.log(persistedLocale);
        if(Trans.isLocaleSupported(persistedLocale)){
            return persistedLocale
        } else{
            return null
        }
    },    

    async switchLanguage(newLocale){
        Trans.currentLocale = newLocale;
        document.querySelector("html").setAttribute("lang", newLocale);
        localStorage.setItem("user-locale", newLocale);
    }    
}

export default Trans;