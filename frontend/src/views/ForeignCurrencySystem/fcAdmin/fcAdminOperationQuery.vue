<template>
  <el-main style="background-color: #f1f1f1;">
    <br>
    <br>
    <div style="display: flex; justify-content: center;">
      <el-table :data="operationRecords" stripe style="width: 1200px;">
        <el-table-column prop="record_id" label="操作编号" width="200px"/>
        <el-table-column prop="data_operator_id" label="操作员编号" width="200px"/>
        <el-table-column prop="fc_id" label="外币ID" width="100px"/>
        <el-table-column prop="operation" label="操作" width="100px"/>
        <el-table-column prop="old_rate" label="原汇率" width="100px"/>
        <el-table-column prop="new_rate" label="新汇率" width="100px"/>
        <el-table-column prop="dest_date" label="目标日期" width="200px"/>
        <el-table-column prop="operation_time" label="操作日期" width="200px"/>
      </el-table>
    </div>
    <br><br>
    <div style="display: flex; justify-content: center; align-items: center;">
    </div>
  </el-main>
</template>

<script>

import axios from "axios";
import Cookies from "js-cookie";
axios.defaults.baseURL = "http://localhost:8082";

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
      formLabelWidth: '150px',
      operationRecords: [{
        record_id: '',
        data_operator_id: '',
        fc_id: '',
        operation: '',
        old_rate: 0,
        new_rate: 0,
        dest_date: '',
        operation_time: ''
      }],
      modify_password_visible: false,
      modify_password: {
        id: '',
        new_password: '',
        new_password_again: '',
      },
      modify_level_visible: false,
      modify_level: {
        id: '',
        new_level: '',
      },
      add_inspector_visible: false,
      new_inspector: {
        name: '',
        level: '',
        password: '',
        password_again: '',
      },
      delete_inspector_id: '',
      delete_inspector_visible: false,
    }
  },
  methods: {
    exit() {
      this.$router.push('/onlineBank/admin/privilege');
    },
    queryOperationRecord() {
      axiosInstance.get("fc/admin/operationRecord").then(response => {
        this.operationRecords = [];
        let operationRecords = response.data.payload;
        operationRecords.forEach(operationRecord => {
          this.operationRecords.push(operationRecord);
        })
      }).catch(error => {
        console.error('admin query inspectors error:', error);
        this.$message.error('请先登录');
        this.$router.push('/fc/admin/login');
      })
    }
  },
  mounted() {
    this.queryOperationRecord();
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

.el-aside .el-menu-item a,
.el-aside .el-sub-menu__title,
.el-aside .el-sub-menu a {
  color: black !important;
}
</style>