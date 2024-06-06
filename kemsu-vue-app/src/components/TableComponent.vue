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
                    <tr v-for="example in filtered_examples" :key="example">
                        <td>
                            <div v-if="!examplesInEditMode.includes(example.id)">
                                <button class="btn btn-primary" @click="activeEditMode(example.id)" hidden>
                                    <font-awesome-icon icon="pen" />
                                </button>

                                <a class="btn btn-info"
                                    v-bind:href="$sanitize('/table/' + this.tableName + '/' + example.id)"
                                    :name="example.id">
                                    <font-awesome-icon icon="search" />
                                </a>

                                <button class="btn btn-danger" @click="removeExample(example.id)">
                                    <font-awesome-icon icon="trash-can" />
                                </button>
                            </div>
                            <div v-else>
                                <button class="btn btn-primary" @click="disactiveEditMode(example.id)">
                                    <font-awesome-icon icon="pen" />
                                </button>
                                <button class="btn btn-success" @click="saveExample(example.id)">
                                    <font-awesome-icon icon="save" />
                                </button>
                            </div>
                        </td>

                        <td v-for="attr in filtered_attributes" :key="attr">
                            <DynamicInput :attribute="attr" :disabled="false" :modelValue="example"
                                v-if="examplesInEditMode.includes(example.id)" />
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
            this.thisTable = response.data[0];
            console.log(response.data[0]);

            AttributeService.getAttributes(this.thisTable.id).then(response => {
                console.log(response.data);
                this.attributes = response.data;
                this.filtered_attributes = this.attributes.filter(function (el) {
                    return el.is_shown;
                });
            });
        });

        TableService.getTableData(tableName).then(response => {
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
        // экспортировать таблицу в Excel
        exportTable() {
            let elem = document.getElementById('exportButton');
            let table = document.getElementById("table");
            let html = table.outerHTML;
            let url = 'data:application/vnd.ms-excel,' + escape(html); // Поставить HTML код в ссылку
            elem.setAttribute("href", url);
            elem.setAttribute("download", this.tableName + ".xls"); // Выбрать имя файла
            return false;
        },

        // иницализировать сортировщик
        initSorter(event) {
            let table = document.getElementById("table");
            // Получить значения клеток, парсить даты в d/m/yyyy формате
            let rxDate = /^(\d{1,2})[-/](\d{1,2})[-/](\d{4})$/;
            let getCellValue = (tr, idx) => {
                let value = tr.children[idx].innerText;
                let match = rxDate && value.match(rxDate);
                if (match) {
                    value = new Date(match[3] + "-" + match[2] + "-" + match[1]);
                }
                return value;
            }

            // сравнить две клетки в одной колонке
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

            // сортировка колонки
            let asc = hdr.classList.contains("sort-asc");
            let desc = hdr.classList.contains("sort-desc");

            // удалить старые значки сортировки
            headers.forEach(h => {
                h.classList.toggle("sort-asc", false);
                h.classList.toggle("sort-desc", false);
            });

            // удалить сортировку из колонки если она в нисходящей сортировке (цикл в три раза)            
            if (desc) {
                originalRows.forEach(row => tbody.appendChild(row));
                return;
            }

            // сортировать ряды
            let rows = Array.from(tbody.querySelectorAll("tr"));
            rows.sort(comparer(index, asc = !asc));
            rows.forEach(row => tbody.appendChild(row));

            // добавить значки новой сортировки
            hdr.classList.toggle("sort-asc", asc);
            hdr.classList.toggle("sort-desc", !asc);
        },

        // инициализировать поиск
        initFilter(event) {
            let table = document.getElementById(event.target.getAttribute("data-filter-for"));
            let rows = table.tBodies[0].rows;

            let search = event.target.value;

            // получить условия для фильтрации
            let terms = search
                .split(/\s+/)
                .filter((x) => x.length > 0) // пропустить пустые условия
                .map((x) => x.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")); // применить regex

            // построить regex
            let pattern = "(" + terms.join("|") + ")";
            let regEx = new RegExp(pattern, "gi");

            // применить ко всем рядам
            for (const element of rows) {
                let row = element;
                let match = row.textContent.match(regEx);
                row.classList.toggle(
                    "hide-row",
                    match == null || match.length < terms.length
                );
            }
        },

        // удалить запись из таблицы
        removeExample(exampleId) {
            if (confirm(this.$t("confirm.remove"))) {
                TableService.removeTableData(this.tableName, exampleId).then(() => {
                    this.update();
                });
            }
        },

        // обновить отфильтрованные атрибуты
        updateFiltererdAttributes() {
            this.filtered_attributes = this.attributes.filter(function (el) {
                return el.is_shown;
            });
        },

        // активировать режим редактирования для таблицы
        activeEditMode(id) {
            this.examplesInEditMode = [];
            this.examplesInEditMode.push(id);
        },

        // активировать режим редактирования для таблицы
        disactiveEditMode(id) {
            const index = this.examplesInEditMode.indexOf(id);
            this.examplesInEditMode.splice(index, 1);
        },        
        
        // отфильтровать по диапазону
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

        // сохранить запись
        saveExample() {
            let example = {};

            this.filtered_attributes.forEach((attr) => {
                let element = document.getElementById(attr.attribute_name);
                console.log(document.getElementById(attr.attribute_name).value);
                example[attr.attribute_name] = element.value;
            });

            console.log(example);
        },

        // применить фильтр
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

        // очистить фильтр формы
        clearFilterForm(submitEvent) {
            let form = document.getElementById("filterForm");
            for (let element of form.elements) {
                element.value = "";
            }
            this.search_params = {};
            this.applyFilter(submitEvent);
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