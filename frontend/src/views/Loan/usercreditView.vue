<template>
    <div class="page-container">
      <div class="submit-button-container">
        <el-button type="primary" @click="fetchCreditInfo">查询征信</el-button>
      </div>
      <div class="credit-info-form">
        <div v-if="creditInfo" class="credit-info-display">
          <el-form-item label="信用评分(Credit Score)">
            <el-input v-model="creditInfo.creditScore" disabled />
          </el-form-item>
          <el-form-item label="最低限额(Minimum Limit)">
            <el-input v-model="creditInfo.minimumLimit" disabled />
          </el-form-item>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { ElFormItem, ElInput, ElButton } from 'element-plus';
  import axios from "axios";
  import Cookies from "js-cookie";

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
      ElFormItem, ElInput, ElButton
    },
    data() {
      return {
        creditInfo: 1
      };
    },
    methods: {
      async fetchCreditInfo() {
        try {
          const response = await axiosInstance.post('/fetch-credit-info');
          this.creditInfo = response.data;
          this.$message.success('征信查询成功！');
        } catch (error) {
          console.error('查询征信时发生错误:', error);
          this.$message.error('查询失败。');
        }
      }
    },
    mounted() {
      this.fetchCreditInfo(); // 页面加载时自动查询征信信息
    }
  }
  </script>
  
  <style scoped>
  .page-container {
    position: relative;
    max-width: 600px;
    margin: 20px auto;
    padding: 20px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 15px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    box-sizing: border-box;
  }
  
  .credit-info-form {
    padding: 20px;
    max-height: calc(100vh - 100px); /* 设置最大高度 */
    overflow-y: auto; /* 添加垂直滚动条 */
  }
  
  .credit-info-display {
    margin-top: 20px;
  }
  
  .submit-button-container {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 1000;
  }
  
  .el-form-item {
    margin-bottom: 30px;
  }
  
  .el-input {
    width: 100%;
  }
  
  .el-button {
    width: 150px;
  }
  </style>
  