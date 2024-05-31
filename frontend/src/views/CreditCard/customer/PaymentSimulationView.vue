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
            <div class="demo-date-picker">
              <div class="block">
                <el-date-picker v-model="start_date" type="date" placeholder="请选择您的开始日期"
                                :default-value="new Date(2010, 9, 1)"/>
                -----
                <el-date-picker v-model="end_date" type="date" placeholder="请选择您的结束日期"
                                :default-value="new Date(2010, 9, 1)"/>
              </div>
            </div>
            <div class="mb-4" style="text-align: center;">
              <el-button type="primary" round @click="query">确认查询</el-button>
            </div>

            <br><br>

            <div style="display: flex; justify-content: center;">
              <el-table :data="bills" stripe style="width: 800px;">
                <el-table-column prop="id" label="交易订单编号" width="200px"/>
                <el-table-column prop="creditCardId" label="信用卡id" width="200px"/>
                <el-table-column label="交易金额：单位(元)" width="200px">
                  <template v-slot="{ row = {} }">
                    <span>{{ row.amount / 100 }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="billDate" label="交易日期" width="200px">
                  <template v-slot="{ row = {} }">
                    <span>{{ formatDate(row.billDate) }}</span>
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

import axios from "axios";
import {format} from "date-fns";
import Cookies from "js-cookie";

export default {
  data() {
    return {
      start_date: '',
      end_date: '',
      bills: [],
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return '';
      return format(new Date(date), 'yyyy年MM月dd日')
    },
    exit() {
      this.$router.push('/creditCard/customer/login');
    },
    query() {
      // 检查开始日期或结束日期是否为空
      if (this.start_date === '' || this.end_date === '') {
        this.$message.error('两个日期不能为空');
        return;
      }
      // 检查开始日期是否在结束日期之后
      if (this.start_date > this.end_date) {
        this.$message.error('开始日期不能在结束日期后');
        return;
      }

      //alert(this.start_date + ' ' + this.end_date);
      axios.post("/creditCard/customer/simulation/query",null,{
        params:{
          start_date: this.start_date,
          end_date: this.end_date,
          id_number: Cookies.get('credit_card_user_id_card'),
        }
      }).then(response =>{
        this.bills = [];
        let bills = response.data.payload;
        bills.forEach(bill => {
          this.bills.push(bill);
        })
      })
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

.el-aside,
.el-menu,
.el-sub-menu {
  background-color: rgb(47, 109, 185) !important;
}

.demo-date-picker {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
}

.demo-date-picker .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  flex: 1;
}

.demo-date-picker .block:last-child {
  border-right: none;
}

.demo-date-picker .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}
</style>