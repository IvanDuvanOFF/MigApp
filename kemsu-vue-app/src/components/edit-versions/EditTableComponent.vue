<template>
    <div class="col" style="margin-top: 10%;">
        <table class="table table-dark">
            <thead>
                <tr>
                    <th scope="col">{{ $t("edit-table.attr_name") }}</th>
                    <th scope="col">{{ $t("edit-table.is_shown") }}</th>
                    <th scope="col">{{ $t("edit-table.at_filter") }}</th>
                    <th scope="col">{{ $t("edit-table.type") }}</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="attr in attributes" :key="attr.attribute_name">
                    <td>
                        {{ attr.attribute_name }}
                    </td>
                    <td>
                        <input type="checkbox" class="form-check-input" v-model="attr.is_shown" />
                    </td>
                    <td class="column">
                        <input type="checkbox" class="form-check-input" v-model="attr.at_filter" />
                    </td>
                    <td>
                        <select class="form-select" v-model="attr.attribute_type" @change="switchLanguage">
                            <option v-for="typeName in types" :key="typeName">
                                {{ typeName }}
                            </option>
                        </select>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
console.log(1);
export default {
    name: "EditTableComponent",
    data() {
        let tableName = this.$route.params.tableName;
        let types = ["email", "string", "number"];
        let attr = {
            table_id: 1,
            attribute_name: "Attr",
            is_shown: false,
            at_filter: false,
            attribute_type: "string"
        }
        let attributes = [attr, attr, attr];

        return {
            tableName,
            attributes,
            types
        };
    }
}
</script>