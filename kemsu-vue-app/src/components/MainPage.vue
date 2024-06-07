<template>
    <div class="m-auto col">
        <div class="row">
            <h2>Добро пожаловать в миграционную службу КемГУ!</h2>
        </div>

        <form class="col align-content-center bg-info-subtle p-5 rounded-5 mt-5" v-if="is_admin">
            <h4 class="mb-5">Настройте стартовую страницу сами!</h4>

            <h4>Загрузите свой HTML файл:</h4>

            <input name="htmlFile" type="file" ref="file" class="m-auto form-control w-50" />
        </form>
    </div>


</template>

<script>
export default {
    data() {
        let user = JSON.parse(localStorage.getItem("user"));
        return {
            is_admin: user.is_admin
        }
    }
}
</script>