import axios from 'axios';

class TableService {
    getTables(config_id) {
        return axios.get('/tables', config_id);
    }

    activateTable(table_id) {
        this.getTable(table_id).then(response => {
            let table = response.data[0];
            table.active = true;
            return axios.put('/tables/' + table_id, table);
        });
    }

    disactivateTable(table_id) {
        this.getTable(table_id).then(response => {
            let table = response.data[0];
            table.active = false;
            return axios.put('/tables/' + table_id, table);
        });
    }

    getTableByName(table_name) {
        return axios.get('/tables', {
            params: {
                table_name: table_name
            }
        });
    }

    getTable(id) {
        return axios.get('/tables', {
            params: {
                id: id
            }
        });
    }

    createTableData(table_name, params) {
        let tableAddress = '/' + table_name;
        return axios.post(tableAddress, params);
    }

    getTableData(table_name, id = null) {
        let tableAddress = '/' + table_name;
        return axios.get(tableAddress,
            {
                params: {
                    id: id
                }
            }
        );
    }

    updateTableData(table_name, params) {
        let tableAddress = '/' + table_name + '/' + params.id;
        return axios.put(tableAddress, params);
    }

    removeTableData(table_name, id) {
        let tableAddress = '/' + table_name;
        return axios.delete(tableAddress, id);
    }
}

export default new TableService();