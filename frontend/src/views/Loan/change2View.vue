<template>
  <div class="page-container">
    <el-form ref="passwordForm" :model="passwordData" :rules="rules" label-position="top" @submit.prevent="submitForm">
      <el-form-item label="账户名" prop="username">
        <el-input v-model="passwordData.username" placeholder="请输入账户名"/>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="passwordData.newPassword" type="password" placeholder="请输入新密码"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {ElForm, ElFormItem, ElInput, ElButton} from 'element-plus';
import axios from "axios";
import Cookies from "js-cookie";
import CryptoJS from "crypto-js";

const axiosInstance = axios.create();
axiosInstance.interceptors.request.use(config => {
  const token = Cookies.get('token');
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

export default {
  components: {
    ElForm, ElFormItem, ElInput, ElButton
  },
  data() {
    return {
      passwordData: {
        username: '',
        newPassword: ''
      },
      rules: {
        username: [
          {required: true, message: '请输入账户名', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'}
        ]
      }
    };
  },
  methods: {
    async submitForm() {
      this.$refs.passwordForm.validate(async (valid) => {
        if (valid) {
          try {
            this.passwordData.newPassword = CryptoJS.SHA256(this.passwordData.newPassword).toString();
            await axiosInstance.put('/admin/update-officer-password', null, {params: this.passwordData});
            this.$message.success('密码修改成功！');
          } catch (error) {
            console.error('修改密码时发生错误:', error);
            if (error.response && error.response.status === 404) {
              this.$message.error('未找到该账户名。');
            } else {
              this.$message.error('修改失败。');
            }
          }
        } else {
          this.$message.error('请完整填写表单');
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.page-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
