import axios from "axios";

class UserService {
    // Получить всех пользователей из рабочего пространства
    getUsers(workspaceId) {
        return axios.get('users', {
            params: {
                workspace_id: workspaceId
            }            
        });
    }

    // Получить пользователя по имени
    getByName(name) {
        return axios.get('users', {
            params: {
                username: name
            }
        });
    }

    // Удалить пользователя
    removeUser(id) {
        return axios.delete('users/' + id);
    }

    // Редактировать пользователя
    editUser(id, params) {        
        // id: int
        // username: string
        // password: string
        // workspace_id: int
        return axios.put('users/' + id, params);
    }

    // Создать нового пользователя для рабочего пространства
    createUser(username, password, workspace_id) {        
        return axios.post('users', {
            username,
            password,
            workspace_id
        })
    }

    createRootUser(username, password, bd_path) {
        // username: string
        // password: string
        // bd_path: string 
        // ===============
        // Сначала создает новый config с bd_path
        // Потом workspace с конфигом
        // Потом user с workspace
        // В конце у workspace.root_user_id = user.id, который только что был создан
        return axios.post('users/root', {
            username: username,
            password: password,
            bd_path: bd_path
        })
    }
}

export default new UserService();