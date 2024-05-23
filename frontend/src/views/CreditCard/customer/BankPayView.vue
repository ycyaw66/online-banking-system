<template>
  <div>
    <div class="common-layout">
      <el-container class="layout-container-demo" style="height: 700px">
        <!--标题区域-->
        <el-header
          style="font-size: 30px; background-color: rgb(149, 211, 242); font-family: 'Lato', sans-serif; color: rgb(43, 47, 58); line-height: 60px;">
          <div style="display: inline-block;">
            <img src="../icons/logo.png" style=" margin-right: 20px; height: 40px;vertical-align: middle;" />
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
                      <UserFilled />
                    </el-icon>
                    用户功能
                  </template>
                  <el-menu-item index="1-1">
                    <router-link to="/creditCard/customer/info">
                      <el-icon>
                        <HomeFilled />
                      </el-icon>
                      个人资料
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/customer/card">
                      <el-icon>
                        <WalletFilled />
                      </el-icon>
                      信用卡相关
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-3">
                    <router-link to="/creditCard/customer/response">
                      <el-icon>
                        <Promotion />
                      </el-icon>
                      请求结果查询
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-4">
                    <router-link to="/creditCard/customer/pay">
                      <el-icon>
                        <Shop />
                      </el-icon>
                      模拟支付
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-5">
                    <router-link to="/creditCard/customer/simulation">
                      <el-icon>
                        <List />
                      </el-icon>
                      流水查询
                    </router-link>
                  </el-menu-item>
                </el-sub-menu>
              </el-menu>
            </el-scrollbar>
            <el-button type="danger" @click="exit" style="display: block; margin: auto;">
              退出登录
            </el-button>
          </el-aside>
          <!--主展示区域-->
          <el-main style="background-color: #f1f1f1; display: flex; justify-content: center; align-items: center;">

            <div class="flex gap-4 mb-4"
              style="background-color: white; width: 60%; max-width: 800px; min-height: 40%; border: 3px solid lightblue; border-radius: 10px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center;">
              <br>
              <div>
                <span>信用卡编号：&nbsp;&nbsp;&nbsp;</span>
                <el-input v-model="credit_card_id" style="width: 250px" placeholder="输入您用于支付的信用卡id" />
              </div>
              <br>
              <div>
                <span>&nbsp;&nbsp;&nbsp;支付金额：&nbsp;&nbsp;&nbsp;</span>
                <el-input v-model="account" style="width: 250px" placeholder="输入您的支付金额" />
              </div>
              <br>
              <div>
                <span>&nbsp;&nbsp;&nbsp;付款日期：&nbsp;&nbsp;&nbsp;</span>
                <el-date-picker v-model="date" style="width: 250px" type="date" placeholder="付款日期" />
              </div>
              <br>
              <div>
                <span>&nbsp;&nbsp;&nbsp;支付密码：&nbsp;&nbsp;&nbsp;</span>
                <el-input v-model="password" style="width: 250px" placeholder="输入您支付的信用卡密码" />
              </div>
              <br>
              <div class="mb-4" style="text-align: center;">
                <el-button type="primary" round @click="pay">确认支付</el-button>
              </div>

              <br>
            </div>

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
      credit_card_id: '',
      account: '',
      date: '',
      password: '',
    }
  },
  methods: {
    exit() {
      this.$router.push('/creditCard/customer/login');
    },
    pay() {
      // 检查信用卡编号、日期、密码、支付金额是否为空
      if (this.credit_card_id === '') {
        this.$message.error('信用卡编号不能为空');
        return;
      }
      if (this.date === '') {
        this.$message.error('付款日期不能为空');
        return;
      }
      if (this.password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      if (this.account === '') {
        this.$message.error('支付金额不能为空');
        return;
      }

      let numAccount = Number(this.account);
      if (isNaN(numAccount)) {
        this.$message.error('输入的支付金额不是一个有效的数字');
      } else {
        var account = numAccount * 100;
        var isInt = account % 1 === 0;
        if(!isInt){
          this.$message.error('支付金额小数部分最多两位');
        }
        this.$message.success('支付成功，共支付 ' + account + ' 元');
      }
    },
  },
  mounted() {
  }
}
</script>

<style>
.el-menu-item>a {
  color: inherit;
  text-decoration: none !important;
}

.el-menu-item>a.is-active {
  color: inherit;
  text-decoration: none !important;
}
</style>