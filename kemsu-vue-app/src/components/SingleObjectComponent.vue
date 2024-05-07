<template>
    <Form @invalid-submit="onInvalidSubmit" @submit="onStudentSubmit" name="studentForm" :validation-schema="schema"
        style="border: 1px solid darkgrey;" class="col d-flex flex-column justify-content-xl-center p-3 gap-3">
        <div class="input-group d-flex flex-column">
            <label for="name" class="form-label">{{ $t("single.name") }}</label>
            <Field :disabled="disabled" type="text" v-model="student.name" class="form-control w-100 rounded-0"
                name="name" v-on:keypress="isLetter($event)" />
            <ErrorMessage class="alert alert-danger" name="name"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label for="surname" class="form-label">{{ $t("single.surname") }}</label>
            <Field :disabled="disabled" type="text" v-model="student.surname" class="form-control w-100 rounded-0"
                name="surname" v-on:keypress="isLetter($event)" />
            <ErrorMessage class="alert alert-danger" name="surname"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label for="patronymic" class="form-label">{{ $t("single.patronymic") }}</label>
            <Field :disabled="disabled" type="text" v-model="student.patronymic" class="form-control w-100 rounded-0"
                name="patronymic" v-on:keypress="isLetter($event)" />
        </div>
        <div class="input-group d-flex flex-column">
            <label for="email" class="form-label">{{ $t("single.email") }}</label>
            <div class="input-group">
                <Field :disabled="disabled" type="email" v-model="student.email" class="form-control rounded-0"
                    name="email" />
                
                <button type="button" class="btn btn-info" :disabled="!disabled"
                    data-bs-toggle="modal" data-bs-target="#mailModal" :data-bs-whatever="student.email">
                    <font-awesome-icon icon="mail-bulk" />
                </button>
            </div>
            <ErrorMessage class="alert alert-danger" name="email"></ErrorMessage>
        </div>

        <div class="d-flex justify-content-around">
            <div class="form-check">                
                <Field :disabled="disabled" type="radio" v-model="student.sex" class="form-check-input"
                    name="sex" id="male" value="true" />
                <label for="male" class="form-check-label">{{ $t("single.male") }}</label>
            </div>
            <div class="form-check">                
                <Field :disabled="disabled" type="radio" v-model="student.sex" class="form-check-input"
                    name="sex" id="female" value="false" />
                <label for="female" class="form-check-label">{{ $t("single.female") }}</label>
            </div>
        </div>

        <div class="input-group d-flex flex-column">
            <label for="phone" class="form-label">{{ $t("single.phone") }}</label>
            <Field :disabled="disabled" type="text" v-model="student.phone" class="form-control w-100 rounded-0"
                name="phone" />
            <ErrorMessage class="alert alert-danger" name="phone"></ErrorMessage>
        </div>
        <div class="input-group d-flex flex-column">
            <label for="birhtday" class="form-label">{{ $t("single.birth") }}</label>
            <Field :disabled="disabled" type="date" v-model="student.birthday" class="form-control w-100 rounded-0"
                name="birthday" />
            <ErrorMessage class="alert alert-danger" name="birthday"></ErrorMessage>
        </div>
        <button v-if="!disabled" type="submit" class="btn btn-success" name="submitButton">{{ $t("single.send") }}</button>
        <span v-if="disabled" class="btn btn-primary" @click="allowEdit" name="allowEditButton">{{ $t("single.edit") }}</span>
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

        <button type="submit" class="btn btn-success" name="sendDocs">{{ $t("single.send-doc") }}</button>
        <div class="dropdown"><button class="btn btn-primary dropdown-toggle w-100" aria-expanded="false"
                data-bs-toggle="dropdown" type="button">{{ $t("single.add-doc") }}</button>
            <div class="dropdown-menu">
                <a class="dropdown-item" @click="addDocument" v-for="docType in docTypes" :key="docType.name"
                    href="#">{{ docType.name }}</a>
            </div>
        </div>
    </Form>

    <MailModal v-model:email="student.email" />    
</template>

<script>
import { Field, Form, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';
import StudentService from '@/services/StudentService.js';
import MailModal from '@/components/MailModal.vue';

export default {
    name: "SingleObjectComponent",
    components: {
        Field,
        Form,
        ErrorMessage,
        MailModal
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
            let id = this.$route.params.id
            StudentService.getStudent(id).then(response => { this.student = response.data[0] });
        }        
        else {
            disabled = false;
            student.sex = false;
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
            let requiredErrorMsg = this.$t("errors.required");

            const phoneRegExp = /^[+]?[(]?\d{3}?[-\s.]?\d{3}[-\s\]?[0-9]{4,6}$/;

            return yup.object({
                email: yup.string().required(requiredErrorMsg).email(this.$t("errors.email")),
                name: yup.string().required(requiredErrorMsg),
                surname: yup.string().required(requiredErrorMsg),
                phone: yup.string().required(requiredErrorMsg).matches(phoneRegExp, this.$t("errors.phone")),
                birthday: yup.date().required(requiredErrorMsg)
            })
        }
    },
    methods: {
        addDocument(value) {
            let docType = value.target.innerText;
            let newDoc = {
                accepted: false,
                name: docType
            }
            this.docTypes.splice(this.docTypes.findIndex(item => item.name === docType), 1);
            this.documents.push(newDoc);
        },
        isDocumentExist(value) {
            console.log(value);
        },
        isLetter(e) {
            let char = String.fromCharCode(e.keyCode);
            if (/^[A-Za-z]+$/.test(char)) return true;
            else e.preventDefault();
        },
        allowEdit() {
            this.disabled = false;
        },
        onInvalidSubmit({ values, errors, results }) {
            console.log(values);
            console.log(errors);
            console.log(results);
        },
        onStudentSubmit() {
            let tempStudent = this.student;
            let params = {
                name: tempStudent.name,
                surname: tempStudent.surname,
                patronymic: tempStudent.patronymic,
                birthday: tempStudent.birthday,
                email: tempStudent.email,
                phone: tempStudent.phone,
                status: tempStudent.status,
                sex: tempStudent.sex,
                countryname: tempStudent.countryname
            };

            if (tempStudent.id) {
                StudentService.updateStudent(tempStudent.id, params)
                    .then(alert("Запрос отправлен"))
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
                this.$router.go();
            }
            else {
                StudentService.createStudent(params)
                    .then(response => {
                        alert("Запрос отправлен");
                        window.location.replace("/students/" + response.data.id);
                    })
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
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