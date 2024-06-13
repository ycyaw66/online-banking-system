<template>
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 60%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div style="text-align: center;">冻结/解冻</div>
      <br>
      <div>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;卡号：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="id" style="width: 250px" placeholder="输入您的卡号"/>
      </div>
      <br>
      <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作：&nbsp;&nbsp;&nbsp;
                <el-select v-model="selected" style="width: 250px;">
                  <el-option label="冻结" value=1></el-option>
                  <el-option label="解冻" value=2></el-option>
                </el-select>
              </span>
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
      selected: ''
    }
  },
  methods: {
    exit() {
      this.$router.push('/counter/cashier/login');
    },
    ensure() {
      // 检查账户id、操作类型是否为空
      if (this.id === '') {
        this.$message.error('卡号id不能为空');
        return;
      }
      if (this.selected === '') {
        this.$message.error('操作选项不能为空');
        return;
      }
      if (this.selected === '1') {
        axiosInstance.post("/account/counter/account/modify/status/freeze", null, {
          params: {
            cardid: this.id,
            // operatorid: Cookies.get('operatorid')
          }
        }).then(response => {
          if (response.data.code === 1) {
            this.$message.error(response.data.err);
          } else {
            this.$message.success('冻结成功');
          }
        })
      } else if (this.selected === '2') {
        axiosInstance.post("/account/counter/account/modify/status/unfreeze", null, {
          params: {
            cardid: this.id,
            // operatorid: Cookies.get('operatorid')
          }
        }).then(response => {
          if (response.data.code === 1) {
            this.$message.error(response.data.err);
          } else {
            this.$message.success('解冻成功');
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