<template>
  <div class="page-container">
    <el-table :data="officersData" style="width: 100%">
      <el-table-column prop="officer_id" label="审查员编号" width="150"></el-table-column>
      <el-table-column prop="officer_name" label="审查员姓名" width="150"></el-table-column>
      <el-table-column prop="phone_number" label="电话号码" width="150"></el-table-column>
      <el-table-column prop="username" label="账户名" width="150"></el-table-column>
      <el-table-column prop="password" label="账户密码" width="150"></el-table-column>
      <el-table-column prop="permissions" label="权限" width="100"></el-table-column>
      <el-table-column label="操作" width="120">
        <template v-slot="scope">
          <el-button type="danger" @click="deleteOfficer(scope.row.officer_id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background
                   layout="prev, pager, next, total"
                   :total="totalOfficers"
                   :page-size="pageSize"
                   @current-change="handlePageChange"
                   :current-page="currentPage">
    </el-pagination>
  </div>
</template>

<script>
import {ElTable, ElTableColumn, ElButton, ElMessageBox, ElMessage, ElPagination} from 'element-plus';
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
  components: {
    ElTable, ElTableColumn, ElButton, ElPagination
  },
  data() {
    return {
      officersData: [],
      totalOfficers: 0,
      pageSize: 10,
      currentPage: 1
    };
  },
  mounted() {
    this.fetchOfficers();
  },
  methods: {
    async fetchOfficers() {
      try {
        const response = await axiosInstance.get('/admin/get-officers', {
          params: {
            page: this.currentPage,
            pageSize: this.pageSize
          }
        });
        //this.officersData = response.data.records;
        this.officersData = response.data.records;
        this.totalOfficers = response.data.total;
        // Log the total number of officers
        console.log('Total Officers:', this.totalOfficers, this.officersData);
      } catch (error) {
        console.error('获取审查员数据时发生错误:', error);
        ElMessage.error('无法加载审查员数据。');
      }
    },
    async deleteOfficer(id) {
      try {
        await ElMessageBox.confirm('此操作将永久删除该审查员, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        await axiosInstance.delete(`/admin/delete-officer/${id}`);
        this.fetchOfficers();
        ElMessage.success('删除成功！');
      } catch (error) {
        if (error !== 'cancel') {
          if (error.response) {
            console.error('Error status:', error.response.status);
            console.error('Error data:', error.response.data);
          }
          console.error('删除审查员时发生错误:', error);
          ElMessage.error('删除失败。');
        }
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchOfficers();
    }
  }
};
</script>

<style scoped>
.page-container {
  max-width: 1800px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
