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
      <el-table-column prop="phone" label="电话号码" width="180"></el-table-column>
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
        账号姓名：
        <el-input v-model="newBlacklistInfo.username" clearable />
      </div>
      <div class="dialog-item">
        电话号码：
        <el-input v-model="newBlacklistInfo.phone" clearable />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addBlacklistVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAddBlacklist" :disabled="!newBlacklistInfo.user_id || !newBlacklistInfo.username || !newBlacklistInfo.phone">确定</el-button>
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
// import { ref, computed, onMounted } from 'vue';
// import { ElMessage } from 'element-plus';
import 'element-plus/dist/index.css';
import { Search } from '@element-plus/icons-vue';
import axios from 'axios';

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
        { user_id: '322010000', username: 'aa', phone: 'aa' },
        { user_id: '322010001', username: 'bb', phone: 'bb' },
        { user_id: '322010002', username: 'cc', phone: 'cc' },
        { user_id: '322010003', username: 'dd', phone: 'dd' },
        { user_id: '322010004', username: 'ee', phone: 'ee' },
        { user_id: '322010005', username: 'ff', phone: 'ff' },
        { user_id: '322010006', username: 'gg', phone: 'gg' },
        { user_id: '322010007', username: 'hh', phone: 'hh' },
        { user_id: '322010008', username: 'ii', phone: 'ii' },
        { user_id: '322010009', username: 'jj', phone: 'jj' }
      ],
      newBlacklistInfo: {
        user_id: '',
        username: '',
        phone: ''
      }
    };
  },
  computed: {
    totalItems() {
      return this.filteredData.length;
    },
    filteredData() {
      const query = this.searchQuery.toLowerCase();
      return this.blacklist.filter(
          item => item.username.toLowerCase().includes(query) || item.user_id.includes(query)
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
      // 获取黑名单目录
      // this.blacklist = [] // 清空列表
      axios.get('/administrator/blacklist/get') // 向黑名单发出GET请求
          .then(response => {
            let blacklist = response.data // 接收响应负载
            blacklist.forEach(account => { // 对于每个账号
              this.blacklist.push(account);// 将其加入到列表中
            })
          })
          .catch(error => {
            console.error('Error fetching blacklist:', error);
          });
    },
    // 切换页码
    handlePageChange(page) {
      this.currentPage = page;
    },
    // 添加黑名单
    confirmAddBlacklist() {
      // 发POST
      axios.post("/administrator/blacklist/add", {
        user_id: this.newBlacklistInfo.user_id,
        username: this.newBlacklistInfo.username,
        phone: this.newBlacklistInfo.phone
      })
          .then(response => {
            if (response.status === 200) {
              // 响应成功
              this.$message.success('已添加到黑名单');
              this.addBlacklistVisible = false;
              this.newBlacklistInfo = { user_id: '', username: '', phone: '' };
              this.fetchBlacklist(); // 重新查询黑名单以刷新页面
            }
          })
          .catch(error => {
            // 响应失败
            this.$message.error('添加黑名单失败：' + error.response.data); // 显示错误消息
          });
    },
    confirmRemoveBlacklist(row) {
      this.rowToRemove = row;
      this.removeBlacklistVisible = true;
    },
// 移除黑名单
    removeFromBlacklistConfirmed() {
      // 发出 DELETE 请求
      axios.delete("/administrator/blacklist/remove", {
        user_id: this.rowToRemove.user_id
      })
          .then(response => {
            if (response.status === 200) {
              // 响应成功
              this.$message.success(`已移除黑名单: ${this.rowToRemove.username}`);
              this.removeBlacklistVisible = false;
              this.rowToRemove = null;
              this.fetchBlacklist(); // 刷新页面
            }
          })
          .catch(error => {
            // 响应失败
            this.$message.error('移除黑名单失败：' + error.response.data); // 显示错误消息
          });
    }

  },
  mounted() {
    this.fetchBlacklist();  // Fetch blacklist from server on mount
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
