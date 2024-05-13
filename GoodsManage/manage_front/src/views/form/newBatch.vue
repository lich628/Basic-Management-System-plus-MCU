<template>
  <div class="main-container">
    <div class="submitBatch">
      <div style="display: flex">
      <el-tag type="info">批次类型:</el-tag>
      <el-radio-group v-model="batchType" :disabled="lockType" style="margin-left: 10px; margin-top: 10px">
        <el-radio label="in">入库</el-radio>
        <el-radio label="out">出库</el-radio>
      </el-radio-group>
        <el-switch
          v-model="lockType"
          active-text="锁定"
          style="margin-top: 8px; margin-left: 20px"
        ></el-switch>
        <div style="margin-left: auto">
          <el-button type="warning" size="small" @click="createBatchId">生成批次号</el-button>
          <el-button type="success" size="small" style="margin-left: 20px" @click="submitBatch">提交批次</el-button>
        </div>
      </div>
      <el-card class="commitCard">
        <el-tag type="danger">Batch ID :</el-tag>
        <el-input style="width: 200px; margin-left: 10px" disabled v-model="newBatchId"></el-input>
        <el-tag type="info" style="margin-left: 10px">操作备注 :</el-tag>
        <el-input style="width: 330px; margin-left: 10px" v-model="opComment"></el-input>
      </el-card>
      <el-card style="margin-top: 10px;height: 412px; overflow: auto">
        <el-table
          :data="batchList"
          style="width: 100%; overflow: auto;">
          <el-table-column prop="picUrl" label="选择的物资">
            <template slot-scope="scope">
              <img :src="scope.row.picUrl" style="width: 50px; height: 50px" alt="图片加载失败">
            </template>
          </el-table-column>
          <el-table-column
            prop="goodsName"
            label="物资名称">
          </el-table-column>
          <el-table-column
            label="操作数量">
            <template slot-scope="scope">
              <el-slider v-if="batchType==='out'"
                         v-model="scope.row.quantity" :min="0" :max="scope.row.currentQuantity"></el-slider>
              <el-slider v-else v-model="scope.row.quantity" :min="0"></el-slider>
            </template>
          </el-table-column>
          <el-table-column>
            <template slot-scope="scope">
              <el-input v-model="scope.row.quantity" :min="0" style="width: 60px" @input="scope.row.quantity = Number($event)"></el-input>
            </template>
          </el-table-column>
          <el-table-column>
            <template slot-scope="scope">
              <el-button
                type="danger"
                icon="el-icon-delete"
                circle
                @click="deleteBatchGoods(scope.row, scope.$index)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <div class="goodsList">
      <el-card class="listCard">
      <el-table style="height: 400px; overflow: auto"  :data="goodsList" :row-class-name="rowClassName"
                @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column prop="goodsName" label="物资名称"></el-table-column>
        <el-table-column prop="picUrl" label="图片">
          <template slot-scope="scope">
            <img :src="scope.row.picUrl" style="width: 50px; height: 50px" alt="图片加载失败">
          </template>
        </el-table-column>
        <el-table-column prop="goodsType" label="物资类型"></el-table-column>
        <el-table-column prop="currentQuantity" label="物资数量"></el-table-column>
        <el-table-column prop="quantityMark" label="低数量值"></el-table-column>
      </el-table>
      <div style="margin-top: 15px">
        <el-tag type="primary" effect="plain" color="rgba(0, 255, 0, 0.20)">正常</el-tag>
        <el-tag type="warning" style="margin: 5px 10px">过低</el-tag>
        <el-tag type="danger">无储货</el-tag>
        <el-button size="small" type="primary"
                   style="width: 85px; margin-left: 335px"
                    @click="addToBatch">添加到批次</el-button>
      </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { goodsListAll} from "@/api/goods";
import {newBatch} from "@/api/batch";

