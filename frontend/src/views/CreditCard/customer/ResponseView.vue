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
                    <span style="color: white;">用户功能</span>
                  </template>
                  <el-menu-item index="1-1">
                    <router-link to="/creditCard/customer/info">
                      <el-icon style="color: white;">
                        <HomeFilled/>
                      </el-icon>
                      <span style="color: white;">个人资料</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-2">
                    <router-link to="/creditCard/customer/card">
                      <el-icon style="color: white;">
                        <WalletFilled/>
                      </el-icon>
                      <span style="color: white;">信用卡相关</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-3">
                    <router-link to="/creditCard/customer/response">
                      <el-icon style="color: white;">
                        <Promotion/>
                      </el-icon>
                      <span style="color: white;">请求结果查询</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-4">
                    <router-link to="/creditCard/customer/pay">
                      <el-icon style="color: white;">
                        <Shop/>
                      </el-icon>
                      <span style="color: white;">模拟支付</span>
                    </router-link>
                  </el-menu-item>
                  <el-menu-item index="1-5">
                    <router-link to="/creditCard/customer/simulation">
                      <el-icon style="color: white;">
                        <List/>
                      </el-icon>
                      <span style="color: white;">流水查询</span>
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
          <el-main style="background-color: #f1f1f1;">
            <br>
            <div style="text-align: center;">您所有发出请求的结果</div>
            <br>
            <div style="display: flex; justify-content: center;">
              <el-table :data="request_responses" stripe style="width: 1100px;">
                <el-table-column prop="id" label="请求编号" width="200px"/>
                <el-table-column prop="creditCardId" label="信用卡id" width="200px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.creditCardId === null || row.creditCardId === ''">暂未创建</span>
                    <span v-else>{{ row.creditCardId }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="请求类型" width="200px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.type === 1">创建信用卡</span>
                    <span v-else-if="row.type === 2">更新信用卡额度</span>
                  </template>
                </el-table-column>
                <el-table-column label="具体请求内容" width="300px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.type === 1">创建一张新的信用卡,额度为{{ row.amount / 100 }}</span>
                    <span v-else-if="row.type === 2">更新信用卡的额度为{{
                        row.amount / 100
                      }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="请求结果" width="200px">
                  <template v-slot="{ row = {} }">
                    <span v-if="row.status === 1">待审核<el-icon>
                        <QuestionFilled/>
                      </el-icon></span>
                    <span v-else-if="row.status === 2" style="color: limegreen;">通过<el-icon>
                        <SuccessFilled/>
                      </el-icon></span>
                    <span v-else-if="row.status === 3" style="color: red;">驳回<el-icon>
                        <CircleCloseFilled/>
                      </el-icon></span>
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
import axios from 'axios';
import Cookies from "js-cookie";


export default {
  data() {
    return {
      request_responses: [{
        id: '1',
        idNumber: '',
        creditCardId: '',
        amount: '20000',
        type: '1',
        status: '1',
        password: ''
      }, {
        id: '2',
        idNumber: '',
        creditCardId: '1',
        amount: '10000',
        type: '2',
        status: '2',
        password: ''
      }, {
        id: '3',
        idNumber: '',
        creditCardId: '1',
        amount: '30000',
        type: '2',
        status: '3',
        password: ''
      }]
    }
  },
  methods: {
    exit() {
      this.$router.push('/creditCard/customer/login');
    },
    queryResponses() {
      axios.get("/creditCard/customer/queryRequests", {params: {idNumber: Cookies.get('credit_card_user_id_card')}})
          .then(response => {
            this.request_responses = []
            let responses = response.data.payload
            responses.forEach(element => {
              this.request_responses.push(element)
            })
          })
    }
  },
  mounted() {
    this.queryResponses();
  }
}
</script>

<style>
.el-menu-item > a {
  color: inherit;
  text-decoration: none !important;
}

.el-aside,
.el-menu,
.el-sub-menu {
  background-color: rgb(47, 109, 185) !important;
}

.el-menu-item > a.is-active {
  color: inherit;
  text-decoration: none !important;
}
</style>