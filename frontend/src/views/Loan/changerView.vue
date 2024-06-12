<template>
    <div class="change-password-page">
      <el-form ref="passwordForm" :model="passwordData" :rules="rules" label-position="top" @submit.prevent="changePassword">
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input v-model="passwordData.currentPassword" type="password" placeholder="请输入当前密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordData.newPassword" type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="changePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
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
          currentPassword: '',
          newPassword: ''
        },
        rules: {
          currentPassword: [
            { required: true, message: '请输入旧密码', trigger: 'blur' }
          ],
          newPassword: [
            { required: true, message: '请输入新密码', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      async changePassword() {
        this.$refs.passwordForm.validate(async (valid) => {
          if (valid) {
            try {
              this.passwordData.currentPassword = CryptoJS.SHA256(this.passwordData.currentPassword).toString();
              this.passwordData.newPassword = CryptoJS.SHA256(this.passwordData.newPassword).toString();
              await axiosInstance.put('/officer-main/update-officer-password-by-officer', null, { params: this.passwordData });
              this.$message.success('密码修改成功！');
            } catch (error) {
              console.error('修改密码时发生错误:', error);
              if (error.response && error.response.status === 404) {
                this.$message.error('旧密码错误。');
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
  .change-password-page {
    padding: 20px;
    background-color: #f9f9f9;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }
  
  .password-form {
    width: 400px;
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  </style>
  