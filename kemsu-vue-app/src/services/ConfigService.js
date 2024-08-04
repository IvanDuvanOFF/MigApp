import axios from 'axios'

class ConfigService {
    // Получить относящиеся к рабочей среде конфигурации
    getConfigures(workspace_id){
        return axios.get("/config", {
            params:{
                workspace_id
            }
        });
    }

    // Получить конфигурацию по id и рабочей среде
    getConfigure(config_id, workspace_id){
        return axios.get("/config", {
            params:{
                id: config_id,
                workspace_id: workspace_id
            }
        });
    }

    // Получить конфигурацию по имени и рабочей среде
    getConfigureByName(path, workspace_id){
        return axios.get("/config", {
            params:{
                bd_path: path,
                workspace_id: workspace_id
            }
        });
    }

    // Поменять конфигурацию
    changeConfigure(config){
        return axios.put("/config/" + config.id, config);
    }
}

export default new ConfigService();