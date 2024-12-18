<template>
  <div id="app" class="container mx-auto p-4">
    <h1 class="text-3xl font-bold text-center mb-6">Activities</h1>
    <Search @update:activities="updateActivities" />
    <Activities :activities="activities" />
  </div>
</template>

<script>
import {onMounted, ref} from 'vue';
import Activities from './components/Activities.vue';
import Search from './components/Search.vue';

export default {
  name: 'App',
  components: {
    Activities,
    Search
  },
  setup() {
    const activities = ref([]);

    const fetchActivities = async (searchQuery = '') => {
      try {
        var price = 40;
        var url = `http://localhost:8080/activities?title=${searchQuery}` + `&price=${price}`;
        const response = await fetch(url, {
          method: 'GET',
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
          },
        });
        activities.value = await response.json();
      } catch (error) {
        console.error('Failed to fetch activities:', error);
      }
    };

    const updateActivities = (searchQuery) => {
      fetchActivities(searchQuery);
    };

    // Fetch all activities on mount
    onMounted(() => fetchActivities());

    return {
      activities,
      updateActivities,
    };
  },
};
</script>

<style>
#app {
  text-align: center;
  color: #2c3e50;
}
</style>
