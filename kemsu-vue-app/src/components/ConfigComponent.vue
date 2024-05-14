<template>
    <div class="col" style="margin-top: 10%;">
        <h1>{{ $t("config.user_manage") }}</h1>
        <table class="table">
            <thead>
                <tr>
                    <th scope="col">{{ $t("config.username") }}</th>
                    <th scope="col">{{ $t("config.password") }}</th>
                    <th scope="col">{{ $t("config.actions") }}</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Mark</td>
                    <td>****</td>
                    <td class="column">
                        <button class="btn btn-danger">
                            {{ $t("config.delete") }}
                        </button>

                        <button class="btn btn-info">
                            {{ $t("config.edit") }}
                        </button>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <button class="btn btn-light w-100">
                            +
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="rounded-2 row gap-2 justify-content-center mx-1 p-2 mt-5 border-danger border border-2 ">
            <h1>{{ $t("config.bd_path") }}</h1>
            <input class="form-control" />
            <button class="w-25 btn btn-dark">
                {{ $t("config.save") }}
            </button>
        </div>

    </div>
</template>

<script>

</script>