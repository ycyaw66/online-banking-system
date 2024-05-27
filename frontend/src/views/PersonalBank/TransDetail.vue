<template>
    <div>
      <div class="main" style="overflow-y: hidden; ">
        <el-container >
          <el-header class="title">
            <div style="margin-top: 12px; display: inline-block;">
              <img src="./icons/logo.png"
                style=" margin-right: 20px; height: 40px;vertical-align: middle;" />
              <span style="font-size: large; font-family: 'Microsoft YaHei';
                  color: black; font-weight: bold;">在线银行系统</span>
            </div>
          </el-header>
          <el-container style="width: 100%; ">
            <el-aside class="aside" style="display: flex;">
              <el-menu active-text-color="#ffd04b" background-color="#0270c1" default-active="1" text-color="#fff"
                style="height:100%; width: 100%;" :router="true">
                <el-menu-item index="InfoManage">
                    <el-icon><operation /></el-icon>
                    <span>账户管理</span>
                </el-menu-item>
                <el-menu-item index="foreign_exchange">
                    <el-icon><coin /></el-icon>
                    <span>外汇交易</span>
                </el-menu-item>
                <el-menu-item index="load">
                    <el-icon><money /></el-icon>
                    <span>贷款模块</span>
                </el-menu-item>
  
              </el-menu>
            </el-aside>
            <el-main style="height: 100%; width: 100%; position: relative;">

                <div style="background-color: white; width: 100%; height: auto; padding: 20px; margin-bottom: 20px;">
                    <div style="font-size: 24px;">交易明细-{{accountNumber}}卡号</div>
                    <button @click="ReturnAcc" style="position: absolute; top: 40px; right: 20px; background-color: blue; color: white; border: none; padding: 10px 20px; cursor: pointer; font-size: 16px; border-radius: 4px;">返回</button>
                    
                    <div style="width: 90%; margin: 0 auto; padding-top: 5vh; display: flex; justify-content: space-between; align-items: center;">
                        <div style="width: 30%; display: flex; align-items: center;">
                            <span style="margin-right: 5px;">交易金额:</span>
                            <el-input v-model="this.Cond.MinAmount" style="flex: 1;"></el-input>
                            <span style="margin: 0 5px;">-</span>
                            <el-input v-model="this.Cond.MaxAmount" style="flex: 1;"></el-input>
                        </div>
                        <el-radio-group v-model="Cond.Direction" style="display: flex; align-items: center;">
                            <el-radio label="">收付款</el-radio>
                            <el-radio label="收款">收款</el-radio>
                            <el-radio label="付款">付款</el-radio>
                        </el-radio-group>
                        <span style="margin-right: 5px;">日期:</span>
                        <el-input v-model="this.Cond.Date" placeholder="日期" style="width: 15%;"></el-input>
                        <span style="margin-right: 5px;">备注:</span>
                        <el-input v-model="this.Cond.Remark" placeholder="备注" style="width: 15%;"></el-input>
                        <el-button type="primary" @click="QueryCond" style="width: 10%;">查询</el-button>
                    </div>

                    <el-table :data="Data" height="600" 
                        style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw; table-layout: fixed;">
                        <el-table-column prop="date" label="交易日期" min-width="150"></el-table-column>
                        <el-table-column prop="direction" label="收/付款" min-width="150"></el-table-column>
                        <el-table-column prop="amount" label="交易金额" min-width="150"></el-table-column>
                        <el-table-column prop="balance" label="余额" min-width="150"></el-table-column>
                        <el-table-column prop="account_number" label="交易账户" min-width="150"></el-table-column>
                        <el-table-column prop="message" label="备注" min-width="150"></el-table-column>
                    </el-table>
                </div>

            </el-main>
          </el-container>
        </el-container>
      </div>
    </div>

</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import Cookies from 'js-cookie'

export default {
    data() {
        return {
            token : '',
            accountNumber: this.$route.query.account_number || '',
            Data: [
                {date: '05.12', direction: '收款', amount: '100', balance: '300.00', account_number: '123213', message: 'aaa'},
                {date: '05.14', direction: '付款', amount: '100', balance: '200.00', account_number: '123213', message: 'aaa'}
            ],
            Cond:{
                MinAmount: '',
                MaxAmount: '',
                Date: '',
                Remark: '',
                Direction: ''  
            }
        };
    },
    methods:{
        QueryData(){
            this.Cond = {
                MinAmount: '',
                MaxAmount: '',
                Date: '',
                Remark: '',
                Direction: ''  
            },
            this.QueryCond()
        },
        ReturnAcc(){
            this.$router.push('/personalBank/infoManage',);
        },
        async QueryCond(){
            this.Data = []
            let response = await axios.get("/account/trans", { params: { "MinAmount": Number(this.Cond.MinAmount), "MaxAmount": this.Cond.MaxAmount, "Date": this.Cond.Date, "Direction": this.Cond.Direction, "Remark": this.Cond.Remark} })
            let querydata = response.data 
            querydata.forEach(querydt => { 
                this.Data.push(querydt)
            });
        }
    },
    mounted() {
        this.token = Cookies.get('token');
         //this.QueryData() // 查询账户信息
    }
}
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

