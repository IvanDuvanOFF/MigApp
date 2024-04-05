import axios from 'axios'   
import AuthHeader from './AuthHeader';

const STUDENT_API_BASE_URL = "http://localhost:8080/api/students"

class StudentService{
    // Получить список всех студентов
    getStudents(){
        return axios.get(STUDENT_API_BASE_URL), {
            headers: AuthHeader()
        };
    }

    // Получить конкретного студента
    getStudent(id){
        return axios.get(STUDENT_API_BASE_URL, {
            params: { studentId: id },
            headers: AuthHeader()
        });
    }

    // Обновить данные о студенте
    updateStudent(id, student: Student){
        axios.put(STUDENT_API_BASE_URL, {
            params: { studentId: id, data: student },
            headers: AuthHeader()
        });
    }

    // Создать нового студента
    createStudent(student: Student){
        axios.post(STUDENT_API_BASE_URL, {
            params: { data: student },
            headers: AuthHeader()
        })
    }

    // Удалить конкретного студента
    removeStudent(id){
        axios.delete(STUDENT_API_BASE_URL, {
            params: { studentId: id },
            headers: AuthHeader()
        })
    }
}

export default new StudentService();