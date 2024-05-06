<template>
  <div>
    <div class="title">
      待入库<el-divider direction="vertical"/>待出库
      <div style="margin-top: -15px;height: 2px">
        <el-divider/>
      </div>
    </div>
    <div class="options" style="margin-top: 15px">
      <el-input style="width: 200px"></el-input>
      <el-button style="margin-left: 10px">搜索</el-button>
    </div>
    <div class="batchDataContainer" style="margin-top: 10px">
      <el-card v-for="batch in batchesData" :key="batch.batchId" class="batch-row" el-card shadow="hover">
        <div slot="header" class="header">
          <span>批次ID: {{batch.batchId}}  | 批次类型: {{batch.batchType}}</span>
          <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
        </div>
        <div  v-if="batch.is_approved !== null">状态:
          <el-tag v-if="batch.is_approved === 1" type="success">审核通过</el-tag>
          <el-tag v-else-if="batch.is_approved === 0" type="danger">审核拒绝</el-tag>
          <el-tag v-else type="info">待审核</el-tag>
        </div>
        <div  v-if="batch.operatorId">操作者: {{ batch.operatorId }}</div>
        <div  v-if="batch.opTime">提交时间: {{ batch.opTime }}</div>
        <div  v-if="batch.reviewerId">审核员: {{ batch.reviewerId }}</div>
        <div  v-if="batch.reviewTime">审核时间: {{ batch.reviewTime }}</div>
      </el-card>
    </div>
  </div>
</template>

<script>

export default {
  name: "Batch",
  data() {
    return {
      batchesData: [
        {
          batchId: 1,
          batchType: '入库',
          operatorId: 100,
          opTime: '类别1',
          reviewerId: 200,
          reviewTime: '2021-01-01',
          comment: '描述1',
          is_approved: 1,
        },
        {
          batchId: 2,
          batchType: '入库',
          operatorId: 100,
          opTime: '2024-05-06',
          reviewerId: 200,
          reviewTime: '2021-01-02',
          comment: '描述2',
          is_approved: 1,
        },
      ]
    }
  },
  created() {
  },
  methods : {

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
  margin-bottom: 20px;
}
.batch-row {
  height: 150px;
  width: 1000px;
  padding: 10px;
  border-bottom: 1px solid #eee;
  background-color: #f9f9f9;
  margin-bottom: 10px;
  border-radius: 20px;
}
</style>
