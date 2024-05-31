<template>
    <div class=" d-flex flex-column" v-if="'string' == attribute.attribute_type">
        <label :for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <Field :disabled="disabled" type="text" class="form-control w-100"
            :value="modelValue[attribute.attribute_name]"
            :name="attribute.attribute_name" :id="attribute.attribute_name"        
            v-on:keypress="isLetter($event)" />
        <ErrorMessage class="alert alert-danger" :name="attribute.attribute_name"></ErrorMessage>
    </div>

    <div class=" d-flex flex-column" v-else-if="'password' == attribute.attribute_type">
        <label :for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <Field :disabled="disabled" type="text" class="form-control w-100"
            :value="modelValue[attribute.attribute_name]"
            :name="attribute.attribute_name" :id="attribute.attribute_name"/>
        <ErrorMessage class="alert alert-danger" :name="attribute.attribute_name"></ErrorMessage>
    </div>

    <div class=" d-flex flex-column" v-else-if="'number' == attribute.attribute_type">
        <label :for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <Field :disabled="disabled" type="number" class="form-control w-100 "
            :value="modelValue[attribute.attribute_name]" 
            :name="attribute.attribute_name" :id="attribute.attribute_name"/>
        <ErrorMessage class="alert alert-danger" :name="attribute.attribute_name"></ErrorMessage>
    </div>

    <div class=" d-flex flex-column" v-else-if="'email' == attribute.attribute_type">
        <label :for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <div class="input-group">
            <Field :disabled="disabled" type="email" class="form-control " 
                :value="modelValue[attribute.attribute_name]"
                :name="attribute.attribute_name" :id="attribute.attribute_name" />

            <button type="button" class="btn btn-info" :disabled="!disabled" data-bs-toggle="modal"
                data-bs-target="#mailModal">
                <font-awesome-icon icon="mail-bulk" />
            </button>
        </div>
        <ErrorMessage class="alert alert-danger" :name="attribute.attribute_name"></ErrorMessage>
        <MailModal :email="modelValue[attribute.attribute_name]" />
    </div>

    <div class="form-check" v-else-if="'bool' == attribute.attribute_type">
        <Field :disabled="disabled" type="radio" class="form-check-input" 
            :value="modelValue[attribute.attribute_name]"
            :name="attribute.attribute_name" :id="attribute.attribute_name" />
        <label :for="attribute.attribute_name" class="form-check-label">
            {{ attribute.attribute_name }}
        </label>
    </div>

    <div class=" d-flex flex-column" v-else-if="'phone' == attribute.attribute_type">
        <label :for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <Field :disabled="disabled" type="text" class="form-control w-100 " 
            :value="modelValue[attribute.attribute_name]"
            :name="attribute.attribute_name" :id="attribute.attribute_name" />
        <ErrorMessage class="alert alert-danger" :name="attribute.attribute_name"></ErrorMessage>
    </div>

    <div class=" d-flex flex-column" v-else-if="'date' == attribute.attribute_type">
        <label :for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <Field :disabled="disabled" type="date" class="form-control w-100 " 
            :value="modelValue[attribute.attribute_name]"
            :name="attribute.attribute_name" :id="attribute.attribute_name" />
        <ErrorMessage class="alert alert-danger" :name="attribute.attribute_name"></ErrorMessage>        
    </div>

</template>

<script>
import MailModal from '@/components/dynamic-components/MailModal.vue';
import { Field, ErrorMessage } from 'vee-validate';

export default {
    props: ["attribute", "disabled", "modelValue"],
    components: {
        Field,
        ErrorMessage,
        MailModal
    },
    methods: {
        isLetter(e) {
            let char = String.fromCharCode(e.keyCode);
            if (/^[A-Za-zА-Яа-я]+$/.test(char)) return true;
            else e.preventDefault();
        }        
    }
}
</script>