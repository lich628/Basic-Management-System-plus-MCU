<template>
  <div>
    <div class="title">
      待入库<el-divider direction="vertical"/>待出库
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
            <el-button v-if="batch.batchStatus === 0" type="primary" size="small"
                       @click="handleCheck(batch)">操作</el-button>
            <el-button v-if="batch.batchStatus === 2" type="danger" size="small" @click="delBatch(batch)">删除</el-button>
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
    <el-drawer
      :title="'审核批次: ' + currentBatch.batchId"
      :visible.sync="checkBatchMenu"
      :direction = " 'ltr' "
      :append-to-body="true"
      :before-close="handleClose"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="操作账号:">
          <el-tag v-model="userName" readonly style="width: 150px">{{userAccount}}</el-tag>
        </el-form-item>
        <el-table :data="currentBatch.batchGoodsDetail" style="height: 250px; overflow: auto">
          <el-table-column prop="goodsName" label="物资名称"></el-table-column>
          <el-table-column prop="quantity" label="数量"></el-table-column>
        </el-table>

        <el-form-item label="审批结果" prop="batchStatus">
          <el-radio-group v-model="form.batchStatus">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批意见" prop="viewerComment">
          <el-input v-model="form.viewerComment" style="width: 250px; max-height: 400px"
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 4}"
                    placeholder="请输入内容"></el-input>
        </el-form-item>

        <el-form-item v-if="form.batchStatus === '1'" label="分配操作标签" prop="cardUid">
          <el-select v-model="form.cardUid" placeholder="请选择">
            <el-option
              v-for="item in uidOptions"
              :key="item"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item style="position: absolute; bottom: 50px">
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button style="margin-left: 30px" @click="handleCheck">取消</el-button>
        </el-form-item>
      </el-form>

    </el-drawer>
    </div>
</template>

<script>
import {
  batchList,
  batchListSelect,
  updateBatch,
  getOccupiedUids,
  closeBatchInfo
} from '@/api/batch'
import { selectNameAndAccountById} from "@/api/user";
import {Message} from "element-ui";
import { mapGetters } from "vuex";
import cardUIDs from '@/cardUIDs.js';

export default {
  name: "Batch",
  computed: {
    ...mapGetters([
      'role',
      'userAccount',
      'userName',
      'userId'
    ])
  },
  data() {
    return {
      currentBatch:{
        batchId: '',
        operatorId:'',
        batchGoodsDetail: []
      },
      form: {
        batchId: '',
        batchStatus: '',
        operatorId:'',
        reviewerId: this.userId,
        viewerComment: '',
        cardUid: '',
        reviewTime: new Date().toLocaleString('zh-CN', { hour12: false })
      },

      rules:{
        batchStatus: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        viewerComment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ],
        cardUid: [
          { required: true, message: '请选择分配的操作标签', trigger: 'change' }
        ]
      },

      uidOptions: cardUIDs,

    showRight: true,
      batchTypeOptions: [
        { value: 'in', label: '入库' },
        { value: 'out', label: '出库' }
      ],
      batchTypeSelect:'',
      batchStatusOptions: [
        { value: 1, label: '审核通过' },
        { value: 2, label: '审核拒绝' },
        { value: 0, label: '待审核' }
      ],
      batchStatusSelect:'',
      batchInfoInput:'',

      checkBatchMenu: false,
      batchesData: [],
    }
  },
  created() {
    this.getBatchList();
    this.form.reviewerId = this.userId;
    this.form.viewerComment = this.form.batchStatus === '1' ? '经审核决定，准许操作': '';
  },
  methods : {
    resetSelect(){
      this.batchInfoInput = '';
      this.batchTypeSelect = '';
      this.batchStatusSelect = '';
      this.getBatchList();
    },
    getBatchList(){
      batchList().then(response => {
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
        '0': '待审核',
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

      batchListSelect(this.batchInfoInput, this.batchTypeSelect, this.batchStatusSelect).then(response => {
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
    handleCheck(batch){
      console.log(this.role);
      if (this.role >= 2) {
        Message.error('权限不足');
      } else {
        this.currentBatch = batch;
        this.form.batchId = batch.batchId;
        this.form.operatorId = batch.operatorId;
        this.checkBatchMenu = !this.checkBatchMenu;
        this.getAvailableUids();
      }
    },
    async getAvailableUids() {
      try {
        const response = await getOccupiedUids();
        const occupiedUids = response.data.occupiedUid;
        this.uidOptions = cardUIDs.filter(uid => !occupiedUids.includes(uid));
        console.log(occupiedUids)
      } catch (error) {
        console.log(error);
      }
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          // 重置表单
          this.form = {
            batchId: '',
            batchStatus: '',
            operatorId:'',
            reviewerId: this.userId,
            viewerComment: '',
            cardUid: '',
            reviewTime: new Date().toLocaleString('zh-CN', { hour12: false })
          };
          done();
        })
        .catch(_ => {});
    },
    onSubmit(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          console.log("onSubmit this form-->");
          console.log(this.form);

          // 创建一个 batches 对象
          let batches = {
            ...this.form
          };

          // 将 batches 对象转换为 JSON 格式的字符串
          let data = JSON.stringify(batches);

          updateBatch(data).then((res) => {
            this.$message({
              message: "添加成功",
              type: "success",
            });
            this.checkBatchMenu = false;
          }).catch((error) => {
            console.log(error);
            // 提交失败后不关闭drawer
          });
        }
      });
      this.getBatchList();
    },
    delBatch(batch){
      if(this.role === 0 || this.userId === batch.operatorId || this.userId === batch.reviewerId) {
        this.$confirm('确认从暂存区移除该批次吗？')
          .then(_ => {
            closeBatchInfo(batch.batchId).then((res) => {
              this.$message({
                message: "删除成功",
                type: "success",
              })
              this.getBatchList();
            }).catch((error) => {
              console.log(error);
            });
          })
      } else {
        this.$message({
          message: "权限不足，非管理员或批次相关人员!",
          type: "error",
        });
      }
    }
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
