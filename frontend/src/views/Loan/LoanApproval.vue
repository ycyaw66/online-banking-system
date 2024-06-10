<template>
  <div class="loan-query-page">
    <div class="loan-list-container">
      <el-table :data="loans" style="width: 100%" class="loan-table" header-align="center" align="center">
        <el-table-column prop="loan_id" label="贷款编号" width="180"></el-table-column>
        <el-table-column prop="amount" label="贷款金额" width="180"></el-table-column>
        <el-table-column prop="user_name" label="贷款人" width="180"></el-table-column>
        <el-table-column prop="date_applied" label="贷款时间" width="180"></el-table-column>
        <el-table-column label="操作" width="450">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewLoanDetails(scope.row)" class="view-button">查看详情
            </el-button>
            <el-button type="success" size="small" @click="openApprovalDialog(scope.row)" class="approve-button">审批
            </el-button>
            <el-button type="info" size="small" @click="viewCreditReport(scope.row)" class="credit-button">查看征信
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background
                     layout="prev, pager, next, total"
                     :total="totalLoans"
                     :page-size="pageSize"
                     @current-change="handlePageChange"
                     :current-page="currentPage">
      </el-pagination>
    </div>

    <!-- 贷款详情对话框 -->
    <el-dialog v-model="dialogVisible" title="贷款详情" width="600px" :modal-append-to-body="false">
      <div class="dialog-content">
        <el-form :model="selectedLoan" label-position="top" label-width="120px" class="loan-form">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名(Name)">
                <el-input v-model="selectedLoan.user_name" disabled/>
              </el-form-item>
              <el-form-item label="身份证号(ID Number)">
                <el-input v-model="selectedLoan.id_number" disabled/>
              </el-form-item>
              <el-form-item label="贷款日期(Date of Apply)">
                <el-date-picker v-model="selectedLoan.date_applied" type="date" placeholder="选择日期" disabled/>
              </el-form-item>
              <el-form-item label="性别(Gender)">
                <el-select v-model="selectedLoan.gender" placeholder="请选择性别" disabled>
                  <el-option label="男" value="male"></el-option>
                  <el-option label="女" value="female"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="婚姻情况(Marital Status)">
                <el-select v-model="selectedLoan.emotion" placeholder="请选择婚姻情况" disabled>
                  <el-option label="已婚" value="married"></el-option>
                  <el-option label="未婚" value="single"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="贷款金额(Loan Amount)">
                <el-input v-model="selectedLoan.amount" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="住宅地址(Residential Address)">
                <el-input v-model="selectedLoan.address" disabled/>
              </el-form-item>
              <el-form-item label="电话号码(Phone Number)">
                <el-input v-model="selectedLoan.phone_number" disabled/>
              </el-form-item>
              <el-form-item label="电子邮件地址(Email Address)">
                <el-input v-model="selectedLoan.email" disabled/>
              </el-form-item>
              <el-form-item label="教育背景(Education Background)">
                <el-input v-model="selectedLoan.education" disabled/>
              </el-form-item>
              <el-form-item label="贷款用途(Loan Purpose)">
                <el-input v-model="selectedLoan.purpose" disabled/>
              </el-form-item>
              <el-form-item label="贷款期限(Loan Term)">
                <el-input v-model="selectedLoan.term" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="个人情况声明(Personal Statement)">
            <el-input type="textarea" :autosize="{ minRows: 7, maxRows: 14 }" v-model="selectedLoan.statement"
                      disabled/>
          </el-form-item>
        </el-form>
      </div>
      <template v-slot:footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 贷款审批对话框 -->
    <el-dialog v-model="approvalDialogVisible" title="贷款审批" width="400px" :modal-append-to-body="false">
      <p>是否同意此贷款申请？</p>
      <template v-slot:footer>
        <span class="dialog-footer">
            <el-button type="success" @click="approveLoan">同意</el-button>
            <el-button type="danger" @click="rejectLoan">拒绝</el-button>
            <el-button @click="approvalDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 征信报告对话框 -->
    <el-dialog v-model="creditDialogVisible" title="征信情况" width="600px" :modal-append-to-body="false">
      <div class="dialog-content">
        <el-form :model="selectedCredit" label-position="top" label-width="120px" class="credit-form">
          <el-form-item label="姓名(Name)">
            <el-input v-model="selectedCredit.name" disabled/>
          </el-form-item>
          <el-form-item label="信用评分(Credit Score)">
            <el-input v-model="selectedCredit.creditScore" disabled/>
          </el-form-item>
          <el-form-item label="信用额度(Credit Limit)">
            <el-input v-model="selectedCredit.creditLimit" disabled/>
          </el-form-item>
        </el-form>
      </div>
      <template v-slot:footer>
        <span class="dialog-footer">
            <el-button @click="creditDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import {ref} from 'vue';
