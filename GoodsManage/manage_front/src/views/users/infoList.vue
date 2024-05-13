<template>
  <div>
    <div style="margin-top: 25px; margin-left: 20px">
      <el-tag type="info">用户查询:</el-tag>
    <el-input v-model="nameOrAccountInput" placeholder="用户姓名或账号" style="width: 200px; margin-left: 8px"></el-input>
    <el-select v-model="sex_value" placeholder="性别" style="margin-left: 10px; width: 100px">
      <el-option
        v-for="item in sex"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <el-select v-model="role" placeholder="职责" style="margin-left: 10px; width: 100px">
      <el-option
        v-for="item in roles"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <el-button icon="el-icon-search" style="margin-left: 10px" @click="clearSlots">清空</el-button>
    <el-button icon="el-icon-search" type="primary" style="margin-left: 15px" @click="userListLike">搜索 / 获取</el-button>
  </div>
  <div>
    <el-table
      :data="tableData"
      border
      style="width: 98%; margin-left: 20px; margin-top: 30px">
      <el-table-column prop="userId" label="用户ID" v-if="false"></el-table-column>
      <el-table-column
        prop="userName"
        label="姓名"
        width="180"
        sortable>
      </el-table-column>
      <el-table-column
        prop="userAccount"
        label="用户账户"
        width="180">
      </el-table-column>
      <el-table-column
        prop="age"
        label="年龄"
        sortable>
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="100" sortable>
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.sex === 1 ? 'primary' : 'danger'"
            disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="phone"
        label="手机号">
      </el-table-column>
      <el-table-column prop="role" label="角色/职责" sortable>
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.role === 0 ? 'danger' : (scope.row.role === 1 ? 'primary' : 'success')"
            disable-transitions>{{scope.row.role === 0 ? '管理员' : (scope.row.role === 1 ? '审核员' : '操作员')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operation" label="操作">
        <template slot-scope="scope">
          <div>
          <el-button type="primary" size="mini" >查看编辑</el-button>
          <el-button type="danger" size="mini" style="margin-left: 20px" @click="openDeleteConfirmDialog(scope.row.userId)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    </div>
    <div style="position: fixed; bottom: 5%; width: 100%; margin-left: 480px">
      <el-pagination
        @current-change="handlePageChange"
        background
        layout="prev, pager, next, total"
        :current-page.sync="pageNum"
        :page-size="pageSize"
        :page-count="pages"
        :total="total">
      </el-pagination>
    </div>
    <el-dialog
      title="确认删除"
      :visible.sync="deleteConfirmDialogVisible"
      width="30%"
      @close="deleteConfirmDialogVisible = false" :append-to-body="true" :center="true">
      <span>确定要删除这个用户吗？</span>
      <span slot="footer" class="dialog-footer">
      <el-button @click="deleteConfirmDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="deleteUser">确定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>

import { userList, userListLike, deleteUserById } from "@/api/user";
export default {
  data() {
    return {
      nameOrAccountInput: '',
      sex_value: '',
      role: '',
      sex: [
        {value: '', label: '全部'},
        {value: '男', label: '男'},
        {value: '女', label: '女'}
      ],
      roles: [
        {value: '', label: '全部'},
        {value: '管理员', label: '管理员'},
        {value: '审核员', label: '审核员'},
        {value: '操作员', label: '操作员'}
      ],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 6,
      pages:0,

      deleteConfirmDialogVisible: false,
      userIdToDelete: null,
    }
  },
  created() {
    this.fetchUserlist()
  },
  methods: {
    handlePageChange(val) {
      this.pageNum = val;
      this.fetchUserlist();
    },
    fetchUserlist() {
      console.log("fetch data of users!");
      userList(this.pageNum, this.pageSize).then(res => {
        console.log(res.data);
        this.tableData = res.data.users.records
        this.total = res.data.users.total
        this.pages = res.data.users.pages
      })
    },
    userListLike() {
      console.log("fetch data of userListLike!");
      userListLike(this.nameOrAccountInput, this.sex_value, this.role, this.pageNum, this.pageSize).then(res => {
        console.log(res.data);
        this.tableData = res.data.users.records
        this.total = res.data.users.total
        this.pages = res.data.users.pages
      })
    },
    clearSlots(){
      this.nameOrAccountInput = ''
      this.sex_value = ''
      this.role = ''
      this.fetchUserlist()
    },
    openDeleteConfirmDialog(userId) {
      this.userIdToDelete = userId;
      this.deleteConfirmDialogVisible = true;
    },
    async deleteUser() {
      try {
        let userToDelete = { userId: this.userIdToDelete };
        console.log(userToDelete);
        await deleteUserById(userToDelete);
        this.fetchUserlist();
        this.$message({
          message: "删除成功",
          type: "success",
        });
      } catch (err) {
        this.$message({
          message: "删除失败",
          type: "error",
        });
      } finally {
        this.deleteConfirmDialogVisible = false;
      }
    }
  }
}

</script>

<style scoped lang="scss">

</style>
