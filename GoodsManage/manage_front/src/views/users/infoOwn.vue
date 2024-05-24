<template>
  <div>
    <div style="display: flex">
    <span style="font-family: 宋体,serif;font-size: 22px">个人信息</span>
      <el-tag style="margin-left: auto;" type="info">更新账户登录信息与头像请点击右上角</el-tag>
    </div>
    <el-card style="margin-left: 10px;margin-right: 20px; margin-top: 10px; height: 400px; width: 95%; border-radius: 50px">
      <div>
        <el-avatar :size="300" :src="avatar" style="margin-top: 20px;margin-left: 50px"></el-avatar>
      </div>
      <div>
        <el-form label-width="100px" :inline="true" style="position: absolute;left: 500px;top: 100px;">
          <el-form-item label="用户账号">
            <el-input v-model="userAccount" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户角色">
            <el-tag v-if="role === 0" type="danger">管理员</el-tag>
            <el-tag v-if="role === 1" type="primary">审核员</el-tag>
            <el-tag v-if="role === 2" type="success">操作员</el-tag>
          </el-form-item>
        </el-form>
        <el-form label-width="100px" :inline="true" style="position: absolute;left: 500px;top: 180px;">
          <el-form-item label="用户名字">
            <el-input v-model="userName" :disabled="!isEditing"></el-input>
          </el-form-item>
          <el-form-item label="用户性别">
            <el-radio-group v-model="sex" :disabled="!isEditing">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="用户电话号码">
            <el-input v-model="phone" :disabled="!isEditing"></el-input>
          </el-form-item>
          <el-form-item style="margin-left: 50px">
            <el-button @click="toggleEdit" :type="isEditing? 'success' : 'primary'">{{ isEditing ? '保存' : '编辑' }}</el-button>
            <el-button v-if="isEditing" @click="isEditing = false" type="warning">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {update, userInfo} from "@/api/user";

export default {
  name: "infoOwn",
  computed: {
    ...mapGetters(['userId','role','userName','userAccount','avatar'])
  },
  created() {
    this.getUserInfo();
  },
  data() {
    return {
      age: '',
      sex: '',
      phone: '',
      isEditing: false
    }
  },
  methods: {
    getUserInfo() {
        userInfo(this.userId).then(res => {
          this.sex = res.data.users.sex;
          this.age = res.data.users.age;
          this.phone = res.data.users.phone;
        })
      },
    toggleEdit() {
      if (this.isEditing) {
        this.saveUserInfo();
      }
      this.isEditing = !this.isEditing;
    },
    saveUserInfo() {
      // 实现保存用户信息的逻辑
      let formData = new FormData();
        formData.append("userId", this.userId)
        formData.append("userName", this.userName)
        formData.append("age", this.age)
        formData.append("sex", this.sex)
        formData.append("phone", this.phone)
      update(formData).then(() => {
        this.$message({
          message: "修改成功",
          type: "success",
        });
      })
    }
  }
}
</script>

<style scoped lang="scss">
.el-card {
  display: flex;
  flex-direction: row;
  align-items: center;
}
</style>
