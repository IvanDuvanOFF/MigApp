<template>
    <div class="col" style="margin-top: 10%;">
        <h1>{{ $t("config.user_manage") }}</h1>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col">{{ $t("config.username") }}</th>
                    <th scope="col">{{ $t("config.password") }}</th>
                    <th scope="col">{{ $t("config.actions") }}</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td v-if="!editList.includes(user.id)">
                        {{ user.username }}
                    </td>
                    <td v-else>
                        <DynamicInput :attribute="user_attr" :disabled="false" :key="user_attr.attribute_name"
                            :modelValue="user" />
                    </td>


                    <td v-if="!editList.includes(user.id)">
                        {{ user.password }}
                    </td>
                    <td v-else>
                        <DynamicInput :attribute="password_attr" :disabled="false" :modelValue="user" />
                    </td>

                    <td class="column" v-if="!editList.includes(user.id) && !isNewUser">
                        <button class="btn btn-danger" @click="removeUser(user.id)">
                            <font-awesome-icon icon="trash-can" />
                        </button>

                        <button class="btn btn-info" @click="activeEditMode(user.id)">
                            <font-awesome-icon icon="pen" />
                        </button>
                    </td>

                    <td v-else-if="!isNewUser">
                        <button class="btn btn-success" @click="saveUser(user.id)">
                            <font-awesome-icon icon="save" />
                        </button>

                        <button class="btn btn-info" @click="disactiveEditMode(user.id)">
                            <font-awesome-icon icon="pen" />
                        </button>
                    </td>

                    <td v-else>

                    </td>
                </tr>
                <td colspan="3" v-if="isNewUser == false">
                    <button class="btn btn-light w-100" @click="isNewUser = true">
                        +
                    </button>
                </td>
                <tr v-else>
                    <td>
                        <DynamicInput :attribute="user_attr" :disabled="false" :modelValue="newUser" />
                    </td>

                    <td>
                        <DynamicInput :attribute="password_attr" :disabled="false" :modelValue="newUser" />
                    </td>

                    <td>
                        <button class="btn btn-success" @click="addUser">
                            <font-awesome-icon icon="save" />
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="rounded-2 row gap-2 justify-content-center mx-1 p-2 mt-5 border-danger border border-2 ">
            <h1>{{ $t("config.bd_path") }}</h1>
            <input class="form-control" name="dbPath" v-model="config.bd_path" />
            <button class="w-25 btn btn-dark" @click="saveBdPath()">
                {{ $t("config.save") }}
            </button>
        </div>

    </div>
</template>

<script>
import UserService from '@/services/UserService.js';
import DynamicInput from './dynamic-components/DynamicInput.vue';
import SettingsController from '@/store/settings-controller';
import ConfigService from '@/services/ConfigService';

export default {
    components: {
        DynamicInput
    },
    data() {
        let users = []
        let config = {};
        let editList = [];

        let user = JSON.parse(localStorage.getItem('user'));

        ConfigService.getConfigureByName(SettingsController.getBdPath(), user.workspace_id).then(response => {
            this.config = response.data[0];
            console.log(this.config);
        });

        UserService.getUsers(user.workspace_id).then(response => {
            this.users = response.data;
        });

        return {
            users,
            editList,
            isNewUser: false,
            newUser: {},
            config,
            user_attr: {
                attribute_type: "string",
                attribute_name: "username"
            },
            password_attr: {
                attribute_type: "password",
                attribute_name: "password"
            }
        }
    },
    methods: {
        // обновить список пользователей после действий
        update() {
            let user = JSON.parse(localStorage.getItem("user"));
            UserService.getUsers(user.workspace_id).then(response => {
                this.users = response.data;
            });
            this.isNewUser = false;
            this.newUser = {};
        },
        // Активировать режим редактирования для пользователя
        activeEditMode(id) {
            this.editList = [];
            this.editList.push(id);
            console.log(this.editList);
        },
        // Выключить режим редактирования для пользователя
        disactiveEditMode(id) {
            const index = this.editList.indexOf(id);
            this.editList.splice(index, 1);
            console.log(this.editList);
        },
        // Удалить пользователя
        removeUser(userId) {
            if (confirm(this.$t("confirm.delete_user"))) {
                UserService.removeUser(userId).then(() => {
                    this.update();
                });
            }
        },
        // Добавить нового пользователя
        addUser() {
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;

            UserService.createUser(username, password, 1).then(() => {
                this.update();
            })
        },
        // Сохранить данные пользователя
        saveUser(userId) {
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;

            UserService.editUser(userId, { username, password }).then(() => {
                this.update();
            })
            this.editList = [];
        },
        // Сохранить путь в конфигурацию
        saveBdPath() {
            if (confirm(this.$t("confirm.sure_bd"))) {
                ConfigService.changeConfigure(this.config).then(() => {
                    SettingsController.setBdPath(this.config.bd_path);
                    this.$router.go();
                });
            }
        }
    }
}
</script>