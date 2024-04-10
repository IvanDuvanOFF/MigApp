<template>
    <Form @invalid-submit="onInvalidSubmit" @submit="onStudentSubmit" name="studentForm" :validation-schema="schema"
        style="border: 1px solid darkgrey;" class="col d-flex flex-column justify-content-xl-center p-3 gap-3">
        <div class="input-group d-flex flex-column">
            <label class="form-label">Имя*</label>
            <Field :disabled="disabled" type="text" v-model="student.name" class="form-control w-100 rounded-0"
                name="name" />
            <ErrorMessage class="alert alert-danger" name="name"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label class="form-label">Фамилия*</label>
            <Field :disabled="disabled" type="text" v-model="student.surname" class="form-control w-100 rounded-0"
                name="surname" />
            <ErrorMessage class="alert alert-danger" name="surname"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label class="form-label">Отчество</label>
            <Field :disabled="disabled" type="text" v-model="student.patronymic" class="form-control w-100 rounded-0"
                name="patronymic" />
        </div>
        <div class="input-group d-flex flex-column">
            <label class="form-label">Почта</label>
            <Field :disabled="disabled" type="email" v-model="student.email" class="form-control w-100 rounded-0"
                name="email" />
            <ErrorMessage class="alert alert-danger" name="email"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label class="form-label">Телефон</label>
            <Field :disabled="disabled" type="text" v-model="student.phone" class="form-control w-100 rounded-0"
                name="phone" />
            <ErrorMessage class="alert alert-danger" name="phone"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label class="form-label">День рождения</label>
            <Field :disabled="disabled" type="date" v-model="student.birthday" class="form-control w-100 rounded-0"
                name="birthday" />
            <ErrorMessage class="alert alert-danger" name="birthday"></ErrorMessage>
        </div>
        <button v-if="!disabled" type="submit" class="btn btn-success" name="submitButton">Отправить</button>
        <span v-if="disabled" class="btn btn-primary" @click="allowEdit" name="allowEditButton">Редактировать</span>
    </Form>
    <Form @invalid-submit="onInvalidSubmit" @submit="onDocSubmit" name="studentForm" :validation-schema="schema"
        style="border: 1px solid darkgrey;" class="col d-flex flex-column justify-content-xl-center p-3 gap-3">
        <div class="d-flex flex-column">
            <span v-for="doc in documents" :key="doc.name" :class="{ 'success': doc.accepted, 'fail': !doc.accepted }"
             class="input-group-text justify-content-between">
                {{ doc.name }}
                <div>
                    <button class="btn btn-outline-light me-1">
                        <font-awesome-icon icon="eye" />
                    </button>
                    <button class="btn btn-outline-light">
                        <font-awesome-icon icon="search" />
                    </button>
                </div>


            </span>
        </div>

        <button type="submit" class="btn btn-success" name="sendDocs">Отправить</button>
        <div class="dropdown"><button class="btn btn-primary dropdown-toggle w-100" aria-expanded="false"
                data-bs-toggle="dropdown" type="button">Добавить</button>
            <div class="dropdown-menu">
                <a class="dropdown-item" @click="addDocument" v-for="docType in docTypes" :key="docType.name" href="#">{{ docType.name }}</a>
            </div>
        </div>

    </Form>
</template>

<script>
import axios from 'axios';
import { Field, Form, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

export default {
    name: "SingleStudentComponent",
    components: {
        Field,
        Form,
        ErrorMessage
    },
    data() {
        let student = {};
        let disabled = true;
        let docTypes = [{ name: 'Паспорт' },
            { name: 'ИНН' },
            { name: 'Загранпаспорт' },
            { name: 'Водительское удостоверение' }];        
        console.log(docTypes);
        if (this.$route.params.id) {            
            let params = {
                id: this.$route.params.id
            };
            axios.get("http://192.168.1.3:3000/students",
                { params }).then(response => { this.student = response.data[0] });
        }
        else
        {
            disabled = false;
        }
        return {
            disabled,
            docTypes,
            student,
            documents: []
        };
    },
    computed: {
        schema() {
            var requiredErrorMsg = "Поле должно быть заполнено";

            const phoneRegExp = /^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s\]?[0-9]{4,6}$/;

            return yup.object({
                email: yup.string().required(requiredErrorMsg).email("Неправильный почтовый адрес"),
                name: yup.string().required(requiredErrorMsg),
                surname: yup.string().required(requiredErrorMsg),
                phone: yup.string().required(requiredErrorMsg).matches(phoneRegExp, "Неправильный номер телефона"),
                birthday: yup.date().required(requiredErrorMsg)
            })
        }
    },
    methods: {
        addDocument(value){
            var docType = value.target.innerText;
            var newDoc = {
                accepted: false,
                name: docType
            }
            this.docTypes.splice(this.docTypes.findIndex(item => item.name === docType), 1);
            this.documents.push(newDoc);
        },
        isDocumentExist(value){
            console.log(value);
        },
        allowEdit() {
            this.disabled = false;
        },
        onInvalidSubmit({ values, errors, results }) {
            console.log(values); // current form values
            console.log(errors); // a map of field names and their first error message
            console.log(results); // a detailed map of field names and their validation results
        },
        onStudentSubmit() {
            var tempStudent = this.student;
            if (tempStudent.id) {
                axios.put("http://192.168.1.3:3000/students/" + tempStudent.id,
                    {
                        name: tempStudent.name,
                        surname: tempStudent.surname,
                        patronymic: tempStudent.patronymic,
                        birthday: tempStudent.birthday,
                        email: tempStudent.email,
                        phone: tempStudent.phone,
                        status: tempStudent.status,
                        countryname: tempStudent.countryname
                    })
                    .then(alert("Запрос отправлен"))
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
                this.$router.go();
            }
            else {
                axios.post("http://192.168.1.3:3000/students",
                    {
                        name: tempStudent.name,
                        surname: tempStudent.surname,
                        patronymic: tempStudent.patronymic,
                        birthday: tempStudent.birthday,
                        email: tempStudent.email,
                        phone: tempStudent.phone,
                        status: tempStudent.status,
                        countryname: tempStudent.countryname,
                    })
                    .then(alert("Запрос отправлен"))
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
                this.$router.go();
            }

        },
        onDocSubmit() {

        }
    }

}
</script>

<style>
.success {
    background-color: #198754 !important;
    color: white !important;
}

.fail {
    background-color: #dc3545 !important;
    color: white !important;
}
</style>