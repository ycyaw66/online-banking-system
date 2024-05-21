<template>
  <div>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height: 700px">
        <!--标题区域-->
        <el-header
            style="font-size: 30px; background-color: rgb(149, 211, 242); font-family: 'Lato', sans-serif; color: rgb(43, 47, 58); line-height: 60px;">
          <div style="display: inline-block;">
            <img src="../icons/logo.png" style=" margin-right: 20px; height: 40px;vertical-align: middle;"/>
          </div>
          线上银行系统--信用卡系统
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
                    用户功能
                  </template>
                  <el-menu-item index="1-1">
                    <router-link to="/creditCard/customer/info">
                      <el-icon>
                        <HomeFilled/>
                      </el-icon>
                      个人资料
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/customer/card">
                      <el-icon>
                        <WalletFilled/>
                      </el-icon>
                      信用卡相关
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-3">
                    <router-link to="/creditCard/customer/response">
                      <el-icon>
                        <Promotion/>
                      </el-icon>
                      请求结果查询
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-4">
                    <router-link to="/creditCard/customer/pay">
                      <el-icon>
                        <Shop/>
                      </el-icon>
                      模拟支付
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-5">
                    <router-link to="/creditCard/customer/simulation">
                      <el-icon>
                        <List/>
                      </el-icon>
                      流水查询
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
            <el-form :inline="true" :model="customer" class="demo-form-inline">
              <el-form-item label="id">
                <el-text>{{ this.$store.state.customer.id }}</el-text>
              </el-form-item>
              <el-form-item label="姓名">
                <el-text>{{ this.$store.state.customer.name }}</el-text>
              </el-form-item>
              <el-form-item label="身份证">
                <el-text>{{ this.$store.state.customer.identification }}</el-text>
              </el-form-item>
              <el-form-item label="居住地址">
                <el-text>{{ this.$store.state.customer.address }}</el-text>
              </el-form-item>
              <el-form-item label="手机号码">
                <el-text>{{ this.$store.state.customer.phoneNumber }}</el-text>
              </el-form-item>
              <el-form-item label="身份证号码">
                <el-text>{{ this.$store.state.user.ID_number }}</el-text>
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
    return {}
  },
  methods: {
    exit() {
      this.$router.push('/creditCard/customer/login');
    },
  },
  mounted() {
    this.$store.state.customer.id='用户id';
    this.$store.state.customer.name='用户姓名';
    this.$store.state.customer.address='用户地址';
    this.$store.state.customer.phoneNumber='用户手机号码';
    this.$store.state.customer.identification='用户身份证号码';
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