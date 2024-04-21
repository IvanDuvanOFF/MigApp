<template>
  <img alt="Kemsu logo" src="./assets/logo.jpeg" v-if="!currentUser">
  <div class="container" v-if="!currentUser">
    <router-view />
  </div>

  
  <div class="row h-100 m-0" v-if="currentUser">
    <div class="col d-flex flex-column col-md-3 p-0 shadow h-100"
      style="background-color: #C8C3FF; width: 240px">
      <a href="/">
        <img alt="Kemsu logo" class="img-fluid" src="./assets/logo.jpeg">
      </a>
      
      <a class="btn rounded-0 navbar-link" href="/students">Учет</a>
      <a class="btn rounded-0 navbar-link" href="#">Уведомления</a>
      <a class="btn rounded-0 navbar-link settings" href="/settings">Настройки</a>
      <a class="btn rounded-0 navbar-link exit" @click.prevent="logOut">Выйти</a>      
    </div>
    
      <router-view />
    
  </div>  
</template>

<script>
export default {
  name: 'App',
  computed:{
    currentUser() {
          console.log('user is');
          console.log(this.$store.state.auth.status.loggedIn);
          return this.$store.state.auth.status.loggedIn;
    }
  },
  methods:{
    logOut() {
      this.$store.dispatch('auth/logout');

      this.$router.go();
      this.$router.push('/login');      
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
}

.navbar-link.exit {
  background-color: rgba(255,122,0,0.52);
}

.navbar-link.settings {
  background-color: rgba(5,0,255,0.32);
}

.navbar-link:hover {
  background-color: rgb(181, 181, 181) !important;
  font-size: large !important;
  transition-duration: 250ms;
}

body{
  height: 100vh;
}

#app {
  font-family: Jura, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  height: 100%;
  color: #2c3e50;  
}
</style>
