<template>
  <div>
    <el-main style="background-color: white;">
      <br>
      <br>
      <div style="display: flex; justify-content: center;">
        <el-table :data="cashiers" stripe style="width: 1200px;">
          <el-table-column prop="id" label="出纳员id" width="200px"/>
          <el-table-column prop="username" label="出纳员姓名" width="200px"/>
          <el-table-column label="权限级别" width="200px">
            <template v-slot="{ row }">
              <span v-if="row.authority === 0">封禁</span>
              <span v-else-if="row.authority === 1">查询和存取款</span>
              <span v-else-if="row.authority === 2">账户相关操作除冻结解冻</span>
              <span v-else-if="row.authority === 3">全部</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="400px">
            <template v-slot="{ row }">
              <el-button type="primary" size="mini"
                         @click="modify_password_visible=true, modify_password.new_password='', modify_password.new_password_again='', modify_password.id=row.id">
                修改密码
              </el-button>
              <el-button type="primary" size="mini"
                         @click="modify_level_visible=true, modify_level.id=row.id, modify_level.new_level=row.permission">
                修改权限级别
              </el-button>
              <el-button type="primary" size="danger"
                         @click="delete_cashier_id=row.id, delete_cashier_visible=true ">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <br><br>
      <div style="display: flex; justify-content: center; align-items: center;">
        <el-button type="primary"
                   @click="add_cashier_visible = true, new_cashier.name='', new_cashier.password='', new_cashier.password_again='', new_cashier.level=''">
          新建出纳员
        </el-button>
      </div>

      <!--接下来是表单区域-->
      <el-dialog title="密码修改" v-model="modify_password_visible" style="width: 25vw;">
        <el-form :model="modify_password">
          <el-form-item label="请输入旧密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="modify_password.old_password" autocomplete="off"
                      style="width: 12.5vw;"></el-input>
          </el-form-item>
          <el-form-item label="请输入新密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="modify_password.new_password" autocomplete="off"
                      style="width: 12.5vw;"></el-input>
          </el-form-item>
          <el-form-item label="请再次输入新密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="modify_password.new_password_again" autocomplete="off"
                      style="width: 12.5vw;"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="modify_password_visible = false">取 消</el-button>
          <el-button type="primary" @click="modifyPassword">确 定</el-button>
        </template>
      </el-dialog>

      <el-dialog title="权限修改" v-model="modify_level_visible" style="width: 25vw;">
        <el-form :model="modify_level">
          <el-form-item label="请选择新的权限等级" :label-width="formLabelWidth">
            <el-select v-model="modify_level.new_level" style="width: 12.5vw;">
              <el-option label="封禁" value="0"></el-option>
              <el-option label="查询和存取款" value="1"></el-option>
              <el-option label="账户相关操作除冻结解冻" value="2"></el-option>
              <el-option label="全部" value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="modify_level_visible = false">取 消</el-button>
          <el-button type="primary" @click="modifyLevel">确 定</el-button>
        </template>
      </el-dialog>

      <el-dialog title="新的出纳员" v-model="add_cashier_visible" style="width: 25vw;">
        <el-form :model="new_cashier">
          <el-form-item label="请输入账号名" :label-width="formLabelWidth">
            <el-input v-model="new_cashier.name" autocomplete="off" style="width: 12.5vw;"></el-input>
          </el-form-item>
          <el-form-item label="请选择权限等级" :label-width="formLabelWidth">
            <el-select v-model="new_cashier.authority" style="width: 12.5vw;">
              <el-option label="封禁" value="0"></el-option>
              <el-option label="查询和存取款" value="1"></el-option>
              <el-option label="账户相关操作除冻结解冻" value="2"></el-option>
              <el-option label="全部" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="请输入密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="new_cashier.password" autocomplete="off"
                      style="width: 12.5vw;"></el-input>
          </el-form-item>
          <el-form-item label="请再次输入密码" :label-width="formLabelWidth">
            <el-input type="password" v-model="new_cashier.password_again" autocomplete="off"
                      style="width: 12.5vw;"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="add_cashier_visible = false">取 消</el-button>
          <el-button type="primary" @click="addCashier">确 定</el-button>
        </template>
      </el-dialog>

      <el-dialog title="删除出纳员" v-model="delete_cashier_visible" style="width: 25vw;">
        <span>确定删除编号为&nbsp;<span style="font-weight: bold;">{{
            delete_cashier_id
          }}</span>&nbsp;的出纳员吗？</span>
        <template #footer>
          <el-button @click="delete_cashier_visible = false">取 消</el-button>
          <el-button type="danger" @click="deleteCashier">确 定</el-button>
        </template>
      </el-dialog>

    </el-main>

  </div>