import {
  ElTable,
  ElTableColumn,
  ElDialog,
  ElButton,
  ElForm,
  ElFormItem,
  ElInput,
  ElRow,
  ElCol,
  ElDatePicker,
  ElSelect,
  ElOption,
  ElMessage
} from 'element-plus';
import axios from 'axios';

export default {
  components: {
    ElTable,
    ElTableColumn,
    ElDialog,
    ElButton,
    ElForm,
    ElFormItem,
    ElInput,
    ElRow,
    ElCol,
    ElDatePicker,
    ElSelect,
    ElOption
  },

  setup() {
    const loans = ref([]);
    const dialogVisible = ref(false);
    const approvalDialogVisible = ref(false);
    const creditDialogVisible = ref(false);
    const selectedLoan = ref({});
    const selectedCredit = ref({});

    const currentPage = ref(1);
    const pageSize = ref(10);
    const totalLoans = ref(0);

    const fetchLoans = async () => {
      try {
        const loanResponse = await axios.get('/get-loans', {
          params: {
            page: currentPage.value,
            pageSize: pageSize.value
          }
        });

        const loansData = loanResponse.data.records;

        if (!loansData || loansData.length === 0) {
          ElMessage.warning('没有贷款申请数据。');
          return;
        }

        // 创建所有表单请求的promise数组
        const formPromises = loansData.map(async loan => {
          try {
            const formResponse = await axios.get(`/get-forms/${loan.form_id}`);
            return {
              ...loan,
              ...formResponse.data
            };
          } catch (formError) {
            console.error(`获取表单数据时发生错误: ${formError}`);
            ElMessage.error('无法加载表单数据。');
            return loan; // 返回原始贷款数据，避免丢失
          }
        });

        // 等待所有请求完成并合并数据
        const loansWithForms = await Promise.all(formPromises);

        // 打印合并后的数据进行调试
        console.log('合并后的贷款数据:', loansWithForms);

        loans.value = loansWithForms;
        console.log(loans);
        totalLoans.value = loanResponse.data.total;
      } catch (error) {
        console.error('获取贷款申请时发生错误:', error);
        ElMessage.error('无法加载贷款申请。');
      }
    };

    const viewLoanDetails = (loan) => {
      selectedLoan.value = loan;
      dialogVisible.value = true;
    };

    const openApprovalDialog = (loan) => {
      selectedLoan.value = loan;
      approvalDialogVisible.value = true;
    };

    const approveLoan = async () => {
      try {
        await axios.put(`/approve-loan/${selectedLoan.value.loan_id}`);
        fetchLoans();
        ElMessage.success('贷款申请已同意。');
        approvalDialogVisible.value = false;
      } catch (error) {
        console.error('审批时发生错误:', error);
        ElMessage.error('审批失败。');
      }
    };

    const rejectLoan = async () => {
      try {
        await axios.put(`/reject-loan/${selectedLoan.value.loan_id}`);
        fetchLoans();
        ElMessage.success('贷款申请已拒绝。');
        approvalDialogVisible.value = false;
      } catch (error) {
        console.error('拒绝申请时发生错误:', error);
        ElMessage.error('拒绝申请失败。');
      }
    };

    const viewCreditReport = async (loan) => {
      try {
        const creditResponse = await axios.get(`/get-credit/${loan.form_id}`);
        selectedCredit.value = creditResponse.data;
        creditDialogVisible.value = true;
      } catch (error) {
        console.error('查看征信报告时发生错误:', error);
        ElMessage.error('无法加载征信报告。');
      }
    };

    const handlePageChange = (page) => {
      currentPage.value = page;
      fetchLoans();
    };


    return {
      loans,
      dialogVisible,
      approvalDialogVisible,
      creditDialogVisible,
      selectedLoan,
      selectedCredit,
      fetchLoans,
      viewLoanDetails,
      openApprovalDialog,
      approveLoan,
      rejectLoan,
      viewCreditReport,
      handlePageChange,
      currentPage,
      pageSize,
      totalLoans
    };
  },

  mounted() {
    this.fetchLoans();
  }
};
</script>
<style scoped>
.loan-query-page {
  padding: 20px;
}

.loan-list-container {
  margin-bottom: 20px;
}

.dialog-content {
  padding: 20px;
}

.dialog-footer {
  text-align: right;
}

.loan-form, .credit-form {
  margin-top: 10px;
}

.view-button {
  margin-right: 10px;
}

.approve-button {
  margin-right: 10px;
}

.credit-button {
  margin-right: 10px;
}
</style>
