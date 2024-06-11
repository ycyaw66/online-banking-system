<script setup>
import {User, Lock, Message} from '@element-plus/icons-vue'
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios';
import {useRouter} from 'vue-router';
import Cookies from "js-cookie";
import CryptoJS from "crypto-js";

axios.defaults.baseURL = 'http://localhost:8082'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
//定义数据模型
const registerData = ref({
  username: '',
  password: '',
  rePassword: '',
  email: '',
  phone: '',
  confirm: ''
})

const form = ref(null)

//校验密码的函数
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== registerData.value.password) {
    callback(new Error('请确保两次输入的密码一样'))
  } else {
    callback()
  }
}

//定义表单校验规则
const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur'}
  ],
  rePassword: [
    {validator: checkRePassword, trigger: 'blur'}
  ],
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
  phone: [
    {required: true, message: '请输入手机号', trigger: 'blur'},
    {pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
  ]
}


//定义函数,清空数据模型的数据
const clearRegisterData = () => {
  registerData.value = {
    username: '',
    password: '',
    rePassword: '',
    email: '',
    phone: '',
    confirm: ''
  }
}

const router = useRouter()

const register = async () => {
  if (!form.value) return
  await form.value.validate((valid) => {
    if (valid) {
      registerData.value.password = CryptoJS.SHA256(registerData.value.password).toString();
      axios.post('/fc/data_operator/start/register', registerData.value).then(res => {
        console.log(res.data)
        if (res.data.code === 0) {
          ElMessage.success('注册成功')
          clearRegisterData()
          isRegister.value = false
        } else {
          ElMessage.error('注册失败')
        }
      })
    } else {
      console.log('error submit!')
    }
  })
}

const login = async () => {
  if (!form.value) return
  await form.value.validate((valid) => {
    if (valid) {
      registerData.value.password = CryptoJS.SHA256(registerData.value.password).toString();
      axios.post('/fc/data_operator/start/login', registerData.value).then(res => {
        console.log(res)
        if (res.data.code === 0) {
          ElMessage.success('登录成功')
          clearRegisterData()
          setTimeout(() => {
            router.push({name: 'opability'})
            Cookies.set('storePersonId',res.data.payload.data_operator_id);
            Cookies.set('storePersonName',res.data.payload.username);
            // store.state.person.id = res.data.payload.data_operator_id
            // store.state.person.name = res.data.payload.username
          }, 1000)
        } else {
          ElMessage.error(res.data.err)
        }
      })
    } else {
      ElMessage.error('格式错误！')
    }
  })
}

</script>

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
          <el-aside width="200px" style="height: 100vh;">
            <el-scrollbar>
              <el-menu :default-openeds="['1', '3']">
                <el-menu-item index="1-1" style="color: white;">
                  <el-icon>
                    <HomeFilled/>
                  </el-icon>
                  登录
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </el-aside>
          <el-main>
            <el-row class="login-page">
              <el-col :span="6" class="bg"></el-col>
              <el-col :span="6" :offset="3" class="form">
                <!-- 注册表单 -->
                <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData"
                         :rules="rules">
                  <el-form-item>
                    <h1>注册</h1>
                  </el-form-item>
                  <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                  </el-form-item>
                  <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                              v-model="registerData.password"></el-input>
                  </el-form-item>
                  <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码"
                              v-model="registerData.rePassword"></el-input>
                  </el-form-item>
                  <el-form-item prop="email">
                    <el-input :prefix-icon="Message" placeholder="请输入邮件"
                              v-model="registerData.email"></el-input>
                  </el-form-item>
                  <el-form-item prop="phone">
                    <el-input :prefix-icon="Message" placeholder="请输入电话"
                              v-model="registerData.phone"></el-input>
                  </el-form-item>
                  <el-form-item prop="confirm">
                    <el-input :prefix-icon="Message" type="password" placeholder="请输入邀请码"
                              v-model="registerData.confirm"></el-input>
                  </el-form-item>
                  <!-- 注册按钮 -->
                  <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                      注册
                    </el-button>
                  </el-form-item>
                  <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false;clearRegisterData()">
                      ← 返回
                    </el-link>
                  </el-form-item>
                </el-form>
                <!-- 登录表单 -->
                <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                  <el-form-item>
                    <h1>登录</h1>
                  </el-form-item>
                  <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                  </el-form-item>
                  <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                              v-model="registerData.password"></el-input>
                  </el-form-item>
                  <el-form-item class="flex">
                    <div class="flex">
                      <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                  </el-form-item>
                  <!-- 登录按钮 -->
                  <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
                  </el-form-item>
                  <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true;clearRegisterData()">
                      注册 →
                    </el-link>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>

          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<style lang="css" scoped>
/* 样式 */
.login-page {
  height: 100vh;
  background-color: #fff;

  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
      margin: 0 auto;
    }

    .button {
      width: 100%;
    }

    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}

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
