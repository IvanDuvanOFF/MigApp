import axios from 'axios'   
import { API_BASE_URL, TEMP_BASE_URL } from '.';

const AUTH_API_BASE_URL = TEMP_BASE_URL + "/api/auth"

class AuthService{    
    signing(user){       
        return axios.post(AUTH_API_BASE_URL + "/signing", {
            login: user.username, password: user.password
        }).then(response => {
            if (response.data) {
              localStorage.setItem('user', JSON.stringify(response.data));
            }    
            return response.data;
          });
    }
    
    signingtfa(username, password){
        return axios.post(AUTH_API_BASE_URL + "/signing/tfa", {
            params: { username: username, password: password }
        });
    }

    restore(email){
        return axios.post(AUTH_API_BASE_URL + "/signing", {
            params: { email: email }
        });
    }
    
    restoreByToken(token, email){
        return axios.post(AUTH_API_BASE_URL + `/signing/${token}`, {
            params: { email: email }
        }); 
    }

    refreshToken(refresh_token){
        return axios.post(AUTH_API_BASE_URL + `/signing/{token}`, {
            params: { refresh_token: refresh_token }
        });
    }

    logout(){
        localStorage.removeItem('user');
    }
}

export default new AuthService();