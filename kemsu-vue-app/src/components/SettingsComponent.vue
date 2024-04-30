<template>
    <div class="d-flex col d-flex flex-column justify-content-center align-items-center gap-3">
        <div class="d-flex w-75">
            <label for="gmt" class="w-50 text-start">Часовой пояс</label>
            <input name="gmt" type="text" class="form-control w-50" />
        </div>

        <button class="btn btn-success w-25" type="button">Сохранить</button>
        <hr class="w-75" />
        <div class="d-flex w-75">
            <label for="backup" class="w-50 text-start">Бэкап</label>
            <input name="backup" type="text" class="form-control w-50" />
        </div>

        <div class="d-flex w-75">
            <label for="lang" class="w-50 text-start">{{ $t("settings.lang") }}</label>
            <select name="lang" class="form-control" v-model="locale" @change="switchLanguage">
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