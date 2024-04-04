<template>
    <div id="root">
        <Form name="loginForm" novalidate @submit="handleLogin">
            <div class="d-flex flex-column mt-5 flex-wrap w-25 m-auto align-items-center">
                <label for="username">Логин</label>
                <Field 
                    :rules="validateField"
                    id="username"                    
                    v-model="user.username"
                    v-validate="'required'"
                    type="text" 
                    class="form-control mt-3 mb-3"
                    name="username"
                    autocomplete="false" />                
                <ErrorMessage name="username" class="alert alert-danger"></ErrorMessage>

                <label for="password">Пароль</label>               
                <Field 
                    :rules="validateField"
                    id="password"                    
                    v-model="user.password"
                    v-validate="'required'"
                    type="password" 
                    class="form-control mt-3 mb-3"
                    name="password"
                    autocomplete="false" />

                <ErrorMessage name="password" class="alert alert-danger"></ErrorMessage>
                
                <button class="btn btn-info" type="submit">Войти</button>                
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
    components:{
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
            this.$router.push('/students');
        }
    },    

    methods: {
        validateField(value){
            if(!value){
                return 'Поле должно быть заполнено';
            }

            return true;
        },

        handleLogin() {
            this.loading = true;
            console.log(this.$store);
            // console.log(AuthService.signing(this.user));
            console.log(this.$store.dispatch('auth/login', this.user));
            if (this.user.username && this.user.password) {
                    this.$store.dispatch('auth/login', this.user).then(
                        () => {
                            this.$router.push('/students');
                        },
                        error => {
                            this.loading = false;
                            this.message =
                                (error.response && error.response.data) ||
                                error.message ||
                                error.toString();
                        }
                    );
                }
        }
    }
};
</script>