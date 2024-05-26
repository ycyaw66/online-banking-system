<template>
    <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
        <div class="forget">
            <el-card class="forget-card" >
                <template #header>
                    <div class="container">
                        <div style="margin-left:5px" >
                            <span>忘记密码</span>
                        </div>
                    </div>
                </template>
                <!-- 忘记密码卡片的body -->
                <el-form :model="forgetForm" :rules="forgetRules" label-width="80px">
                    <el-form-item label="邮箱" prop="email" style="margin-top: 20px">
                        <el-input v-model="forgetForm.email" placeholder="邮箱" style="width: 200px"></el-input>
                        <el-button type="primary" style="width: 100px" >获取验证码</el-button>
                    </el-form-item>
                    <el-form-item label="验证码" prop="verificationCode" style="margin-top: 20px">
                        <el-input v-model="forgetForm.verificationCode" placeholder="六位数字验证码" style="width: 200px"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password" style="margin-top: 20px">
                        <el-input v-model="forgetForm.password" placeholder="6-16位，至少包括字母和数字" style="width: 300px" show-password></el-input> 
                    </el-form-item>
                    <el-form-item label="重复密码" prop="repeatPassword" style="margin-top: 20px">
                        <el-input v-model="forgetForm.repeatPassword" placeholder="请再次输入密码" style="width: 300px" show-password></el-input> 
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="handleForget" style="width: 300px">重置密码</el-button>
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
            forgetForm: {
                email: '',
                verificationCode: '',
                password: '',
                repeatPassword: ''
            },
            forgetRules: {
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'change' },
                    { pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/, message: '请输入有效的邮箱地址', trigger: 'change' }
                ],
                verificationCode: [
                    { required: true, message: '请输入验证码', trigger: 'change' },
                    { pattern: /^\d{6}$/, message: '请输入有效的验证码', trigger: 'change' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'change' },
                    { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{6,16}$/, message: '密码必须为6-16位，且同时包含字母和数字', trigger: 'blur' }
                ],
            }
        };
    },
    methods: {
        jumpLogin() {
            this.$router.push('/personalBank/login');
        },
        handleForget() {
            // 判断两次密码是否相同
            if (this.forgetForm.password !== this.forgetForm.repeatPassword) {
                this.$message.error('重置失败，两次密码不一致');
                return;
            }
            // 加密后传给后端
            const encrypted = CryptoJS.SHA256(this.forgetForm.password).toString();
            axios.post("/user/forget",
                {
                    "email": this.forgetForm.email,
                    "password": encrypted,
                    "verificationCode": this.forgetForm.verificationCode
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

.forget {
    position: absolute;
    width: 100vw;
    height: 100vh;
}

.forget-card {
    position: absolute;
    left: 25vw;
    top: 15vh;
    width: 450px;
}

.container {
    display: flex;
    justify-content: center;
    align-items: center;
}

</style>