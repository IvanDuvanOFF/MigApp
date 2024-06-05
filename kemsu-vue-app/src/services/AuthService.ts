import axios from 'axios'
import UserService from './UserService';

class AuthService {
    login(user, response) {
        if (response.data) {
            let isAdmin = user.username == "admin";

            user["is_admin"] = isAdmin;
            user["password"] = "";
            localStorage.setItem('user', JSON.stringify(user));
        }
        return response.data;
    }

    signing(user) {
        UserService.getByName(user.username).then(response => {            
            user = response.data[0];
        })


        return axios.post("signing", {
            login: user.username, password: user.password
        }).then(response => {
            return this.login(user, response);
        });
    }

    signingTfa(user, code) {
        return axios.post("auth/signing/tfa", {
            username: user.username,
            code: code
        }).then(response => {
            let userData = {
                username: user.username,
                access_token: response.data.access_token,
                tfa_enabled: false
            }
            localStorage.setItem('user', JSON.stringify(userData));

            return response;
        });
    }

    restore(email) {
        return axios.post("auth/restore", {
            email: email
        });
    }

    restoreByToken(token, email) {
        return axios.post(`auth/restore/${token}`, {
            params: { email: email }
        });
    }

    refreshToken(refresh_token) {
        return axios.post(`auth/refresh`, {
            params: { refresh_token: refresh_token }
        });
    }

    logout() {
        localStorage.removeItem('user');
    }
}

export default new AuthService();