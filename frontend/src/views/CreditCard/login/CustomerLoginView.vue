<template>
  <div>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height: 700px">
        <!--标题区域-->
        <el-header
            style="font-size: 30px; background-color: rgb(149, 211, 242); font-family: 'Lato', sans-serif; color: rgb(43, 47, 58); line-height: 60px;">
          <div style="display: inline-block;">
            <img src="../icons/logo.png"
                 style=" margin-right: 20px; height: 40px;vertical-align: middle;"/>
          </div>
          线上银行系统--信用卡系统
        </el-header>
        <el-container>
          <!--侧边栏区域-->
          <el-aside width="200px" style="height: 100vh;">
            <el-scrollbar>
              <el-menu :default-openeds="['1', '3']">
                <el-menu-item index="1-1">
                  <el-icon>
                    <HomeFilled/>
                  </el-icon>
                  登录
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </el-aside>
          <!--主展示区域-->
          <el-main>
            <div style="margin-top: 20px; margin-left: 40px;">
              <el-button type="primary" @click="customerLoginVisible = true">
                登录用户
              </el-button>
            </div>

            <el-dialog title="用户登录" v-model="customerLoginVisible" style="width: 25vw;">
              <el-form :model="customer">
                <el-form-item label="请输入用户身份证号码" :label-width="formLabelWidth">
                  <el-input type="password" v-model="customer.ID_card" autocomplete="off"
                            style="width: 12.5vw;"></el-input>
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="customerLoginVisible = false">取 消</el-button>
                <el-button type="primary" @click="loginCustomer">确 定</el-button>
              </template>
            </el-dialog>
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
      formLabelWidth: '170px',
      customerLoginVisible: false,
      customer: {
        ID_card: '',
      }
    }
  },
  methods: {
    loginCustomer() {
      if (!this.customer.ID_card) {
        this.$message.warning('用户身份证号码不能为空');
        return;
      }
      this.$store.state.user.ID_number = this.customer.ID_card;
      this.$router.push('/creditCard/customer/info');
    },
    //   loginInspector(){
    //     this.$router.push('/creditCard/inspector/info');
    //   },
    //   loginAdmin(){
    //     this.$router.push('/creditCard/admin/info');
    //   },
    //   registerCustomer(){
    //     alert('新用户注册')
    //   },
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

.el-aside,
.el-menu,
.el-sub-menu {
  background-color: white !important;
}

.el-aside .el-menu-item a,
.el-aside .el-sub-menu__title,
.el-aside .el-sub-menu a {
  color: black !important;
}
</style>