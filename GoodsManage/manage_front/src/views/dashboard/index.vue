<template>
  <div class="dashboard-container">
    <div class="top-left">
    <el-card class="left-card-top">
      <goods-pie-chart></goods-pie-chart>
    </el-card>
  </div>
      <el-card class="right-card-top"></el-card>
      <el-card class="right-card-down"></el-card>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import GoodsPieChart from "@/views/chart/piechart/index.vue";

export default {
  name: "Dashboard",
  components: {GoodsPieChart},
  computed: {
    ...mapGetters(["userName"]),
  },
};
</script>

<style lang="scss" scoped>
.dashboard-container {
  display: flex;
  background-color: #d6d4e8;
  height: 100vh;
}
.top-left{
  width: 65vw;
  height: 50vh;
  margin: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background-color: #fff;

  .left-card-top{
    height: 100%;
    width: 100%;
  }
}
.top-right{
  width: 20vw;
  height: 50vh;
  margin: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background-color: #fff;

  .right-card-top{
    height: 50%;
  }
  .right-card-down{
    margin-top: 10px;
    height: 50%;
  }
}

</style>
