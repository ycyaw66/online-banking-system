<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <br>
      <br>
      <div style="text-align: center;">活期取款</div>
      <br>
      <br>
      <div style="text-align: center;">
        <div style="display: inline-block;">
          <div>
            <span>卡号：</span>
            <el-input v-model="id" style="width: 250px;" placeholder="输入您的取款卡号"/>
          </div>
          <div>
            <span>密码：</span>
            <el-input v-model="password" type="password" style="width: 250px;" placeholder="输入您的密码"/>
          </div>
          <br>
          <div class="mb-4" style="text-align: center;">
            <el-button type="primary" round @click="find_current_account">查询余额</el-button>
          </div>
        </div>
      </div>
      <br>
      <div style="display: flex; justify-content: center;">
                <span>您的活期存款余额为:&nbsp;
                  {{ amount }} &nbsp;元
                </span>
      </div>
      <br>
      <br>
      <br>
      <div style="text-align: center;">
        <div style="display: inline-block;">
          <div>
            <span>取款金额:&nbsp;&nbsp;&nbsp;</span>
            <el-input v-model="withdraw_amount" style="width: 250px;" placeholder="输入您的取款金额（单位为元）"/>
          </div>
          <br>
          <div class="mb-4" style="text-align: center;">
            <el-button type="primary" round @click="currentWithdraw">取款</el-button>
          </div>
        </div>
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
      id: '',
      password: '',
      amount: '0',
      withdraw_amount: '',
    }
  },
  methods: {
    exit() {
      this.$router.push('/counter/cashier/login');
    },
    find_current_account() {
      if (this.id === '') {
        this.$message.error('账户ID不能为空');
        return;
      }
      if (this.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      axiosInstance.get("/dp/counter/dp/show", {
        params: {
          id: this.id,
          password: CryptoJS.SHA256(this.password).toString(),
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.amount = response.data.payload
        }
      })
    },
    currentWithdraw() {
      if (this.withdraw_amount === '' || this.withdraw_amount === '0') {
        this.$message.error('取款金额不能为空或为0');
        return;
      }

      if (isNaN(this.withdraw_amount) || this.withdraw_amount === '') {
        // 如果不是数字，就弹出警告并返回，不继续执行函数
        this.$message.error('请输入数字作为取款金额！');
        return;
      }
      var limit = this.withdraw_amount * 100;
      var isInt = limit % 1 === 0;
      if (!isInt) {
        this.$message.error('取款金额的小数部分最多两位');
        return;
      }

      axiosInstance.post("/dp/counter/dp/draw", null, {
        params: {
          id: this.id,
          password: CryptoJS.SHA256(this.password).toString(),
          amount: this.withdraw_amount,
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success("成功取款：" + this.withdraw_amount + " 元");
          this.find_current_account();
          this.password = "";
        }
      })
      this.withdraw_amount = "";
    }
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