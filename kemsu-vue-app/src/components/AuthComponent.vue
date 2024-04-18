<template>
    <div id="root">
        <Form name="loginForm" novalidate @submit="handleLogin">
            <div class="d-flex flex-column mt-5 flex-wrap w-25 m-auto align-items-center">
                <label for="username">Логин</label>
                <Field :rules="validateField" id="username" v-model="user.username" v-validate="'required'" type="text"
                    class="form-control mt-3 mb-3" name="username" autocomplete="false" />
                <ErrorMessage name="username" class="alert alert-danger"></ErrorMessage>

                <label for="password">Пароль</label>
                <Field :rules="validateField" id="password" v-model="user.password" v-validate="'required'"
                    type="password" class="form-control mt-3 mb-3" name="password" autocomplete="false" />

                <ErrorMessage name="password" class="alert alert-danger"></ErrorMessage>

                <button class="btn btn-dark btn-lg" type="submit">Войти</button>

                <div class="form-group mt-3 w-100">
                    <div v-if="message" class="alert alert-danger" role="alert">{{ message }}</div>
                </div>
            </div>
        </Form>
    </div>
</template>

<style></style>

<script>
import { Field, Form, ErrorMessage } from 'vee-validate';
import User from '../models/user';
// import AuthService from '@/services/AuthService.ts';

export default {
    name: "AuthComponent",
    components: {
        Field,
        Form,
        ErrorMessage
    },
    data() {
        return {
            user: new User('', ''),
            loading: false,
            message: ''
        }
    },
    computed: {
        loggedIn() {
            return this.$store.state.auth.status.loggedIn;
        }
    },
    created() {
        if (this.loggedIn) {
            this.$router.push('/');
        }
    },

    methods: {
        validateField(value) {
            if (!value) {
                return 'Поле должно быть заполнено';
            }

            return true;
        },

        handleLogin() {
            this.loading = true;            
            if (this.user.username && this.user.password) {
                this.$store.dispatch('auth/login', this.user).then(
                    () => {
                        console.log(localStorage.getItem('user'));
                        if (this.$store.state.auth.status.loggedIn) {                            
                            window.location.replace("/");
                        }
                        else {
                            // this.$router.go();
                            window.location.replace("/login/tfa");
                        }

                    },
                    error => {
                        this.loading = false;                        
                        let errorMessage = error.message;
                        if(error.response){
                            errorMessage += "\n" + error.response.data.message; 
                        }                           
                        console.log("error is" + errorMessage);
                        this.message = errorMessage;
                    }
                );
            }
        }
    }
};
</script>