</template>

<script>

import axios from "axios";
import Cookies from "js-cookie";
import CryptoJS from "crypto-js";

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
      cashiers: [{
        id: '1',
        username: 'name1',
        authority: '1',
      }, {
        id: '2',
        username: 'name2',
        authority: '1',
      }, {
        id: '3',
        username: 'name3',
        authority: '2',
      }],
      modify_password_visible: false,
      modify_password: {
        id: '',
        old_password: '',
        new_password: '',
        new_password_again: '',
      },
      modify_level_visible: false,
      modify_level: {
        id: '',
        new_level: '',
      },
      add_cashier_visible: false,
      new_cashier: {
        name: '',
        authority: '',
        password: '',
        password_again: '',
      },
      delete_cashier_id: '',
      delete_cashier_visible: false,
    }
  },
  methods: {
    exit() {
      this.$router.push('/counter/admin/login');
    },
    modifyPassword() {
      // 验证两次输入的密码是否一致
      if (this.modify_password.new_password !== this.modify_password.new_password_again) {
        this.$message.error('两次输入的密码不一致');
        return;
      }
      // 判断密码是否为空
      if (this.modify_password.new_password === '') {
        this.$message.error('新密码不能为空');
        return;
      }
      //this.$message.success('修改id为' + this.modify_password.id + '的审查员密码成功，新密码为' + this.modify_password.new_password);
      this.modify_password_visible = false;
      axiosInstance.post("/admin/counter/admin/cashier/modify", null, {
        params: {
          id: this.modify_password.id,
          oldpassword: CryptoJS.SHA256(this.modify_password.old_password).toString(),
          newpassword: CryptoJS.SHA256(this.modify_password.new_password).toString()
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success("密码修改成功");
        }
      })
    },
    modifyLevel() {
      //this.$message.success('修改id为' + this.modify_level.id + '的审查员权限等级至' + this.modify_level.new_level);
      this.modify_level_visible = false;
      if (this.modify_level.new_level !== '0' && this.modify_level.new_level !== '1' && this.modify_level.new_level !== '2' && this.modify_level.new_level !== '3') {
        this.$message.error('选择不能为空');
        return;
      }
      axiosInstance.post("/admin/counter/admin/cashier/authority", null, {
        params: {
          id: this.modify_level.id,
          authority: this.modify_level.new_level
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err)
        } else {
          this.$message.success('修改权限等级成功')
          this.queryCashier();
        }
      });
    },
    addCashier() {
      // 判断账号名是否为空
      if (this.new_cashier.name === '') {
        this.$message.error('账号名不能为空');
        return;
      }

      if (this.new_cashier.authority !== '0' && this.new_cashier.authority !== '1' && this.new_cashier.authority !== '2' && this.new_cashier.authority !== '3') {
        this.$message.error('权限等级不能为空');
        return;
      }
      // 判断密码是否为空
      if (this.new_cashier.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      // 验证两次输入的密码是否一致
      if (this.new_cashier.password !== this.new_cashier.password_again) {
        this.$message.error('两次输入的密码不一致');
        return;
      }
      //this.$message.success('创建成功，账号名为' + this.new_inspector.name + '；密码为：' + this.new_inspector.password + '；权限等级为:' + this.new_inspector.level)
      this.add_cashier_visible = false;

      axiosInstance.post("/admin/counter/admin/cashier/add", null, {
        params: {
          password: CryptoJS.SHA256(this.new_cashier.password).toString(),
          username: this.new_cashier.name,
          authority: this.new_cashier.authority
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success('添加出纳员成功');
        }
        this.queryCashier();
      });
    },
    deleteCashier() {
      axiosInstance.delete("/admin/counter/admin/cashier/delete", {
        params: {
          id: this.delete_cashier_id
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err)
        } else {
          this.$message.success('删除审查员成功')
          this.queryCashier();
        }
      });
      this.delete_cashier_visible = false;
    },
    queryCashier() {
      axiosInstance.get("/admin/counter/admin/cashier").then(response => {
        this.cashiers = [];
        let cashiers_ = response.data.payload;
        cashiers_.forEach(cashier => {
          this.cashiers.push(cashier);
        })
      });
    }
  },
  mounted() {
    this.queryCashier();
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