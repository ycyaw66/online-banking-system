<template>
  <div class="main" style="overflow-y: hidden;">
    <el-container>
      <!--标题区域-->
      <el-header class="title">
        <div style="margin-top: 12px; display: inline-block;">
          <img src="./Icon/logo.png"
               style=" margin-right: 20px; height: 40px; vertical-align: middle;"/>
          <span style="font-size: large; font-family: 'Microsoft YaHei',serif;
              color: black; font-weight: bold;">在线银行系统</span>
          <span style="margin-left: 40px; color:rgba(0, 0, 0, 0.2)">柜台子系统</span>
        </div>
      </el-header>
      <el-container style="width: 100%;">
        <!--侧边栏区域-->
        <el-aside class="aside" style="position: relative;">
          <el-menu active-text-color="#ffd04b" background-color="#0270c1" default-active="1" text-color="#fff"
                   style="height:100%; width: 100%;" :router="true">
            <el-menu-item index="openAccount">
              <el-icon>
                <HomeFilled/>
              </el-icon>
              <span>开设账户</span>
            </el-menu-item>
            <el-menu-item index="lossAndReissue">
              <el-icon>
                <WalletFilled/>
              </el-icon>
              <span>挂失/补发</span>
            </el-menu-item>
            <el-menu-item index="freeAndUnfreeze">
              <el-icon>
                <Promotion/>
              </el-icon>
              <span>冻结/解冻</span>
            </el-menu-item>
            <el-menu-item index="currentDeposit">
              <el-icon>
                <HomeFilled/>
              </el-icon>
              <span>活期存款</span>
            </el-menu-item>
            <el-menu-item index="currentWithdrawal">
              <el-icon>
                <WalletFilled/>
              </el-icon>
              <span>活期取款</span>
            </el-menu-item>
            <el-menu-item index="timeDeposit">
              <el-icon>
                <Promotion/>
              </el-icon>
              <span>定期存款</span>
            </el-menu-item>
            <el-menu-item index="timeWithdrawal">
              <el-icon>
                <Shop/>
              </el-icon>
              <span>定期取款</span>
            </el-menu-item>
            <el-menu-item index="transferAccount">
              <el-icon>
                <List/>
              </el-icon>
              <span>转账</span>
            </el-menu-item>
          </el-menu>
          <el-button class="logout-button" type="danger" @click="logoutVisible = true">
            退出柜台页面
          </el-button>
        </el-aside>
        <!--主展示区域-->
        <el-main style="height: 100%; width: 100%; ">
          <el-scrollbar height="100%">
            <RouterView class="content" style="height: 90vh; max-height: 100%; background-color: white; color: black;"/>
          </el-scrollbar>
        </el-main>

      </el-container>
    </el-container>

    <el-dialog title="提示" v-model="logoutVisible" width="30%" align-center>
      确认退出柜台系统？
      <template #footer>
          <span>
            <el-button @click="logoutVisible = false">取消</el-button>
            <el-button type="primary" @click="exit">确定</el-button>
          </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>

export default {
  data() {
    return {
      logoutVisible: false,
    }
  },
  methods: {
    exit() {
      this.$router.push('/personalBank/user/account');
    },
  },
};

</script>

<style scoped>

#app {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: #dcdcdc;
  width: 100vw;
  height: 100vh;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  min-height: 100%;
  height: auto;
  background-color: #dcdcdc;

}

.title {
  background-color: #ffffff;
  height: 60px;
}

.aside {
  min-height: calc(100vh - 60px);
  width: 180px;
  background-color: red;
}

.logout-button {
  position: absolute;
  bottom: 10%;
  left: 50%;
  transform: translateX(-50%);
}

</style>