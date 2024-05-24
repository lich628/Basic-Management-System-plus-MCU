<template>
  <div style="margin-left: 10px">
    <div style="font-family: 宋体,serif; font-size: 22px">新建物资信息</div>
    <el-card style="border-radius: 20px;margin: 20px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="width: 50%">
        <el-form-item prop="goodsName" label="物资名称">
          <el-input v-model="form.goodsName" />
        </el-form-item>
        <el-form-item prop="goodsType" label="物资类型">
          <el-input v-model="form.goodsType" />
        </el-form-item>
        <el-form-item prop="quantityMark" label="安全库存值">
          <el-input v-model="form.quantityMark" />
        </el-form-item>
        <el-form-item prop="goodsDescription" label="物资描述">
          <el-input v-model="form.goodsDescription" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交记录</el-button>
          <el-button @click="onCancel">取消上传</el-button>
        </el-form-item>
      </el-form>
      <el-upload style="position: absolute; top: 100px; right: 250px"
                 v-if="isShowUpload"
                 ref="upload"
                 class="avatar-uploader"
                 :show-file-list="false"
                 :limit="1"
                 :auto-upload="false"
                 :on-change="onChange"
                 accept=".jpg,.png,.jpeg"
                 name="pic"
                 action="">
        <i class="el-icon-plus avatar-uploader-icon" />
      </el-upload>
      <div v-if="isShowImgUpload" style="position: absolute; top: 50px; right: 200px">
        <div style="position: relative; background-color: #f0f3fa">
          <el-image
            :src="localUrl"
            :preview-src-list="[localUrl]"
            style="width: 300px; height: 300px"
            fit="scale-down"
          ></el-image>
        </div>
        <div style="margin-left: 35%">
          <el-button
            type="primary"
            size="small"
            @click="reLoad">重新上传</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { addGoods } from "@/api/goods";

export default {
  data() {
    const isTrueNumber = (rule, value, callback) => {
      if (!value) {
        return new Error("请输入数量");
      } else {
        value = Number(value); // 转换为数字
        if (typeof value === "number" && !isNaN(value)) {
          // 判断是否为数字
          if (value <= 0) {
            callback(new Error("数量应该大于0"));
          } else {
            callback();
          }
        } else {
          callback(new Error("请正确输入数量"));
        }
      }
    };

    return {
      isShowImgUpload: false,
      isShowUpload: true,
      localUrl: "",
      form: {
        goodsName: "",
        goodsType: "",
        goodsDescription: "",
        quantityMark: "",
        pic: "",
      },
      rules: {
        goodsName: [{ required: true, message: "请输入物品名称", trigger: "blur" }],
        goodsType: [{ required: true, message: "请选择物资类型", trigger: "blur" }],
        quantityMark: [
          { required: true, message: "请输入安全库存数量", trigger: "blur" },
          { validator: isTrueNumber, trigger: "blur" },
        ],
        goodsDescription: [{ required: true, message: "请输入物资描述", trigger: "blur" }],
      },
    };
  },
  methods: {
    onSubmit() {
      if (!this.form.pic) {
        this.$message({
          message: "请上传图片",
          type: "error",
        });
        return;
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          let formData = new FormData();
          for (let key in this.form) {
            formData.append(key, this.form[key]);
          }

          addGoods(formData).then((res) => {
            this.$message({
              message: "添加成功",
              type: "success",
            });
            this.$router.push({path: "/info/goods"});
            this.clearForm();
          });
        }
      });
    },
    onCancel() {
      this.$message({
        message: "已取消!",
        type: "warning",
      });
      this.clearForm();
    },
    reLoad() {
      this.isShowImgUpload = false;
      this.isShowUpload = true;
      this.form.pic = "";
      this.$refs.upload.clearFiles();
    },
    clearForm() {
      this.isShowImgUpload = false;
      this.isShowUpload = true;
      this.$refs.form.resetFields();
    },
    onChange(event) {
      const isPic = ["image/jpeg", "image/png", "image/jpg"].indexOf(event.raw.type) !== -1;
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
        this.form.pic = "";
        this.$refs.upload.clearFiles();
        return;
      }
      var URL = null;
      if (window.createObjectURL != undefined) {
        URL = window.createObjectURL(event.raw);
      } else if (window.URL != undefined) {
        URL = window.URL.createObjectURL(event.raw);
      } else if (window.webkitURL != undefined) {
        URL = window.webkitURL.createObjectURL(event.raw);
      }
      this.localUrl = URL;
      this.isShowImgUpload = true;
      this.isShowUpload = false;
      this.form.pic = event.raw;
    },
  },
};
</script>

<style lang='scss'>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
