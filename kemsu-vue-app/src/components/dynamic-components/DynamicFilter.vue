<template>
    <div class="d-flex flex-column align-items-center" v-if="'string' == attribute.attribute_type">
        <label v-bind:for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <input v-bind:name="attribute.attribute_name" type="text"
                v-bind:id="attribute.attribute_name" class="form-control w-75">
    </div>

    <div class="d-flex flex-column align-items-center" v-else-if="'list' == attribute.attribute_type">
        <label v-bind:for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <select v-bind:name="attribute.attribute_name"
                v-bind:id="attribute.attribute_name" class="form-select w-75">
            <optgroup label="">
                <option value="" selected>-</option>
            </optgroup>
        </select>
    </div>

    <div class="d-flex flex-column align-items-center" v-else-if="'number' == attribute.attribute_type">
        <label v-bind:for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <input v-bind:name="attribute.attribute_name" 
               v-bind:id="attribute.attribute_name + '_min'" type="number" class="form-control w-75" />
        <input v-bind:name="attribute.attribute_name" 
               v-bind:id="attribute.attribute_name + '_max'" type="number" class="form-control w-75" />
    </div>

    <div class="d-flex flex-column align-items-center" v-else-if="'email' == attribute.attribute_type">
        <label v-bind:for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <input v-bind:name="attribute.attribute_name" 
               v-bind:id="attribute.attribute_name" type="text" class="form-control w-75" />
    </div>

    <div class="d-flex flex-column align-items-center" v-else-if="'phone' == attribute.attribute_type">
        <label v-bind:for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <input v-bind:name="attribute.attribute_name" 
               v-bind:id="attribute.attribute_name" type="text" class="form-control w-75" />
    </div>

    <div class="d-flex flex-column align-items-center" v-else-if="'date' == attribute.attribute_type">
        <label v-bind:for="attribute.attribute_name" class="form-label">
            {{ attribute.attribute_name }}
        </label>
        <input v-bind:name="attribute.attribute_name" 
               v-bind:id="attribute.attribute_name + '_min'" type="date" class="form-control w-75" />
        <input v-bind:name="attribute.attribute_name" 
               v-bind:id="attribute.attribute_name + '_max'" type="date" class="form-control w-75" />
    </div>
</template>

<script>
export default {
    props: ['attribute'],    
    computed() {
        console.log(this.attribute);
    }
}
</script>