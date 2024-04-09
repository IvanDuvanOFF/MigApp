import axios from 'axios'
import AuthHeader from './AuthHeader';
import { TEMP_BASE_URL } from '.';

const STUDENT_API_BASE_URL = TEMP_BASE_URL + "";

class StudentService {
    // Получить список всех студентов
    async getStudents() {
        return axios.get(STUDENT_API_BASE_URL + '/students', {
            headers: AuthHeader()
        }).then(res => {return res.data})
    }

    // Получить конкретного студента
    getStudent(id) {
        return axios.get(STUDENT_API_BASE_URL, {
            params: { studentId: id },
            headers: AuthHeader()
        });
    }

    // Обновить данные о студенте
    updateStudent(id, student) {
        axios.put(STUDENT_API_BASE_URL, {
            params: { studentId: id, data: student },
            headers: AuthHeader()
        });
    }

    // Создать нового студента
    createStudent(student) {
        axios.post(STUDENT_API_BASE_URL, {
            params: { data: student },
            headers: AuthHeader()
        })
    }

    // Удалить конкретного студента
    removeStudent(id) {
        axios.delete(STUDENT_API_BASE_URL, {
            params: { studentId: id },
            headers: AuthHeader()
        })
    }
}

export default new StudentService();