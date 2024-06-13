<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">
    <div class="flex gap-4 mb-4"
         style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
      <br>
      <div style="text-align: center;">定期存款</div>
      <br>
      <div>
        <span>卡号：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="id" style="width: 250px" placeholder="输入您的存款卡号"/>
      </div>
      <br>

      <el-form-item label="时长：" :label-width="formLabelWidth">
        <el-select v-model="Length" style="width: 250px">
          <el-option label="3个月" value=3></el-option>
          <el-option label="6个月" value=6></el-option>
          <el-option label="1年" value=12></el-option>
          <el-option label="2年" value=24></el-option>
          <el-option label="3年" value=36></el-option>
          <el-option label="5年" value=60></el-option>
        </el-select>
      </el-form-item>

      <div>
        <span>金额：&nbsp;&nbsp;&nbsp;</span>
        <el-input v-model="amount" style="width: 250px" placeholder="输入您的存款金额(单位为元)"/>
      </div>
      <br>

      <div class="mb-2 flex items-center text-sm">
                <span>是否自动转存：&nbsp;&nbsp;&nbsp;
                <el-radio-group v-model="ifauto" class="ml-4">
                  <el-radio value="1" size="large">是</el-radio>
                  <el-radio value="0" size="large">否</el-radio>
                </el-radio-group>
                </span>
      </div>
      <br>

      <div class="mb-4" style="text-align: center;">
        <el-button type="primary" round @click="deposit">确认存款</el-button>
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
      Length: '',
      amount: '',
      ifauto: ''
    }
  },
  methods: {
    exit() {
      this.$router.push('/fp/counter/fp/save');
    },
    deposit() {
      if (this.id === '') {
        this.$message.error('存款账户不能为空');
        return;
      }
      if (this.amount === '' || this.amount === '0') {
        this.$message.error('存款金额不能为空或为0');
        return;
      }
      if (this.Length === '') {
        this.$message.error('未选择存款时间');
        return;
      }
      if (this.ifauto === '') {
        this.$message.error('未选择是否自动转存');
        return;
      }

      if (isNaN(this.amount) || this.amount === '') {
        this.$message.error('输入的存款金额不是一个有效的数字');
        return;
      } else {
        var money_10 = this.amount * 100;
        var isInt = money_10 % 1 === 0;
        if (!isInt) {
          this.$message.error('存款金额小数部分最多两位');
          return;
        }
      }

      axiosInstance.post("/fp/counter/fp/save", null, {
        params: {
          id: this.id,
          amount: this.amount,
          Length: this.Length,
          ifauto: this.ifauto,
          // operatorid: Cookies.get('operatorid')
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success(response.data.payload);
        }
      })
      this.id = '';
      this.Length = '';
      this.amount = '';
      this.ifauto = '';
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