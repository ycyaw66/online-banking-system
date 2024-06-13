<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div style="text-align: center;">开设账户</div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="name" style="width: 250px" placeholder="输入您的名字"/>
      </div>
      <br>
      <div>
        <span>身份证号：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="ID_number" style="width: 250px" placeholder="输入您的身份证号"/>
      </div>
      <br>
      <div>
        <span>电话号码：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="phone_number" style="width: 250px" placeholder="输入您的电话号码"/>
      </div>
      <br>
      <el-form-item label="账户类型：" :label-width="formLabelWidth">
        <el-select v-model="account_type" style="width: 250px">
          <el-option label="存折" value='Book'></el-option>
          <el-option label="卡片" value='Card'></el-option>
        </el-select>
      </el-form-item>
      <div>
        <span>设置密码：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="password" style="width: 250px" type="password" placeholder="输入您的密码"/>
      </div>
      <br>
      <div>
        <span>确认密码：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="password_again" style="width: 250px" type="password" placeholder="确认您的密码"/>
      </div>
      <br>
      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="openAccount">确认开户</el-button>
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
      name: '',
      ID_number: '',
      phone_number: '',
      account_type: '',
      password: '',
      password_again: ''
    }
  },
  methods: {
    exit() {
      this.$router.push('/counter/cashier/login');
    },
    openAccount() {
      if (this.name === '') {
        this.$message.error('姓名不能为空');
        return;
      }
      if (this.ID_number === '') {
        this.$message.error('身份证号不能为空');
        return;
      }
      if (this.phone_number === '') {
        this.$message.error('电话号码不能为空');
        return;
      }
      if (this.account_type === '') {
        this.$message.error('账户类型未选择');
        return;
      }
      if (this.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      if (this.password_again === '') {
        this.$message.error('确认密码不能为空');
        return;
      }

      if (this.ID_number.length !== 18) {
        this.$message.error('身份证号码必须为18位');
        return;
      }
      if (this.phone_number.length !== 11) {
        this.$message.error('手机号码必须为11位');
        return;
      }

      axiosInstance.post("/account/counter/account/add", null, {
        params: {
          name: this.name,
          phoneNumber: this.phone_number,
          password: CryptoJS.SHA256(this.password).toString(),
          cardType: this.account_type,
          citizenid: this.ID_number,
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$alert("开户成功，您的账户卡号为： " + response.data.payload.card_id);
        }
      })
      this.name = '';
      this.ID_number = '';
      this.phone_number = '';
      this.account_type = '';
      this.password = '';
      this.password_again = '';
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