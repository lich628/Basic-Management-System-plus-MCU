<template>
  <div>
    <div class="title">
      <span style="font-family: 宋体,serif; font-size: 25px; margin-left: 15px">历史批次</span>
      <div style="margin-top: -15px;height: 2px">
        <el-divider/>
      </div>
    </div>
    <div class="options">
      <el-tag type="info" style="height: 30px; margin-top: 5px">搜索批次:</el-tag>
      <el-input style="width: 230px;margin-left: 10px" v-model="batchInfoInput" placeholder="请输入批次ID或卡片UID信息" clearable></el-input>
      <el-button style="margin-left: 10px; height: 38px" type="primary" @click="batchSelect">搜索</el-button>
      <div class="selectBar">
        <el-tag type="info" style="height: 30px">批次类型:</el-tag>
        <el-select v-model="batchTypeSelect" clearable placeholder="请选择"
                   style="margin-left: 8px; width: 100px">
          <el-option
            v-for="type in batchTypeOptions"
            :key="type.value"
            :label="type.label"
            :value="type.value">
          </el-option>
        </el-select>
        <el-tag type="info" style="height: 30px; margin-left: 10px">批次状态:</el-tag>
        <el-select v-model="batchStatusSelect" clearable placeholder="请选择"
                   style="margin-left: 8px; width: 105px">
          <el-option
            v-for="type in batchStatusOptions"
            :key="type.value"
            :label="type.label"
            :value="type.value">
          </el-option>
        </el-select>
        <el-button style="margin-left: 20px;height: 40px" @click="resetSelect">重置</el-button>
      </div>
      <el-switch v-model="showRight"
                 active-text="显示物资清单"
                 style="margin-left: auto; margin-right: 20px;margin-top: 10px">
      </el-switch>
    </div>
    <div class="batchDataContainer" style="margin-top: 10px">
      <template v-if="batchesData.length === 0">
        <div class="no-data">暂无批次!</div>
      </template>
      <el-card v-for="batch in batchesData" :key="batch.batchId" :shadow="'hover'" class="rootCard">
        <div class="card-batch-row">
          <div class="cardLeft" :style="{ width: showRight ? '70%' : '97%' }">
            <el-descriptions class="batchInfo" direction="vertical"
                             :title="'批次ID: ' + batch.batchId" :column="3" :size="'small'" border>
              <template slot="extra">
                标签卡号:
                <el-tag v-if="batch.cardUid" style="margin: 10px">{{batch.cardUid}}</el-tag>
                <el-tag v-else style="margin: 10px">未分配</el-tag>
              </template>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-user"></i>
                  操作员:
                </template>
                <span style="font-family: 宋体; font-size: 16px; color: #006de7;">
              {{ batch.operator ? batch.operator.userName : batch.operatorId }}
            </span>
                <span style="font-family: 宋体; font-size: 10px; color: gray;">
              用户账号:[
              {{ batch.operator ? batch.operator.userAccount : '' }}
            </span>
                ]
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-time"></i>
                  添加时间:
                </template>
                {{ batch.opTime}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-takeaway-box"></i>
                  批次类型:
                </template>
                <el-tag v-if="batch.batchType === 'in'" type="primary">入库</el-tag>
                <el-tag v-else-if="batch.batchType === 'out'" type="warning">出库</el-tag>
              </el-descriptions-item>
              <el-descriptions-item v-if="batch.batchStatus !== 0">
                <template slot="label">
                  <i class="el-icon-s-check"></i>
                  审核员:
                </template>
                <span style="font-family: 宋体; font-size: 16px; color: #ff0000;">
              {{ batch.reviewer ? batch.reviewer.userName : batch.reviewerId }}
            </span>
                <span style="font-family: 宋体; font-size: 10px; color: gray;">
              用户账号:
              {{ batch.reviewer ? batch.reviewer.userAccount : '' }}
            </span>
              </el-descriptions-item>
              <el-descriptions-item v-if="batch.batchStatus !== 0">
                <template slot="label">
                  <i class="el-icon-time"></i>
                  审核时间:
                </template>
                {{ batch.reviewTime}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-finished"></i>
                  批次状态:
                </template>
                <el-tag v-if="batch.batchStatus === 1" type="success">审核通过</el-tag>
                <el-tag v-else-if="batch.batchStatus === 2" type="danger">审核拒绝</el-tag>
                <el-tag v-else-if="batch.batchStatus === 0" type="info">待审核</el-tag>
              </el-descriptions-item>
              <el-descriptions-item v-if="batch.opComment">
                <template slot="label">
                  <i class="el-icon-chat-line-square"></i>
                  提交备注/描述:
                </template>
                {{ batch.opComment}}
              </el-descriptions-item>
              <el-descriptions-item v-if="batch.batchStatus !== 0">
                <template slot="label">
                  <i class="el-icon-chat-dot-square"></i>
                  审核备注/描述:
                </template>
                {{ batch.viewerComment}}
              </el-descriptions-item>
            </el-descriptions>
          </div>
          <div class="cardRight" v-if="showRight" :style="{ width: '30%' }">
            <el-card shadow="hover">
              <div class="header">
                物资清单
                <div style="margin-top: -15px"><el-divider/></div>
              </div>
              <div class="content">
                <el-card v-for="goods in batch.batchGoodsDetail" :key="goods.goodsId"
                         :body-style="{ padding: '0px' }" style="margin-bottom: 8px; margin-right: 20px;" shadow="hover">
                  <div class="goodCards">
                    <div style="width: 50px"><img :src="goods.picUrl" class="goodsImage" alt="加载失败"></div>
                    <div style="display: flex; justify-content: space-between;width: 220px">
                      <span style="margin-left: 10px; margin-top: 8px; font-size: 13px">{{goods.goodsName}}</span>
                      <el-tag style="margin-right: 10px; margin-top: 8px">数量:{{goods.quantity}}</el-tag>
                    </div>
                  </div>
                </el-card>
              </div>
            </el-card>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { historyListSelect, historyList } from '@/api/batch'
import { selectNameAndAccountById} from "@/api/user";
import {Message} from "element-ui";

export default {
  name: "Batch",
  data() {
    return {
      showRight: true,
      batchTypeOptions: [
        { value: 'in', label: '入库' },
        { value: 'out', label: '出库' }
      ],
      batchTypeSelect:'',
      batchStatusOptions: [
        { value: 1, label: '审核通过' },
        { value: 2, label: '审核拒绝' },
      ],
      batchStatusSelect:'',
      batchInfoInput:'',

      checkBatchMenu: false,
      batchesData: [],
    }
  },
  created() {
    this.getBatchList();
  },
  methods : {
    resetSelect(){
      this.batchInfoInput = '';
      this.batchTypeSelect = '';
      this.batchStatusSelect = '';
      this.getBatchList();
    },
    getBatchList(){
      historyList().then(response => {
        const batches = response.data.batches;
        const promises = batches.map(batch => {
          return Promise.all([
            this.selectNameAndAccountById(batch.operatorId),
            batch.reviewerId ? this.selectNameAndAccountById(batch.reviewerId) : Promise.resolve(null)
          ]).then(([operator, reviewer]) => {
            return {
              ...batch,
              operator,
              reviewer
            };
          });
        });
        Promise.all(promises).then(batchesData => {
          this.batchesData = batchesData;
        });
      }).catch(error => {
        console.log(error);
      });
    },
    batchSelect(){
      const batchTypeMap = {
        'in': '入库',
        'out': '出库'
      };
      const batchStatusMap = {
        '1': '审核通过',
        '2': '审核拒绝'
      };
      // 使用映射对象将select框的值转换为相应的字段
      const batchType = batchTypeMap[this.batchTypeSelect];
      const batchStatus = batchStatusMap[this.batchStatusSelect];
      let messageContent = `搜索字段为: ${this.batchInfoInput} ${batchType} ${batchStatus}`;
      if (!this.batchInfoInput && !batchType && !batchStatus) {
        Message.error("查询内容为空 "+messageContent);
      } else {
        Message.success(messageContent);
      }
      console.log(this.batchInfoInput, batchType, batchStatus)

      historyListSelect(this.batchInfoInput, this.batchTypeSelect, this.batchStatusSelect).then(response => {
        const batches = response.data.batches;
        const promises = batches.map(batch => {
          return Promise.all([
            this.selectNameAndAccountById(batch.operatorId),
            batch.reviewerId ? this.selectNameAndAccountById(batch.reviewerId) : Promise.resolve(null)
          ]).then(([operator, reviewer]) => {
            return {
              ...batch,
              operator,
              reviewer
            };
          });
        });
        Promise.all(promises).then(batchesData => {
          this.batchesData = batchesData;
        });
      }).catch(error => {
        console.log(error);
      });
    },
    async selectNameAndAccountById(userId) {
      try {
        let response = await selectNameAndAccountById(userId);
        return response.data.users;
      } catch (error) {
        console.log(error);
      }
    },
  }
}
</script>

<style>
.title {
  font-family: 宋体,serif;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}
.options {
  display: flex;
  margin-top: 15px;
  margin-bottom: 20px;
  margin-left: 20px;
}
.no-data {
  display: flex;
  justify-content: center;
  margin-top: 50px;
  height: 100vh; /* Adjust as needed */
  font-size: 1.5em; /* Adjust as needed */
  font-family: '宋体', sans-serif; /* Adjust as needed */
}
.rootCard{
  margin-left: 10px;
  margin-bottom: 20px;
  width: 96%;
  border-radius: 30px;
}
.card-batch-row {
  display: flex;
  height: auto;
  width: 100%;
  padding: 10px;
  background-color: #f9f9f9;
  margin-bottom: 10px;
  border-radius: 20px;
  overflow: auto;
}
.cardLeft{
  transition: width 0.5s;
}
.cardRight{
  margin-top: 15px;
  margin-left: 40px;
  margin-right: 20px;
  width: 30%;
  overflow: auto;
  height: 250px;
  transition: width 0.5s;

  .header {
    height: 50px;
  }
  .content{
    margin-top: -10px;
    overflow: auto;
  }
  .goodCards{
    height: 50px;
    display: flex;
  }
  .goodsImage {
    width: 100%;
    height: 100%;
  }
}
.batchInfo{
  width: 100%;
}
.selectBar{
  margin-left: 20px;
}
</style>
