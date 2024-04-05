import axios from 'axios'   
import { API_BASE_URL } from '.';

const AUTH_API_BASE_URL = API_BASE_URL + "/auth"

class AuthService{    
    signing(user){       
        return axios.post(AUTH_API_BASE_URL + "/signing", {
            params: { login: user.username, password: user.password }
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