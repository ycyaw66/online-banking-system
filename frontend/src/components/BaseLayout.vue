<template>
  <div>
    <div class="main" style="overflow-y: hidden; ">
      <el-container >
        <el-header class="title">
          <div style="margin-top: 12px; display: inline-block;">
            <img src="./Icon/logo.png"
                 style=" margin-right: 20px; height: 40px;vertical-align: middle;"  alt=""/>
            <span style="font-size: large; font-family: 'Microsoft YaHei';
                color: black; font-weight: bold;">银行管理系统</span>
            <span style="margin-left: 40px; color:rgba(0, 0, 0, 0.2)">浙江大学软件工程基础项目</span>
          </div>
        </el-header>
        <el-container style="width: 100%; ">
          <el-aside class="aside" style="display: flex;">
            <el-menu active-text-color="#ffd04b" background-color="#0270c1" default-active="1" text-color="#fff"
                     style="height:100%; width: 100%;" :router="true">
              <el-menu-item index="privilege">
                <el-icon>
                  <Reading />
                </el-icon>
                <span>权限管理</span>
              </el-menu-item>
              <el-menu-item index="blacklist">
                <el-icon>
                  <Postcard />
                </el-icon>
                <span>黑名单修改</span>
              </el-menu-item>

            </el-menu>
          </el-aside>

          <el-main style="height: 100%; width: 100%; ">
            <el-scrollbar height="100%">
              <RouterView class="content" style="height: 90vh; max-height: 100%; background-color: white; color: black;" />
            </el-scrollbar>

          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>



</template>

<script>

import { useRouter } from 'vue-router';
import { Reading, Postcard } from '@element-plus/icons-vue';

export default {
  name: 'BaseLayout',
  components: {
    Reading,
    Postcard
  },
  setup() {
    const router = useRouter();

    const handleMenuSelect = (index) => {
      if (index === '/administrator/privilege') {
        router.push('/administrator/privilege'); // 替换为黑名单修改页面的路由路径
      } else if (index === 'blackList') {
        router.push('/administrator/blackList'); // 替换为权限管理页面的路由路径
      }
    };

    return {
      handleMenuSelect
    };
  }
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

</style>
