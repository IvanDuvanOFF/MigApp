<template>
    <div class="video-container">
        <video autoplay="autoplay" muted="muted" loop="loop" id="myVideo" playsinline="playsinline">
            <source src="../assets/footage.mp4" type="video/mp4">
        </video>
    </div>


    <div id="root" class="rounded-5 text-white">
        <h1>{{ $t("welcome.title") }}</h1>

        <a class="btn btn-primary" href="/login">
            {{ $t("welcome.login") }}
        </a>

        <a class="btn btn-danger" data-bs-toggle="collapse" href="#registerForm" aria-expanded="false"
            aria-controls="registerForm">
            {{ $t("welcome.register") }}
        </a>

        <div class="collapse" id="registerForm">
            <Form name="loginForm" @submit="handleRegister" :validation-schema="schema"
                class="d-flex flex-column mt-5 flex-wrap m-auto align-items-center">
                <label for="username">{{ $t("auth.username") }}</label>
                <Field id="username" v-model="user.username" type="text" class="form-control mt-3 mb-3" name="username"
                    autocomplete="off" />
                <ErrorMessage name="username" class="alert alert-danger"></ErrorMessage>

                <label for="password">{{ $t("auth.password") }}</label>
                <Field id="password" type="password" class="form-control mt-3 mb-3" name="password"
                    autocomplete="off" />
                <ErrorMessage name="password" class="alert alert-danger"></ErrorMessage>

                <label for="repeat_password">{{ $t("auth.password") }}</label>
                <Field id="repeat_password" v-model="user.password" type="password" class="form-control mt-3 mb-3"
                    name="repeat_password" autocomplete="off" />
                <ErrorMessage name="repeat_password" class="alert alert-danger"></ErrorMessage>

                <label for="bd_path">{{ $t("config.bd_path") }}</label>
                <Field id="bd_path" v-model="bd_path" type="text" class="form-control mt-3 mb-3" name="bd_path"
                    autocomplete="off" />
                <p class="badge">{{ $t("welcome.bd_explanation") }}</p>
                <ErrorMessage name="bd_path" class="alert alert-danger"></ErrorMessage>

                <button class="btn btn-dark btn-lg" type="submit">{{ $t("welcome.create_account") }}</button>
            </Form>
        </div>

    </div>
</template>

<script>
import UserService from "@/services/UserService";
import { Field, Form, ErrorMessage } from "vee-validate";
import * as yup from 'yup';

export default {
    components: {
        Field,
        Form,
        ErrorMessage
    },
    name: "WelcomePage",
    data() {
        return {
            bd_path: "",
            user: {},
            // Схема для валидации полей
            schema: yup.object({
                "username": yup.string().required(this.$t("errors.required")),
                "password": yup.string().required(this.$t("errors.required")),
                "bd_path": yup.string(),
                "repeat_password": yup.string().required(this.$t("errors.required"))
                    .oneOf([yup.ref('password'), null], this.$t("errors.repeat_password")
                    ),
            })
        };
    },
    methods: {
        // Регистрация пользователя
        handleRegister() {            
            this.$router.push('/login');

            UserService.createRootUser(this.user.username, this.user.password, this.bd_path).then(
                () => {
                    this.$router.push('/login');
                }
            )
        }
    }
}
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

body {
    background-color: rgba(0, 0, 0, 0) !important;
}

#root {
    background-color: rgba(0, 0, 0, 0.5) !important;
    padding: 5em;
}
</style>