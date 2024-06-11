<template>
  <div class="main" style="overflow-y: hidden;">
    <el-container>
      <el-header class="title">
        <div style="margin-top: 12px; display: inline-block;">
          <img src="../../assets/logo.png"
            style=" margin-right: 20px; height: 40px; vertical-align: middle;" />
          <span style="font-size: large; font-family: 'Microsoft YaHei';
              color: black; font-weight: bold;">在线银行系统</span>
          <span style="margin-left: 40px; color:rgba(0, 0, 0, 0.2)">浙江大学软件工程基础课程项目</span>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-card class="login-card" style="margin-top: -100px;">
          <template #header>
            <div class="container">
              <div style="margin-left:5px" >
                <span>管理员登录</span>
              </div>
            </div>
          </template>
          <!-- 登陆卡片的body -->
          <el-form :model="loginForm" ref="loginForm" :rules="loginRules" label-width="80px">
            <el-form-item label="登陆选项" prop="loginType">
              <el-cascader
                v-model="loginForm.loginType"
                :options="options"
                style="width: 250px"
              />
            </el-form-item>
            <el-form-item label="用户名" prop="username">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" style="width: 250px"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password" style="margin-top: 20px">
              <el-input v-model="loginForm.password" placeholder="请输入密码" style="width: 250px" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleLogin" style=" width: 250px">登录</el-button>
              <el-divider style="width: 250px"/>
            </el-form-item>
            <el-form-item style="margin-top: -40px">
              <el-link type="primary" :underline="false" @click="jumpLogin">用户登录</el-link>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>
 
<script>
import CryptoJS from 'crypto-js';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import Cookies from "js-cookie";

export default {
  data() {
    return {
      options: [
        {
          value: "admin",
          label: "系统管理员"
        },
        {
          value: "inspector",
          label: "审查员"
        },
        {
          value: "cashier",
          label: "出纳员"
        },
        // TODO not merge
        // {
        //   value: "operator",
        //   label: "数据操纵员"
        // },
        {
          value: "officer",
          label: "贷款审查员"
        },
      ], 
      loginForm: {
        loginType: '', 
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'change' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'change' }
        ]
      }
    };
  },
  methods: {
    jumpLogin() {
      this.$router.push('/personalBank/user/login');
    },
    jumpInfo() {
      this.$router.push('/onlineBank/admin/privilege');
    },
    async handleLogin() {
      // 加密后传给后端
      const encrypted = CryptoJS.SHA256(this.loginForm.password).toString();
      console.log(this.loginForm.loginType)
      switch (String(this.loginForm.loginType)) {
        case "admin":
          console.log("admin login")
          axios.post("/admin/login",
            {
              "username": this.loginForm.username,
              "password": encrypted
            })
            .then(response => {
              if (response.data.code === 0) {
                ElMessage.success("登录成功");
                Cookies.set('token', response.data.payload.token);
                this.jumpInfo();
              } else {
                ElMessage.error(response.data.err);
                return;
              }
            })
            .catch(error => {
              ElMessage.error(error.response.data);
            })
          break ;
        case "inspector": 
          axios.post("/credit-card/inspector/login", null, {
            params: {
              name: this.loginForm.username,
              password: encrypted
            }
          }).then(response => {
            if (response.data.code === 1) {
              this.$message.error(response.data.err);
              return;
            } else {
              Cookies.set('token', response.data.payload.token);
              Cookies.set('credit_card_inspector_permission', response.data.payload.permission);
              // this.$store.state.creditCardInspector.permission = response.data.payload.permission;
              // console.log(this.$store.state.creditCardInspector.permission);
              this.$router.push('/creditCard/inspector/request');
            }
          }).catch(error => {
            console.error('login error:', error);
          });
          break ; 
        case "cashier":
          axios.post("/counter/cashier/login",null,{
            params:{
              username:this.loginForm.username,
              password:encrypted
            }
          }).then(response =>{
            if(response.data.code === 1){
              this.$message.error(response.data.err);
              return;
            }else{
              // Cookies.set('operatorid',this.cashier.id);
              Cookies.set('token',response.data.payload.token);
              this.$message.success('出纳员登录成功');
              this.$router.push('/counter/cashier/openAccount');
            }
          })
          break ;
        case "operator":
          break ;
        case "officer": 
          try {
              console.log('Sending login request with data:', this.loginForm);
              const response = await this.$axios.post('/officer/login', {
                username: this.loginForm.username,
                password: encrypted
              });
              console.log('Login response:', response.data);
              if (response.data.message === "登录成功") {
                const token = response.data.token;
                localStorage.setItem('token', token);
                Cookies.set('token',token);
                console.log(localStorage.getItem('token'));

                console.log('登录成功');
                this.$router.push('/officer-main'); // 跳转到主页面
              } else {
                ElMessage.error(response.data.message);
              }
            } catch (error) {
              console.error('登录失败', error);
              ElMessage.error('错误');
            }
          break ;
      }

    },
  }
};
</script>

<style scoped>

.main {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  min-height: 100%;
  height: auto;
  background-color: #dcdcdc;
}

.login-card {
  width: 450px;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.main-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  background-color: #ececec;
}

.title {
  background-color: #ffffff;
  height: 60px;
}
</style>