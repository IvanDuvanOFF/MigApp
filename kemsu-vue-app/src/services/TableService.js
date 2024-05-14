import axios from 'axios';

class TableService {    
    // 
    getTables(config_id) {        
        console.log(config_id);
        let temp = ["Students", "Documents"];
        return temp;
    }

    getRecordedTables(config_id) {
        return axios.get('/tables', {
            config_id: config_id
        });
    }

    getTableByName(table_name) {
        return axios.get('/tables', {
            table_name: table_name
        });
    }
}

export default new TableService();