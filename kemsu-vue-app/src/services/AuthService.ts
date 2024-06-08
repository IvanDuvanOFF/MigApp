import axios from 'axios'
import UserService from './UserService';
import ConfigService from './ConfigService';
import SettingsController from '@/store/settings-controller';

class AuthService {
    // Общий метод авторизации
    login(user, response) {
        if (response.data) {
            let isAdmin = user.username.includes("admin");

            user["is_admin"] = isAdmin;
            user["password"] = "";
            localStorage.setItem('user', JSON.stringify(user));

            // Поставить первую конфигурацию по умолчанию
            ConfigService.getConfigures(user.workspace_id).then(response => {
                let firstConfigPath = response.data[0].bd_path;
                SettingsController.setBdPath(firstConfigPath);
            })
        }
        return response.data;
    }

    // Первичная авторизация пользователя
    signing(user) {
        UserService.getByName(user.username).then(response => {            
            user = response.data[0];
        })

        if(user == null){
            throw(new Error("User not found"));
        }        

        return axios.post("signing", {
            login: user.username, password: user.password
        }).then(response => {
            return this.login(user, response);
        });
    }

    // Вторичная авторизация пользователя по коду 2F
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

    // Восстановить аккаунт
    restore(email) {
        return axios.post("auth/restore", {
            email: email
        });
    }

    // Восстановить по ключу
    restoreByToken(token, email) {
        return axios.post(`auth/restore/${token}`, {
            params: { email: email }
        });
    }

    // Восстановить ключ доступа к Универсальной Консоли
    refreshToken(refresh_token) {
        return axios.post(`auth/refresh`, {
            params: { refresh_token: refresh_token }
        });
    }

    // Осуществить выход из аккаунта пользователя
    logout() {
        localStorage.removeItem('user');
    }
}

export default new AuthService();