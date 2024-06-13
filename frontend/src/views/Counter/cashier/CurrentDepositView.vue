<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div style="text-align: center;">活期存款</div>
      <br>
      <div>
        <span>卡号：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="id" style="width: 250px" placeholder="输入您的存款卡号"/>
      </div>
      <br>
      <div>
        <span>金额：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="amount" style="width: 250px" placeholder="输入您的存款金额（单位为元）"/>
      </div>
      <br>
      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="deposit">确认存款</el-button>
      </div>
      <br>
    </div>
  </el-main>
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
      id: '',
      amount: ''
    }
  },
  methods: {
    exit() {
      this.$router.push('/counter/cashier/login');
    },
    deposit() {
      // 检查信用卡编号、日期、密码、支付金额是否为空
      if (this.id === '') {
        this.$message.error('存款账户不能为空');
        return;
      }
      if (this.amount === '' || this.amount === '0') {
        this.$message.error('存款金额不能为空或为0');
        return;
      }

      if (isNaN(this.amount) || this.amount === '') {
        this.$message.error('输入的存款金额不是一个有效的数字');
        return;
      } else {
        var money_10 = this.amount * 100;
        var isInt = money_10 % 1 === 0;
        if (!isInt) {
          this.$message.error('存款金额小数部分最多两位');
          return;
        }
      }

      axiosInstance.post("/dp/counter/dp/save", null, {
        params: {
          id: this.id,
          amount: this.amount,
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success('活期存款成功');
        }
      })
      this.id = "";
      this.amount = "";
    },
  },
  mounted() {
  }
}
</script>

<style scoped>
.el-menu-item > a {
  color: inherit;
  text-decoration: none !important;
}

.el-aside,
.el-menu,
.el-sub-menu {
  background-color: rgb(47, 109, 185) !important;
}

.el-menu-item > a.is-active {
  color: inherit;
  text-decoration: none !important;
}
</style>