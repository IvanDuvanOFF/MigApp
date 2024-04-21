<template>
    <div id="root">
        <Form name="loginForm" novalidate @submit="handleLogin">
            <div class="d-flex flex-column mt-5 flex-wrap w-25 m-auto align-items-center">
                <h3>Вам на почту был отправлен шестизначный код</h3>
                <label for="code">Код</label>
                <Field :rules="validateField" id="code" v-model.number="code" type="text"
                    class="form-control text-center mt-3 mb-3" name="code" autocomplete="off" maxlength="6"/>
                <ErrorMessage name="code" class="alert alert-danger"></ErrorMessage>

                <button class="btn btn-dark btn-lg" type="submit">Войти</button>

                <div class="form-group mt-3 w-100">
                    <div v-if="message" class="alert alert-danger" role="alert">{{ message }}</div>
                </div>
            </div>
        </Form>
    </div>
</template>

<script>
import { Field, Form, ErrorMessage } from 'vee-validate';

export default {
    name: "CodeComponent",
    components: {
        Field,
        Form,
        ErrorMessage
    },
    data() {
        return {
            user: this.$store.state.auth.user,
            code: 0,
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
            console.log();
            if (this.code) {
                this.$store.dispatch(this.$store.dispatch('auth/tfa', this.user, this.code)).then(
                    () => {
                        console.log(localStorage.getItem('user'));

                        this.$router.go();
                        this.$router.push('/');
                    },
                    error => {
                        this.loading = false;
                        this.message = error.response.data.message;
                    }
                );
            }
        }
    }
};
</script>