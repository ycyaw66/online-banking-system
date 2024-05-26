<template>
    <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
        <div class="register">
            <el-card class="register-card" >
                <template #header>
                    <div class="container">
                        <div style="margin-left:5px" >
                            <span>用户注册</span>
                        </div>
                    </div>
                </template>
                <!-- 注册卡片的body -->
                <el-form :model="registerForm" :rules="registerRules" label-width="80px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="registerForm.username" placeholder="3-10位，仅包含大小写字母和数字" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password" style="margin-top: 20px">
                        <el-input v-model="registerForm.password" placeholder="6-16位，至少包括字母和数字" style="width: 300px" show-password></el-input> 
                    </el-form-item>
                    <el-form-item label="重复密码" prop="repeatPassword" style="margin-top: 20px">
                        <el-input v-model="registerForm.repeatPassword" placeholder="请再次输入密码" style="width: 300px" show-password></el-input> 
                    </el-form-item>
                    <el-form-item label="身份证号" prop="idCard" style="margin-top: 20px">
                        <el-input v-model="registerForm.idCard" placeholder="身份证号" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号" prop="phone" style="margin-top: 20px">
                        <el-input v-model="registerForm.phone" placeholder="手机号" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email" style="margin-top: 20px">
                        <el-input v-model="registerForm.email" placeholder="邮箱" style="width: 200px"></el-input>
                        <el-button type="primary" style="width: 100px" >获取验证码</el-button>
                    </el-form-item>
                    <el-form-item label="验证码" prop="verificationCode" style="margin-top: 20px">
                        <el-input v-model="registerForm.verificationCode" placeholder="六位数字验证码" style="width: 200px"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="handleRegister" style="width: 300px">注册</el-button>
                        <el-divider style="width: 300px"/>
                    </el-form-item>
                    <el-form-item style="margin-top: -40px">
                        已有账号？
                        <el-link type="primary" :underline="false" @click="jumpLogin">登录</el-link>
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
            registerForm: {
                username: '',
                password: '',
                repeatPassword: '',
                idCard: '',
                phone: '',
                email: '',
                verificationCode: ''
            },
            registerRules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'change' },
                    { pattern: /^[a-zA-Z0-9]{3,10}$/, message: '用户名必须为3-10位，仅包含大小写字母和数字', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'change' },
                    { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{6,16}$/, message: '密码必须为6-16位，且同时包含字母和数字', trigger: 'blur' }
                ],
                idCard: [
                    { required: true, message: '请输入身份证号', trigger: 'change' },
                    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(([0-2][1-9])|10|20|30|31)\d{3}(\d|X)$/, message: '请输入有效的身份证号', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'change' },
                    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'change' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'change' },
                    { pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/, message: '请输入有效的邮箱地址', trigger: 'change' }
                ],
                verificationCode: [
                    { required: true, message: '请输入验证码', trigger: 'change' },
                    { pattern: /^\d{6}$/, message: '请输入有效的验证码', trigger: 'change' }
                ]
            }
        };
    },
    methods: {
        jumpLogin() {
            this.$router.push('/personalBank/login');
        },
        handleRegister() {
            // 判断两次密码是否相同
            if (this.registerForm.password !== this.registerForm.repeatPassword) {
                this.$message.error('注册失败，两次密码不一致');
                return;
            }
            // 加密后传给后端
            const encrypted = CryptoJS.SHA256(this.registerForm.password).toString();
            axios.post("/user/register",
                {
                    "username": this.registerForm.username,
                    "password": encrypted,
                    "id_number": this.registerForm.idCard,
                    "phone_number": this.registerForm.phone,
                    "email": this.registerForm.email,
                    "verificationCode": this.registerForm.verificationCode
                })
                .then(response => {
                    ElMessage.success(response.data);
                    // handle successful login, redirect
                    this.jumpLogin();
                })
                .catch(error => {
                    ElMessage.error(error.response.data);
                })
        }
    }
};
</script>

<style scoped>

.register {
    position: absolute;
    width: 100vw;
    height: 100vh;
}

.register-card {
    position: absolute;
    left: 25vw;
    top: 5vh;
    width: 450px;
}

.container {
    display: flex;
    justify-content: center;
    align-items: center;
}

</style>