import axios from 'axios';

class AttributeService {    
    async getAttributes(table_id) {
        return axios.get('/attributes', {            
            table_id: table_id
        });
    }
    
    
}

export default new AttributeService();