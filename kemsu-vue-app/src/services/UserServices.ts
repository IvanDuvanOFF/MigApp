import axios from 'axios'   

const USER_API_BASE_URL = "http://localhost:8080/api/users"

class UserService{
    // Аутентифицировать пользователя
    signing(login, password){
        return axios.post(USER_API_BASE_URL, {
            params: { login: login, password: password }
        });
    }

    // Выйти из учетной записи
    exit(id){
        return axios.get(USER_API_BASE_URL, {
            params: { studentId: id }
        });
    }

    
}

export default new UserService();