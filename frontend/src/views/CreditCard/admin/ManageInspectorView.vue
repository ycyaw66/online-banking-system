<template>
  <div>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height: 700px">
        <!--标题区域-->
        <el-header
            style="font-size: 30px; background-color: white; font-family: 'Lato', sans-serif; color: rgb(43, 47, 58); line-height: 60px;">
          <div style="display: inline-block;">
            <img src="../icons/logo.png"
                 style=" margin-right: 20px; height: 40px;vertical-align: middle;"/>
          </div>
          <span style="font-size: large; font-family: 'Microsoft YaHei',serif;color: black; font-weight: bold;">线上银行系统--信用卡系统</span>
        </el-header>
        <el-container>
          <!--侧边栏区域-->
          <el-aside width="200px" style="height: 87vh; display: flex; flex-direction: column;">
            <el-scrollbar style="flex: 1">
              <el-menu :default-openeds="['1', '3']">
                <el-sub-menu index="1">
                  <template #title>
                    <el-icon style="color: white;">
                      <UserFilled/>
                    </el-icon>
                    <span style="color: white;">系统管理员功能</span>
                  </template>
                  <!--                  <el-menu-item index="1-1">-->
                  <!--                    <router-link to="/creditCard/admin/info">-->
                  <!--                      <el-icon>-->
                  <!--                        <HomeFilled/>-->
                  <!--                      </el-icon>-->
                  <!--                      系统管理员信息-->
                  <!--                    </router-link>-->
                  <!--                  </el-menu-item>-->
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/admin/inspector">
                      <el-icon style="color: white;">
                        <Avatar/>
                      </el-icon>
                      <span style="color: white;">管理审查员</span>
                    </router-link>
                  </el-menu-item>
                </el-sub-menu>
              </el-menu>
            </el-scrollbar>
            <el-button type="danger"
                       @click="exit"
                       style="display: block; margin: auto;">
              退出登录
            </el-button>
          </el-aside>
          <!--主展示区域-->
          <el-main style="background-color: #f1f1f1;">
            <br>
            <br>
            <div style="display: flex; justify-content: center;">
              <el-table :data="inspectors" stripe style="width: 1200px;">
                <el-table-column prop="id" label="审查员编号" width="200px"/>
                <el-table-column prop="name" label="审查员姓名" width="200px"/>
                <el-table-column label="密码" width="200px">
                  <template v-slot="{ row }">
                    <el-button type="text" @click="row.passwordVisible = !row.passwordVisible">
                      {{ row.passwordVisible ? row.password : '**********' }}
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column label="权限级别" width="200px">
                  <template v-slot="{ row }">
                    <span v-if="row.permission === 1">仅创建信用卡</span>
                    <span v-else-if="row.permission === 2">创建信用卡及修改额度</span>
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
                               @click="delete_inspector_id=row.id, delete_inspector_visible=true ">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <br><br>
            <div style="display: flex; justify-content: center; align-items: center;">
              <el-button type="primary"
                         @click="add_inspector_visible = true, new_inspector.name='', new_inspector.password='', new_inspector.password_again='', new_inspector.level=''">
                新建审查员
              </el-button>
            </div>

            <!--接下来是表单区域-->
            <el-dialog title="密码修改" v-model="modify_password_visible" style="width: 25vw;">
              <el-form :model="modify_password">
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
                    <el-option label="仅创建信用卡" value=1></el-option>
                    <el-option label="创建信用卡及修改额度" value=2></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="modify_level_visible = false">取 消</el-button>
                <el-button type="primary" @click="modifyLevel">确 定</el-button>
              </template>
            </el-dialog>

            <el-dialog title="新的审查员" v-model="add_inspector_visible" style="width: 25vw;">
              <el-form :model="new_inspector">
                <el-form-item label="请输入账号名" :label-width="formLabelWidth">
                  <el-input v-model="new_inspector.name" autocomplete="off" style="width: 12.5vw;"></el-input>
                </el-form-item>
                <el-form-item label="请选择权限等级" :label-width="formLabelWidth">
                  <el-select v-model="new_inspector.level" style="width: 12.5vw;">
                    <el-option label="仅创建信用卡" value="1"></el-option>
                    <el-option label="创建信用卡及修改额度" value="2"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="请输入密码" :label-width="formLabelWidth">
                  <el-input type="password" v-model="new_inspector.password" autocomplete="off"
                            style="width: 12.5vw;"></el-input>
                </el-form-item>
                <el-form-item label="请再次输入密码" :label-width="formLabelWidth">
                  <el-input type="password" v-model="new_inspector.password_again" autocomplete="off"
                            style="width: 12.5vw;"></el-input>
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="add_inspector_visible = false">取 消</el-button>
                <el-button type="primary" @click="addInspector">确 定</el-button>
              </template>
            </el-dialog>

            <el-dialog title="删除审查员" v-model="delete_inspector_visible" style="width: 25vw;">
              <span>确定删除编号为&nbsp;<span style="font-weight: bold;">{{ delete_inspector_id }}</span>&nbsp;的审查员吗？</span>
              <template #footer>
                <el-button @click="delete_inspector_visible = false">取 消</el-button>
                <el-button type="danger" @click="deleteInspector">确 定</el-button>
              </template>
            </el-dialog>

          </el-main>

        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>

