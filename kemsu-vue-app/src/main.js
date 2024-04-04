import { createApp } from 'vue';
import { createStore } from 'vuex';
import store from './store';
import App from './App.vue';
import {createRouter, createWebHistory} from 'vue-router'  
import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
// import { VeeValidate } from 'vee-validate';
import AuthComponent from './components/AuthComponent.vue';
import { auth } from './store/auth.module';



// Vue.use(VeeValidate);
// Vue.config.productionTip = false;
const app = createApp(App);
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: AuthComponent,//shsould be imported 

    },
];
const thisstore = createStore(store);
thisstore.registerModule('auth', auth);

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
  });

//app.use(VeeValidate);
app.use(router);
app.use(thisstore);
app.mount('#app')
//new Vue({router, store, render: h => h(App)}).$ mount('#app')
