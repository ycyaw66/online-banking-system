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
        <!-- 排序条件 -->
        <el-form-item label="排序条件">
          <el-select v-model="loanHistoryData.sortCondition" placeholder="请选择排序条件">
            <el-option label="贷款金额" value="amount"></el-option>
            <el-option label="贷款时间" value="date"></el-option>
            <!-- 其他排序条件选项 -->
          </el-select>
        </el-form-item>
        <!-- 查询按钮 -->
        <el-form-item>
          <el-button type="primary" @click="queryLoanHistory">查询贷款历史</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 贷款历史记录表格 -->
    <div class="loan-history-table">
      <el-table :data="loanHistoryList" border style="width: 100%">
        <el-table-column prop="loanId" label="贷款ID"></el-table-column>
        <el-table-column prop="amount" label="贷款金额"></el-table-column>
        <el-table-column prop="date" label="贷款时间"></el-table-column>
        <el-table-column prop="status" label="贷款状态"></el-table-column>
        <el-table-column prop="rate" label="利率"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
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
      }
    };
  },
  methods: {
    queryLoanHistory() {
      // 处理查询贷款历史逻辑
    }
  }
};
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
}

.repayment-form {
  padding: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>
