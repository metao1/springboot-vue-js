<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
    <div v-if="loading" class="col-span-full text-center">Loading...</div>
    <div v-if="error" class="col-span-full text-center text-red-500">{{ error }}</div>
    <div v-for="activity in activities" :key="activity.id"
         class="bg-white p-4 rounded shadow hover:shadow-lg transition-shadow duration-300">
      <h2 class="text-xl font-semibold mb-2">{{ activity.title }}</h2>
      <p class="text-gray-700 mb-1">Price: {{ activity.currency }}{{ activity.price }}</p>
      <p class="text-gray-700 mb-1">Rating: {{ activity.rating }}</p>
      <p class="text-gray-700 mb-1">Special offer: {{ activity.specialOffer ? "Yes" : "No" }}</p>
      <p class="text-gray-700 mb-1">Supplier Name: {{ activity.supplierName }} </p>
      <p class="text-gray-700 mb-1">Supplier Location: {{ activity.supplierLocation }} </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "activities-component",
  props: {
    activities: {
      type: Array,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    },
    error: {
      type: String,
      default: ''
    }
  },
}
</script>
