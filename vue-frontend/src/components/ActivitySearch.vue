<script setup lang="ts">
import { computed } from 'vue'
import { useStore } from '../store/useStore'

const store = useStore()

const searchQuery = computed({
  get: () => store.getters['activities/getSearchQuery'],
  set: (value) => store.commit('activities/SET_SEARCH_QUERY', value)
})

const activities = computed(() => store.getters['activities/getActivities'])
const loading = computed(() => store.getters['ui/isLoading'])
const error = computed(() => store.getters['ui/getError'])

const handleSearch = () => {
  store.dispatch('activities/searchActivities', searchQuery.value)
}

// Load initial data
handleSearch()
</script>

<template>
  <div class="search__container">
    <div class="search__box">
      <input
          v-model="searchQuery"
          type="text"
          placeholder="Search activities..."
          @keyup.enter="handleSearch"
      />
      <button @click="handleSearch" :disabled="loading">
        {{ loading ? 'Searching...' : 'Search' }}
      </button>
    </div>

    <p v-if="error" class="error-message">{{ error }}</p>

    <div v-if="activities.length" class="activities__container">
      <div v-for="activity in activities" :key="activity.id" class="activities__card">
        <h3 class="activities__title">{{ activity.title }}</h3>
        <div class="activities__details">
          <p class="activities__price">${{ activity.price.toFixed(2) }}</p>
          <p class="activities__rating">Rating: {{ activity.rating }}/5</p>
          <p v-if="activity.specialOffer" class="activities__special-offer">Special Offer!</p>
        </div>
        <div class="activities__supplier">
          <p>Supplier: {{ activity.supplierName }}</p>
          <p>Supplier Rating: {{ activity.supplierRating }}/5</p>
        </div>
      </div>
    </div>
    <p v-else-if="!loading">No activities found</p>
  </div>
</template>

<style lang="scss">
@use '../styles/activities';

.error-message {
  color: #e44d26;
  margin-bottom: 1rem;
  text-align: center;
}
</style>
