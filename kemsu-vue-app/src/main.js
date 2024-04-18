import { createApp } from 'vue';
import { createStore } from 'vuex';
import store from './store';
import App from './App.vue';
import axios from 'axios';
import { createRouter, createWebHistory } from 'vue-router'
import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from './plugins/font-awesome'
import AuthComponent from './components/AuthComponent.vue';
import StudentsComponent from './components/StudentsComponent.vue';
import SingleStudentComponent from './components/SingleStudentComponent.vue';
import { auth } from './store/auth.module';
import CodeComponent from './components/CodeComponent.vue';
import SettingsComponent from './components/SettingsComponent.vue';

const app = createApp(App);
const routes = [
  {
    path: '/students/create',
    name: 'CreateSingleStudent',
    component: SingleStudentComponent,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/',
    name: 'Index',
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/students/:id',
    name: 'SingleStudent',
    component: SingleStudentComponent,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/students',
    name: 'Students',
    component: StudentsComponent,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: SettingsComponent,
    meta: {
      requiresAuth: true
    }    
  },
  {
    path: '/login',
    name: 'Login',
    component: AuthComponent,
  },  
  {
    path: '/login/tfa',
    name: 'Tfa',
    component: CodeComponent
  }
];
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});
router.beforeEach((to, from, next) => {
  const loggedIn = thisstore.state.auth.status.loggedIn;
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!loggedIn) {
      next({ name: 'Login' })
    } else {
      next()
    }
  } else {
    next()
  }
});

const thisstore = createStore(store);
thisstore.registerModule('auth', auth);

axios.defaults.baseURL = "http://109.71.242.151/api/";
axios.interceptors.response.use(response => {
  return response;
});

axios.defaults.headers.get['Access-Control-Allow-Origin'] = '*';
axios.defaults.headers.get['Access-Control-Allow-Methods'] = 'GET, PUT, POST, DELETE, OPTIONS, post, get';
axios.defaults.headers.get['Access-Control-Max-Age'] = '3600';
axios.defaults.headers.get['Access-Control-Allow-Headers'] = 'Origin, Content-Type, X-Auth-Token';
axios.defaults.headers.get['Access-Control-Allow-Credentials'] = 'true';


app.config.productionTip = false;
app.use(thisstore);
app.use(router);
app.component("font-awesome-icon", FontAwesomeIcon);
app.mount('#app');
