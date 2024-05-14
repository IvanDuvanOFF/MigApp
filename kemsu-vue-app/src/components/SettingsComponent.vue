<template>
    <div class="d-flex col d-flex flex-column justify-content-center align-items-center gap-3">
        <div class="d-flex w-75">
            <label for="theme" class="w-50 text-start">{{ $t("settings.theme") }}</label>
            <select name="theme" class="form-select" :value="theme" @change="switchTheme">
                <option value="true">
                    {{ $t("settings.dark_theme") }}
                </option>
                <option value="false">
                    {{ $t("settings.light_theme") }}
                </option>
            </select>
        </div>

        <div class="d-flex w-75">
            <label for="lang" class="w-50 text-start">{{ $t("settings.lang") }}</label>
            <select name="lang" class="form-select" v-model="locale" @change="switchLanguage">
                <option v-for="sLocale in supportedLocales" :key="`locale-${sLocale}`" :value="sLocale">
                    {{ sLocale }}
                </option>
            </select>
        </div>

        <div class="d-flex w-75">
            <label for="font" class="w-50 text-start">{{ $t("settings.font") }}</label>
            <select name="font" class="form-select" :value="fontSize" @change="switchFont">
                <option value="true">
                    {{ $t("settings.large_font") }}
                </option>
                <option value="false">
                    {{ $t("settings.small_font") }}
                </option>
            </select>
        </div>

        <div class="d-flex w-75">
            <label for="gmt" class="w-50 text-start">{{ $t("settings.gmt") }}</label>
            <select name="gmt" class="form-select">
                <option>
                </option>
            </select>
        </div>
    </div>
</template>

<script>
import Trans from '@/i18n/translate.js';
import SettingsController from '@/store/settings-controller';
import { useI18n } from 'vue-i18n';


export default {
    setup() {
        const { t, locale } = useI18n();

        const supportedLocales = Trans.supportedLocales;

        const switchLanguage = async (event) => {
            const newLocale = event.target.value;
            await Trans.switchLanguage(newLocale);
        };

        return {
            t,
            locale,
            supportedLocales,
            switchLanguage,
            theme: SettingsController.getDarkTheme(),
            fontSize: SettingsController.getFontSize()
        };
    },
    methods: {
        switchTheme(event) {
            let value = event.target.value == "true";            
            SettingsController.setDarkTheme(value);
            this.$router.go();
        },
        switchFont(event){
            let value = event.target.value == "true";
            SettingsController.setFontSize(value);
            this.$router.go();
        }
    },
    name: "SettingsComponent"
}
</script>