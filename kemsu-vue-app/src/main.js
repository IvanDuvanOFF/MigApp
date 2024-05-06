import { createApp } from 'vue/dist/vue.esm-bundler';
import { createStore } from 'vuex';
import Vue3Sanitize from "vue-3-sanitize";
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
import CodeComponent from './components/CodeComponent.vue';
import SettingsComponent from './components/SettingsComponent.vue';
import ConfigComponent from './components/ConfigComponent.vue';

import EditMainPage from './components/edit-versions/EditMainPage.vue';

import { auth } from './store/auth.module';


import i18n from '@/i18n/index.js';
import Trans from '@/i18n/translate';

import { LOCAL_URL } from './urls';
import MainPage from './components/MainPage.vue';
import EditController from './store/edit-controller';

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
    component: MainPage,
    meta: {
      requiresAuth: true,
      requiresEdit: false
    }
  },
  {
    path: '/edit',
    name: 'EditIndex',
    component: EditMainPage,
    meta: {
      requiresAuth: true,
      requiresEdit: true
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
    path: '/config',
    name: 'Config',
    component: ConfigComponent,
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
  Trans.switchLanguage(Trans.getPersistedLocale());

  const loggedIn = thisstore.state.auth.status.loggedIn;

  if (to.meta.requiresAuth && !loggedIn) {
    next({ name: 'Login' });
  }

  if (to.meta.requiresEdit != null) {
    if (to.meta.requiresEdit != EditController.mode) {
      if (to.meta.requiresEdit) {
        let nameWithoutEdit = to.name.replace("Edit", "");
        next({ name: nameWithoutEdit });
      }
      else {
        next({ name: "Edit" + to.name });
        
      }
    }
  }

  next();
});

const thisstore = createStore(store);
thisstore.registerModule('auth', auth);

axios.defaults.baseURL = LOCAL_URL;
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
app.use(Vue3Sanitize);
app.use(router);
app.use(i18n);
app.component("font-awesome-icon", FontAwesomeIcon);
app.mount('#app');
