<template>
  <!--主展示区域-->
  <el-main style="background-color: #f1f1f1;">
    <br>
    <br>
    <div style="display: flex; justify-content: center;">
      <el-table :data="operators" stripe style="width: 1290px;">
        <el-table-column prop="data_operator_id" label="编号" width="200px"/>
        <el-table-column prop="username" label="姓名" width="200px"/>
        <el-table-column label="邮箱" width="200px">
          <template v-slot="{ row }">
            <span>{{ row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column label="添加权限" width="130px">
          <template v-slot="{ row }">
            <span v-if="row.add_permission === 1">是</span>
            <span v-else-if="row.add_permission === 0">否</span>
          </template>
        </el-table-column>
        <el-table-column label="修改权限" width="130px">
          <template v-slot="{ row }">
            <span v-if="row.update_permission === 1">是</span>
            <span v-else-if="row.update_permission === 0">否</span>
          </template>
        </el-table-column>
        <el-table-column label="删除权限" width="130px">
          <template v-slot="{ row }">
            <span v-if="row.delete_permission === 1">是</span>
            <span v-else-if="row.delete_permission === 0">否</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300px">
          <template v-slot="{ row }">
            <el-button type="primary" size="small"
                       @click="modify_permission_visible=true, modify_permission.id=row.data_operator_id, modify_permission.addPermission='',modify_permission.deletePermission='', modify_permission.updatePermission=''">
              修改权限
            </el-button>
            <el-button type="primary" size="small"
                       @click="delete_operator_id=row.data_operator_id, delete_operator_visible=true ">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <br><br>
    <div style="display: flex; justify-content: center; align-items: center;">
      <el-button type="primary"
                 @click="add_operator_visible = true,new_operator.id='',new_operator.deletePermission='', new_operator.name='', new_operator.password='',new_operator.phoneNumber='', new_operator.email='',new_operator.updatePermission='',new_operator.password_again='', new_operator.addPermission=''">
        新建数据操作员
      </el-button>
    </div>

    <!--表单-->

    <el-dialog title="权限修改" v-model="modify_permission_visible" style="width: 25vw;">
      <el-form :model="modify_permission">
        <el-form-item label="请选择新的权限" :label-width="formLabelWidth">
          <el-form-item label="增添权限">
            <el-select v-model="modify_permission.addPermission" style="width: 12.5vw;">
              <el-option label="是" value=1></el-option>
              <el-option label="否" value=0></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="修改权限">
            <el-select v-model="modify_permission.updatePermission" style="width: 12.5vw;">
              <el-option label="是" value=1></el-option>
              <el-option label="否" value=0></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="删除权限">
            <el-select v-model="modify_permission.deletePermission" style="width: 12.5vw;">
              <el-option label="是" value=1></el-option>
              <el-option label="否" value=0></el-option>
            </el-select>
          </el-form-item>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="modify_permission_visible = false">取 消</el-button>
        <el-button type="primary" @click="modifyPermission">确 定</el-button>
      </template>
    </el-dialog>
    <el-dialog title="新建数据操作员" v-model="add_operator_visible" style="width: 25vw;">
      <el-form :model="new_operator">
        <el-form-item label="请输入账号" :label-width="formLabelWidth">
          <el-input v-model="new_operator.id" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请输入姓名" :label-width="formLabelWidth">
          <el-input v-model="new_operator.name" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请输入邮箱" :label-width="formLabelWidth">
          <el-input v-model="new_operator.email" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请输入手机号" :label-width="formLabelWidth">
          <el-input v-model="new_operator.phoneNumber" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="增加权限" :label-width="formLabelWidth">
          <el-select v-model="new_operator.addPermission" style="width: 12.5vw;">
            <el-option label="是" value=1></el-option>
            <el-option label="否" value=0></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="修改权限" :label-width="formLabelWidth">
          <el-select v-model="new_operator.updatePermission" style="width: 12.5vw;">
            <el-option label="是" value=1></el-option>
            <el-option label="否" value=0></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="删除权限" :label-width="formLabelWidth">
          <el-select v-model="new_operator.deletePermission" style="width: 12.5vw;">
            <el-option label="是" value=1></el-option>
            <el-option label="否" value=0></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请输入密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="new_operator.password" autocomplete="off"
                    style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请再次输入密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="new_operator.password_again" autocomplete="off"
                    style="width: 12.5vw;"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="add_operator_visible = false">取 消</el-button>
        <el-button type="primary" @click="addOperator">确 定</el-button>
      </template>
    </el-dialog>
    <el-dialog title="删除数据操作员" v-model="delete_operator_visible" style="width: 25vw;">
      <span>确定删除编号为&nbsp;<span style="font-weight: bold;">{{
          delete_operator_id
        }}</span>&nbsp;的数据操作员吗？</span>
      <template #footer>
        <el-button @click="delete_operator_visible = false">取 消</el-button>
        <el-button type="danger" @click="deleteOperator">确 定</el-button>
      </template>
    </el-dialog>

  </el-main>
</template>

<script>

import axios from "axios";
import Cookies from "js-cookie";
import CryptoJS from "crypto-js";

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
      operators: [{
        data_operator_id: '',
        username: '',
        email: '',
        phoneNumber: '',
        addPermission: 2,
        updatePermission: 2,
        deletePermission: 2,
        password: ''
      },

      ],
      modify_permission_visible: false,
      modify_permission: {
        id: '',
        addPermission: 0,
        updatePermission: 0,
        deletePermission: 0
      },
      add_operator_visible: false,
      new_operator: {
        name: '',
        updatePermission: '',
        password: '',
        password_again: '',
        phoneNumber: '',
        email: '',
        addPermission: 0,
        deletePermission: 0,
      },
      delete_operator_id: '',
      delete_operator_visible: false,
    }
  },
  methods: {
    exit() {
      this.$router.push('/onlineBank/admin/privilege');
    },
    modifyPermission() {
      this.modify_permission_visible = false;
      axiosInstance.post("/fc/admin/home/update", null, {
        params: {
          id: this.modify_permission.id,
          addPermission: this.modify_permission.addPermission,
          deletePermission: this.modify_permission.deletePermission,
          updatePermission: this.modify_permission.updatePermission
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error('修改权限失败')
        } else {
          this.$message.success('修改权限成功')
        }
        this.queryOperator();
      }).catch(error => {
        console.error('admin update inspector level error:', error);
        this.$message.error('修改权限失败');
      })
    },
    addOperator() {
      if (this.new_operator.id === '') {
        this.$message.error('账号名不能为空');
        return;
      }
      if (this.new_operator.name === '') {
        this.$message.error('姓名不能为空');
        return;
      }
      if (this.new_operator.email === '') {
        this.$message.error('邮箱不能为空');
        return;
      }
      if (this.new_operator.phoneNumber === '') {
        this.$message.error('手机号不能为空');
        return;
      }
      if (this.new_operator.addPermission === '' || this.new_operator.deletePermission === '' || this.new_operator.updatePermission === '') {
        this.$message.error('权限等级不能为空');
        return;
      }
      if (this.new_operator.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      if (this.new_operator.password !== this.new_operator.password_again) {
        this.$message.error('两次输入的密码不一致');
        return;
      }
      this.add_operator_visible = false;
      axiosInstance.post("/fc/admin/home/insert", null, {
        params: {
          data_operator_id: this.new_operator.id,
          username: this.new_operator.name,
          email: this.new_operator.email,
          phone_number: this.new_operator.phoneNumber,
          password: CryptoJS.SHA256(this.new_operator.password).toString(),
          add_permission: this.new_operator.addPermission,
          delete_permission: this.new_operator.deletePermission,
          update_permission: this.new_operator.updatePermission
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error('添加失败');
        } else {
          this.$message.success('添加成功');
        }
        this.queryOperator();
      }).catch(error => {
        console.error('admin add inspector error:', error);
        this.$message.error('添加失败');
      })
    },
    deleteOperator() {
      this.$message.info('删除编号为' + this.delete_operator_id + '的数据操作员');
      this.delete_operator_visible = false;
      axiosInstance.delete("/fc/admin/home/delete", {params: {id: this.delete_operator_id}})
          .then(response => {
            if (response.data.code === 1) {
              this.$message.error('删除审查员失败')
            } else {
              this.$message.success('删除审查员成功')
            }
            this.queryOperator();
          }).catch(error => {
        console.error('admin delete inspector error:', error);
        this.$message.error('删除审查员失败');
      })
    },
    queryOperator() {
      axiosInstance.get("/fc/admin/home").then(response => {
        this.operators = [];
        let operators = response.data.payload;
        operators.forEach(operator => {
          this.operators.push(operator);
        })
      }).catch(error => {
        console.error('admin query inspectors error:', error);
        this.exit()
      })
    }
  },
  mounted() {
    this.queryOperator();
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