<template>
  <div class="container">
    <div class="header">
      <el-button type="primary" @click="addBlacklistVisible = true">添加黑名单</el-button>
      <div style="font-size: 2em; font-weight: bold; display: flex; justify-content: flex-end; width: 100%;">
        <el-input v-model="searchQuery" placeholder="根据用户名/用户ID搜索" class="search-input">
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    <el-table :data="paginatedData" style="width: 100%">
      <el-table-column prop="user_id" label="账号ID" width="180"></el-table-column>
      <el-table-column prop="username" label="账号姓名" width="180"></el-table-column>
      <el-table-column prop="reason" label="封禁原因" width="180"></el-table-column>
      <el-table-column label="黑名单操作" width="calc(100% - 540px)">
        <template #default="scope">
          <el-button @click="confirmRemoveBlacklist(scope.row)" type="primary">移除</el-button>
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
      ></el-pagination>
    </div>

    <!-- 添加黑名单对话框 -->
    <el-dialog v-model="addBlacklistVisible" title="添加黑名单" width="30%" align-center>
      <div class="dialog-item">
        账号ID：
        <el-input v-model="newBlacklistInfo.user_id" clearable />
      </div>
      <div class="dialog-item">
        封禁原因：
        <el-input v-model="newBlacklistInfo.reason" clearable />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addBlacklistVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAddBlacklist" :disabled="!newBlacklistInfo.user_id || !newBlacklistInfo.reason">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 移除黑名单确认对话框 -->
    <el-dialog v-model="removeBlacklistVisible" title="确认移除黑名单" width="30%" align-center>
      <div class="dialog-item" style="text-align: center;">
        确定要移除 <strong>{{ rowToRemove?.username }}</strong> 的黑名单吗？
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="removeBlacklistVisible = false">取消</el-button>
          <el-button type="primary" @click="removeFromBlacklistConfirmed">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import 'element-plus/dist/index.css';
import { Search } from '@element-plus/icons-vue';
import axios from 'axios';
import Cookies from "js-cookie";

export default {
  name: 'BlacklistEdit',
  components: {
    Search
  },
  data() {
    return {
      searchQuery: '',
      pageSize: 10,
      currentPage: 1,
      addBlacklistVisible: false,
      removeBlacklistVisible: false,
      rowToRemove: null,
      blacklist: [
        { user_id: '322010000', username: 'aa', reason: 'aa' },
        { user_id: '322010001', username: 'bb', reason: 'bb' },
        { user_id: '322010002', username: 'cc', reason: 'cc' },
        { user_id: '322010003', username: 'dd', reason: 'dd' },
        { user_id: '322010004', username: 'ee', reason: 'ee' },
        { user_id: '322010005', username: 'ff', reason: 'ff' },
        { user_id: '322010006', username: 'gg', reason: 'gg' },
        { user_id: '322010007', username: 'hh', reason: 'hh' },
        { user_id: '322010008', username: 'ii', reason: 'ii' },
        { user_id: '322010009', username: 'jj', reason: 'jj' }
      ],
      newBlacklistInfo: {
        user_id: '',
        reason: '',
      },
      previousBlacklist: []
    };
  },
  computed: {
    totalItems() {
      return this.filteredData.length;
    },
    filteredData() {
      const query = this.searchQuery.toLowerCase();
      return this.blacklist.filter(
          item => item.username.toLowerCase().includes(query) || String(item.user_id).includes(query)
      );
    },
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      return this.filteredData.slice(start, end);
    }
  },
  methods: {
    fetchBlacklist() {
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.get('/blacklist/get')
          .then(response => {
            this.blacklist = response.data.payload;
          })
          .catch(error => {
            console.error('Error fetching blacklist:', error);
          });
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    confirmAddBlacklist() {
      // 检查用户是否已在黑名单中
      const userIdToCheck = String(this.newBlacklistInfo.user_id);

      const existingUser = this.blacklist.some(user => {
        return String(user.user_id) === userIdToCheck;
      });


      if (existingUser) {
        this.$message.error('该用户已在黑名单中');
        return;
      }


      this.previousBlacklist = [...this.blacklist];

      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.post("/blacklist/add", {
        user_id: this.newBlacklistInfo.user_id,
        reason: this.newBlacklistInfo.reason,
      })
          // eslint-disable-next-line no-unused-vars
          .then(response => {
            this.fetchBlacklist();
            // 如果黑名单列表没有变化，说明添加失败（用户不存在）
            // eslint-disable-next-line no-unused-vars
            axios.get('/blacklist/get').then(response => {
              if (JSON.stringify(this.blacklist) === JSON.stringify(this.previousBlacklist)) {
                this.$message.error('添加黑名单失败：用户不存在');
              } else {
                this.$message.success('已添加到黑名单');
                this.addBlacklistVisible = false;
                this.newBlacklistInfo = { user_id: '', reason: '' };
              }
            });
          });
    },
    confirmRemoveBlacklist(row) {
      this.rowToRemove = row;
      this.removeBlacklistVisible = true;
    },
    removeFromBlacklistConfirmed() {
      axios.delete("/blacklist/remove", {
        data: { user_id: this.rowToRemove.user_id }
      })
          .then(response => {
            if (response.status === 200) {
              this.$message.success(`已移除黑名单: ${this.rowToRemove.username}`);
              this.removeBlacklistVisible = false;
              this.rowToRemove = null;
              this.fetchBlacklist();
            }
          })
          .catch(error => {
            this.$message.error('移除黑名单失败：' + error.response.data);
          });
    }
  },
  mounted() {
    this.fetchBlacklist();
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
}
.search-input {
  width: 300px;
}
.pagination {
  margin-top: 20px;
  text-align: center;
}
.dialog-item {
  margin: 20px 0;
  font-weight: bold;
  font-size: 1rem;
}
</style>
