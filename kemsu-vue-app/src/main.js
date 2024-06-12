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
import TableComponent from './components/TableComponent.vue';
import SingleObjectComponent from './components/SingleObjectComponent.vue';
import CodeComponent from './components/CodeComponent.vue';
import SettingsComponent from './components/SettingsComponent.vue';
import ConfigComponent from './components/ConfigComponent.vue';

import { auth } from './store/auth.module';


import i18n from '@/i18n/index.js';
import Trans from '@/i18n/translate';

import { LOCAL_URL } from './urls';
import MainPage from './components/MainPage.vue';
import WelcomePage from './components/WelcomePage.vue';

const app = createApp(App);
const routes = [
  {
    path: '/',
    name: 'Index',
    component: MainPage,
    meta: {
      requiresAuth: true,
      title: "Главная страница"
    },
  },
  {
    path: '/welcome',
    name: 'Welcome',
    component: WelcomePage
  },  
  {
    path: '/table/:tableName/:id',
    name: 'SingleObject',
    component: SingleObjectComponent,
    meta: {
      title: "Просмотр экземпляра таблицы \"tableName\"",
      requiresAuth: true
    }
  },
  {
    path: '/table/:tableName/create',
    name: 'CreateSingleStudent',
    component: SingleObjectComponent,
    meta: {
      title: "Создать новый экземпляр таблицы \"tableName\"",
      requiresAuth: true
    }
  },
  {
    path: '/table/:tableName',
    name: 'Table',
    component: TableComponent,
    meta: {
      title: "Таблица \"tableName\"",
      requiresAuth: true,
    }
  },  
  {
    path: '/config',
    name: 'Config',
    component: ConfigComponent,
    meta: {
      requiresAuth: true,
      title: "Конфигурация"
    }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: SettingsComponent,
    meta: {
      requiresAuth: true,
      title: "Настройки"
    }
  },
  {
    path: '/login',
    name: 'Login',
    meta: {
      title: "Вход"
    },
    component: AuthComponent,
  },
  {
    path: '/login/tfa',
    name: 'Tfa',
    component: CodeComponent,
    meta: {
      title: "Проверочный код"
    },
  }
];
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});
router.beforeEach((to, from, next) => {
  let titleWithParams = to.meta?.title ?? 'Универсальная Консоль';
  if (to.params) {
    Object.getOwnPropertyNames(to.params).forEach(param => {
      titleWithParams = titleWithParams.replace(param, to.params[param]);
    });
  }
  document.title = titleWithParams;  
  Trans.switchLanguage(Trans.getPersistedLocale());

  const loggedIn = thisstore.state.auth.status.loggedIn;

  if (to.meta.requiresAuth && !loggedIn) {
    next({ name: 'Login' });
  }

  if (!to.meta.requiresAuth && loggedIn) {
    next({ name: 'Index' });
  }  

  next();
});

const thisstore = createStore(store);
thisstore.registerModule('auth', auth);

axios.defaults.baseURL = LOCAL_URL;
axios.interceptors.response.use(response => {
  return response;
});
axios.defaults.withCredentials = true;
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
