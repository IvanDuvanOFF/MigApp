<template>
    <div class="video-container">
        <video autoplay="autoplay" muted="muted" loop="loop" id="myVideo" playsinline="playsinline">
            <source src="../assets/login.mp4" type="video/mp4">
        </video>
    </div>
    <a href="/welcome" class="position-absolute btn btn-dark" style="top: 0px; left: 0px">
        {{ "<<" + $t("auth.back") }} </a>
            <div id="root" class="rounded-5 text-white">
                <Form name="loginForm" @submit="handleLogin" :validation-schema="schema">
                    <div class="d-flex flex-column flex-wrap align-items-center">
                        <label for="username">{{ $t("auth.username") }}</label>
                        <Field id="username" v-model="user.username" type="text" class="form-control mt-3 mb-3"
                            name="username" autocomplete="off" />
                        <ErrorMessage name="username" class="alert alert-danger"></ErrorMessage>

                        <label for="password">{{ $t("auth.password") }}</label>
                        <Field id="password" v-model="user.password" type="password" class="form-control mt-3 mb-3"
                            name="password" autocomplete="off" />
                        <ErrorMessage name="password" class="alert alert-danger"></ErrorMessage>

                        <button class="btn btn-dark btn-lg" type="submit">{{ $t("auth.button") }}</button>

                        <div class="form-group mt-3 w-100">
                            <div v-if="message" class="alert alert-danger" role="alert">{{ message }}</div>
                        </div>
                    </div>
                </Form>
            </div>
</template>

<script>
import { Field, Form, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

export default {
    name: "AuthComponent",
    components: {
        Field,
        Form,
        ErrorMessage
    },
    data() {
        return {
            user: {},
            loading: false,
            message: '',
            schema: yup.object({
                "username": yup.string().required(this.$t("errors.required")),
                "password": yup.string().required(this.$t("errors.required"))
            })
        }
    },
    computed: {
        // динамическое состояние авторизации пользователя
        loggedIn() {
            return this.$store.state.auth.status.loggedIn;
        }
    },
    created() {
        // перевод на главную при авторизованном доступе
        if (this.loggedIn) {
            this.$router.push('/');
        }
    },

    methods: {
        // действие при авторизации
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
                            window.location.replace("/login/tfa");
                        }

                    },
                    error => {
                        this.loading = false;
                        let errorMessage = error.message;
                        if (error.response) {
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

<style>
.video-container {
    position: fixed;
    z-index: -3;
    background-color: black;
    height: 75vh;
    min-height: 25rem;
    width: 100%;
}

video {
    position: absolute;
    top: 50%;
    left: 50%;
    object-fit: cover;
    min-width: 100%;
    min-height: 100%;
    width: auto;
    height: auto;
    z-index: -2;
    -ms-transform: translateX(-50%) translateY(-50%);
    -moz-transform: translateX(-50%) translateY(-50%);
    -webkit-transform: translateX(-50%) translateY(-50%);
    transform: translateX(-50%) translateY(-50%);
}
</style>