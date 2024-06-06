import axios from 'axios';
import SettingsController from '@/store/settings-controller';

class TableService {
    // Получить таблицы, которые относятся к конфигурации
    getTables(config_id) {        
        return axios.get(SettingsController.getBdPath() + 'tables', {
            params:{
                config_id: config_id
            }
        });
    }
    
    // Поставить таблицу на учет
    activateTable(table_id) {
        return this.getTable(table_id).then(response => {
            let table = response.data[0];
            table.active = true;
            return axios.put('/tables/' + table_id, table);
        });
    }

    // Убрать таблицу с учета
    disactivateTable(table_id) {
        return this.getTable(table_id).then(response => {
            let table = response.data[0];
            table.active = false;
            return axios.put('/tables/' + table_id, table);
        });
    }

    // Получить таблицу по имени
    getTableByName(table_name) {
        return axios.get('/tables', {
            params: {
                table_name: table_name
            }
        });
    }

    // Получить таблицу по id
    getTable(id) {
        return axios.get('/tables', {
            params: {
                id: id
            }
        });
    }

    // Создать новую запись в таблице
    createTableData(table_name, params) {
        let tableAddress = SettingsController.getBdPath() + table_name;
        return axios.post(tableAddress, params);
    }

    // Получить запись из таблицы
    getTableData(table_name, id = null) {
        let tableAddress = SettingsController.getBdPath() + table_name;        
        return axios.get(tableAddress,
            {
                params: {
                    id: id
                }
            }
        );
    }

    // Обновить запись из таблицы
    updateTableData(table_name, params) {
        let tableAddress = SettingsController.getBdPath() + table_name + '/' + params.id;
        return axios.put(tableAddress, params);
    }

    // Удалить запись из таблицы
    removeTableData(table_name, id) {
        let tableAddress = SettingsController.getBdPath() + table_name;
        return axios.delete(tableAddress, id);
    }
}

export default new TableService();