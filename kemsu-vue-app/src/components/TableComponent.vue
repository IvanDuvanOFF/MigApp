<template>
    <div class="col table-responsive" style="margin-top: 100px;">
        <div class="input-group">
            <span class="input-group-text">
                <font-awesome-icon icon="search" />
            </span>
            <input class="form-control" type="text" data-filter-for="table" autocomplete="off" @change="initFilter"
                :placeholder="$t('list.search')" />
            <button class="btn btn-info sub-item dropdown-toggle" data-bs-toggle="dropdown" aria-bs-haspopup="true"
                aria-bs-expanded="false" id="dropdownAttrs">
                &#x2699;
            </button>
            <a class="btn btn-success" @click="exportTable" id="exportButton">
                <font-awesome-icon icon="file-excel"></font-awesome-icon>
            </a>
            <div class="dropdown-menu" aria-bs-labelledby="dropdownAttrs">
                <div class="form-check" v-for="attr in attributes" :key="attr">
                    <input :name="attr.attribute_name" type="checkbox" class="form-check-input" v-model="attr.is_shown"
                        @change="updateFiltererdAttributes" />
                    <label :for="attr.attribute_name" class="form-check-label">
                        {{ attr.attribute_name }}
                    </label>
                </div>
            </div>
            <button class="btn btn-danger" type="button" data-bs-toggle="collapse" data-bs-target="#filterForm"
                aria-expanded="false" aria-controls="filterForm">
                <font-awesome-icon icon="filter" />
            </button>
            <a class="btn btn-primary" v-bind:href="$sanitize('/table/' + this.tableName + '/create')" type="button">
                <font-awesome-icon icon="user-plus" />
            </a>
        </div>

        <form @submit.prevent="applyFilter" class="card card-body collapse text-bg-dark" id="filterForm">
            <DynamicFilter :attribute="attr" v-for="attr in filtered_attributes" :key="attr">

            </DynamicFilter>
            <div class="input-group mt-5 w-75 m-auto d-inline">
                <button type="submit" class="btn btn-success w-50">
                    <font-awesome-icon icon="save" />
                </button>
                <button type="button" @click="clearFilterForm" class="btn btn-danger w-50">
                    <font-awesome-icon icon="trash-can" />
                </button>
            </div>

            
        </form>

        <div class="table-responsive card mt-5">
            <table class="table table-bordered table-striped table-hover" id="table" data-sorter>
                <thead>
                    <tr>
                        <th>

                        </th>
                        <th v-for="attr in filtered_attributes" :key="attr" @click="initSorter">
                            {{ attr.attribute_name }}
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="d-talbe" v-for="example in filtered_examples" :key="example">
                        <td>
                            <div>
                                <button class="btn btn-success" :name="example.id"
                                    v-if="examplesInEditMode.includes(example.id)" :form="'form-' + example.id">
                                    <font-awesome-icon icon="save" />
                                </button>
                                <button class="btn btn-primary" :name="example.id" v-else hidden
                                    @click="activeEditMode(example.id)">
                                    <font-awesome-icon icon="pen" />
                                </button>
                                <a class="btn btn-info"
                                    v-bind:href="$sanitize('/table/' + this.tableName + '/' + example.id)"
                                    :name="example.id">
                                    <font-awesome-icon icon="pen" />
                                </a>
                            </div>
                        </td>

                        <td v-for="attr in filtered_attributes" :key="attr">
                            <DynamicInput :attribute="attr" :disabled="false" :modelValue="example"
                                :key="attr.attribute_name" v-if="examplesInEditMode.includes(example.id)" />
                            <span v-else>
                                {{ example[attr.attribute_name] }}
                            </span>
                        </td>
                    </tr>

                </tbody>
            </table>

        </div>
    </div>

</template>

<script>
import StudentService from '@/services/StudentService.js';
import AttributeService from '@/services/AttributeService.js';
import TableService from '@/services/TableService';
import DynamicFilter from '@/components/dynamic-components/DynamicFilter.vue';
import DynamicInput from './dynamic-components/DynamicInput.vue';

