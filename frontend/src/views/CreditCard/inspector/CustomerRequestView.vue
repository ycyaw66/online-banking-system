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
                    审查员功能
                  </template>
                  <el-menu-item index="1-1">
                    <router-link to="/creditCard/inspector/info">
                      <el-icon>
                        <HomeFilled/>
                      </el-icon>
                      审查员信息
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/inspector/request">
                      <el-icon>
                        <Management />
                      </el-icon>
                      用户请求
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
            <br>
            <br>
            <div style="display: flex; justify-content: center;">
              <el-table :data="request" stripe style="width: 1100px;">
                <el-table-column prop="id" label="请求编号" width="200px"/>
                <el-table-column prop="credit_card_id" label="信用卡id" width="200px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.credit_card_id === null || row.credit_card_id === ''">暂未创建</span>
                    <span v-else>{{ row.credit_card_id }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="请求类型" width="200px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.type === '1'">创建信用卡</span>
                    <span v-else-if="row.type === '2'">更新信用卡额度</span>
                  </template>
                </el-table-column>
                <el-table-column label="具体请求内容" width="300px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.type === '1'">创建一张新的信用卡,额度为{{row.amount}}</span>
                    <span v-else-if="row.type === '2'">更新信用卡{{ row.credit_card_id }}的额度为{{ row.amount }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="处理结果" width="200px">
                  <template v-slot="{ row = {} }">
                    <div style="margin-top: 10px;">
                      <el-button type="success" size="mini" @click="accept(row.id)">通过</el-button>
                      <el-button type="danger" size="mini" @click="reject(row.id)">驳回</el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
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
      request: [{
        id: '1',
        identification: '123456',
        credit_card_id: '',
        amount: '',
        type: '1',
        status: '1',
      }, {
        id: '2',
        identification: '234567',
        credit_card_id: '1',
        amount: '100',
        type: '2',
        status: '2',
      }, {
        id: '3',
        identification: '345678',
        credit_card_id: '1',
        amount: '300',
        type: '2',
        status: '3',
      }]
    }
  },
  methods: {
    exit() {
      this.$router.push('/creditCard/inspector/login');
    },
    accept(id){
      this.$message.success('通过id为'+id+'的请求');
    },
    reject(id){
      this.$message.error('拒绝了id为'+id+'的请求');
    }
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