import { createApp } from 'vue';
import { createStore } from 'vuex';
import store from './store';
import App from './App.vue';
import {createRouter, createWebHistory} from 'vue-router'  
import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

import AuthComponent from './components/AuthComponent.vue';
import { auth } from './store/auth.module';


const app = createApp(App);
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: AuthComponent,

    },
];
const thisstore = createStore(store);
thisstore.registerModule('auth', auth);

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
  });

app.config.productionTip = false;
app.use(router);
app.use(thisstore);
app.mount('#app')
