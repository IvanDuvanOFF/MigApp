<template>
    <Form @invalid-submit="onInvalidSubmit" @submit="onStudentSubmit" name="studentForm" id="inputForm"
        :validation-schema="schema" style="border: 1px solid darkgrey;"
        class="col d-flex flex-column justify-content-xl-center p-3 gap-3">

        <DynamicInput :attribute="attr" :disabled="disabled" v-model="example" v-for="attr in attributes"
            :key="attr.attribute_name" />

        <button v-if="!disabled" type="submit" class="btn btn-success" name="submitButton">{{ $t("single.send")
            }}</button>
        <span v-if="disabled" class="btn btn-primary" @click="allowEdit" name="allowEditButton">{{ $t("single.edit")
            }}</span>
    </Form>
</template>

<script>
import { Form } from 'vee-validate';
import * as yup from 'yup';
import AttributeService from '@/services/AttributeService.js';
import StudentService from '@/services/StudentService.js';
import TableService from '@/services/TableService.js';
import DynamicInput from '@/components/dynamic-components/DynamicInput.vue';

export default {
    name: "SingleObjectComponent",
    components: {
        Form,
        DynamicInput
    },
    data() {
        let tableName = this.$route.params.tableName;
        let example = {};
        let disabled = true;
        let thisTable = {};
        let attributes = {};

        TableService.getTableByName(tableName).then(response => {
            this.thisTable = response.data;

            AttributeService.getAttributes(this.thisTable.id).then(response => {
                console.log(response.data);
                this.attributes = response.data;
                this.makeSchema();
            });
        })

        if (this.$route.params.id) {
            let id = this.$route.params.id
            StudentService.getStudent(id).then(response => {
                this.example = response.data
                console.log(this.example);
            });

        }
        else {
            disabled = false;
        }

        return {
            tableName,
            disabled,
            example,
            thisTable,
            attributes,
            schema: yup.object()
        };
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
            let inputForm = document.getElementById("inputForm");

            for (let element of inputForm.elements) {
                if (element.value) {
                    this.example[element.id] = element.value;
                }
            }
        },

        onStudentSubmit() {
            console.log(this.example);
            this.updateExampleObject();
            console.log(this.example);

            if (this.example.id) {
                StudentService.updateStudent(this.example.id, this.example)
                    .then(alert("Запрос отправлен"))
                    .catch(err => {
                        alert("Ошибка на стороне сервера " + err)
                    });
                this.$router.go();
            }
            else {
                StudentService.createStudent(this.example)
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