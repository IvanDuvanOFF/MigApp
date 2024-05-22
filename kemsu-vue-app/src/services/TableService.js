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

    getTableData(config_id, table_id){
        return axios.get('/tables/data', {
            config_id: config_id,
            id: table_id
        });
    }

    updateTableData(table_id, params){
        params.table_id = table_id;
        return axios.post('/tables/data', params)
    }

    removeTableData(table_id, id){
        return axios.delete('/table/data', {
            table_id: table_id,
            id: id
        })
    }
}

export default new TableService();