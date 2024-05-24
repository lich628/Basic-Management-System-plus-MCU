<template>
  <div>
    <div style="display: flex">
      <span style="font-family: 宋体;font-size: 20px; margin-left: 15px">出入库记录</span>
    </div>
    <el-card style="height: 90vh; margin: 5px">
      <div>
        <div>
          <el-table :data="ioData">
            <el-table-column prop="ioId" label="ID" width="100px">
            </el-table-column>
            <el-table-column prop="batchId" label="批次ID" sortable >
            </el-table-column>
            <el-table-column prop="type" label="记录类型" sortable width="120px">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type === 'in'" type="success">入库</el-tag>
                <el-tag v-if="scope.row.type === 'out'" type="warning">出库</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="operatorName" label="操作员" sortable width="200px">
            </el-table-column>
            <el-table-column prop="time" label="时间" sortable>
            </el-table-column>
            <el-table-column prop="operate" label="操作">
              <template slot-scope="scope">
                <el-button type="danger" @click="openDeleteConfirmDialog(scope.row.ioId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-dialog :visible.sync="deleteDialogVisible" width="30%" :append-to-body="true" :center="true">
            <span>确定删除订单ID为 {{ deleteIoId }} 的记录吗？</span>
            <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRecord" >确 定</el-button>
      </span>
          </el-dialog>
        </div>
        <el-empty v-if="ioData.length === 0" description="暂无订单"></el-empty>
      </div>
    </el-card>
    <el-tag style="position: fixed; bottom: 30px; margin-left: 40%">共{{this.ioData.length}}条记录</el-tag>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {deleteIORecords, IOList} from "@/api/io_records";
import {mapGetters} from "vuex";

export default {
  name: "ioRecords",
  computed: {
    ...mapGetters(['role'])
  },
  data() {
    return {
      ioData: [],
      deleteDialogVisible: false,
      deleteIoId: null
    }
  },
  created() {
    this.getIOList();
  },
  methods: {
    getIOList() {
      IOList().then(response => {
        this.ioData = response.data.ioRecords;
      }).catch(error => {
        console.error(error);
      });
    },
    openDeleteConfirmDialog(id) {
      if (this.role !== 0) {
        Message.error('权限不足');
        return ;
      }
      this.deleteIoId = id;
      this.deleteDialogVisible = true;
    },
    deleteRecord() {
      deleteIORecords(this.deleteIoId).then(response => {
        console.log(response);
        this.deleteDialogVisible = false;
        this.getIOList();
        Message.success("删除成功");
      }).catch(error => {
        console.error(error);
        this.deleteDialogVisible = false;  // 确保对话框关闭
        Message.error("删除失败");
      });
    }
  }
}
</script>

<style scoped lang="scss">

</style>
