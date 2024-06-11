<template>
  <div class="page-container">
    <div class="credit-report-form">
      <el-form ref="creditReportForm" :model="creditReportData" label-position="top">
        <el-form-item label="信用评分 (Credit Score)">
          <el-input v-model="creditReportData.creditScore" placeholder="请输入信用评分" disabled />
        </el-form-item>
        <el-form-item label="最低限额 (Minimum Limit)">
          <el-input v-model="creditReportData.creditLimit" placeholder="请输入最低限额" disabled />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="generateCreditReport">生成信用报告 (Generate Report)</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="credit-report-result" v-if="creditReportData.creditScore !== '' && creditReportData.creditLimit !== ''">
      <p>信用评分: {{ creditReportData.creditScore }}</p>
      <p>信用额度: {{ creditReportData.creditLimit }}</p>
    </div>
  </div>
</template>

<script>
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
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
    ElForm, ElFormItem, ElInput, ElButton
  },
  data() {
    return {
      creditReportData: {
        creditScore: '',
        creditLimit: ''
      }
    };
  },
  methods: {
    async generateCreditReport() {
      try {
        const response = await axiosInstance.post('/generate-credit-report');
        this.creditReportData.creditScore = response.data.creditScore;
        this.creditReportData.creditLimit = response.data.creditLimit;
        this.$message.success('信用报告生成成功！');
      } catch (error) {
        console.error('生成信用报告时发生错误:', error);
        this.$message.error('生成信用报告失败。');
      }
    }
  }
}
</script>

<style scoped>
.page-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 15px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  box-sizing: border-box;
}

.credit-report-form {
  padding: 20px;
}

.credit-report-result {
  margin-top: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border: 1px solid #eee;
  border-radius: 10px;
}
</style>