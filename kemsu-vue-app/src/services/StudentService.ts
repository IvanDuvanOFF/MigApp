import axios from 'axios'   

const STUDENT_API_BASE_URL = "http://localhost:8080/api/students"

class StudentService{
    getStudents(){
        return axios.get(STUDENT_API_BASE_URL);
    }

    getStudent(id){
        return axios.get(STUDENT_API_BASE_URL, {
            params: { studentId: id }
        });
    }

    updateStudent(id, student: Student){
        axios.put(STUDENT_API_BASE_URL, {
            params: { studentId: id, data: student }
        });
    }

    createStudent(student: Student){
        axios.post(STUDENT_API_BASE_URL, {
            params: { data: student }
        })
    }

    removeStudent(id){
        axios.delete(STUDENT_API_BASE_URL, {
            params: { studentId: id }
        })
    }
}

export default new StudentService();