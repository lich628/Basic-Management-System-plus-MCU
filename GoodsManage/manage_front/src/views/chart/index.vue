<template>
  <div class="main">
    <div class="pie">
      <pie-chart/>
    </div>
    <div class="bar">
      <bar-chart/>
    </div>
  </div>
</template>
<style>
.main{
  display: flex;
}
.pie {
  width: 48%;
  height: 500px;
  margin-left: 20px;
}
.bar{
  width: 48%;
  height: 500px;
  margin-left: 20px;
}
</style>
<script lang="ts">
import Vue from "vue";
import PieChart from "@/views/chart/piechart.vue";
import BarChart from "@/views/chart/bar.vue";

export default Vue.extend({
  components: {BarChart, PieChart}
})
</script>
