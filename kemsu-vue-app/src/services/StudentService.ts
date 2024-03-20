import axios from 'axios'   

const STUDENT_API_BASE_URL = "http://localhost:8080/api/students"

class StudentService{
    // Получить список всех студентов
    getStudents(){
        return axios.get(STUDENT_API_BASE_URL);
    }

    // Получить конкретного студента
    getStudent(id){
        return axios.get(STUDENT_API_BASE_URL, {
            params: { studentId: id }
        });
    }

    // Обновить данные о студенте
    updateStudent(id, student: Student){
        axios.put(STUDENT_API_BASE_URL, {
            params: { studentId: id, data: student }
        });
    }

    // Создать нового студента
    createStudent(student: Student){
        axios.post(STUDENT_API_BASE_URL, {
            params: { data: student }
        })
    }

    // Удалить конкретного студента
    removeStudent(id){
        axios.delete(STUDENT_API_BASE_URL, {
            params: { studentId: id }
        })
    }
}

export default new StudentService();