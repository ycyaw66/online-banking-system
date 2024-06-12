<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div style="text-align: center;">转账</div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账户：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="account_id" style="width: 250px" placeholder="输入您的发起转账账户"/>
      </div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密码：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="password" style="width: 250px" type="password" placeholder="输入您的密码"/>
      </div>
      <br>
      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="find_current_account">查询余额</el-button>
      </div>
      <br>
      <div style="display: flex; justify-content: center;">
                <span>您的活期存款余额为:&nbsp;
                  {{ the_amount }} &nbsp;元
                </span>
      </div>
      <br>
      <br>
      <div>
        <span>目标账户：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="target_account_id" style="width: 250px" placeholder="输入您的目标转账账户"/>
      </div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;金额：&nbsp;&nbsp;</span>
        <el-input v-model="money" style="width: 250px" placeholder="输入您的转账金额（单位为元）"/>
      </div>
      <br>
      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="transfer">确认转账</el-button>
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
      account_id: '',
      password: '',
      the_amount: '0',
      money: '',
      target_account_id: ''
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
          id: this.account_id,
          password: CryptoJS.SHA256(this.password).toString(),
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.the_amount = response.data.payload
        }
      })
    },
    transfer() {
      // 检查卡号、密码、转账金额是否为空
      if (this.account_id === '' || this.target_account_id === '') {
        this.$message.error('账户ID不能为空');
        return;
      }
      if (this.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      if (this.money === '' || this.money === '0') {
        this.$message.error('金额不能为空或为0');
        return;
      }
      if (this.account_id === this.target_account_id) {
        this.$message.error('目标账户不可为原账户');
        return;
      }

      if (isNaN(this.money)) {
        this.$message.error('输入的金额不是一个有效的数字');
      } else {
        var money_10 = this.money * 100;
        var isInt = money_10 % 1 === 0;
        if (!isInt) {
          this.$message.error('金额小数部分最多两位');
          return;
        }
      }
      axiosInstance.post("/transfer/counter/transfer", null, {
        params: {
          id: this.account_id,
          password: CryptoJS.SHA256(this.password).toString(),
          toid: this.target_account_id,
          amount: this.money,
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success('转账成功');
          this.find_current_account();
          this.password = "";
          this.target_account_id = "";
          this.money = "";
        }
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