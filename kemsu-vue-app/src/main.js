import { createApp } from 'vue';
import { createStore } from 'vuex';
import store from './store';
import App from './App.vue';
import axios from 'axios';
import {createRouter, createWebHistory} from 'vue-router'  
import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

import AuthComponent from './components/AuthComponent.vue';
import { auth } from './store/auth.module';


const app = createApp(App);
const routes = [
    {
        path: '/',
        name: 'Index',
        meta:{
            requiresAuth: true
        }
    },
    {
        path: '/login',
        name: 'Login',
        component: AuthComponent,

    },
];
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
  });
  router.beforeEach((to, from, next) => {
    const loggedIn = localStorage.getItem('user');

    if (to.matched.some(record => record.meta.requiresAuth)) {    
      if (!loggedIn) {
        next({ name: 'Login' })
      } else {
        next() // go to wherever I'm going
      }
    } else {
      next() // does not require auth, make sure to always call next()!
    }
  });

const thisstore = createStore(store);
thisstore.registerModule('auth', auth);


app.config.productionTip = false;
app.use(router);
app.use(thisstore);
app.mount('#app')


axios.defaults.headers.get['Access-Control-Allow-Origin'] = '*';
axios.defaults.headers.get['Access-Control-Allow-Methods'] = 'GET, PUT, POST, DELETE, OPTIONS, post, get';
axios.defaults.headers.get['Access-Control-Max-Age'] = '3600';
axios.defaults.headers.get['Access-Control-Allow-Headers'] = 'Origin, Content-Type, X-Auth-Token';
axios.defaults.headers.get['Access-Control-Allow-Credentials'] = 'true';