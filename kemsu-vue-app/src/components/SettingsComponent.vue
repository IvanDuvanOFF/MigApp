<template>
    <div class="d-flex col d-flex flex-column justify-content-center align-items-center gap-3">
        <div class="d-flex w-75">
            <label for="theme" class="w-50 text-start">{{ $t("settings.theme") }}</label>
            <select name="theme" class="form-select">
                <option>                    
                </option>
            </select>
        </div>
        
        <div class="d-flex w-75">
            <label for="lang" class="w-50 text-start">{{ $t("settings.lang") }}</label>
            <select name="lang" class="form-select">
                <option>                    
                </option>
            </select>
        </div>

        <div class="d-flex w-75">
            <label for="font" class="w-50 text-start">{{ $t("settings.font") }}</label>
            <select name="font" class="form-select">
                <option>                    
                </option>
            </select>
        </div>

        <div class="d-flex w-75">
            <label for="gmt" class="w-50 text-start">{{ $t("settings.gmt") }}</label>
            <select name="gmt" class="form-select" v-model="locale" @change="switchLanguage">
                <option v-for="sLocale in supportedLocales" :key="`locale-${sLocale}`" :value="sLocale">
                    {{ sLocale }}
                </option>
            </select>
        </div>
    </div>
</template>

<script>
import Trans from '@/i18n/translate.js';
import { useI18n } from 'vue-i18n';


export default {
    setup() {
        const { t, locale } = useI18n();

        const supportedLocales = Trans.supportedLocales;
        
        const switchLanguage = async (event) => {
            const newLocale = event.target.value;
            await Trans.switchLanguage(newLocale);
        };
        return { t, locale, supportedLocales, switchLanguage };
    },
    name: "SettingsComponent"
}
</script>