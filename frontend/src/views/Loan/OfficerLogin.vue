<template>
  <div class="login-page">
    <div class="login-container">
      <el-form :model="loginForm" ref="loginForm" :rules="rules" class="login-form">
        <h2>登录</h2>
        <el-form-item prop="username" :rules="rules.username">
          <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password" :rules="rules.password">
          <el-input type="password" v-model="loginForm.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" class="login-button">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import { ElForm, ElFormItem, ElInput, ElButton, ElMessage } from 'element-plus';
  import Cookies from "js-cookie";
  import CryptoJS from "crypto-js";

  export default {
    components: {
      ElForm, ElFormItem, ElInput, ElButton
    },
    data() {
      return {
        loginForm: {
          username: '',
          password: ''
        },
        rules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      async onSubmit() {
        this.$refs.loginForm.validate(async (valid) => {
          if (valid) {
            try {
              console.log('Sending login request with data:', this.loginForm);
              const response = await this.$axios.post('/officer/login', {
                username: this.loginForm.username,
                password: CryptoJS.SHA256(this.loginForm.password).toString()
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
          } else {
            console.log('登录表单验证失败');
            ElMessage.error('请完整填写表单');
            return false;
          }
        });
      }
    }
  };
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #ffffff; /* 设置背景为白色 */
}

.login-container {
  width: 400px;
  padding: 40px;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.login-form h2 {
  margin-bottom: 20px;
  font-weight: bold;
  color: #409eff;
}

.login-button {
  width: 100%;
}
</style>
