<template>
  <div>
    <div style="display: flex">
    <span style="font-family: 宋体;font-size: 20px; margin-left: 15px">采购订单</span>
    </div>
    <el-card style="height: 90vh; margin: 5px">
    <div>
    <div>
      <el-table :data="ordersData">
        <el-table-column prop="orderId" label="采购ID" width="100px">
        </el-table-column>
        <el-table-column prop="batchId" label="批次ID" sortable width="200px">
        </el-table-column>
        <el-table-column prop="sourceType" label="供应来源" sortable width="120px">
          <template slot-scope="scope">
            <el-select v-if="scope.row.isEditing" v-model="scope.row.sourceType" placeholder="请选择">
              <el-option label="供应商" value="供应商"></el-option>
              <el-option label="零售商" value="零售商"></el-option>
            </el-select>
            <span v-else>{{scope.row.sourceType}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="具体来源" width="400px">
          <template slot-scope="scope">
            <el-input v-if="scope.row.isEditing" v-model="scope.row.location"></el-input>
            <span v-else>{{scope.row.location}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" sortable>
          <template slot-scope="scope">
            <el-select v-if="scope.row.isEditing" v-model="scope.row.status" placeholder="请选择">
              <el-option label="待入库" value="待入库"></el-option>
              <el-option label="已入库" value="已入库"></el-option>
            </el-select>
            <span v-else>{{scope.row.status}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="operate" label="操作">
          <template slot-scope="scope">
            <el-button @click="toggleEdit(scope.row)">{{ scope.row.isEditing ? '保存' : '编辑' }}</el-button>
            <el-button type="danger" @click="openDeleteConfirmDialog(scope.row.orderId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :visible.sync="deleteDialogVisible" width="30%" :append-to-body="true" :center="true">
        <span>确定删除订单ID为 {{ deleteOrderId }} 的采购订单吗？</span>
        <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteOrder" >确 定</el-button>
      </span>
      </el-dialog>
    </div>
    <el-empty v-if="ordersData.length === 0" description="暂无订单"></el-empty>
  </div>
    </el-card>
    <el-tag style="position: fixed; bottom: 30px; margin-left: 40%">共{{this.ordersData.length}}条记录</el-tag>
  </div>
</template>

<script>
import {deleteOrder, getOrders, updateOrder} from "@/api/orders";
import {Message} from "element-ui";

export default {
  name: "ordersList",
  data() {
    return {
      ordersData: [],
      deleteDialogVisible: false,
      deleteOrderId: null
    }
  },
  created() {
    this.OrdersList();
  },
  methods: {
    OrdersList() {
      getOrders().then(response => {
        this.ordersData = response.data.orders;
      }).catch(error => {
        console.error(error);
      });
    },
    toggleEdit(row) {
      if (row.isEditing) {
        updateOrder(row).then(response => {
          console.log(response);
        }).catch(error => {
          console.error(error);
        });
        row.isEditing = false;
      } else {
        this.$set(row, 'isEditing', true);
      }
    },
    openDeleteConfirmDialog(orderId) {
      if (this.role !== 0) {
        Message.error('权限不足');
        return ;
      }
      this.deleteOrderId = orderId;
      this.deleteDialogVisible = true;
    },
    deleteOrder() {
      deleteOrder(this.deleteOrderId).then(response => {
        console.log(response);
        this.deleteDialogVisible = false;
        this.OrdersList();
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
