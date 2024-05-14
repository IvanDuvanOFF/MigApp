<template>
    <div class="col" style="margin-top: 10%;">
        <table class="table">
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

        <button class="btn btn-success w-75" @click="sendAttributes">
            <font-awesome-icon icon="save" />
        </button>
    </div>
</template>

<script>
import AttributeService from '@/services/AttributeService';
import TableService from '@/services/TableService';
import PossibleAttributeTypes from '@/store/possible-attr-types.js';

console.log(1);
export default {
    name: "EditTableComponent",
    data() {
        let tableName = this.$route.params.tableName;
        let tableId = TableService.getTableByName(tableName).id;        
        
        let types = PossibleAttributeTypes;
        let attributes = [];        

        AttributeService.getAttributes(tableId).then(response => {
            this.attributes = response.data
        })

        return {
            tableName,
            attributes,
            types            
        };
    },
    methods:{
        sendAttributes(){
            console.log(this.attributes[0]);
            AttributeService.updateAttributes(this.attributes[0]).then(() => 
                {
                    alert("Данные успешно изменеы");
                    this.$router.go();
                },
                error => {
                    alert("Данные не отправлены: " + error.message  );
                }
            )
        }
    }
}
</script>