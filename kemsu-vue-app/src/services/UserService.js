import axios from "axios";

class UserService {
    getUsers(workspaceId) {
        return axios.get('users', {
            workspace_id: workspaceId
        });
    }

    getByName(name) {
        return axios.get('users', {
            params: {
                username: name
            }
        });
    }

    removeUser(id) {
        return axios.delete('users/' + id);
    }

    editUser(id, params) {
        // id: int
        // username: string
        // password: string
        // workspace_id: int
        return axios.put('users/' + id, params);
    }

    createUser(username, password, workspace_id) {
        // username: string
        // password: string
        // workspace_id: int        
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
        // В конце у workspace.root_user_id = user.id которого мы сделали только что
        return axios.post('users/root', {
            username: username,
            password: password,
            bd_path: bd_path
        })
    }
}

export default new UserService();