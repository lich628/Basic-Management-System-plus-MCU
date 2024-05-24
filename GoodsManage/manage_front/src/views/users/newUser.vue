<template>
  <div>
    <span style="font-family: 宋体,serif; font-size: 25px">新增用户</span>
    <div style="display: flex; margin-top: 20px">
    <el-card style="border-radius: 20px; margin-left: 50px;width: 40%;height: 60vh">
    <el-form>
      <el-divider>账户信息</el-divider>
      <el-form-item label="用户账号">
        <el-input v-model="userAccount" placeholder="请输入账号"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="用户角色" style="margin-top: 50px">
        <el-select v-model="role">
          <el-option label="管理员" value= 0 ></el-option>
          <el-option label="审核员" value= 1 ></el-option>
          <el-option label="操作员" value= 2 ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    </el-card>
    <el-card style="width: 30%; margin-left: 10px; border-radius: 20px">
      <el-form>
        <el-divider>用户信息</el-divider>
        <el-form-item label="用户名字" style="margin-top: 50px">
          <el-input v-model="userName" placeholder="请输入用户名字" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="年龄" style="margin-top: 40px">
          <el-input v-model="age" placeholder="用户年龄" style="width: 100px; margin-left: 27px"></el-input>
        </el-form-item>
        <el-form-item label="性别" style="margin-top: 40px">
          <el-radio-group v-model="sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户电话号码" style="margin-top: 30px">
          <el-input v-model="phone" placeholder="电话号码" style="width: 200px"></el-input>
        </el-form-item>
      </el-form>
    </el-card>
      <el-card style="margin-left: 20px; width: 300px">
        <el-divider>默认头像</el-divider>
        <div style="height: 300px; display: flex; justify-content: center; align-items: center;">
          <img v-if="sex === 1" src="http://localhost:8088/avatar/default_boy.png" style="width: 255px; height: 255px">
          <img v-else-if="sex === 0" src="http://localhost:8088/avatar/default_girl.png" style="width: 255px; height: 255px">
          <img v-else src="~@/assets/noPic.jpg" style="width: 200px; height: 200px">
        </div>
        <div style="display: flex">
        <el-button type="success" @click="generateInfo">快速生成账号信息</el-button>
        <el-button type="primary" @click="onSubmit">添加用户</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import {addUser} from "@/api/user";

export default {
  name: "newUsers",
  data() {
    return {
      userName: '',
      userAccount: '',
      password: '',
      age: '',
      sex: '',
      role: '',
      phone: ''
    }
  },
  methods: {
    onSubmit() {
      if (this.userName === '' || this.userAccount === ''
        || this.password === '' || this.age === ''
        || this.sex === '' || this.role === '' || this.phone === '') {
        this.$message({
          message: "请填写完整信息",
          type: "error",
        });
        return;
      }
      let formData = new FormData();
      formData.append("userName", this.userName)
      formData.append("userAccount", this.userAccount)
      formData.append("password", this.password)
      formData.append("age", this.age)
      formData.append("role", this.role)
      formData.append("sex", this.sex)
      formData.append("phone", this.phone)

      addUser(formData).then(() => {
        this.$message({
          message: "添加成功",
          type: "success",
        });
        this.$router.push({path: 'list'})
      })
   },
    generateInfo() {
      this.userAccount = 'user' + Math.floor(Math.random() * 10000);
      this.password = 'pass' + Math.floor(Math.random() * 10000);
      this.$message({
        message: "账号和密码已生成",
        type: "success",
      });
    }
 }
}
</script>

<style scoped lang="scss">

</style>
