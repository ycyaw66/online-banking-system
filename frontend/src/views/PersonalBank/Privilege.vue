<template>
  <div class="container">
    <div class="header">
      <div style="font-size: 2em; font-weight: bold; display: flex; justify-content: flex-end; width: 100%;">
        <el-input v-model="searchQuery" placeholder="根据用户名/用户ID搜索" class="search-input">
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    <el-table :data="paginatedData" style="width: 100%">
      <el-table-column prop="user_id" label="账号ID" width="150"></el-table-column>
      <el-table-column prop="username" label="账号姓名" width="150" ></el-table-column>
      <el-table-column label="账号权限" width="calc(100% - 300px)" >
        <template #default="scope">
          <el-button
              v-for="permission in scope.row.permissions"
              :key="permission.name"
              :type="permission.enabled ? 'success' : 'danger'"
              @click="togglePermission(scope.row, permission.name)"
          >
            {{ permission.name }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          background
          layout="prev, pager, next, jumper, ->, total"
          :total="totalItems"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus';
import 'element-plus/dist/index.css';
import { Search } from '@element-plus/icons-vue';
import axios from "axios";
import Cookies from "js-cookie";

export default {
  name: 'AuthorityManagement',
  components: {
    Search
  },
  data() {
    return {
      searchQuery: '',
      pageSize: 10,
      currentPage: 1,
      users: [
        {
          user_id: '322010000',
          username: 'aa',
          permissions: [
            {name: '支付', enabled: true},
            {name: '转账', enabled: false},
            {name: '收款', enabled: true}
          ]
        },
        // ... more users
        {
          user_id: '322010010',
          username: 'jj',
          permissions: [
            {name: '支付', enabled: true},
            {name: '转账', enabled: true},
            {name: '收款', enabled: false}
          ]
        }
      ]
    };
  },
  methods: {
    fetchPermission() {
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.get('/privilege/get')
          .then(response => {
            this.users = response.data.payload;
          })
          .catch(error => {
            console.error('Error fetching users permission:', error);
          });
    },
    togglePermission(user, permissionName) {
      const permission = user.permissions.find(perm => perm.name === permissionName);
      if (permission) {
        permission.enabled = !permission.enabled;
        axios.defaults.headers.common['Authorization'] = Cookies.get('token');
        axios.post('/privilege/update', {
          user_id: user.user_id,
          payment: user.permissions.find(perm => perm.name === "支付").enabled,
          transfer: user.permissions.find(perm => perm.name === "转账").enabled,
          receive: user.permissions.find(perm => perm.name === "收款").enabled,
        }).then((response) => {
          if (response.status === 200) {
            this.fetchPermission();
            ElMessage.success(`${permissionName} 权限已 ${permission.enabled ? '启用' : '禁用'}`);
          }
        }).catch(error => {
          ElMessage.error('权限更新失败：' + error.response.data);
          permission.enabled = !permission.enabled;  // revert change
        });
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
    }
  },
  computed: {
    totalItems() {
      return this.filteredData.length;
    },
    filteredData() {
      const query = this.searchQuery.toLowerCase();
      return this.users.filter(item =>
          item.username.toLowerCase().includes(query) ||
          String(item.user_id).includes(query)
      );
    },
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      return this.filteredData.slice(start, end);
    }
  },
  mounted() {
    this.fetchPermission();
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  margin-right: 20px;
}

.search-input {
  width: 300px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
