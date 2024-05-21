<template>
  <div>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height: 700px">
        <!--标题区域-->
        <el-header style="font-size: 30px; background-color: rgb(149, 211, 242); font-family: 'Lato', sans-serif; color: rgb(43, 47, 58); line-height: 60px;">
          <div style="display: inline-block;">
            <img src="../icons/logo.png" style=" margin-right: 20px; height: 40px;vertical-align: middle;" />
          </div>线上银行系统--信用卡系统
        </el-header>
        <el-container>
          <!--侧边栏区域-->
          <el-aside width="200px" style="height: 87vh; display: flex; flex-direction: column;">
            <el-scrollbar style="flex: 1">
              <el-menu :default-openeds="['1', '3']">
                <el-sub-menu index="1">
                  <template #title>
                    <el-icon>
                      <UserFilled/>
                    </el-icon>
                    系统管理员功能
                  </template>
                  <el-menu-item index="1-1">
                    <router-link to="/creditCard/admin/info">
                      <el-icon>
                        <HomeFilled/>
                      </el-icon>
                      系统管理员信息
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/admin/inspector">
                      <el-icon>
                        <Avatar />
                      </el-icon>
                      管理审查员
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
          <el-main>
            <el-form :inline="true" :model="admin" class="demo-form-inline">
              <el-form-item label="姓名">
                <el-text>{{ admin.name }}</el-text>
              </el-form-item>
              <el-form-item label="身份证">
                <el-text>{{ admin.identificationCard }}</el-text>
              </el-form-item>
              <el-form-item label="居住地址">
                <el-text>{{ admin.address }}</el-text>
              </el-form-item>
              <el-form-item label="手机号码">
                <el-text>{{ admin.phoneNumber }}</el-text>
              </el-form-item>
            </el-form>
          </el-main>

        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      admin: {
        name: '这里是系统管理员的姓名',
        identificationCard: '这里是系统管理员的身份证号',
        address: '这里是系统管理员的地址',
        phoneNumber: '这里是系统管理员的手机号码'
      },
    }
  },
  methods: {
    exit() {
      this.$router.push('/creditCard/admin/login');
    },
  },
  mounted() {
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

.el-aside, .el-menu, .el-sub-menu {
  background-color: white !important;
}

.el-aside .el-menu-item a,
.el-aside .el-sub-menu__title,
.el-aside .el-sub-menu a {
  color: black !important;
}
</style>