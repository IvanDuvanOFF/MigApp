import axios from 'axios';
import AuthHeader from './AuthHeader';

class StudentService {
    // Получить список всех студентов
    async getStudents(params = null) {
        return axios.get('/students', {
            headers: AuthHeader(),
            params
        });
    }

    // Получить конкретного студента
    getStudent(id) {
        return axios.get('/students/' + id, {            
            headers: AuthHeader()
        });
    }

    // Обновить данные о студенте
    updateStudent(id, params) {
        params.id = id;

        return axios.put('students/' + id, 
            params
        );
    }

    // Создать нового студента
    createStudent(params) {
        return axios.post('students', params);
    }

    // Удалить конкретного студента
    removeStudent(id) {
        return axios.delete('students/' + id, {
            id: id,
            headers: AuthHeader()
        })
    }
}

export default new StudentService();