<template>
  <div class="dashboard-container">
    <el-row :gutter="10" style="height: 40vh; margin: 0;">
      <el-col :span="12" style="height: 90%">
        <span style="font-family: 宋体,serif;font-size: 20px">系统仪表盘</span>
        <div class="gauge-container">
          <div class="gauge-wrapper">
            <div ref="temperatureChart" class="gauge-chart"></div>
            <el-tag v-if="temperature > 40" type="danger">异常</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </div>
          <div class="gauge-wrapper">
            <div ref="humidityChart" class="gauge-chart"></div>
            <el-tag v-if="humidity > 70" type="danger">异常</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </div>
          <div class="gauge-wrapper">
            <div ref="smokeSensorChart" class="gauge-chart"></div>
            <el-tag v-if="smokeSensor > 300" type="danger">异常</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </div>
        </div>
      </el-col>
      <el-col :span="6" style="height: 100%">
        <el-card class="box-card" style="width: 100%;height: 100%" :shadow="'hover'">
          <span style="font-family: 宋体,serif;font-size: 16px"> 用户总览</span>
            <el-divider></el-divider>
            <el-table></el-table>

        </el-card>
      </el-col>
      <el-col :span="6" style="height: 100%">
        <el-card class="box-card" style="width: 100%;height: 100%" :shadow="'hover'">
          <span style="font-family: 宋体,serif;font-size: 16px"> 批次总览</span>
            <el-divider></el-divider>
            <el-table></el-table>

        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10" style="margin-left:0; margin-top: 10px; margin-right: 0; height: 58vh">
      <el-col :span="12" style="height: 100%">
        <el-card class="box-card" style="width: 100%; height: 100%" :shadow="'hover'">
          <span style="font-family: 宋体,serif;font-size: 18px"> 物资库存情况</span>
            <el-divider></el-divider>
            <div></div>
        </el-card>
      </el-col>
      <el-col :span="12" style="height: 100%">
        <el-card class="box-card" style="width: 100%; height: 100%" :shadow="'hover'">
          <span style="font-family: 宋体,serif;font-size: 16px"> 待处理物资</span>
          <el-divider></el-divider>
          <div></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import * as echarts from 'echarts';

export default {
  name: "Dashboard",
  computed: {
    ...mapGetters(["userName"]),
  },
  data() {
    return {
      isConnected: null,
      goodsList: [],
      sensorMessage: "",
      temperature: 0,
      smokeSensor: 0,
      humidity: 0,
      temperatureChart: null,
      humidityChart: null,
      smokeSensorChart: null,
    };
  },
  created() {
    this.$socket = new WebSocket('ws://localhost:8088/sensorData');
    this.$socket.onopen = () => {
      this.startScan();
    };
  },
  methods: {
    startScan(){
      this.$socket.onmessage = (e) => {
        let data = JSON.parse(e.data);
        console.log(data);
        this.temperature = data.temperature;
        this.humidity = data.humidity;
        this.smokeSensor = data.smokeSensor
        if(data.temperature === 0 && data.humidity === 0){
          this.isConnected = false;
          return;
        }
        this.isConnected = true;
        this.setChartData();
      }
    },
    stopScan(){
      if (this.$socket) {
        this.$socket.close();
      }
    },
    notify(){
      if(this.isConnected === false) {
        this.$notify({
          title: '提示',
          message: '似乎没有连接上传感器',
          type: 'warning',
          position: 'bottom-left',
          duration: 0
        });
      }
      if(this.isConnected === true) {
        this.$notify({
          title: '提示',
          message: '连接成功',
          type: 'success',
          position: 'bottom-left',
        });
      }
    },
    setChartData(){
      this.temperatureChart.setOption({
        title: {
          text: '温度',
          left: 'center',
          top: '5%', // 调整标题位置
        },
        grid: {
          top: '20%', // 调整图表位置
          height: '60%', // 调整图表高度
        },
        series: [{
          type: 'gauge',
          detail: {
            formatter: '{value}°C',
            offsetCenter: ['0%', '80%']
          },
          data: [{ value: this.temperature}]
        }]
      });

      this.humidityChart.setOption({
        title: {
          text: '湿度',
          left: 'center',
          top: '5%', // 调整标题位置
        },
        grid: {
          top: '20%', // 调整图表位置
          height: '60%', // 调整图表高度
        },
        series: [{
          type: 'gauge',
          detail: {
            formatter: '{value}%',
            offsetCenter: ['0%', '80%']
          },
          data: [{ value: this.humidity}]
        }]
      });

      this.smokeSensorChart.setOption({
        title: {
          text: '烟雾数值',
          left: 'center',
          top: '5%', // 调整标题位置
        },
        grid: {
          top: '20%', // 调整图表位置
          height: '60%', // 调整图表高度
        },
        series: [{
          type: 'gauge',
          detail: {
            formatter: '{value}',
            offsetCenter: ['0%', '80%']
          },
          data: [{ value: this.smokeSensor}],
          max: 500
        }]
      });
    }
  },
  mounted() {
    this.$nextTick(() => {
        this.temperatureChart = echarts.init(this.$refs.temperatureChart);
        this.humidityChart = echarts.init(this.$refs.humidityChart);
        this.smokeSensorChart = echarts.init(this.$refs.smokeSensorChart);
        this.setChartData();
    });
  },
  watch: {
    isConnected: function(newVal, oldVal) {
      this.notify();
    }
  },
  beforeDestroy() {
    this.$socket.close();
  }
};
</script>

<style lang="scss" scoped>
.dashboard-container {
  margin: 10px;
  height: 100vh;
}
.gauge-container {
  display: flex;
  justify-content: space-around;
  height: 100%;
}
.gauge-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
.gauge-chart {
  width: 100%;
  height: 100%;
  text-align: center;
}
.gauge-tag {
  margin-top: 10px;
}

</style>
