<template>
  <div class="dashboard-container">
    <el-row :gutter="10" style="height: 40vh; margin: 0;">
      <el-col :span="12" style="height: 90%">
        <span style="font-family: 宋体,serif;font-size: 20px">系统仪表盘</span>
        <div class="gauge-container">
          <div class="gauge-wrapper">
            <div ref="temperatureChart" class="gauge-chart"></div>
            <el-tag v-if="temperature === 0 && humidity === 0 && smokeSensor === 0" type="warning">未连接</el-tag>
            <el-tag v-else-if="temperature < thresholdTemperature" type="success">正常</el-tag>
            <el-tag v-if="temperature > thresholdTemperature" type="danger">异常</el-tag>
          </div>
          <div class="gauge-wrapper">
            <div ref="humidityChart" class="gauge-chart"></div>
            <el-tag v-if="temperature === 0 && humidity === 0 && smokeSensor === 0" type="warning">未连接</el-tag>
            <el-tag v-else-if="humidity < thresholdHumidity" type="success">正常</el-tag>
            <el-tag v-if="humidity > 70" type="danger">异常</el-tag>
          </div>
          <div class="gauge-wrapper">
            <div ref="smokeSensorChart" class="gauge-chart"></div>
            <el-tag v-if="temperature === 0 && humidity === 0 && smokeSensor === 0" type="warning">未连接</el-tag>
            <el-tag v-else-if="smokeSensor < thresholdSmokeSensor" type="success">正常</el-tag>
            <el-tag v-else-if="smokeSensor > thresholdSmokeSensor" type="danger">异常</el-tag>
          </div>
        </div>
      </el-col>
      <el-col :span="9" style="height: 100%">
        <el-card class="box-card" style="height: 100%;width: 100%" :shadow="'always'">
          <span style="font-family: 宋体, serif; font-size: 20px">系统概况</span>
          <el-divider></el-divider>
          <div style="display: flex; gap: 10px;">
            <div style="display: flex; flex-direction: column; align-items: center;">
              <el-icon class="el-icon-s-custom"></el-icon>
              <el-tag type="danger" style="margin-left: 10px">管理员</el-tag>
              <span style="font-family: 宋体,serif;font-size: 18px">{{role0}}人</span>
            </div>
            <div style="display: flex; flex-direction: column; align-items: center;">
              <el-icon class="el-icon-s-check" style="margin-left: 10px"></el-icon>
              <el-tag type="primary" style="margin-left: 10px">审核员</el-tag>
              <span style="font-family: 宋体,serif;font-size: 18px">{{role1}}人</span>
            </div>
            <div style="display: flex; flex-direction: column; align-items: center;">
              <el-icon class="el-icon-user" style="margin-left: 10px"></el-icon>
              <el-tag type="success" style="margin-left: 10px">操作员</el-tag>
              <span style="font-family: 宋体,serif;font-size: 18px">{{role2}}人</span>
            </div>
          </div>
          <div style="margin-top: 30px">
            <span style="font-family: 宋体,serif;font-size: 20px;margin-left: 10px">库存状态</span>
          </div>
          <div style="margin-top: 15px">
            <el-tag v-if="lowInv === 0 && NoInv === 0" type="success"
                    style="margin-left: 10px"><el-icon class="el-icon-check"></el-icon></el-tag>
            <el-tag v-else type="warning" style="margin-left: 10px"><el-icon class="el-icon-warning"></el-icon></el-tag>
            <el-badge :value="lowInv">
            <el-tag type="warning" effect="plain" style="margin-left: 20px">存货低</el-tag>
            </el-badge>
            <el-badge :value="NoInv">
            <el-tag type="danger" effect="plain" style="margin-left: 20px">无库存</el-tag>
            </el-badge>
          </div>
        </el-card>
      </el-col>
      <el-col :span="3" style="height: 100%">
        <el-card class="box-card" style="width: 100%;height: 100%" :shadow="'always'">
          <span style="font-family: 宋体,serif;font-size: 16px">待处理</span>
          <el-divider></el-divider>
          <div>
          <el-badge :value="batchCheck">
            <el-tag type="info">待审核</el-tag>
          </el-badge>
          </div>
          <div style="margin-top: 30px">
            <el-badge :value="batchIn">
              <el-tag type="success">待入库</el-tag>
            </el-badge>
          </div>
          <div style="margin-top: 30px">
            <el-badge :value="batchOut">
              <el-tag>待出库</el-tag>
            </el-badge>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10" style="margin-left:0; margin-top: 10px; margin-right: 0; height: 58vh">
      <el-col :span="12" style="height: 100%">
        <BarChart></BarChart>
      </el-col>
      <el-col :span="12" style="height: 100%">
        <el-card style="width: 100%; height: 100%; border-radius: 20px">
          <span style="font-family: 宋体,serif;font-size: 20px">库存详情</span>
          <el-divider></el-divider>
          <el-table :data="goodsList" style="height: 350px; margin-top: -22px; overflow: auto">
            <el-table-column prop="picUrl" label="" >
              <template slot-scope="scope">
                <img :src="scope.row.picUrl" style="width: 50px; height: 50px" alt="图片加载失败">
              </template>
            </el-table-column>
            <el-table-column prop="goodsName" label="物资名称"></el-table-column>
            <el-table-column prop="goodsType" label="物资类型" ></el-table-column>
            <el-table-column prop="currentQuantity" label="当前数量"></el-table-column>
            <el-table-column v-if="false" prop="quantityMark" label="低数量值"></el-table-column>
            <el-table-column label="库存状态">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.currentQuantity > scope.row.quantityMark" type="success">正常</el-tag>
                <el-tag v-if="scope.row.currentQuantity <= scope.row.quantityMark && scope.row.currentQuantity !== 0" type="warning">过低</el-tag>
                <el-tag v-if="scope.row.currentQuantity === 0" type="danger">无库存</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import * as echarts from 'echarts';
