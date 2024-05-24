<template>
  <div style="display: flex">
    <div style="width: 70%">
    <el-card v-for="supplier in suppliersData" :key="supplier.id" :shadow="'hover'" class="supplierCard">
      <el-descriptions title="供应商信息" style="width: 100%">
        <el-descriptions-item label="供应商名" >
          <el-input v-if="editingSupplier.id === supplier.id" v-model="editingSupplier.name"
                    style="width: 180px"></el-input>
          <span v-else>{{ supplier.name }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="地址">
          <el-input v-if="editingSupplier.id === supplier.id" v-model="editingSupplier.address"></el-input>
          <span v-else>{{ supplier.address }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="联系方式">
          <el-input v-if="editingSupplier.id === supplier.id" v-model="editingSupplier.contact"
                    style="width: 180px"></el-input>
          <span v-else>{{ supplier.contact }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="备注">
          <el-input v-if="editingSupplier.id === supplier.id" v-model="editingSupplier.mark"></el-input>
          <span v-else>{{supplier.mark}}</span>
        </el-descriptions-item>
      </el-descriptions>
      <div style="width: 20%">
        <el-button v-if="editingSupplier.id !== supplier.id" type="primary" size="mini" style="margin-left: 10px;" @click="editSupplier(supplier)">编辑</el-button>
        <el-button v-else type="primary" size="mini" style="margin-left: 10px;" @click="saveSupplier">保存</el-button>
        <el-button type="danger" size="mini" style="margin-left: 10px;" @click="deleteSupplier(supplier.id)">删除</el-button>
      </div>
    </el-card>
    </div>
    <div style="width: 26%; margin-top: 20px; position: fixed; margin-left: 58%">
      <el-card style="border-radius: 20px">
        <span style="font-family: 宋体,serif; font-size: 20px">新增供应商</span>
        <el-divider></el-divider>
        <el-form :model="newSupplier">
          <el-form-item label="供应商名">
            <el-input v-model="newSupplier.name"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="newSupplier.address"></el-input>
          </el-form-item>
          <el-form-item label="联系方式">
            <el-input v-model="newSupplier.contact"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="newSupplier.mark"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addSupplier">添加</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import {addSupplier, deleteSupplier, getSuppliers, updateSupplier} from "@/api/suppliers";
import {Message} from "element-ui";
import {mapGetters} from "vuex";

export default {
  name: "suppliers",
  computed: {
    ...mapGetters(['role'])
  },
  data(){
    return {
      suppliersData: [],
      editingSupplier: {},
      newSupplier: {}
    }
  },
  created() {
    this.fetchSuppliers();
  },
  methods: {
    fetchSuppliers() {
      getSuppliers().then(res => {
        this.suppliersData = res.data.suppliers;
      })
    },
    editSupplier(supplier) {
      this.editingSupplier = Object.assign({}, supplier);
    },
    saveSupplier() {
      if (this.role !== 0) {
        Message.error('权限不足');
        return ;
      }
      updateSupplier(this.editingSupplier).then(() => {
        this.fetchSuppliers();
        this.editingSupplier = {};
      });
    },
    deleteSupplier(id) {
      if (this.role !== 0) {
        Message.error('权限不足');
        return ;
      }
      deleteSupplier(id).then(() => {
        this.fetchSuppliers();
        Message.success('删除成功');
      }).catch(() => {
        this.$message.error('删除失败');
      });
    },
    addSupplier() {
      if (this.role !== 0) {
        Message.error('权限不足');
        return ;
      }
      if (this.newSupplier.name == null || this.newSupplier.address == null
        || this.newSupplier.contact == null || this.newSupplier.mark == null) {
        Message.error('信息不能为空')
        return ;
      }
      addSupplier(this.newSupplier).then(() => {
        this.fetchSuppliers();
        this.newSupplier = {};
        Message.success('添加成功');
      }).catch(() => {
        this.$message.error('添加失败');
      });
    }
  },
}
</script>

<style scoped lang="scss">
.supplierCard{
  display: flex;
  margin: 10px;
  margin-right: 50px;
  border-radius: 20px;
}
</style>