import axios from "axios";

export default {
  data() {
    return {
      formLabelWidth: '150px',
      inspectors: [{
        id: '1',
        name: 'name1',
        password: 'password1',
        permission: '1',
      }, {
        id: '2',
        name: 'name2',
        password: 'password2',
        permission: '1',
      }, {
        id: '3',
        name: 'name3',
        password: 'password3',
        permission: '2',
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
      this.$router.push('/creditCard/admin/login');
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
      axios.post("/creditCard/admin/inspector/modify", null, {
        params: {
          id: this.modify_password.id,
          password: this.modify_password.new_password
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error('修改密码失败');
        } else {
          this.$message.success('修改密码成功');
        }
      })
    },
    modifyLevel() {
      //this.$message.success('修改id为' + this.modify_level.id + '的审查员权限等级至' + this.modify_level.new_level);
      this.modify_level_visible = false;
      axios.post("/creditCard/admin/inspector/update", null, {
        params: {
          id: this.modify_level.id,
          permission: this.modify_level.new_level
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error('修改权限等级失败')
        } else {
          this.$message.success('修改权限等级成功')
        }
      });
      this.queryInspector();
    },
    addInspector() {
      // 判断账号名是否为空
      if (this.new_inspector.name === '') {
        this.$message.error('账号名不能为空');
        return;
      }
      if (this.new_inspector.level === '') {
        this.$message.error('权限等级不能为空');
        return;
      }
      // 判断密码是否为空
      if (this.new_inspector.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      // 验证两次输入的密码是否一致
      if (this.new_inspector.password !== this.new_inspector.password_again) {
        this.$message.error('两次输入的密码不一致');
        return;
      }
      //this.$message.success('创建成功，账号名为' + this.new_inspector.name + '；密码为：' + this.new_inspector.password + '；权限等级为:' + this.new_inspector.level)
      this.add_inspector_visible = false;

      axios.post("/creditCard/admin/inspector/add", null, {
        params: {
          name: this.new_inspector.name,
          password: this.new_inspector.password,
          permission: this.new_inspector.level
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error('添加失败');
        } else {
          this.$message.success('添加成功');
        }
        this.queryInspector();
      });

    },
    deleteInspector() {
      this.$message.error('删除编号为' + this.delete_inspector_id + '的审查员');
      axios.get("/creditCard/admin/inspector/delete", {params: {id: this.delete_inspector_id}})
          .then(response => {
            if (response.data.code === 1) {
              this.$message.error('删除审查员失败')
            } else {
              this.$message.success('删除审查员成功')
            }
            this.queryInspector();
          });
      this.delete_inspector_visible = false;
    },
    queryInspector() {
      axios.get("/creditCard/admin/inspector").then(response => {
        this.inspectors = [];
        let inspectors = response.data.payload;
        inspectors.forEach(inspector => {
          this.inspectors.push(inspector);
        })
      });
    }
  },
  mounted() {
    this.queryInspector();
  }
}
</script>

<style>
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