<template>
  <el-main style="background-color: #f1f1f1;">
    <div class="demo-date-picker">
      <div class="block">
        <el-date-picker v-model="start_date" type="date" placeholder="请选择您的开始日期"
                        :default-value="new Date(2024, 7, 14)"/>
        -----
        <el-date-picker v-model="end_date" type="date" placeholder="请选择您的结束日期"
                        :default-value="new Date(2024, 7, 14)"/>
      </div>
    </div>
    <div class="mb-4" style="text-align: center;">
      <el-button type="primary" round @click="query">确认查询</el-button>
    </div>

    <br><br>

    <div style="display: flex; justify-content: center;">
      <el-table :data="bills" stripe style="width: 800px;">
        <el-table-column prop="id" label="交易订单编号" width="200px"/>
        <el-table-column prop="creditCardId" label="信用卡id" width="200px"/>
        <el-table-column label="交易金额：单位(元)" width="200px">
          <template v-slot="{ row = {} }">
            <span>{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="billDate" label="交易日期" width="200px">
          <template v-slot="{ row = {} }">
            <span>{{ formatDate(row.billDate) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </el-main>
</template>

<script>

import axios from "axios";
import {format} from "date-fns";
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
      start_date: '',
      end_date: '',
      bills: [],
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return '';
      return format(new Date(date), 'yyyy年MM月dd日')
    },
    exit() {
      this.$router.push('/personalBank/user/account');
    },
    query() {
      // 检查开始日期或结束日期是否为空
      if (this.start_date === '' || this.end_date === '') {
        this.$message.error('两个日期不能为空');
        return;
      }
      // 检查开始日期是否在结束日期之后
      if (this.start_date > this.end_date) {
        this.$message.error('开始日期不能在结束日期后');
        return;
      }

      //alert(this.start_date + ' ' + this.end_date);
      axiosInstance.post("/credit-card/bills/query", null, {
        params: {
          start_date: this.start_date,
          end_date: this.end_date,
        }
      }).then(response => {
        this.bills = [];
        let bills = response.data.payload;
        bills.forEach(bill => {
          this.bills.push(bill);
        })
      }).catch(error => {
        console.error('query bills error:', error);
      })

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

.el-menu-item > a.is-active {
  color: inherit;
  text-decoration: none !important;
}

.el-aside,
.el-menu,
.el-sub-menu {
  background-color: rgb(47, 109, 185) !important;
}

.demo-date-picker {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
}

.demo-date-picker .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  flex: 1;
}

.demo-date-picker .block:last-child {
  border-right: none;
}

.demo-date-picker .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}
</style>