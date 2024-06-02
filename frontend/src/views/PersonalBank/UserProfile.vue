<template>
  <el-container style="width: 100%;">
    <el-main style="display: flex; flex-direction:column; flex: 1; margin-left: 30px; margin-top: 20px">
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
        <el-button type="primary" style="margin-left: 40px; margin-top: 20px" @click="modifyInfoVisible = true, modifyForm.username = userInfo.username, modifyForm.id_number = userInfo.id_number, modifyForm.phone_number = userInfo.phone_number, modifyForm.email = userInfo.email, modifyForm.password = '', modifyForm.new_password = ''">
          编辑资料
        </el-button>
        <el-button type="warning" style="margin-left: 20px; margin-top: 20px" @click="modifyPasswordVisible = true, modifyForm.username = userInfo.username, modifyForm.id_number = userInfo.id_number, modifyForm.phone_number = userInfo.phone_number, modifyForm.email = userInfo.email, modifyForm.password = '', modifyForm.new_password = '', passwordForm.password = '', passwordForm.new_password = '', passwordForm.repassword = ''">
          修改密码
        </el-button>
      </el-form>
    </el-main>

    <!-- 编辑资料对话框 -->
    <el-dialog title="编辑资料" v-model="modifyInfoVisible" width="30%" align-center>
      <el-form :model="modifyForm" :rules="modifyRules" ref="modifyForm"  label-width="80px">
        <el-form-item label="手机号" prop="phone_number" style="margin-top: 20px">
          <el-input v-model="modifyForm.phone_number" placeholder="手机号" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" style="margin-top: 20px">
          <el-input v-model="modifyForm.password" placeholder="请输入密码" style="width: 300px" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="modifyInfoVisible = false">取消</el-button>
          <el-button type="primary" @click="handleModify">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog title="修改密码" v-model="modifyPasswordVisible" width="30%" align-center>
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm"  label-width="80px">
        <el-form-item label="旧密码" prop="password">
          <el-input v-model="passwordForm.password" placeholder="请输入密码" style="width: 300px" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="new_password" style="margin-top: 20px">
          <el-input v-model="passwordForm.new_password" placeholder="6-16位，至少包括字母和数字" style="width: 300px" show-password></el-input>
        </el-form-item>
        <el-form-item label="重复密码" prop="repassword" style="margin-top: 20px">
          <el-input v-model="passwordForm.repassword" placeholder="请再次输入密码" style="width: 300px" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="modifyPasswordVisible = false">取消</el-button>
          <el-button type="primary" @click="handleModify">确定</el-button>
        </span>
      </template>
    </el-dialog>

  </el-container>
</template>

<script>
import CryptoJS from 'crypto-js';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import Cookies from 'js-cookie';

export default {
  data() {
    const checkPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.password) {
        callback(new Error('两次密码不一致'));
      } else {
        callback();
      }
    };
    return {
      logoutVisible: false,
      editVisible: false,
      modifyInfoVisible: false,
      modifyPasswordVisible: false,
      userInfo: {
        username: '',
        id_number: '',
        phone_number: '',
        email: ''
      },
      modifyForm: { // 待查询用户信息，加载页面时通过get查询
        username: '',
        password: '',
        new_password: '',
        id_number: '',
        phone_number: '',
        email: '',
      },
      modifyRules: {
        phone_number: [
          { required: true, message: '请输入手机号', trigger: 'change' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      passwordForm: {
        password: '',
        new_password: '',
        repassword: '',
      },
      passwordRules: {
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        new_password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{6,16}$/, message: '密码必须为6-16位，且同时包含字母和数字', trigger: 'blur' }
        ],
        repassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur'},
          { validator: checkPassword, message: '两次密码不一致', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    handleLogout() {
      this.$router.push('/personalBank/user/login');
    },
    handleModify() {
      if (this.modifyPasswordVisible) {
        this.modifyForm.password = this.passwordForm.password;
        this.modifyForm.new_password = this.passwordForm.new_password;
      } else {
        this.modifyForm.new_password = this.modifyForm.password;
      }
      const encryptedOld = CryptoJS.SHA256(this.modifyForm.password).toString();
      const encryptedNew = CryptoJS.SHA256(this.modifyForm.new_password).toString();
      axios.put("/user/profile",
        {
          headers: {
            'Authorization': 'token',
          }
        },
        {
          data: {
            "username": this.modifyForm.username,
            "password": encryptedOld,
            "id_number": this.modifyForm.id_number,
            "phone_number": this.modifyForm.phone_number,
            "email": this.modifyForm.email,
            "new_password": encryptedNew
          }
        }
      )
      .then(response => {
        ElMessage.success(response.data);
        this.modifyInfoVisible = false;
        this.modifyPasswordVisible = false;
        this.queryInfo();
      })
      .catch(error => {
        ElMessage.error(error.response.data);
      })
    },
    async queryInfo() {
      // let response = await axios.get("user/profile", {
      //   headers: {
      //     'Authorization': 'token',
      //   }
      // });
      // this.userInfo = response.data;

      // for debug
      this.userInfo = {
        username: 'xyxyxy',
        id_number: '330812200001013316',
        phone_number: '18888888888',
        email: 'xyxyxy@xy.com'
      };
    }
  },
  mounted() {
    this.queryInfo();
    this.token = Cookies.get('token');
    if (this.token) this.token = this.token.toString();
  }
}
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

.information-card {
  width: 100%;
  min-height: 95%;
  height: auto;
}

.title {
  background-color: #ffffff;
  height: 60px;
}

.aside {
  min-height: calc(100vh - 60px);
  width: 180px;
  background-color: red;
}

.logout-button {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
}

</style>
