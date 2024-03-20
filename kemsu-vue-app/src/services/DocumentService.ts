import axios from 'axios'

const DOCUMENT_API_BASE_URL = "http://localhost:8080/api/documents"


class DocumentService{
    // Получить список всех документов
    getDocuments(){
        return axios.get(DOCUMENT_API_BASE_URL);
    }

    // Получить конкретный документ
    getDocument(id){
        return axios.get(DOCUMENT_API_BASE_URL, {
            params: { documentId: id }
        });
    }

    // Обновить данные о документе
    updateDocument(id, doc: KemsuDocument){
        axios.put(DOCUMENT_API_BASE_URL, {
            params: { documentId: id, data: doc }
        });
    }

    // Создать новый документ
    createDocument(doc: KemsuDocument){
        axios.post(DOCUMENT_API_BASE_URL, {
            params: { doc }
        })
    }

    // Удалить конкретный документ
    removeDocument(id){
        axios.delete(DOCUMENT_API_BASE_URL, {
            params: { documentId: id }
        })
    }
}

export default new DocumentService();