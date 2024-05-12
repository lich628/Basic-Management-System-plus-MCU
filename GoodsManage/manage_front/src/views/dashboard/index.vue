<template>
  <div class="dashboard-container">
    <el-card></el-card>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Dashboard",
  computed: {
    ...mapGetters(["userName"]),
  },
};
</script>

<style lang="scss" scoped>

</style>
