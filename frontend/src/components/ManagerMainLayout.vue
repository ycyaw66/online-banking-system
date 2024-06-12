<template>
  <div>
    <div class="main" style="overflow-y: hidden;">
      <el-container>
        <el-header class="title">
          <div style="margin-top: 12px; display: inline-block;">
            <img src="../components/icons/logo.png" 
                 style="margin-right: 20px; height: 40px; vertical-align: middle;" />
            <span style="font-size: large; font-family: 'Microsoft YaHei';
                         color: black; font-weight: bold;">系统管理员</span>
            <span style="margin-left: 40px; color: rgba(0, 0, 0, 0.2)">在线银行系统/管理员</span>
          </div>
        </el-header>

        <el-container style="width: 100%;">
          <el-aside class="aside" style="display: flex;">
            <el-menu active-text-color="#ffd04b" background-color="#0270c1" default-active="1" text-color="#fff"
                     style="height: 100%; width: 100%;" :router="true">
              <el-menu-item index="/manager-main/add">
                <el-icon>
                  <Reading />
                </el-icon>
                <span>增加贷款审查员账户</span>
              </el-menu-item>
              <el-menu-item index="/manager-main/change1">
                <el-icon>
                  <Postcard />
                </el-icon>
                <span>修改审查员账号权限</span>
              </el-menu-item>
              <el-menu-item index="/manager-main/change2">
                <el-icon>
                  <Tickets />
                </el-icon>
                <span>修改审查员账号密码</span>
              </el-menu-item>
             
              <el-menu-item index="/manager-main/delete">
                <el-icon>
                  <Tickets />
                </el-icon>
                <span>删除审查员账号</span>
              </el-menu-item>

              <el-button class="logout-button" type="danger" @click="exit">
                返回上一级页面
              </el-button>
             
            </el-menu>
          </el-aside>

          <el-main style="height: 100%; width: 100%;">
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
import { RouterView } from 'vue-router';

export default {
  components: {
    RouterView
  },
  methods:{
    exit(){
      this.$router.push('/onlineBank/admin/privilege');
    }
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
  width: 200px; /* 调整宽度 */
  background-color: #0270c1; /* 深蓝色背景 */
  color: white; /* 文本颜色为白色 */
  border-right: 1px solid #bdc3c7; /* 添加右侧边框 */
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}
.el-menu {
  height: 100%;
  width: 100%;
  background-color: transparent; /* 透明背景，依靠 .aside 设置背景色 */
}

.el-menu-item {
  margin: 10px 0; /* 上下边距增加些间隔 */
  border-radius: 4px; /* 圆角效果 */
}

.el-menu-item:not(.is-disabled):hover {
  background-color: #c7ab0d; /* 悬停时的背景色 */
}

.logout-button {
  position: absolute;
  bottom: 20%;
  left: 50%;
  transform: translateX(-50%);
}
</style>
