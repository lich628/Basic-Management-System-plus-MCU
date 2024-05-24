<template>
  <div>
    <div style="display: flex">
      <span style="font-family: 宋体; font-size: 25px; margin-left: 20px">物流信息</span>
      <el-button style="margin-left: auto; margin-right: 50px" type="success" @click="addNewRecord">新增记录</el-button>
    </div>
    <el-card style="height: 90vh; margin: 5px">
      <div>
        <el-table :data="logisticsData">
          <el-table-column prop="id" label="信息ID" width="100px" />
          <el-table-column prop="batchId" label="批次ID" sortable width="200px">
            <template slot-scope="scope">
              <el-input v-if="scope.row.isEditing" v-model="scope.row.batchId" />
              <span v-else>{{ scope.row.batchId }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="logisticsStatus" label="物流情况" sortable width="120px">
            <template slot-scope="scope">
              <el-select v-if="scope.row.isEditing" v-model="scope.row.logisticsStatus" placeholder="请选择">
                <el-option label="待揽件" value="待揽件" />
                <el-option label="运输中" value="运输中" />
                <el-option label="派件中" value="派件中" />
                <el-option label="流转中" value="流转中" />
                <el-option label="已送达" value="已送达" />
              </el-select>
              <span v-else>{{ scope.row.logisticsStatus }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="logisticsInfo" label="物流信息" width="400px">
            <template slot-scope="scope">
              <el-input v-if="scope.row.isEditing" v-model="scope.row.logisticsInfo" />
              <span v-else>{{ scope.row.logisticsInfo }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="destination" label="目的地" sortable>
            <template slot-scope="scope">
              <el-input v-if="scope.row.isEditing" v-model="scope.row.destination" />
              <span v-else>{{ scope.row.destination }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="operate" label="操作">
            <template slot-scope="scope">
              <el-button @click="toggleEdit(scope.row)">{{ scope.row.isEditing ? '保存' : '编辑' }}</el-button>
              <el-button type="danger" v-if="!scope.row.isNewRecord" @click="openDeleteConfirmDialog(scope.row.id)">删除</el-button>
              <el-button type="warning" v-if="scope.row.isNewRecord" @click="cancelNewRecord(scope.$index)">取消</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog :visible.sync="deleteDialogVisible" width="30%" :append-to-body="true" :center="true">
          <span>确定删除ID为 {{ deleteId }} 的物流信息吗？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="deleteDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="deleteInfo">确 定</el-button>
          </span>
        </el-dialog>
        <el-empty v-if="logisticsData.length === 0" description="暂无信息"></el-empty>
      </div>
    </el-card>
    <el-tag style="position: fixed; bottom: 30px; margin-left: 40%">共{{ logisticsData.length }}条记录</el-tag>
  </div>
</template>

<script>
import { Message } from "element-ui";
import { deleteLogistics, getLogistics, updateLogistics, addLogistics } from "@/api/logistics";
import { mapGetters } from "vuex";

export default {
  name: "logistics",
  computed: {
    ...mapGetters(['role'])
  },
  data() {
    return {
      logisticsData: [],
      deleteDialogVisible: false,
      deleteId: null
    }
  },
  created() {
    this.logisticsList();
  },
  methods: {
    logisticsList() {
      getLogistics().then(res => {
        this.logisticsData = res.data.logistics;
      }).catch(error => {
        console.error(error);
      });
    },
    addNewRecord() {
      this.logisticsData.push({
        id: null, // 新记录在数据库保存后会生成 id
        batchId: '',
        logisticsStatus: '',
        logisticsInfo: '',
        destination: '',
        isEditing: true,
        isNewRecord: true // 新增一个标志字段
      });
    },
    toggleEdit(row) {
      if (row.isEditing) {
        // 校验
        if (!row.batchId || !row.logisticsStatus || !row.logisticsInfo || !row.destination) {
          Message.error("所有字段不能为空");
          return;
        }
        if (row.id) {
          updateLogistics(row).then(response => {
            console.log(response);
            row.isEditing = false;
            row.isNewRecord = false; // 保存成功后，设置为非新记录
            this.logisticsList(); // 刷新列表
          }).catch(error => {
            console.error(error);
          });
        } else {
          addLogistics(row).then(response => {
            console.log(response);
            row.isEditing = false;
            row.isNewRecord = false; // 保存成功后，设置为非新记录
            this.logisticsList(); // 刷新列表
          }).catch(error => {
            console.error(error);
          });
        }
      } else {
        this.$set(row, 'isEditing', true);
      }
    },
    cancelNewRecord(index) {
      this.logisticsData.splice(index, 1);
    },
    openDeleteConfirmDialog(id) {
      if (this.role !== 0) {
        Message.error('权限不足');
        return;
      }
      this.deleteId = id;
      this.deleteDialogVisible = true;
    },
    deleteInfo() {
      deleteLogistics(this.deleteId).then(response => {
        console.log(response);
        this.deleteDialogVisible = false;
        this.logisticsList();
        Message.success("删除成功");
      }).catch(error => {
        console.error(error);
        Message.error("删除失败");
      });
    }
  }
}
</script>

<style scoped lang="scss">
</style>
