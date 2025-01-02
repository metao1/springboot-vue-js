<script setup lang="ts">
import ActivitySearch from './components/ActivitySearch.vue'
</script>

<template>
  <div class="app-container">
    <h1>Activity Search</h1>
    <ActivitySearch />
  </div>
</template>

<style>
.app-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

h1 {
  color: #42b883;
  margin-bottom: 2rem;
  text-align: center;
}
</style>
