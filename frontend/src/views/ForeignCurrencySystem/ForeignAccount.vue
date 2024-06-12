<template>
  <el-scrollbar height="100%" style="width: 100%;">

    <!-- 标题和搜索框 -->
    <div style="margin-top: 20px; margin-left: 40px; font-size: 2em; font-weight: bold;">
      账户余额查询
      <el-input v-model="toSearch" :prefix-icon="Search"
                style=" width: 15vw;min-width: 150px; margin-left: 30px; margin-right: 30px; float: right; ;"
                clearable/>
    </div>

    <!-- 查询按钮 -->
    <div style="width:100%; margin:0 auto; padding-top:5vh; align-items: center;">
      <el-input style="width: 10vw; margin-left: 50px;" v-model="this.creditCardId"
                placeholder="输入信用卡ID"></el-input>
      <el-button v-if="!AccountVisible" style="margin-left: 50px;" type="primary" @click="QueryAccount"
                 :disabled="this.creditCardId.length === 0">显示余额
      </el-button>
      <el-button v-if="AccountVisible" style="margin-left: 50px;" type="primary" @click="HideAccount">隐藏余额
      </el-button>
    </div>

    <!-- 结果表格 -->
    <el-table v-if="AccountVisible" :data="tableData" height="600"
              :default-sort="{ prop: 'fc_id', order: 'ascending' }" :table-layout="'auto'"
              style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw;">
      <el-table-column prop="fc_id" label="货币" sortable/>
      <el-table-column prop="amount" label="余额" sortable/>
    </el-table>

  </el-scrollbar>
</template>

<script>
import axios from 'axios';
import {Search} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus';
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

axios.defaults.baseURL = 'http://localhost:8082'

export default {
  data() {
    return {
      AccountVisible: false,
      tableData: [{ // 列表项
        fc_id: 'CNY',
        amount: 999.99
      }],
      userId: "0", // no use
      creditCardId: "",
      toQuery: '', // 待查询内容(对某一借书证号进行查询)
      toSearch: '', // 待搜索内容(对查询到的结果进行搜索)
      Search
    }
  },

  computed: {},
  methods: {
    HideAccount() {
      this.AccountVisible = false;
    },
    async QueryAccount() {
      this.tableData = [] // 清空列表
      await axiosInstance.get(`/fc/account/${this.creditCardId}`).then(response => {
        console.log(response);
        if(response.data.code === 1){
          ElMessage.error(response.data.err);
          return;
        }
        let accounts = response.data.payload // 获取响应负载
        accounts.forEach(record => { // 对于每一个交易记录
          this.tableData.push(record) // 将它加入到列表项中
        });
        this.AccountVisible = true // 显示结果列表
        ElMessage.success('Successfully fetched account records!')
      }).catch(error => {
        console.log(error)
      })
    },
    // To do: get user id
    getuserId() {
      this.userId = 0;
    }
  },
  mounted() {

  }
}
</script>

<style scoped>
.search-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form-row {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-bottom: 10px;
}
</style>
