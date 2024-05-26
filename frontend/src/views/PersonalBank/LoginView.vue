<template>
    <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
        <div class="login">
            <el-card class="login-card" >
                <template #header>
                    <div class="container">
                        <div style="margin-left:5px" >
                            <span>用户登录</span>
                        </div>
                    </div>
                </template>
                <!-- 登陆卡片的body -->
                <el-form :model="loginForm" ref="loginForm" :rules="loginRules" label-width="80px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="loginForm.username" placeholder="请输入用户名" style="width: 250px"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password" style="margin-top: 20px">
                        <el-input v-model="loginForm.password" placeholder="请输入密码" style="width: 250px" show-password></el-input> 
                        <el-link type="primary" style="margin-left: 15px; font-size: 13px" :underline="false" @click="jumpForget">忘记密码？</el-link>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="handleLogin" style=" width: 250px">登录</el-button>
                        <el-divider style="width: 250px"/>
                    </el-form-item>
                    <el-form-item style="margin-top: -40px">
                        没有账号？
                        <el-link type="primary" :underline="false" @click="jumpRegister">注册</el-link>
                    </el-form-item>
                </el-form>

            </el-card>
        </div>
    </el-scrollbar>
</template>
 
<script>
import CryptoJS from 'crypto-js';
import axios from 'axios';
import { ElMessage } from 'element-plus';

export default {
    data() {
        return {
            loginForm: {
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
        jumpRegister() {
            this.$router.push('/personalBank/register');
        },
        jumpForget() {
            this.$router.push('/personalBank/forget');
        },
        jumpInfo() {
            this.$router.push('/personalBank/infoManage');
        },
        handleLogin() {
            // 加密后传给后端
            const encrypted = CryptoJS.SHA256(this.loginForm.password).toString();
            axios.post("/user/login",
                {
                    "username": this.loginForm.username,
                    "password": encrypted
                })
                .then(response => {
                    ElMessage.success(response.data);
                    // handle successful login, redirect
                    this.jumpInfo();
                })
                .catch(error => {
                    ElMessage.error(error.response.data);
                })
        },

    }
};
</script>

<style scoped>

.login {
    position: absolute;
    width: 100vw;
    height: 100vh;
}

.login-card {
    position: absolute;
    left: 25vw;
    top: 20vh;
    width: 450px;
}

.container {
    display: flex;
    justify-content: center;
    align-items: center;
}

.login-button {
    width: 100%;
}

</style>