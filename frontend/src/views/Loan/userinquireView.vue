<template>
  <div class="page-container">
    <div class="repayment-form">
      <el-form ref="loanHistoryForm" :model="loanHistoryData" label-position="top">
        <el-form-item label="贷款金额">
          <el-input v-model="loanHistoryData.amount" placeholder="请输入贷款金额" />
        </el-form-item>
        <el-form-item label="贷款时间">
          <el-date-picker
              v-model="loanHistoryData.startDate"
              type="date"
              placeholder="开始日期"
              value-format="yyyy-MM-dd"
          ></el-date-picker>
          <el-date-picker
              v-model="loanHistoryData.endDate"
              type="date"
              placeholder="结束日期"
              value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="贷款状态">
          <el-select v-model="loanHistoryData.status" placeholder="请选择贷款状态">
            <el-option label="全部" value=""></el-option>
            <el-option label="申请中" value="application"></el-option>
            <el-option label="已拒绝" value="declined"></el-option>
            <el-option label="还款中" value="repayment"></el-option>
            <el-option label="已结清" value="settled"></el-option>
            <el-option label="逾期" value="overdue"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="利率">
          <el-input v-model="loanHistoryData.rate" placeholder="请输入利率" />
        </el-form-item>
        <el-form-item label="排序条件">
          <el-select v-model="loanHistoryData.sortCondition" placeholder="请选择排序条件">
            <el-option label="无" value=""></el-option>
            <el-option label="贷款金额" value="amount"></el-option>
            <el-option label="贷款时间" value="date_applied"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryLoanHistory(1)">查询贷款历史</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="loan-history-table">
      <el-table :data="loanHistoryList" border style="width: 100%">
        <el-table-column prop="card_id" label="银行卡号"></el-table-column>
        <el-table-column prop="amount" label="贷款金额"></el-table-column>
        <el-table-column prop="rate" label="利率"></el-table-column>
        <el-table-column prop="term" label="贷款期限"></el-table-column>
        <el-table-column prop="status" label="贷款状态"></el-table-column>
        <el-table-column prop="date_applied" label="申请日期"></el-table-column>
        <el-table-column prop="date_approved" label="批准日期"></el-table-column>
      </el-table>
      <el-pagination
          background
          layout="prev, pager, next, jumper"
          :total="totalLoans"
          :page-size="pageSize"
          v-model:current-page="currentPage"
          @current-change="handlePageChange"
      ></el-pagination>
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
      loanHistoryData: {
        amount: '',
        startDate: '',
        endDate: '',
        status: '',
        rate: '',
        sortCondition: ''
      },
      loanHistoryList: [], // 贷款历史记录列表
      totalLoans: 0, // 总记录数
      currentPage: 1, // 当前页码
      pageSize: 10 // 每页记录数
    };
  },
  methods: {
    queryLoanHistory(page = 1) {
      // 创建一个新的对象来存储有效的筛选条件
      const validFilters = {};
      if (this.loanHistoryData.amount) validFilters.amount = this.loanHistoryData.amount;
      if (this.loanHistoryData.startDate) validFilters.startDate = this.loanHistoryData.startDate;
      if (this.loanHistoryData.endDate) validFilters.endDate = this.loanHistoryData.endDate;
      if (this.loanHistoryData.status !== '') validFilters.status = this.loanHistoryData.status;
      if (this.loanHistoryData.rate) validFilters.rate = this.loanHistoryData.rate;
      if (this.loanHistoryData.sortCondition !== '') validFilters.sortCondition = this.loanHistoryData.sortCondition;

      axiosInstance.post(`/loan-history?page=${page}&size=${this.pageSize}`, validFilters)
          .then(response => {
            // 请求成功，将后端返回的贷款历史记录数据赋值给 loanHistoryList
            this.loanHistoryList = response.data.loans;
            this.totalLoans = response.data.total;
            this.currentPage = page; // 更新当前页码
          })
          .catch(error => {
            console.error('查询贷款历史失败:', error);
          });
    },
    handlePageChange(page) {
      this.queryLoanHistory(page);
    }
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

.repayment-form {
  margin-bottom: 20px;
}

.loan-history-table {
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 50px; /* 添加空白区域 */
}

/* 添加额外的空白区域 */
.empty-space {
  height: 50px; /* 设置空白区域的高度 */
}
</style>