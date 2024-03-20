import axios from 'axios'   

const USER_API_BASE_URL = "http://localhost:8080/api/users"

class UserService{
    // Аутентифицировать пользователя
    auth(user: User){
        return axios.get(USER_API_BASE_URL, {
            params: { user: User }
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