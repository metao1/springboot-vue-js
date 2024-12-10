<script>
import {computed, onMounted, ref} from 'vue';
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
    const searchQuery = ref('');

    const filteredActivities = computed(() => {
      if (!searchQuery.value) {
        return activities.value;
      }
      return activities.value.filter(activity =>
          activity.title.toLowerCase().includes(searchQuery.value.toLowerCase())
      );
    });

    const fetchActivities = async () => {
      try {
        const response = await fetch('http://localhost:8080/activities', {
          method: 'GET',
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
          },
        });
        activities.value = (await response.json()).map(activity => ({
          id: activity.id,
          title: activity.title,
          price: activity.price,
          currency: activity.currency || '$',
          rating: activity.rating,
          specialOffer: activity.specialOffer || false,
          supplierId: activity.supplierId,
          supplierName: activity.supplierName,
          supplierLocation: activity.supplierLocation
        }));
      } catch (error) {
        console.error('Failed to fetch activities:', error);
      }
    };

    onMounted(fetchActivities);

    return {
      activities,
      searchQuery,
      filteredActivities,
    };
  },
};
</script>

<template>
  <div id="app" class="container mx-auto p-4">
    <h1 class="text-3xl font-bold text-center mb-6">Activities</h1>
    <Search :onSearchQueryChange="val => (searchQuery = val)"/>
    <Activities :activities="filteredActivities"/>
  </div>
</template>

<style>
#app {
  text-align: center;
  color: #2c3e50;
}
</style>
