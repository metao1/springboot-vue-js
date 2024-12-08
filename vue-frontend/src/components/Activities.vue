<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
    <div v-if="!activities.length" class="col-span-full text-center">Loading...</div>
    <div v-for="activity in activities" :key="activity.id"
         class="bg-white p-4 rounded shadow hover:shadow-lg transition-shadow duration-300">
      <h2 class="text-xl font-semibold mb-2">{{ activity.title }}</h2>
      <p class="text-gray-700 mb-1">Price: {{ activity.currency }}{{ activity.price }}</p>
      <p class="text-gray-700 mb-1">Rating: {{ activity.rating }}</p>
      <p class="text-gray-700 mb-1">Special offer: {{ activity.specialOffer ? "Yes" : "No" }}</p>
      <p class="text-gray-700 mb-1">Supplier: {{ activity.supplierId }} </p>
    </div>
  </div>
</template>

<style lang="scss">
.activities {
  &__container {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: center;
    margin: 0 auto;
    max-width: 1200px;
    padding: 0 20px;
  }
  &__activity {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    max-width: 300px;
    margin: 0 20px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
    text-align: center;
    transition: all 0.3s ease-in-out;
    &:hover {
      border: 1px solid #000;
      box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.2);
    }
  }
}

@media screen and (min-width: 768px) {
  .activities {
    &__container {
      width: 100%;
      padding: 0;
      row-gap: 20px;
    }
  }
}
</style>

<script>
export default {
  name: "activities-component",
  props: {
    activities: {
      type: Array,
      required: true
    }
  },
  mounted() {
    console.log(this.activities);
  }
}
</script>
