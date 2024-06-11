<template>
  <div class="approval-history-page">
    <div class="approval-list-container">
      <el-table :data="approvals" style="width: 100%" class="approval-table" header-align="center" align="center">
        <el-table-column prop="loan_id" label="贷款编号" width="180"></el-table-column>
        <el-table-column prop="borrow_id" label="借款人编号" width="180"></el-table-column>
        <el-table-column prop="amount" label="贷款金额" width="180"></el-table-column>
        <el-table-column prop="date_applied" label="申请日期" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewApprovalDetails(scope.row)" class="view-button">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background
                     layout="prev, pager, next, total"
                     :total="totalApprovals"
                     :page-size="pageSize"
                     @current-change="handlePageChange"
                     :current-page="currentPage">
      </el-pagination>
    </div>
    <el-dialog v-model="dialogVisible" title="审批详情" width="600px" :modal-append-to-body="false">
      <div class="dialog-content">
        <el-form :model="selectedApproval" label-position="top" label-width="120px" class="approval-form">
          <el-form-item label="贷款编号">
            <el-input v-model="selectedApproval.loan_id" disabled/>
          </el-form-item>
          <el-form-item label="借款人编号">
            <el-input v-model="selectedApproval.borrow_id" disabled/>
          </el-form-item>
          <el-form-item label="转入卡编号">
            <el-input v-model="selectedApproval.card_id" disabled/>
          </el-form-item>
          <el-form-item label="审查员编号">
            <el-input v-model="selectedApproval.officer_id" disabled/>
          </el-form-item>
          <el-form-item label="贷款金额">
            <el-input v-model="selectedApproval.amount" disabled/>
          </el-form-item>
          <el-form-item label="贷款利率">
            <el-input v-model="selectedApproval.rate" disabled/>
          </el-form-item>
          <el-form-item label="贷款期限">
            <el-input v-model="selectedApproval.term" disabled/>
          </el-form-item>
          <el-form-item label="贷款状态">
            <el-input v-model="selectedApproval.status" disabled/>
          </el-form-item>
          <el-form-item label="申请日期">
            <el-input v-model="selectedApproval.date_applied" disabled/>
          </el-form-item>
          <el-form-item label="审批日期">
            <el-input v-model="selectedApproval.date_approved" disabled/>
          </el-form-item>
          <el-form-item label="表单编号">
            <el-input v-model="selectedApproval.form_id" disabled/>
          </el-form-item>
        </el-form>
      </div>
      <template v-slot:footer>
    <span class="dialog-footer">
        <el-button @click="dialogVisible = false" type="primary">关闭</el-button>
    </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {ref} from 'vue';
import {ElTable, ElTableColumn, ElDialog, ElButton, ElForm, ElFormItem, ElInput, ElMessage} from 'element-plus';
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
    ElTable, ElTableColumn, ElDialog, ElButton, ElForm, ElFormItem, ElInput
  },
  setup() {
    const approvals = ref([]);

    const dialogVisible = ref(false);
    const selectedApproval = ref({});

    const viewApprovalDetails = (approval) => {
      selectedApproval.value = approval;
      dialogVisible.value = true;
    };

    return {
      approvals,
      dialogVisible,
      selectedApproval,
      viewApprovalDetails
    };
  },
  data() {
    return {
      approvalHistory: [],
      totalApprovals: 0,
      pageSize: 10,
      currentPage: 1
    };
  },
  mounted() {
    this.fetchApprovals();
  },
  methods: {
    async fetchApprovals() {
      try {
        const response = await axiosInstance.get('/get-approvals', {
          params: {
            page: this.currentPage,
            pageSize: this.pageSize
          }
        });
        this.approvals = response.data.records;
        this.totalApprovals = response.data.total;
        // Log the total number of approvals
        console.log('Total Approvals:', this.totalApprovals, this.approvalHistory);
      } catch (error) {
        console.error('获取审批历史时发生错误:', error);
        ElMessage.error('无法加载审批历史。');
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchOfficers();
    }
  }
};
</script>

<style scoped>
.approval-history-page {
  padding: 20px;
  background-color: #f9f9f9;
}

.approval-list-container {
  max-height: calc(100vh - 150px);
  overflow-y: auto;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.approval-table .el-table__header-wrapper {
  background-color: #409eff;
  color: #fff;
  font-weight: bold;
  font-size: 16px;
}

.approval-table .el-table__body-wrapper .el-table__row {
  font-size: 18px;
  font-weight: bold;
  height: 60px;
  border-bottom: 1px solid #ebeef5;
}

.approval-table .el-table__body-wrapper .el-table__row:hover {
  background-color: #f2f6fc;
}

.approval-table .el-table__cell {
  padding: 10px;
  font-weight: bold;
  color: #303133;
  text-align: center; /* 确保内容居中 */
}

.approval-table .el-table__header {
  text-align: center; /* 确保表头居中 */
}

.view-button {
  background-color: #67c23a;
  border-color: #67c23a;
  color: #fff;
}

.loan-dialog .el-dialog__header {
  background-color: #409eff;
  color: #fff;
}

.loan-dialog .dialog-content {
  max-height: 400px;
  overflow-y: auto;
}

.loan-dialog .el-dialog__body {
  padding: 0 20px;
}

.loan-dialog .el-dialog__footer {
  background-color: #f9f9f9;
  text-align: right;
  padding: 10px 20px;
}

.dialog-footer {
  text-align: right;
}

.loan-form .el-form-item__label {
  color: #409eff;
}
</style>
