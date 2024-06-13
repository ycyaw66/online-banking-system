<template>
  <div class="page-container">
    <div class="reminder-form">
      <el-form ref="reminderForm" :model="reminderData" label-position="top">
        <el-form-item label="提醒时间">
          <el-select v-model="reminderData.time" placeholder="请选择提醒时间">
            <el-option label="一天" value="1"></el-option>
            <el-option label="一周" value="7"></el-option>
            <el-option label="一月" value="30"></el-option>
            <el-option label="两月" value="60"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveReminder">保存设置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="loan-history-table">
      <el-table :data="loanHistoryList" border style="width: 100%">
        <el-table-column prop="loan_id" label="贷款ID"></el-table-column>
        <el-table-column prop="card_id" label="银行卡号"></el-table-column>
        <el-table-column prop="amount" label="贷款金额"></el-table-column>
        <el-table-column prop="rate" label="利率"></el-table-column>
        <el-table-column prop="term" label="贷款期限"></el-table-column>
        <el-table-column prop="status" label="贷款状态"></el-table-column>
        <el-table-column prop="date_applied" label="申请日期"></el-table-column>
        <el-table-column prop="date_approved" label="批准日期"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>

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
  data() {
    return {
      reminderData: {
        time: ''
      },
      loanHistoryList: [] // 贷款历史记录列表
    };
  },
  methods: {
    saveReminder() {
      if (!this.reminderData.time) {
        this.$message.error('请选择提醒时间');
        return;
      }

      axiosInstance.post('/save-reminder', this.reminderData)
          .then(response => {
            if(response.data.code === 0){
              this.$message.success('提醒设置已保存');
              this.queryLoanHistory(this.reminderData.time);
            }
            this.getReminder();
          })
          .catch(error => {
            console.error('保存提醒设置失败:', error);
            this.$message.error('保存提醒设置失败');
          });
    },
    queryLoanHistory(time) {
      axiosInstance.get(`/loanget-reminder?time=${time}`)
          .then(response => {
            this.loanHistoryList = response.data;
          })
          .catch(error => {
            console.error('查询贷款历史失败:', error);
          });
    },
    getReminder() {
      axiosInstance.get('/timeget-reminder')
          .then(response => {
            this.reminderData.time = response.data;
            this.queryLoanHistory(this.reminderData.time);
          })
          .catch(error => {
            console.error('获取提醒时间失败:', error);
          });
    }
  },
  mounted() {
    // 页面加载时获取提醒设置并查询贷款历史记录
    this.getReminder();
  }
};
</script>
<style scoped>
.page-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  overflow-y: auto; /* 增加纵向滚动 */
  max-height: 100vh; /* 设置最大高度 */
}

.reminder-form {
  margin-bottom: 20px;
}

.loan-history-table {
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 50px; /* 添加空白区域 */
}
</style>