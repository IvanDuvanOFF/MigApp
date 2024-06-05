<template>
  <div class="container d-flex align-items-center justify-content-center h-100" v-if="!currentUser">
    <router-view />
  </div>

  <div class="row h-100 m-0" v-if="currentUser">
    <div class="col d-flex flex-column col-md-3 p-0 shadow h-100" style="background-color: #595959; width: 240px">
      <a href="/" class="logo" v-if="isAdmin">
        <div class="position-absolute dropend">
          <a class="btn btn-info mt-1 mx-1 dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
            <font-awesome-icon icon="image" />
          </a>
          <input type="file" class="form-control dropdown-menu" @change="logoLoaded" />
        </div>
        <img alt="Kemsu logo" class="img-fluid logo" src="./assets/logo.jpeg">
      </a>
      <a href="/" v-else>
        <img alt="Kemsu logo" class="img-fluid" src="./assets/logo.jpeg">
      </a>


      <a class="btn rounded-0 navbar-link" data-bs-toggle="collapse" href="#collapse">{{ $t("navbar.record") }}</a>
      <div class="collapse" id="collapse">
        <div class="card rounded-0 border-0">
          <div class="d-flex" v-for="table in recordedTables" :key="table.id">
            <a class="btn rounded-0 w-100 navbar-link sub-item" v-bind:href="$sanitize('/table/' + table.table_name)">{{
    table.table_name }}</a>
            <button v-if="isAdmin == true" @click="disactivateTable(table.id)"
              class="btn btn-danger position-absolute mt-1 mx-1">
              <font-awesome-icon icon="trash-can" />
            </button>
          </div>

          <a v-if="isAdmin" class="btn rounded-0 navbar-link sub-item dropdown-toggle" id="dropdownMenuLink"
            data-bs-toggle="dropdown" aria-bs-haspopup="true" aria-bs-expanded="false">
          </a>
          <div v-if="isAdmin" class="dropdown-menu" aria-bs-labelledby="dropdownMenuLink">
            <a v-for="table in remainingTables" @click="addRemainingTableToRecorded(table.id)" :key="table"
              class="dropdown-item" href="#">
              {{ table.table_name }}
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
import SettingsController from "@/store/settings-controller.js";

export default {
  name: 'App',
  data() {
    let recordedTables = [];
    let remainingTables = [];

    remainingTables = TableService.getTables(1);
    TableService.getTables(1).then(response => {
      console.log(response.data);
      let allTables = response.data;
      this.remainingTables = allTables.filter(function (el) {
        return !el.active;
      });
      this.recordedTables = allTables.filter(function (el) {
        return el.active;
      });
    });

    return {
      recordedTables,
      remainingTables
    };
  },
  mounted() {
    document.onreadystatechange = () => {
      if (document.readyState == "complete") {
        this.switchTheme();
      }
    }
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
    }
  },
  methods: {
    switchTheme() {
      SettingsController.switchAll();
    },
    logOut() {
      this.$store.dispatch('auth/logout');

      this.$router.go();
      this.$router.push('/login');
    },
    addRemainingTableToRecorded(table_id) {
      console.log(table_id);
      TableService.activateTable(table_id).then(() => {
        this.$router.go();
      });

    },
    disactivateTable(table_id) {
      console.log(table_id);
      TableService.disactivateTable(table_id).then(() => {
        this.$router.go();
      });

    }
  }
}
</script>

<style>
@font-face {
  font-family: "Jura";
  src: url("\\fonts\\jura.ttf");
}

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
  background-color: #D6D6D6 !important;
}

.navbar-link.sub-item {
  background-color: #404040 !important;
  color: white !important;
  padding-left: 20% !important;
}

.navbar-link:hover {
  background-color: #eee7e7 !important;
  color: black !important;
  transition-duration: 250ms;
  font-size: larger !important;
}

body {
  height: 100vh;
}

.large-font {
  font-size: x-large !important;
}

.small-font {
  font-size: small !important;
}

#app {
  font-family: Jura, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  height: 100%;
  color: #2c3e50;
}

.dark {
  background-color: black;
}
</style>