export default {
    name: "TableComponent",
    components: {
        DynamicFilter,
        DynamicInput
    },
    data() {
        let tableName = this.$route.params.tableName;
        let search_params = {};
        let attributes = [];
        let examples = [];
        let filtered_examples = [];
        let filtered_attributes = [];
        let thisTable = {};

        TableService.getTableByName(tableName).then(response => {
            this.thisTable = response.data;

            AttributeService.getAttributes(this.thisTable.id).then(response => {
                console.log(response.data);
                this.attributes = response.data;
                this.filtered_attributes = this.attributes.filter(function (el) {
                    return el.is_shown;
                });
            });
        });

        StudentService.getStudents().then(response => {
            console.log(response.data);
            this.examples = response.data;
            this.filtered_examples = JSON.parse(JSON.stringify(this.examples));
        });

        return {
            tableName,
            thisTable,
            examples,
            filtered_examples,
            attributes,
            filtered_attributes,
            search_params,
            examplesInEditMode: []
        };
    },
    methods: {
        exportTable() {
            let elem = document.getElementById('exportButton');
            let table = document.getElementById("table");
            let html = table.outerHTML;
            let url = 'data:application/vnd.ms-excel,' + escape(html); // Set your html table into url 
            elem.setAttribute("href", url);
            elem.setAttribute("download", "export.xls"); // Choose the file name
            return false;
        },
        initSorter(event) {
            let table = document.getElementById("table");
            // get cell values, parsing dates in d/m/yyyy format
            let rxDate = /^(\d{1,2})[-/](\d{1,2})[-/](\d{4})$/;
            let getCellValue = (tr, idx) => {
                let value = tr.children[idx].innerText;
                let match = rxDate && value.match(rxDate);
                if (match) {
                    //try {
                    // parse dates (correct ones only)
                    value = new Date(match[3] + "-" + match[2] + "-" + match[1]);
                    //} catch {}
                }
                return value;
            }

            // compare two cells in the same column
            let comparer = (idx, asc) => (a, b) =>
                ((v1, v2) =>
                    v1 !== "" && v2 !== "" && !isNaN(v1) && !isNaN(v2)
                        ? v1 - v2
                        : v1.toString().localeCompare(v2))(
                            getCellValue(asc ? a : b, idx),
                            getCellValue(asc ? b : a, idx)
                        );

            let headers = Array.from(table.tHead.children[0].querySelectorAll("th"));
            let tbody = table.querySelector("tbody");
            let originalRows = Array.from(tbody.querySelectorAll("tr"));

            console.log(headers);

            let hdr = event.target;
            let index = headers.indexOf(hdr);

            // keep order per column
            let asc = hdr.classList.contains("sort-asc");
            let desc = hdr.classList.contains("sort-desc");

            // remove old sort symbols
            headers.forEach(h => {
                h.classList.toggle("sort-asc", false);
                h.classList.toggle("sort-desc", false);
            });

            // remove sort from this column if already in descending order (three-way cycle)
            if (desc) {
                originalRows.forEach(row => tbody.appendChild(row));
                return;
            }

            // sort the rows
            let rows = Array.from(tbody.querySelectorAll("tr"));
            rows.sort(comparer(index, asc = !asc));
            rows.forEach(row => tbody.appendChild(row));

            // add new sort symbols
            hdr.classList.toggle("sort-asc", asc);
            hdr.classList.toggle("sort-desc", !asc);


        },

        initFilter(event) {
            let table = document.getElementById(event.target.getAttribute("data-filter-for"));
            let rows = table.tBodies[0].rows;

            let search = event.target.value;

            // get terms to filter on
            let terms = search
                .split(/\s+/)
                .filter((x) => x.length > 0) // skip empty terms
                .map((x) => x.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")); // escape regex

            // build pattern/regex
            let pattern = "(" + terms.join("|") + ")";
            let regEx = new RegExp(pattern, "gi");

            // apply to all rows
            for (const element of rows) {
                let row = element;
                let match = row.textContent.match(regEx);
                row.classList.toggle(
                    "hide-row",
                    match == null || match.length < terms.length
                );
            }
        },

        updateFiltererdAttributes() {
            this.filtered_attributes = this.attributes.filter(function (el) {
                return el.is_shown;
            });
        },
        activeEditMode(id) {
            this.examplesInEditMode.push(id);
            console.log(this.examplesInEditMode);
        },
        disactiveEditMode(id) {
            const index = this.examplesInEditMode.indexOf(id);
            this.examplesInEditMode.splice(index, 1);
            console.log(this.examplesInEditMode);
        },
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
        filterByDiapason(attr_name) {
            let maxValue = document.getElementById(attr_name + "_max").value;
            if (maxValue) {
                this.filtered_examples = this.filtered_examples.filter(
                    (example) => example[attr_name] <= maxValue
                );
            }

            let minValue = document.getElementById(attr_name + "_min").value;
            if (minValue) {
                this.filtered_examples = this.filtered_examples.filter(
                    (example) => example[attr_name] >= minValue
                );
            }
        },

        applyFilter(submitEvent) {
            this.filtered_examples = JSON.parse(JSON.stringify(this.examples));

            for (let element of submitEvent.target.elements) {
                if (element.value) {
                    let attr_name = element.name;
                    let attr = this.attributes.find(x => x.attribute_name == attr_name);

                    if (attr.attribute_type == "string" || attr.attribute_type == "email"
                        || attr.attribute_type == "phone") {
                        this.filtered_examples = this.filtered_examples.filter(
                            (example) => example[attr_name].indexOf(element.value) > -1
                        );
                    }
                    else if (attr.attribute_type == "number" || attr.attribute_type == "date") {
                        this.filterByDiapason(attr_name);
                    }
                    else {
                        this.filtered_examples = this.filtered_examples.filter(
                            (example) => example[attr_name] == element.value
                        );
                    }

                    this.search_params[element.id] = element.value;
                }
            }
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
                element.value = "";
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

/* data-filter */
div.input-group.data-filter {
    display: inline-flex;
    width: 16em;
}

/* data-colpicker */
div[data-colpicker-for] {
    display: inline-block;
    vertical-align: top;
    width: 12em;
}

ul.dropdown-menu li {
    margin: 0.15em 1em;
}

ul.dropdown-menu li label {
    width: 100%;
}

/* data-sorter */
table[data-sorter] thead th {
    cursor: pointer;
}

table[data-sorter] thead th.sort-asc:after {
    content: '\25B2';
    margin-left: 0.5em;
}

table[data-sorter] thead th.sort-desc:after {
    content: '\25BC';
    margin-left: 0.5em;
}

/* hide table rows/columns */
th.hide-cell,
td.hide-cell,
tr.hide-row {
    display: none;
}
</style>