<template>
  <img alt="Kemsu logo" src="./assets/logo.jpeg" v-if="!currentUser">
  <div class="container" v-if="!currentUser">
    <router-view />
  </div>

  <button @click="changeEditMode" v-if="isAdmin" class="btn" :class="[getEditMode ? 'btn-info' : 'btn-dark']"
    style="z-index: 10; position: fixed; right: 50px; top: 25px;">
    Режим редактирования: {{ getEditMode ? 'ON' : 'OFF' }}
  </button>

  <div class="row h-100 m-0" v-if="currentUser">
    <div class="col d-flex flex-column col-md-3 p-0 shadow h-100" style="background-color: #C8C3FF; width: 240px">
      <a href="/">
        <img alt="Kemsu logo" class="img-fluid" src="./assets/logo.jpeg">
      </a>

      <a class="btn rounded-0 navbar-link" data-bs-toggle="collapse" href="#collapse">{{ $t("navbar.record") }}</a>
      <div class="collapse" id="collapse">
        <div class="card rounded-0 border-0">
          <div class="d-grid" v-for="table in recordedTables" :key="table.id">
            <a class="btn rounded-0 navbar-link sub-item" v-bind:href="$sanitize('/table/' + table.table_name)">{{
    table.table_name }}</a>
            <button v-if="getEditMode" class="btn btn-danger position-absolute rounded-0 h-50" style="right: 0">
              <font-awesome-icon icon="trash-can" />
            </button>
          </div>

          <a v-if="getEditMode" class="btn rounded-0 navbar-link sub-item dropdown-toggle" id="dropdownMenuLink"
            data-bs-toggle="dropdown" aria-bs-haspopup="true" aria-bs-expanded="false">
          </a>
          <div v-if="getEditMode" class="dropdown-menu" aria-bs-labelledby="dropdownMenuLink">
            <a v-for="table in remainingTables" @click="addRemainingTableToRecorded" :key="table" class="dropdown-item"
              href="#">
              {{ table }}
            </a>
          </div>
        </div>
      </div>
      <a class="btn rounded-0 navbar-link" href="#">{{ $t("navbar.notifications") }}</a>
      <a class="btn rounded-0 navbar-link settings" href="/settings">{{ $t("navbar.settings") }}</a>
      <a v-if="isAdmin" class="btn rounded-0 navbar-link settings" href="/config">{{ $t("navbar.config") }}</a>
      <a class="btn rounded-0 navbar-link exit" @click.prevent="logOut">{{ $t("navbar.exit") }}</a>
    </div>

    <router-view />

  </div>
</template>

<script>
import TableService from "@/services/TableService.js";
import EditController from "./store/edit-controller.js";

export default {
  name: 'App',
  data() {
    let recordedTables = [];
    let remainingTables = [];

    remainingTables = TableService.getTables(1);
    TableService.getRecordedTables(1).then(response => {
      this.recordedTables = response.data;
    });

    remainingTables = remainingTables.filter(function (el) {
      return !recordedTables.includes(el);
    });
    return {
      recordedTables,
      remainingTables
    };
  },

  computed: {
    currentUser() {
      console.log('user is');
      console.log(this.$store.state.auth.status.loggedIn);
      return this.$store.state.auth.status.loggedIn;
    },
    isAdmin() {
      let user = JSON.parse(localStorage.getItem('user'));
      if (user == null) {
        return false;
      }

      return user.is_admin;
    },
    getEditMode() {
      return EditController.mode;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');

      this.$router.go();
      this.$router.push('/login');
    },
    changeEditMode() {
      EditController.mode = !EditController.mode;
      this.$router.go();
    },
    addRemainingTableToRecorded() {
      alert("Таблица должна добавиться в список учтенных");
    }
  }
}
</script>

<style>
@import url("https://fonts.googleapis.com/css?family=Jura");

.font-weight-bold {
  font-weight: bold;
}

.font-weight-light {
  font-weight: bolder;
}

.font-italic {
  font-style: italic;
}

.application {
  font-family: "Jura", serif;
}

.navbar-link {
  padding-top: 10px !important;
  padding-bottom: 10px !important;
  padding-left: 25px !important;
  border-bottom-color: #000000 !important;
  border-bottom-width: 1px !important;
  text-align: left !important;
  font-size: larger !important;
}

.navbar-link.exit {
  background-color: rgba(255, 122, 0, 0.52);
}

.navbar-link.settings {
  background-color: rgba(5, 0, 255, 0.32);
}

.navbar-link.sub-item {
  background-color: rgba(92, 92, 92);
  color: white !important;
  padding-left: 20% !important;
}

.navbar-link:hover {
  background-color: rgb(255, 255, 255) !important;
  color: black !important;
  transition-duration: 250ms;
  font-size: larger !important;
}

body {
  height: 100vh;
}

#app {
  font-family: Jura, sans-serif;
  font-size: larger !important;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  height: 100%;
  color: #2c3e50;
}

</style>
