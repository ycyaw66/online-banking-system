<template>
  <div>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height: 700px">
        <!--标题区域-->
        <el-header
            style="font-size: 30px; background-color: white; font-family: 'Lato', sans-serif; color: rgb(43, 47, 58); line-height: 60px;">
          <div style="display: inline-block;">
            <img src="../icons/logo.png"
                 style=" margin-right: 20px; height: 40px;vertical-align: middle;"/>
          </div>
          <span style="font-size: large; font-family: 'Microsoft YaHei',serif;color: black; font-weight: bold;">线上银行系统--信用卡系统</span>
        </el-header>
        <el-container>
          <!--侧边栏区域-->
          <el-aside width="200px" style="height: 87vh; display: flex; flex-direction: column;">
            <el-scrollbar style="flex: 1">
              <el-menu :default-openeds="['1', '3']">
                <el-sub-menu index="1">
                  <template #title>
                    <el-icon style="color: white;">
                      <UserFilled/>
                    </el-icon>
                    <span style="color: white;">用户功能</span>
                  </template>
                  <el-menu-item index="1-1">
                    <router-link to="/creditCard/customer/info">
                      <el-icon style="color: white;">
                        <HomeFilled/>
                      </el-icon>
                      <span style="color: white;">个人资料</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/customer/card">
                      <el-icon style="color: white;">
                        <WalletFilled/>
                      </el-icon>
                      <span style="color: white;">信用卡相关</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-3">
                    <router-link to="/creditCard/customer/response">
                      <el-icon style="color: white;">
                        <Promotion/>
                      </el-icon>
                      <span style="color: white;">请求结果查询</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-4">
                    <router-link to="/creditCard/customer/pay">
                      <el-icon style="color: white;">
                        <Shop/>
                      </el-icon>
                      <span style="color: white;">模拟支付</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-5">
                    <router-link to="/creditCard/customer/simulation">
                      <el-icon style="color: white;">
                        <List/>
                      </el-icon>
                      <span style="color: white;">流水查询</span>
                    </router-link>
                  </el-menu-item>
                </el-sub-menu>
              </el-menu>
            </el-scrollbar>
            <el-button type="danger"
                       @click="exit"
                       style="display: block; margin: auto; margin-bottom: 10%;">
              退出信用卡页面
            </el-button>
          </el-aside>
          <!--主展示区域-->
          <el-main style="display: flex; flex-direction:column; flex: 1; margin-left: 30px; margin-top: 20px;">
            <el-divider orientation="left" style="align-self: flex-start; width: 300px">用户信息</el-divider>
            <el-form label-width="80px">
              <el-form-item label="用户名" prop="username">
                <el-text>{{userInfo.username}}</el-text>
              </el-form-item>
              <el-form-item label="身份证号" prop="idCard" style="margin-top: 20px">
                <el-text>{{userInfo.id_number}}</el-text>
              </el-form-item>
              <el-form-item label="手机号" prop="phone" style="margin-top: 20px">
                <el-text>{{userInfo.phone_number}}</el-text>
              </el-form-item>
              <el-form-item label="邮箱" prop="email" style="margin-top: 20px">
                <el-text>{{userInfo.email}}</el-text>
              </el-form-item>
            </el-form>
          </el-main>

        </el-container>
      </el-container>
    </div>
  </div>
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
    queryInfo(){
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