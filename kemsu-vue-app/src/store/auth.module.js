import AuthService from '../services/AuthService.ts';

const user = JSON.parse(localStorage.getItem('user'));
const tfa = user != null ? user.tfa : true;
const initialState = user != null && !tfa
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit }, user) {      
      return AuthService.signing(user).then(
        response => {
          console.log(response);
          if(response.tfa_enabled) {
            console.log("enabled tfa for user: " + user)
            commit('tfaNeeded', user);
          }
          else{
            commit('loginSuccess', user);
          }
          
          return Promise.resolve(user);
        },
        (error) => {
          commit('loginFailure');
          console.log(error);
          return Promise.reject(error);
        }        
      );
    },
    logout({ commit }) {
      AuthService.logout();
      commit('logout');
    },
    tfa({ commit }, user, code) {
      return AuthService.signingTfa(user, code).then(
        () => {
          commit('loginSuccess', user);
          return Promise.resolve(user);
        },
        error => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    }
  },
  mutations: {
    loginSuccess(state, user) {
      state.status.loggedIn = true;
      state.user = user;
    },
    tfaNeeded(state, user) {
      state.status.loggedIn = false;
      state.user = user;
    },
    loginFailure(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state) {
      state.status.loggedIn = false;
      state.user = null;
    },    
  }
};