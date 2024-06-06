import axios from 'axios';

class AttributeService {
    // Получить атрибуты по таблице
    async getAttributes(table_id) {
        return axios.get('/attributes', {
            params: {
                table_id: table_id
            }

        });
    }

    // Обновить атрибуты
    async updateAttributes(attr) {
        return axios.put('/attributes/' + attr.id,
            attr
        );
    }
}

export default new AttributeService();