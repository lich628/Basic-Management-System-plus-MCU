<template>
<div>
<div class="menu">
  <el-card>
    <i class="el-icon-s-claim" style="font-size: 25px;"></i>
    <el-tag  type="info" style="margin-left: 20px">扫描批次UID : </el-tag>
    <el-button v-if="!scanSwitch" type="success" style="margin-left: 20px"
               @click="startScan">开始读取</el-button>
    <el-button v-if="scanSwitch" type="info" style="margin-left: 20px"
               @click="stopScan">停止读取</el-button>
    <el-button type="warning" style="margin-left: 20px"
    @click="read">获取批次</el-button>
    <el-tag  :type="messageStatus" style="margin-left: 20px">操作提示 : </el-tag>
    <el-input v-model="scanMessage" style="width: 200px; margin-left: 10px" disabled></el-input>
    <el-button type="danger" style="margin-left:20px;"
              @click="submitCloseBatch">操作</el-button>
    <i class="el-icon-warning" v-if="operatorId !== '' && operatorId !== userId"
       style="margin-left: 140px;margin-top: 18px; color: red; position: fixed">注意 : 目前操作的不是你提交的批次!</i>
  </el-card>
</div>
  <div class="batchInfo">
    <el-card class="data">
      <el-tag class="el-icon-s-flag" style="font-size: 13px;">批次UID : </el-tag>
      <el-input v-model="scanedCardUid" style="width: 150px; margin-left: 10px" ></el-input>
      <el-tag class="el-icon-s-custom" style=" margin-left: 20px">提交者 : </el-tag>
      <el-input v-model="operatorName" style="width: 120px; margin-left: 10px" disabled></el-input>
      <el-tag class="el-icon-s-check" style="margin-left: 5px">审核者 : </el-tag>
      <el-input v-model="reviewerName" style="width: 120px; margin-left: 10px" disabled></el-input>
      <el-tag class="el-icon-info" type="info" style="margin-left: 25px">批次类型 : </el-tag>
      <el-tag v-if="batchType==='in'" type="success" style="margin-left: 10px">入库</el-tag>
      <el-tag v-if="batchType==='out'" type="warning" style="margin-left: 10px">出库</el-tag>
    </el-card>
    <el-card class="info">
      <div v-if="goodsList.length === 0 || scanedCardUid === ''" class="scan-prompt">
        请扫描RFID标签
        <i  class="el-icon-s-ticket" style="font-size: 50px;"></i>
        <div v-if="scanSwitch" class="scan-prompt">
          请将标签放入读写区域内
          <i  class="el-icon-loading" style="font-size: 50px;"></i>
        </div>
      </div>
      <el-table v-else class="goodsList" :data="goodsList">
        <el-table-column prop="picUrl" label="包含物资">
          <template slot-scope="scope">
            <img :src="scope.row.picUrl" style="width: 50px; height: 50px; border-radius: 50%">
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="物资名称"></el-table-column>
        <el-table-column prop="quantity" label="数量"></el-table-column>
      </el-table>
    </el-card>
  </div>
</div>
</template>

<script>
import { mapGetters} from "vuex";
import { selectBatchByUid, closeBatchByBatchId} from "@/api/batch";
import {selectNameAndAccountById} from "@/api/user";

export default {
  name: "batchOut",
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  beforeDestroy() {
    this.$socket.close();
  },
  data(){
    return {
      batchId:'',
      scanSwitch: false,
      scanedCardUid: '',
      scanMessage:'',
      messageStatus: 'info',
      operatorId: '',
      operatorName: '',
      reviewerId:'',
      reviewerName: '',
      batchType: '',
      goodsList: []
    }
  },
  methods: {
    clear(){
      this.scanedCardUid = '';
      this.scanMessage = '';
      this.operatorId = '';
      this.operatorName = '';
      this.reviewerId = '';
      this.reviewerName = '';
      this.batchType = '';
      this.goodsList = [];

      this.scanSwitch = true;
    },
    startScan(){
      this.clear();
      this.$socket.onmessage = (e) => {
        let data = JSON.parse(e.data);
        console.log(data);
        if(data.cardUid === null && this.scanedCardUid === ''){
          this.scanMessage = '请将卡片/标签放入感应区';
          this.messageStatus = 'warning';
        } else if(data.cardUid !== null){
          this.scanedCardUid = data.cardUid.toString();
          this.read();
        }
      };
    },
    stopScan(){
      if (this.$socket) {
        this.$socket.close();
        this.scanSwitch = false;
      }
    },
    // 读取
    read(){
      if(this.scanedCardUid === ''){
        this.$message({
          message: '请扫描UID',
          type: 'error'
        });
        return;
      }
      selectBatchByUid(this.scanedCardUid).then(res => {
        if(res.code === 20000){
          this.scanMessage = '读取成功';
          this.messageStatus = 'success';
          this.batchId = res.data.batches.batchId;
          this.operatorId = res.data.batches.operatorId;
          this.reviewerId = res.data.batches.reviewerId;
          selectNameAndAccountById(this.operatorId).then(res => {
            if(res.code === 20000){
              this.operatorName = res.data.users.userName;
            }
          });
          selectNameAndAccountById(this.reviewerId).then(res => {
            if(res.code === 20000){
              this.reviewerName = res.data.users.userName;
            }
          });
          this.batchType = res.data.batches.batchType;
          this.goodsList = res.data.batches.batchGoodsDetail;
        }else{
          if(res.code === 10000) {
            this.scanMessage = res.message;
            this.messageStatus = 'error';
          }
          else{
            this.scanMessage = '读取失败';
            this.messageStatus = 'error';
          }
        }
      })
    },
    submitCloseBatch(){
      closeBatchByBatchId(this.batchId).then(res => {
        if(res.code === 20000){
          this.$message({
            message: '操作成功',
            type: 'success'
          });
          this.clear();
        }else if(res.code === 10000){
          this.$message({
            message: res.message,
            type: 'error'
          });
        } else if(res.code === 20001){
          this.$message({
            message: res.message,
            type: 'error'
          });
        } else {
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
  .menu{
    margin-left: 10px;
    height: 70px;
    width: 98%;
    .el-card{
      width: 100%;
      height:100%;
      display: flex;
    }
  }
  .batchInfo{
    margin-top: 10px;
    margin-left: 10px;
    width: 98%;
    .data{
      width: 100%;
      height: 20%;
      display: flex;
    }
    .info{
      overflow: auto;
      margin-top: 8px;
      height: 380px;
    }
  }
  .scan-prompt {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    font-size: 20px;
    margin-top: 80px;
    .el-icon-s-ticket {
      font-size: 50px;
    }
    .el-icon-loading{
      font-size: 50px;
    }
  }
</style>
