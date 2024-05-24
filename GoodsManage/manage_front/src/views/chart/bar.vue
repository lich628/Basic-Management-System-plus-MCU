<template>
  <el-card class="box-card" style="width: 100%; height: 100%; border-radius: 20px" :shadow="'always'">
    <span style="font-family: 宋体,serif;font-size: 20px">物资种类及数量分布</span>
    <el-divider></el-divider>
    <div class="container">
      <div id="chart"></div>
    </div>
  </el-card>
</template>

<style>
.container {
  margin-top: -40px;
  width: 100%;
  height: 350px;
}
#chart {
  width: 100%;
  height: 100%;
}
</style>

<script>
import * as echarts from 'echarts';
import {getTypeQuantity} from "@/api/goods";

export default {
  name: "BarChart",
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      // 等待 DOM 元素渲染完毕后再初始化 ECharts 实例
      this.$nextTick(() => {
        var myChart = echarts.init(document.getElementById('chart'));
        myChart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            top: 'bottom'
          },
          xAxis: {
            type: 'category',
            data: []  // 初始数据为空
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '物资数量',
              type: 'bar',
              data: [],  // 初始数据为空
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        });

        // 获取数据并更新图表
        getTypeQuantity().then(res => {
          const records = res.data.goodsTypeAndQuantity;
          console.log("Fetched records:", records); // 打印获取到的数据

          const categories = records.map(record => record.goods_type);
          const colorList = [
            '#fddc04', '#009bff', '#00e6ff', '#ff4500', '#90c6b0',
            '#00ff5a', '#ff9800', '#a000ff', '#97a9a7', '#546570',
            '#c4ccd3'
          ];

          const data = records.map((record, index) => ({
            value: record.totalQuantity,
            itemStyle: {
              color: colorList[index % colorList.length]  // 直接设置颜色
            }
          }));

          console.log("Processed chart data:", data); // 打印处理后的数据

          myChart.setOption({
            xAxis: {
              data: categories
            },
            series: [{
              data: data
            }],
          });
        }).catch(error => {
          console.error("Error fetching data:", error); // 打印错误
        });

        // 窗口大小改变时调整图表大小
        window.addEventListener("resize", function () {
          myChart.resize();
        });
      });
    }
  }
};
</script>
