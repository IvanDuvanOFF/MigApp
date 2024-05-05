import axios from 'axios'

class AuthService {
    signing(user) {
        return axios.post("signing", {
            login: user.username, password: user.password
        }).then(response => {
            if (response.data) {
                let userData = {
                    username: user.username,
                    jwt: response.data.access_token,
                    tfa: response.data.tfa_enabled,
                    is_admin: true,
                    edit_mode: false
                }
                localStorage.setItem('user', JSON.stringify(userData));
            }
            return response.data;
        });
    }

    signingTfa(user, code) {
        return axios.post("auth/signing/tfa", {
            username: user.username, 
            code: code
        }).then(response => {
            let userData = {
                username: user.username,
                jwt: response.data.access_token,
                tfa: false
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