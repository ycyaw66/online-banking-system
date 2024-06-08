<template>
  <el-main style="display: flex; flex-direction:column; flex: 1; margin-left: 30px; margin-top: 20px">
    <el-divider orientation="left" style="align-self: flex-start; width: 300px">用户信息</el-divider>
    <el-form label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-text>{{ userInfo.username }}</el-text>
      </el-form-item>
      <el-form-item label="身份证号" prop="idCard" style="margin-top: 20px">
        <el-text>{{ userInfo.id_number }}</el-text>
      </el-form-item>
      <el-form-item label="手机号" prop="phone" style="margin-top: 20px">
        <el-text>{{ userInfo.phone_number }}</el-text>
      </el-form-item>
      <el-form-item label="邮箱" prop="email" style="margin-top: 20px">
        <el-text>{{ userInfo.email }}</el-text>
      </el-form-item>
    </el-form>
  </el-main>
</template>

<script>

import Cookies from "js-cookie";
import axios from "axios";
import {ElMessage} from "element-plus";

export default {
  data() {
    return {
      userInfo: {
        username: '',
        id_number: '',
        phone_number: '',
        email: ''
      },
      id_number: Cookies.get('credit_card_user_id_card'),
    }
  },
  methods: {
    queryInfo() {
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.get("/user/profile")
          .then(response => {
            if (response.data.code === 0) {
              this.userInfo.username = response.data.payload.username;
              this.userInfo.id_number = response.data.payload.id_number;
              this.userInfo.phone_number = response.data.payload.phone_number;
              this.userInfo.email = response.data.payload.email;
            } else {
              ElMessage.error(response.data.err);
              return;
            }
          })
          .catch(error => {
            console.log(error);
          })

    },
    exit() {
      this.$router.push('/personalBank/user/account');
    },
  },
  mounted() {
    this.queryInfo();
  }
}
</script>

<style>
.el-menu-item > a {
  color: inherit;
  text-decoration: none !important;
}

.el-menu-item > a.is-active {
  color: inherit;
  text-decoration: none !important;
}

.el-aside,
.el-menu,
.el-sub-menu {
  background-color: rgb(47, 109, 185) !important;
}

.el-aside .el-menu-item a,
.el-aside .el-sub-menu__title,
.el-aside .el-sub-menu a {
  color: black !important;
}
</style>