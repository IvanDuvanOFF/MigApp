<template>
    <div class="col d-flex flex-column col-3" style="border: 1px solid darkgrey;">
        <h4 class="font-weight-bold mt-5">{{ $t("list.filter") }}</h4>
        <div class="d-flex flex-column mt-5">
            <div class="input-group d-flex flex-column align-items-center align-items-center">
                <label for="filter-age" class="form-label">{{ $t("list.age") }}</label>
                <input id="filter-age" v-model="age" type="number" class="form-control w-75 rounded-0" />

                <div class="input-group d-flex d-flex flex-column align-items-center">
                    <label for="filter-country" class="form-label">{{ $t("list.country") }}</label>
                    <select id="filter-country" v-model="country" class="form-select w-75 rounded-0">
                        <optgroup label="">
                            <option value="" selected>-</option>
                            <option value="country1" selected>Country 1</option>
                            <option value="country2" selected>Country 2</option>
                        </optgroup>
                    </select>
                </div>

                <div class="input-group d-flex flex-column align-items-center">
                    <label for="filter-status" class="form-label">
                        {{ $t("list.status") }}
                    </label>
                    <select id="filter-status" v-model="studentStatus" class="form-select w-75 rounded-0">
                        <optgroup label="">
                            <option value="">-</option>
                            <option value="status1">Status 1</option>
                            <option value="status2">Status 2</option>
                        </optgroup>
                    </select>
                </div>
            </div>
        </div>
    </div>

    <div class="col d-flex flex-column p-0">
        <div>
            <div class="input-group">
                <input id="fio" type="text" class="form-control rounded-0" />
                <button class="btn btn-dark rounded-0" @click="makeSearch">
                    <font-awesome-icon icon="search" />
                </button>
                <a class="btn btn-primary rounded-0" 
                    v-bind:href="$sanitize('/table/' + this.tableName + '/create')" type="button">
                    <font-awesome-icon icon="user-plus" />
                </a>
            </div>

            <div>
                <a class="card-a rounded-0 text-decoration-none card" v-for="student in students" :key="student.id"
                    v-bind:href="$sanitize('/students/' + student.id)">
                    <div class=" card-body p-2 align-self-start">
                        <h4 class="card-title" align-self-start>{{ student.surname }} {{ student.name }} {{
                            student.patronymic }}</h4>
                    </div>
                </a>
            </div>
        </div>
    </div>
</template>

<script>
import StudentService from '@/services/StudentService.js';

export default {
    name: "TableComponent",
    data() {
        let tableName = this.$route.params.tableName;

        let students = [];
        console.log();
        StudentService.getStudents().then(response => { 
            console.log(response.data); 
            this.students = response.data
        });
        return {
            tableName,
            age: 0,
            country: '',
            studentStatus: '',
            students
        };
    },

    methods: {
        makeDynamicLink(id){
            return '/students/' + id;
        },
        makeSearch() {
            let params = {};

            if (this.age != 0) {
                params['age'] = this.age;
            }

            if (this.country) {
                params['countryname'] = this.country;
            }

            if (this.studentStatus) {
                params['status'] = this.studentStatus;
            }

            console.log(params);
            StudentService.getStudents(params)
                .then(response => {
                    console.log(response.data); 
                    this.students = response.data
                });
        }
    }
}


</script>

<style>
.card-a:hover {
    background-color: gray !important;
    transition-duration: 500ms
}
</style>