export default {
  name: "Batch",
  data() {
    return {
      newBatchId:'',
      lockType: false,
      goodsList: [], // 物资列表
      batchList: [], // 批次列表
      multipleSelection: [], // 选中的物资
      operatorId: '',
      batchType: '',
      opComment: ''
    }
  },
  methods: {
    // 获取物资列表
    getGoodsList() {
      goodsListAll().then(res => {
        this.goodsList = res.data.goods
      })
    },
    rowClassName({ row }) {
      if(row.currentQuantity === 0 ){
       return 'danger-row' ;
      }
      if (row.currentQuantity <= row.quantityMark) {
        return 'warning-row';
      }
      return 'normal-row';
    },
    handleSelectionChange(rowVal) {
      this.multipleSelection = rowVal
    },
    addToBatch() {
      this.batchList.push(...this.multipleSelection);
      this.multipleSelection = [];
    },
    deleteBatchGoods(row, index) {
      this.batchList.splice(index, 1);
    },
    createBatchId(){
      if (!this.batchType) {
        this.$message({
          type: 'warning',
          message: '请先选择批次类型'
        });
        return;
      }
      let date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      let hour = date.getHours();
      let minute = date.getMinutes();
      let second = date.getSeconds();

      // 月份、日期、小时、分钟和秒如果小于10，前面补0
      month = month < 10 ? '0' + month : month;
      day = day < 10 ? '0' + day : day;
      hour = hour < 10 ? '0' + hour : hour;
      minute = minute < 10 ? '0' + minute : minute;
      second = second < 10 ? '0' + second : second;

      // 生成3位随机数
      let randomNum = Math.floor(Math.random() * 1000);
      while(randomNum < 100) {
        randomNum = Math.floor(Math.random() * 1000);
      }
      // 拼接批次ID
      let batchId = `${year}${month}${day}${hour}${minute}${second}${randomNum}`;

      this.newBatchId = batchId;
      this.lockType = true;
    },
    submitBatch() {
      if (!this.newBatchId) {
        this.$message({
          type: 'warning',
          message: '批次ID不能为空!'
        });
        return;
      }
      if (!this.opComment) {
        this.$message({
          type: 'warning',
          message: '操作备注不能为空!'
        });
        return;
      }
      // 检查批次列表是否为空
      if (this.batchList.length === 0) {
        this.$message({
          type: 'warning',
          message: '物资内容不能为空!'
        });
        return;
      }
      for (let i = 0; i < this.batchList.length; i++) {
        if (this.batchList[i].quantity === 0) {
          this.$message({
            type: 'warning',
            message: '物资 ' + this.batchList[i].goodsName + ' 的数量不能为0!'
          });
          return;
        }
      }
      // 显示确认窗口
      this.$confirm(`批次类型: ${this.batchType}<br>批次ID: ${this.newBatchId}<br>批次包含物资种类: ${this.batchList.length}`, '确认提交', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 如果用户点击了"确定"按钮，执行提交的操作
        console.log(this.newBatchId)
        console.log(this.batchList)
        newBatch({
          batchId: this.newBatchId,
          operatorId: this.userId,
          batchType: this.batchType,
          opComment: this.opComment,
          batchGoodsDetail: this.batchList,
          opTime: new Date().toLocaleString('zh-CN', { hour12: false })
        }).then(res => {
          this.$message({
            type: 'success',
            message: '提交成功'
          });
          console.log(res);
          this.newBatchId = '';
          this.lockType = false;
          this.batchList = [];
          this.batchType = '';
          this.opComment = '';
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消提交'
        });
      });
    }
  },
  created() {
    this.getGoodsList()
  },
  computed: {
    ...mapGetters([
      'userId',
    ])
  }
}
</script>

<style>
  .main-container{
    display: flex;
  }
  .submitBatch{
    margin-left: 10px;
    width:  60%;
    .commitCard{
      overflow: auto;
      margin-top: 10px;
    }
    .batchList{
      margin-top: 10px;
      height: 412px;
    }
  }
  .goodsList{
    width: 38%;
    margin-left: auto;

    .listCard{
      height: 545px;
    }
  }

  .normal-row{
    background-color: rgba(0, 255, 0, 0.20) !important; /* Element UI info tag color */
  }
  .warning-row {
    background-color: rgba(255, 255, 0, 0.20) !important; /* Element UI warning tag color */
  }
  .danger-row{
    background-color: rgba(255, 0, 0, 0.20) !important; /* Element UI info tag color */
  }
</style>

