import axios from 'axios';

class AttributeService {    
    async getAttributes(table_id) {
        return axios.get('/attributes', {            
            table_id: table_id
        });
    }

    async updateAttributes(attr){
        return axios.put('/attributes/' + attr.id, 
            attr
        );
    }
}

export default new AttributeService();