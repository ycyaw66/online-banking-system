<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 60%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div style="text-align: center;">挂失/补发</div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;卡号：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="id" style="width: 250px" placeholder="输入您的卡号"/>
      </div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密码：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="password" type="password" style="width: 250px" placeholder="输入您的密码"/>
      </div>
      <br>
      <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作：&nbsp;&nbsp;
                <el-select v-model="selected" style="width: 250px;">
                  <el-option label="挂失" value=1></el-option>
                  <el-option label="补发" value=2></el-option>
                  <el-option label="取消挂失" value=3></el-option>
                </el-select>
              </span>
      <br>
      <div v-if="this.selected === '2'">
                <span>选择补发类型：&nbsp;&nbsp;
                  <el-select v-model="cardType" style="width: 250px;">
                    <el-option label="卡片" value='Card'></el-option>
                    <el-option label="存折" value='Book'></el-option>
                  </el-select>
                </span>
      </div>
      <br>
      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="ensure">确认</el-button>
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
      cardType: '',
      selected: ''
    }
  },
  methods: {
    exit() {
      this.$router.push('/counter/cashier/login');
    },
    ensure() {
      if (this.id === '') {
        this.$message.error('卡号不能为空');
        return;
      }
      if (this.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      if (this.selected === '') {
        this.$message.error('操作选项不能为空');
        return;
      }

      if (this.selected === '1') {
        axiosInstance.post("/account/counter/account/modify/status/lost", null, {
          params: {
            cardid: this.id,
            password: CryptoJS.SHA256(this.password).toString(),
            // operatorid: Cookies.get('operatorid')
          }
        }).then(response => {
          if (response.data.code === 1) {
            this.$message.error(response.data.err);
          } else {
            this.$message.success('挂失成功');
          }
        })
      } else if (this.selected === '3') {
        axiosInstance.post("/account/counter/account/modify/status/unlost", null, {
          params: {
            cardid: this.id,
            password: CryptoJS.SHA256(this.password).toString(),
            // operatorid: Cookies.get('operatorid')
          }
        }).then(response => {
          if (response.data.code === 1) {
            this.$message.error(response.data.err);
          } else {
            this.$message.success('取消挂失成功');
          }
        })
      } else if (this.selected === '2') {
        if (this.cardType === '') {
          this.$message.error('类型不能为空');
          return;
        }
        axiosInstance.post("/account/counter/account/modify/replace", null, {
          params: {
            cardid: this.id,
            password: CryptoJS.SHA256(this.password).toString(),
            cardtype: this.cardType,
            // operatorid: Cookies.get('operatorid')
          }
        }).then(response => {
          if (response.data.code === 1) {
            this.$message.error(response.data.err);
            this.$alert("您的账户未挂失，无法补发");
          } else {
            this.$alert('补发成功，您的新卡号为：' + response.data.payload);
          }
        })
      }
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