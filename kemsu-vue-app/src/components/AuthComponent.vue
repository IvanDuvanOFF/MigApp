<template>
    <div id="root">
        <div class="d-flex flex-column mt-5 flex-wrap w-25 m-auto align-items-center">
            <label>Логин</label>
            
            <input class="form-control mt-3 mb-3" type="" v-model="login">
        
            <label>Пароль</label>
            
            <input class="form-control mt-3 mb-3" type="password" v-model="password">
        
        <button class="btn btn-dark mt-3 mb-3 w-100" v-on:click="buttonPressed">Войти</button>
        </div>
    </div>    
</template>

<style>

</style>

<script>
import { ApiClient, AuthenticationControllerApi, SignRequest } from '..';

export default{
    name: "AuthComponent",
    data(){
        return{
            login: '',
            password: ''
        }
    },
    methods:{
        buttonPressed(event){
            console.log(event);
            function printResult(error, data, response) {
                console.log(`error: ${error}`);
                console.log(`error: ${data}`);
                console.log(`error: ${response}`);
                }
            var signRequest = new SignRequest(this.login, this.password);            
            var auth = new AuthenticationControllerApi(new ApiClient());
            var response = auth.signIn(signRequest, printResult);
            console.log(this.login);
            console.log(response);
            console.log(signRequest);
            console.log(this.password);
        }
    }
}
</script>