<template>
    <div class="d-flex col d-flex flex-column justify-content-center align-items-center gap-3">
        <div class="d-flex w-75">
            <label for="dbPath" class="w-50 text-start">Путь до БД</label>
            <input name="dbPath" type="text" class="form-control w-50" />
        </div>
        <div class="d-flex w-75">
            <label for="gmt" class="w-50 text-start">Часовой пояс</label>
            <input name="gmt" type="text" class="form-control w-50" />
        </div>
        <button class="btn btn-success w-25" type="button">Сохранить</button>
        <hr class="w-75" />
        <div class="d-flex w-75">
            <label for="backup" class="w-50 text-start">Бэкап</label>
            <input name="backup" type="text" class="form-control w-50" /></div>
    </div>
</template>

<script>
    export default{
        name: "SettingsComponent"
    }
</script>