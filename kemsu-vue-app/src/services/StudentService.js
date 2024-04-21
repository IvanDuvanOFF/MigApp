import axios from 'axios'
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
        return axios.get('students', {
            params: { id: id },
            headers: AuthHeader()
        });
    }

    // Обновить данные о студенте
    updateStudent(id, params) {
        params.id = id;

        return axios.put('students', {
            params: {
                id: params.id,
                name: params.name,
                surname: params.surname,
                patronymic: params.patronymic,
                birthday: params.birthday,
                email: params.email,
                phone: params.phone,
                sex: params.sex,
                status: params.status,
                countryname: params.countryname
            },
            headers: AuthHeader()
        });
    }

    // Создать нового студента
    createStudent(params) {
        return axios.post('students', {
            name: params.name,
            surname: params.surname,
            patronymic: params.patronymic,
            birthday: params.birthday,
            email: params.email,
            phone: params.phone,
            sex: params.sex,
            status: params.status,
            countryname: params.countryname,

            headers: AuthHeader()
        })
    }

    // Удалить конкретного студента
    removeStudent(id) {
        return axios.delete('students', {
            params: { id: id },
            headers: AuthHeader()
        })
    }
}

export default new StudentService();