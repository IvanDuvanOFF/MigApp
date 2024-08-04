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

        <div class="d-flex w-75">
            <label for="bd_path" class="w-50 text-start">{{ $t("settings.config") }}</label>
            <select name="bd_path" class="form-select" @change="switchConfig" :value="selecetedConfig.bd_path">
                <option v-for="config in configures" v-bind:key="{id: config.id, bd_path: config.bd_path}" :value="config.bd_path">
                    {{ config.bd_path }}
                </option>
            </select>
        </div>
    </div>
</template>

<script>
import Trans from '@/i18n/translate.js';
import SettingsController from '@/store/settings-controller';
import ConfigService from '@/services/ConfigService.js';
import { useI18n } from 'vue-i18n';


export default {
    // Инициализация уже выбранной конфигурации
    data(){        
        let user = JSON.parse(localStorage.getItem("user"));
        ConfigService.getConfigures(user.workspace_id).then(response => {
            this.configures = response.data;
        })

        ConfigService.getConfigureByName(SettingsController.getBdPath(), user.workspace_id).then(response => {
            this.selecetedConfig = response.data[0];         
            console.log(SettingsController.getBdPath());
            console.log(this.selecetedConfig);
        })        

        return {
            configures: [],
            selecetedConfig: {}
        }
    },
    // Инициализация языкового плагина
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
        // Смена темы при выборе опции
        switchTheme(event) {
            let value = event.target.value == "true";            
            SettingsController.setDarkTheme(value);
            this.$router.go();
        },
        // Смена шрифта при выборе опции
        switchFont(event){
            let value = event.target.value == "true";
            SettingsController.setFontSize(value);
            this.$router.go();
        },
        // Смена конфигурации при выборе опции
        switchConfig(event){
            console.log(event.target.value);
            SettingsController.setBdPath(event.target.value);
            this.$router.go();
        }
    },
    name: "SettingsComponent"
}
</script>