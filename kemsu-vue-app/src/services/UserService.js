import axios from "axios";

class UserService {
    getUsers(workspaceId) {
        return axios.get('users', {            
            workspace_id: workspaceId
        });
    }

    removeUser(id) {
        return axios.delete('users/' + id);
    }

    editUser(id, params) {
        return axios.put('users/' + id, params);
    }
}

export default new UserService();