import axios from 'axios'   

const AUTH_API_BASE_URL = "http://localhost:8080/api/auth"

class AuthService{    
    signing(login, password){
        return axios.post(AUTH_API_BASE_URL + "/signing", {
            params: { login: login, password: password }
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
}

export default new AuthService();