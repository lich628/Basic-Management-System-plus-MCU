<template>
  <div class="big-header">
    <div class="title-container">
      <div class="title">EGM —— 应急物资管理系统</div>
      <el-image
        class="title-image"
        :src="require('@/assets/boy1.jpeg')"
        alt=""
      ></el-image>
    </div>
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <i class="el-icon-user" />
          <img :src="avatar + '?imageView2/1/w/80/h/80'" class="user-avatar" />
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item> 主页 </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided>
            <el-button type="text" @click="showDialog = true"
            >更新账号登录信息</el-button
            >
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span style="display: block">退出系统</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog title="用户信息更新" :visible.sync="showDialog" width="450px" :append-to-body="true" :center="true">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账户名" prop="userAccount">
          <el-input  v-model="form.userAccount" ></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password" >
          <el-input v-model="form.password" type="password" show-password></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password"></el-input>
        </el-form-item>

        <!-- 上传图片组件 action为接收上传的后端地址 -->
        <el-form-item prop="file" label="头像">
          <el-upload
            v-if="isShowUpload"
            ref="upload"
            class="avatar-uploader"
            :show-file-list="false"
            :limit="1"
            :auto-upload="false"
            :on-change="onChange"
            accept=".jpg,.png,.jpeg"
            name="file"
            action="">
            <i class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>

        <el-row v-if="isShowImgUpload">
          <el-col :span="6" style="color: white">1</el-col>
          <el-col :span="10">
            <div style="position: relative; background-color: #f0f3fa">
              <el-image
                :src="localUrl"
                :preview-src-list="[localUrl]"
                style="width: 100%; height: 100%"
                fit="scale-down"
              ></el-image>
            </div>
            <div style="padding: 10% 40%">
              <el-button
                type="primary"
                :loading="false"
                size="small"
                @click="reLoad"
              >重新上传</el-button
              >
            </div>
          </el-col>
          <el-col :span="4" style="color: white">1</el-col>
        </el-row>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="onSubmit">确 定</el-button>
        <el-button @click="onCancel">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { update } from "@/api/user";
export default {
  data() {
    const diffPsd = (rule, value, callback) => {
      if (!value) {
        return new Error("请输入新密码");
      } else {
        if (value.trim().length < 6) {
          callback(new Error("密码必须长于6位"));
        } else {
          callback();
        }
      }
    };
    const validatePsd = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致"));
      } else {
        callback();
      }
    };

    return {
      isShowImgUpload: false,
      isShowUpload: true,
      showDialog: false,

      form: {
        userId: this.$store.state.user.userId,
        userName: this.$store.state.user.userName,
        userAccount: this.$store.state.user.userAccount,
        password: this.$store.state.user.password,
        confirmPassword: '',
        avatar: this.$store.state.user.avatar,
        file: "",
      },

      rules: {
        userAccount: [
          { required: true, message: "请输入账号", trigger: "blur" },
        ],

        password: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { validator: diffPsd, trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "请再次输入新密码", trigger: "blur" },
          { validator: validatePsd, trigger: "blur" },
        ],
        file: [{ required: false, message: "请上传头像", trigger: "blur" }],
      },
    };
  },
  computed: {
    ...mapGetters(["avatar"]),
  },
  methods: {
    onCancel() {
      this.isShowImgUpload = false;
      this.isShowUpload = true;
      this.$refs.form.resetFields();
      this.showDialog = false;
    },

    //重新选择图片
    reLoad() {
      this.isShowImgUpload = false; //呈现本地预览组件
      this.isShowUpload = true; //隐藏上传组件
      this.form.file = ""; //清除表单中的文件记录
      this.$refs.upload.clearFiles(); //不符合的文件类型，清除已经上传的文件
    },

    onSubmit() {
      this.$loading = true;
      this.$refs.form.validate((valid) => {
        if (valid) {
          console.log("onSubmit this form-->");
          console.log(this.form);

          //必须新建formData来才能把图片传到后端
          let formData = new FormData();
          for (let key in this.form) {
            if (key === 'file' && !this.form.file) {
              continue;
            }
            if (key !== "confirmPassword"){
              formData.append(key, this.form[key]);
            }
          }
          update(formData).then((res) => {
            this.$message({
              message: "信息更新成功",
              type: "success",
            });
          });
          setTimeout(() => {
            this.isShowImgUpload = false;
            this.isShowUpload = true;
            this.$refs.form.resetFields();
            this.showDialog = false;
            this.$router.push({ path: "/dashboard" }); //跳转到欢迎页面
            location.reload();
          }, 1500);
        }
      });
    },
    //可用于预览图片
    onChange(event) {
      const isPic =
        ["image/jpeg", "image/png", "image/jpg"].indexOf(event.raw.type) !== -1;
      const isLt = event.raw.size / 1024 / 1024 < 5;
      if (!isPic) {
        this.$message({
          type: "error",
          message: "上传的图片只能是 png 或 jpg 格式!",
          duration: 1000 * 3,
        });
      }
      if (!isLt) {
        this.$message({
          type: "error",
          message: "上传图片大小不能超过 5MB!",
          duration: 1000 * 2,
        });
      }
      if (!isPic || !isLt) {
        this.form.file = ""; //清除表单文件记录
        this.$refs.upload.clearFiles(); //不符合的文件类型，清除已经上传的文件
        return;
      }

      //event.raw为图片实体，可通过consolse打印查看详情
      // 获取上传图片的本地URL，用于上传前的本地预览
      var URL = null;
      if (window.createObjectURL !== undefined) {
        // basic
        URL = window.createObjectURL(event.raw);
      } else if (window.URL !== undefined) {
        // mozilla(firefox)
        URL = window.URL.createObjectURL(event.raw);
      } else if (window.webkitURL !== undefined) {
        // webkit or chrome
        URL = window.webkitURL.createObjectURL(event.raw);
      }
      // 转换后的地址为 blob:http://xxx/7bf54338-74bb-47b9-9a7f-7a7093c716b5
      this.localUrl = URL;
      this.isShowImgUpload = true; //呈现本地预览组件
      this.isShowUpload = false; //隐藏上传组件
      this.form.file = event.raw;
      console.log("the file of form is-->");
      console.log(this.form.file);
    },
    async logout() {
      await this.$store.dispatch("user/logout");
      this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    },
  }
}
</script>

<style scoped lang="scss">
.big-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .title {
    font-family: 宋体,serif;
    font-size: 24px;
    font-weight: bold;
    margin-top: 25px;
  }
  .title-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .title-image {
    margin-top: 50px;
    max-width: 110px;
    max-height: 110px;
    margin-left: 5px;
  }
  .right-menu {
    display: flex;
    align-items: center;
  }
  .avatar-container {
    margin-right: 30px;

    .avatar-wrapper {
      margin-top: 5px;
      position: relative;

      .user-avatar {
        cursor: pointer;
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }

      .el-icon-caret-bottom {
        cursor: pointer;
        position: absolute;
        right: -28px;
        top: 10px;
        font-size: 25px;
      }
      .el-icon-user{
        cursor: pointer;
        position: absolute;
        right: 45px;
        top: 10px;
        font-size: 20px;
      }
    }
  }
}
</style>
