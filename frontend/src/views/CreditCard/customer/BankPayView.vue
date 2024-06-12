<template>
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">

    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div>
        <span>信用卡编号：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="credit_card_id" style="width: 250px" placeholder="输入您用于支付的信用卡id"/>
      </div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;支付金额：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="account" style="width: 250px" placeholder="输入您的支付金额"/>
      </div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;付款日期：&nbsp;&nbsp;&nbsp;</span>
        <el-date-picker v-model="date" style="width: 250px" type="date" placeholder="付款日期"/>
      </div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;支付密码：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="password" type="password" style="width: 250px" placeholder="输入您支付的信用卡密码" show-password/>
      </div>
      <br>
      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="pay">确认支付</el-button>
      </div>

      <br>
    </div>

  </el-main>
</template>

<script>

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
  data() {
    return {
      credit_card_id: '',
      account: '',
      date: '',
      password: '',
    }
  },
  methods: {
    exit() {
      this.$router.push('/personalBank/user/account');
    },
    pay() {
      // 检查信用卡编号、日期、密码、支付金额是否为空
      if (this.credit_card_id === '') {
        this.$message.error('信用卡编号不能为空');
        return;
      }
      if (this.date === '') {
        this.$message.error('付款日期不能为空');
        return;
      }
      if (this.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      if (this.account === '') {
        this.$message.error('支付金额不能为空');
        return;
      }

      let numAccount = Number(this.account);
      if (isNaN(numAccount)) {
        this.$message.error('输入的支付金额不是一个有效的数字');
      } else {
        var account = numAccount * 100;
        var isInt = account % 1 === 0;
        if (!isInt) {
          this.$message.error('支付金额小数部分最多两位');
        }
        //this.$message.success('支付成功，共支付 ' + account + ' 元');
      }

      const encrypted_password = CryptoJS.SHA256(this.password).toString();

      axiosInstance.post("/credit-card/pay", null, {
        params: {
          card_id: this.credit_card_id,
          amount: this.account,
          date: this.date,
          password: encrypted_password
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success('支付成功');
        }
      }).catch(error => {
        console.error('BankPay error:', error);
        this.$message.error('支付失败');
      })
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