import {buzz} from "@/api/env_record";
import BarChart from "@/views/chart/bar.vue";
import {goodsListAll} from "@/api/goods";
import {userCounts} from "@/api/user";
import {batchStatistics} from "@/api/batch";

export default {
  name: "Dashboard",
  components: {BarChart},
  computed: {
      ...mapGetters({
        userName: 'userName',
        role: 'role',
        thresholdTemperature: 'temperature',
        thresholdHumidity: 'humidity',
        thresholdSmokeSensor: 'smokeSensor'
      })
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

      role0:0,
      role1:0,
      role2:0,

      batchIn: 0,
      batchCheck: 0,
      batchOut: 0,

      lowInv: 0,
      NoInv: 0,
    };
  },
  created() {
    this.getGoodsList();
    this.userCounts();
    this.batchCounts();
    this.$socket = new WebSocket('ws://localhost:8088/sensorData');
    this.$socket.onopen = () => {
      this.startScan();
    };
  },
  methods: {
    getGoodsList() {
      goodsListAll().then(res => {
        this.goodsList = res.data.goods;
        let lowInvCount = 0;
        let noInvCount = 0;
        this.goodsList.forEach(item => {
          if (item.currentQuantity === 0) {
            noInvCount++;
          } else if (item.currentQuantity <= item.quantityMark) {
            lowInvCount++;
          }
        });
        this.lowInv = lowInvCount;
        this.NoInv = noInvCount;
      });
    },
    userCounts() {
      userCounts().then(res => {
        let role0Count = 0;
        let role1Count = 0;
        let role2Count = 0;
        res.data.usersCount.forEach(item => {
          if (item.role === 0) {
            role0Count = item.total;
          } else if (item.role === 1) {
            role1Count = item.total;
          } else if (item.role === 2) {
            role2Count = item.total;
          }
        });
        this.role0 = role0Count;
        this.role1 = role1Count;
        this.role2 = role2Count;
      });
    },
    batchCounts(){
      batchStatistics().then(res => {
        this.batchCheck = res.data.batchStatistics.pendingReview;
        this.batchIn = res.data.batchStatistics.pendingInStock;
        this.batchOut = res.data.batchStatistics.pendingOutStock;
        console.log('Batch Statistics:', this.batchCheck, this.batchIn, this.batchOut); // 添加日志检查数据
      })
    },
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
        if (this.isException(data)) {
          buzz(true);
          setTimeout(() => {
            buzz(false);
          }, 2000);
        }
      }
    },
    isException(data) {
      return data.temperature > this.thresholdTemperature ||
        data.humidity > this.thresholdHumidity ||
        data.smokeSensor > this.thresholdSmokeSensor;
    },
    stopScan(){
      if (this.$socket) {
        this.$socket.close();
      }
    },
    notify(){
      if(this.isConnected === false || this.isConnected === null) {
        this.$notify({
          title: '提示',
          message: '似乎没有连接上硬件',
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
    },
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

.box-card::before {
  content: "";
  position: absolute;
  bottom: 0;
  right: -50px;
  width: 50%;
  height: 80%;
  background-image: url('~@/assets/3boy.png');
  background-repeat: no-repeat;
  background-size: contain;
  background-position: right bottom;
  filter: blur(4px);
  opacity: 0.7; /* Adjust the opacity as needed */
  pointer-events: none; /* Ensure the background image does not interfere with other elements */
}
</style>
