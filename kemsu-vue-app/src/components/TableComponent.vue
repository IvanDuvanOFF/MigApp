<template>
    <div class="col d-flex flex-column col-3" style="border: 1px solid darkgrey;">
        <h4 class="font-weight-bold mt-5">{{ $t("list.filter") }}</h4>
        <form id="filterForm" class="d-flex flex-column mt-5" @submit.prevent="applyFilter">
            <DynamicFilter :attribute="attr" v-for="attr in filtered_attributes" :key="attr.attribute_name" />
            <div class="btn-group w-75 m-auto mt-2">
                <button type="submit" class="btn btn-dark rounded-0">
                    <font-awesome-icon icon="search" />
                </button>
                <button type="button" @click="clearFilterForm" class="btn btn-danger rounded-0">
                    <font-awesome-icon icon="trash-can" />
                </button>
            </div>
        </form>
    </div>

    <div class="col d-flex flex-column p-0">
        <div>
            <div class="input-group">
                <a class="btn btn-primary rounded-0" v-bind:href="$sanitize('/table/' + this.tableName + '/create')"
                    type="button">
                    <font-awesome-icon icon="user-plus" />
                </a>
            </div>

            <div>
                <a class="card-a rounded-0 text-decoration-none card" v-for="example in examples" :key="example.id"
                    v-bind:href="$sanitize('/table/' + this.tableName + '/' + example.id)">
                    <div class=" card-body p-2 align-self-start">
                        <h4 class="card-title" align-self-start>
                            {{ getTextFromShownValues(example) }}
                        </h4>
                    </div>
                </a>
            </div>
        </div>
    </div>
</template>

<script>
import StudentService from '@/services/StudentService.js';
import AttributeService from '@/services/AttributeService.js';
import DynamicFilter from '@/components/dynamic-components/DynamicFilter.vue';
import TableService from '@/services/TableService';

export default {
    name: "TableComponent",
    components: {
        DynamicFilter
    },
    data() {
        let tableName = this.$route.params.tableName;
        let search_params = {};
        let attributes = [];
        let examples = [];
        let filtered_attributes = [];
        let thisTable = {};

        TableService.getTableByName(tableName).then(response => {
            this.thisTable = response.data;

            AttributeService.getAttributes(this.thisTable.id).then(response => {
            console.log(response.data);
            this.attributes = response.data;
            this.filtered_attributes = this.attributes.filter(function (el) {
                return el.at_filter;
            });             
        });
        });

        StudentService.getStudents().then(response => {
            console.log(response.data);
            this.examples = response.data
        });

        

        return {
            tableName,
            thisTable,
            examples,
            attributes,
            filtered_attributes,
            search_params
        };
    },

    methods: {
        getTextFromShownValues(example) {
            let represents = [];

            this.attributes.forEach((attr) => {
                if (attr.is_shown) {
                    represents.push(example[attr.attribute_name]);
                }
            });

            return represents.join(" ");
        },
        makeDynamicLink(id) {
            return '/students/' + id;
        },
        applyFilter(submitEvent) {
            for (let element of submitEvent.target.elements) {
                if (element.value) {
                    this.search_params[element.id] = element.value;
                }
            }
            this.makeSearch();
        },
        makeSearch() {
            console.log(this.search_params);
            StudentService.getStudents(this.search_params)
                .then(response => {
                    console.log(response.data);
                    this.examples = response.data
                });
        },
        clearFilterForm() {
            let form = document.getElementById("filterForm");
            for (let element of form.elements) {
                element.value = null;
            }
            this.search_params = {};
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