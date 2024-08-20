<template>
  <div>
    <line-chart :chart-data="chartData" :options="chartOptions"></line-chart>
  </div>
</template>

<script>
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement);

export default {
  name: 'PriceChart',
  components: {
    LineChart: Line,
  },
  props: {
    prices: {
      type: Array,
      required: true,
    },
  },
  computed: {
    chartData() {
      return {
        labels: this.prices.map(item => item.date),
        datasets: [
          {
            label: 'Price',
            backgroundColor: '#f87979',
            data: this.prices.map(item => item.price),
          },
        ],
      };
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
      };
    },
  },
};
</script>

<style scoped>
/* Optional: Styling */
.chart-container {
  position: relative;
  height: 400px;
  width: 100%;
}
</style>
