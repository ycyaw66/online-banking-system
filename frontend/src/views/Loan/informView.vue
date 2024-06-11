<template>
  <div class="repayment-reminder-page">
    <el-form :model="reminderSettings" label-width="120px" class="reminder-form">
      <el-form-item label="选择提醒时间">
        <el-select v-model="reminderSettings.timeBefore" placeholder="请选择提醒时间">
          <el-option label="1天" value="1_day"></el-option>
          <el-option label="1周" value="1_week"></el-option>
          <el-option label="1个月" value="1_month"></el-option>
          <el-option label="2个月" value="2_months"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
      </el-form-item>
    </el-form>
    <el-dialog v-model="reminderDialogVisible" title="还款提醒" width="400px">
      <p>您的贷款ID: {{ upcomingLoanId }} 将于 {{ upcomingLoanDate }} 到期，请及时归还。</p>
      <template v-slot:footer>
    <span class="dialog-footer">
        <el-button @click="reminderDialogVisible = false" type="primary">关闭</el-button>
    </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {ElForm, ElFormItem, ElButton, ElSelect, ElOption, ElDialog, ElMessage} from 'element-plus';
import axios from 'axios';
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
    ElForm, ElFormItem, ElButton, ElSelect, ElOption, ElDialog
  },
  setup() {
    const reminderSettings = ref({
      timeBefore: '1_day'
    });

    const reminderDialogVisible = ref(false);
    const upcomingLoanId = ref('');
    const upcomingLoanDate = ref('');

    const saveSettings = async () => {
      try {
        // Convert reminder time string to days
        let timeBeforeInDays = 1; // Default value
        switch (reminderSettings.value.timeBefore) {
          case '1_day':
            timeBeforeInDays = 1;
            break;
          case '1_week':
            timeBeforeInDays = 7;
            break;
          case '1_month':
            timeBeforeInDays = 30;
            break;
          case '2_months':
            timeBeforeInDays = 60;
            break;
          default:
            timeBeforeInDays = 1;
        }

        // Send the converted days to the backend
        await axiosInstance.post('/save-reminder', {timeBefore: timeBeforeInDays});

        // Check reminders after saving settings
        checkReminders();
      } catch (error) {
        console.error('保存设置时发生错误:', error);
        ElMessage.error('保存设置失败。');
      }
    };

    const checkReminders = async () => {
      try {
        const response = await axiosInstance.get('/get-reminder');
        const reminderTime = response.data.reminderTime;
        const loans = response.data.loans;
        const now = new Date();

        for (const loan of loans) {
          const dueDate = new Date(loan.due_date);
          const reminderDate = new Date(dueDate);
          reminderDate.setDate(dueDate.getDate() - reminderTime);

          if (now >= reminderDate && now <= dueDate) {
            upcomingLoanId.value = loan.loan_id;
            upcomingLoanDate.value = loan.due_date;
            reminderDialogVisible.value = true;
            break;
          }
        }
      } catch (error) {
        console.error('检查提醒时发生错误:', error);
        ElMessage.error('检查提醒失败。');
      }
    };

    onMounted(() => {
      checkReminders();
    });

    return {
      reminderSettings,
      reminderDialogVisible,
      upcomingLoanId,
      upcomingLoanDate,
      saveSettings
    };
  }
};
</script>
<style scoped>
.repayment-reminder-page {
  padding: 20px;
  background-color: #f9f9f9;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.reminder-form {
  width: 300px;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
