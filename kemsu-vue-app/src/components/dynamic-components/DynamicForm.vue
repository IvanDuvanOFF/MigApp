<template>
    <Form @invalid-submit="onInvalidSubmit" @submit="onStudentSubmit" name="studentForm" :id="'form-' + modelValue.id"
        :validation-schema="schema">
    </Form>
</template>

<script>
import { Form } from 'vee-validate';
import * as yup from 'yup';
import StudentService from '@/services/StudentService';

export default {
    name: "SingleObjectComponent",
    props: ['modelValue', 'attributes'],        
    components: {
        Form
    },
    computed: {

    },
    methods: {
        makeSchema() {
            let requiredErrorMsg = this.$t("errors.required");

            const phoneRegExp = /^[+]?[(]?\d{3}?[-\s.]?\d{3}[-\s\]?[0-9]{4,6}$/;

            const yup_schema = {};
            
            this.attributes.forEach(attr => {
                switch (attr.attribute_name) {
                    case ("string"):
                        yup_schema[attr.attribute_name] = yup.string().required(requiredErrorMsg);
                        break;
                    case ("email"):
                        yup_schema[attr.attribute_name] = yup.string().required(requiredErrorMsg).email(this.$t("errors.email"));
                        break;
                    case ("number"):
                        yup_schema[attr.attribute_name] = yup.number().required(requiredErrorMsg);
                        break;
                    case ("phone"):
                        yup_schema[attr.attribute_name] = yup.string().required(requiredErrorMsg).matches(phoneRegExp, this.$t("errors.phone"));
                        break;
                    case ("date"):
                        yup_schema[attr.attribute_name] = yup.date().required(requiredErrorMsg);
                        break;
                }
            })

            console.log(yup_schema);
            this.schema = yup.object(yup_schema);
        },
        allowEdit() {
            this.disabled = false;
        },
        onInvalidSubmit({ values, errors, results }) {
            console.log(values);
            console.log(errors);
            console.log(results);
        },

        updateExampleObject() {
            let inputForm = document.getElementById('form-' + this.modelValue.id);

            for (let element of inputForm.elements) {
                if (element.value) {
                    // this.modelValue[element.id] = element.value;
                }
            }
        },

        onStudentSubmit() {
            console.log(this.modelValue);            
            this.updateExampleObject();
            console.log(this.modelValue);

            if (this.modelValue.id) {
                StudentService.updateStudent(this.modelValue.id, this.modelValue)
                    .then(alert("Запрос отправлен"))
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
                this.$router.go();
            }
            else {
                StudentService.createStudent(this.modelValue)
                    .then(response => {
                        alert("Запрос отправлен");
                        window.location.replace("/table/" + this.tableName + "/" + response.data.id);
                    })
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
            }
        },
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