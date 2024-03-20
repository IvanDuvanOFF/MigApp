import axios from 'axios'

const DOCUMENT_API_BASE_URL = "http://localhost:8080/api/documents"


class DocumentService{
    getDocuments(){
        return axios.get(DOCUMENT_API_BASE_URL);
    }

    getDocument(id){
        return axios.get(DOCUMENT_API_BASE_URL, {
            params: { documentId: id }
        });
    }

    updateDocument(id, doc: KemsuDocument){
        axios.put(DOCUMENT_API_BASE_URL, {
            params: { documentId: id, data: doc }
        });
    }

    createDocument(doc: KemsuDocument){
        axios.post(DOCUMENT_API_BASE_URL, {
            params: { doc }
        })
    }

    removeDocument(id){
        axios.delete(DOCUMENT_API_BASE_URL, {
            params: { documentId: id }
        })
    }
}

export default new DocumentService();