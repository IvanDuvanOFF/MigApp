import axios from 'axios'

class ConfigService {
    getConfigures(workspace_id){
        return axios.get("/config", {
            params:{
                workspace_id
            }
        });
    }

    getConfigure(config_id){
        return axios.get("/config", {
            params:{
                id: config_id
            }
        });
    }

    getConfigureByName(path){
        return axios.get("/config", {
            params:{
                bd_path: path
            }
        });
    }

    changeConfigure(config){
        return axios.put("/config/" + config.id, config);
    }
}

export default new ConfigService();