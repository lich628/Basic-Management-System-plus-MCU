<template>
  <div>
    <div id="app" style="display: flex; height: 90vh">
      <el-card class="chart-card" style="margin: 5px; border-radius: 20px; height: 100%; width: 60%">
        <div ref="chart" style="width: 100%;height:500px"></div>
        <el-tag>自动记录异常数据:</el-tag>
        <el-switch v-model="autoRec" style="margin-left: 20px"
        active-text="开启"
        inactive-text="关闭">
        </el-switch>
        <el-tag style="margin-left: 20px" type="warning">阈值设置:</el-tag>
        <el-input v-model="inputTemperature" style="width: 50px;margin-left: 10px" placeholder="温度" :disabled="!setLimit"></el-input>
        <el-input v-model="inputHumidityMax" style="width: 50px;margin-left: 5px" placeholder="最大湿度" :disabled="!setLimit"></el-input>
        <el-input v-model="inputSmokeSensor" style="width: 80px;margin-left: 5px" placeholder="烟雾值" :disabled="!setLimit"></el-input>
        <el-button v-if="!setLimit" type="primary" @click="setLimit = true" style="margin-left: 20px">编辑</el-button>
        <el-button v-if="setLimit" type="success" @click="submitThresholds">保存</el-button>
      </el-card>
      <el-card style="margin-left: 10px; width: 40%; height: 100%; border-radius: 20px">
        <span style="font-family: 宋体,serif;font-size: 20px">异常记录</span>
        <el-divider></el-divider>
        <el-table :data="exceptionData" style="width: 100%; height: 500px;overflow: auto;">
          <el-table-column prop="temperature" label="温度" width="50px"></el-table-column>
          <el-table-column prop="humidity" label="湿度" width="50px"></el-table-column>
          <el-table-column prop="smokeSensor" label="烟雾值" ></el-table-column>
          <el-table-column prop="time" label="记录时间" width="150px"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button @click="deleteData(scope.$index)" type="danger" size="mini">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts';
import {addData, deleteData, getData} from "@/api/env_record";
import {Message} from "element-ui";
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'LineChart',
  computed: {
    ...mapGetters({
      role: 'role',
      thresholdTemperature: 'temperature',
      thresholdHumidity: 'humidity',
      thresholdSmokeSensor: 'smokeSensor'
    })
  },
  data() {
    return {
      inputTemperature: 0,
      inputHumidityMax: 0,
      inputSmokeSensor: 0,

      isConnected: null,
      temperature: 0,
      humidity: 0,
      smokeSensor: 0,
      chart: null,
      xAxisData: [],
      temperatureData: [],
      humidityData: [],
      smokeSensorData: [],

      autoRec: true,
      exceptionData:[],

      setLimit : false
    };
  },
  created() {
    this.$socket = new WebSocket('ws://localhost:8088/sensorData');
    this.$socket.onopen = () => {
      this.startScan();
    };
    this.getExceptionData();
  },
  mounted() {
    this.$nextTick(() => {
      this.chart = echarts.init(this.$refs.chart);
      window.addEventListener('resize', this.chart.resize);
      this.chart.setOption({
        title: {
          text: '传感器数据'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['温度', '湿度', '烟雾值']
        },
        xAxis: {
          type: 'category',
          data: this.xAxisData
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '温度',
            type: 'line',
            data: this.temperatureData
          },
          {
            name: '湿度',
            type: 'line',
            data: this.humidityData
          },
          {
            name: '烟雾值',
            type: 'line',
            data: this.smokeSensorData
          }
        ]
      });
    });
    this.inputTemperature = this.thresholdTemperature;
    this.inputHumidityMax = this.thresholdHumidity.max;
    this.inputSmokeSensor = this.thresholdSmokeSensor;
  },
  methods: {
    ...mapActions('thresholds', [
      'setThresholds'
    ]),
    startScan() {
      this.$socket.onmessage = (e) => {
        let data = JSON.parse(e.data);
        console.log(data);
        this.temperature = data.temperature;
        this.humidity = data.humidity;
        this.smokeSensor = data.smokeSensor;
        if (data.temperature === 0 && data.humidity === 0) {
          this.isConnected = false;
          return;
        }
        this.isConnected = true;
        this.updateChartData();

        if (this.autoRec && this.isException(data)) {
          let dataWithTime = {...data, time: new Date().toLocaleString('zh-CN', { hour12: false })};
          this.addExceptionData(dataWithTime);
        }
      }
    },
    isException(data) {
      return data.temperature > this.thresholdTemperature ||
        data.humidity < this.thresholdHumidity.min ||
        data.humidity > this.thresholdHumidity.max ||
        data.smokeSensor > this.thresholdSmokeSensor;
    },
    submitThresholds(){
      if (this.role !== 0) {
        Message.error('权限不足');
        return ;
      }
      const newThresholds = {
        temperature: this.inputTemperature,
        humidity: { max: this.inputHumidityMax },
        smokeSensor: this.inputSmokeSensor
      };
      this.setThresholds(newThresholds);
      this.setLimit = false;
    },
    updateChartData() {
      const now = new Date();
      const time = `${now.getHours()}:${now.getMinutes()}:${now.getSeconds()}`;
      this.xAxisData.push(time);
      this.temperatureData.push(this.temperature);
      this.humidityData.push(this.humidity);
      this.smokeSensorData.push(this.smokeSensor);
      if (this.xAxisData.length > 80) {
        this.xAxisData.shift();
        this.temperatureData.shift();
        this.humidityData.shift();
        this.smokeSensorData.shift();
      }
      this.chart.setOption({
        xAxis: {
          data: this.xAxisData
        },
        series: [
          {data: this.temperatureData},
          {data: this.humidityData},
          {data: this.smokeSensorData}
        ]
      });
    },
    notify() {
      if (this.isConnected === false) {
        this.$notify({
          title: '提示',
          message: '似乎没有连接上传感器',
          type: 'warning',
          position: 'bottom-left',
          duration: 0
        });
      }
      if (this.isConnected === true) {
        this.$notify({
          title: '提示',
          message: '连接成功',
          type: 'success',
          position: 'bottom-left',
        });
      }
    },
    async getExceptionData() {
      const res = await getData();
      this.exceptionData = res.data.sensorData;
    },
    async addExceptionData(data) {
      await addData(data);
      this.getExceptionData();
    },
    deleteData(index) {
      if (this.role >= 2) {
        Message.error('权限不足');
      }
      const id = this.exceptionData[index].id;
      console.log(id)
      deleteData(id).then(response => {
        console.log(response);
        this.getExceptionData();
        Message.success("删除成功");
      }).catch(error => {
        console.error(error);
      });
    }
  },
  beforeDestroy() {
    if (this.chart) {
      window.removeEventListener('resize', this.chart.resize);
      this.chart.dispose();
    }
    this.$socket.close();
  },
  watch: {
    isConnected: function (newVal, oldVal) {
      this.notify();
    }
  },
}
</script>

<style scoped>
.chart-card {
  padding: 0 !important;
}
</style>
