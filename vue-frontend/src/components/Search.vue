<template>
  <div class="mb-4 search__container">
    <input type="text" v-model="searchQuery" @input="onSearch"
           placeholder="Search activities..." class="w-full p-2 border border-gray-300 rounded">
  </div>
</template>

<script>
export default {
  components: {},
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Search",
  data() {
    return {
      searchQuery: ''
    };
  },
  methods: {
    onSearch() {
      this.$emit('update:activities', this.searchQuery);
    }
  }
};
</script>

<style scoped>
.search__container {
  margin: 20px auto;
  text-align: center;
}

.search__input {
  width: 300px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}
</style>
