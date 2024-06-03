<template>
  <el-container style="width: 100%;">
    <el-main style="height: 100%; width: 100%; position: relative;">
      <div style="background-color: white; width: 100%; height: auto; padding: 20px; margin-bottom: 20px;">
        <div style="font-size: 24px;">交易明细-{{accountNumber}}卡号</div>
        <button @click="ReturnAcc" style="position: absolute; top: 40px; right: 20px; background-color: blue; color: white; border: none; padding: 10px 20px; cursor: pointer; font-size: 16px; border-radius: 4px;">返回</button>
        
        <div style="width: 90%; margin: 0 auto; padding-top: 5vh; display: flex; justify-content: space-between; align-items: center; flex-wrap: nowrap;">
          <span style="margin-right: 5px;">付款账户:</span>
          <el-input v-model="this.Cond.cardID" style="width: 8%;"></el-input>
          <span style="margin-right: 5px;">收款账户:</span>
          <el-input v-model="this.Cond.target_id" style="width: 8%;"></el-input>
          
          <div style="width: 25%; display: flex; align-items: center;">
            <span style="margin-right: 5px;">交易金额:</span>
            <el-input v-model="this.Cond.MinAmount" style="flex: 1;"></el-input>
            <span style="margin: 0 5px;">-</span>
            <el-input v-model="this.Cond.MaxAmount" style="flex: 1;"></el-input>
          </div>

          <div style="width: 25%; display: flex; align-items: center;">
            <span style="margin-right: 5px;">日期:</span>
            <el-input v-model="this.Cond.MinDate" style="flex: 1;"></el-input>
            <span style="margin: 0 5px;">-</span>
            <el-input v-model="this.Cond.MaxDate" style="flex: 1;"></el-input>
          </div>
          <span style="margin-right: 5px;">备注:</span>
          <el-input v-model="this.Cond.Remark" placeholder="备注" style="width: 8%;"></el-input>
          <el-button type="primary" @click="QueryCond" style="width: 8%;">查询</el-button>
        </div>

        <el-table :data="Data" height="600" 
            style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw; table-layout: fixed;">
          <el-table-column prop="date" label="交易日期" min-width="150"></el-table-column>
          <el-table-column prop="card_id" label="付款账户" min-width="150"></el-table-column>
          <el-table-column prop="target_id" label="收款账户" min-width="150"></el-table-column>
          <el-table-column prop="amount" label="交易金额" min-width="150"></el-table-column>
          <el-table-column prop="message" label="备注" min-width="150"></el-table-column>
        </el-table>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import axios from 'axios'
import Cookies from 'js-cookie'
import { ElMessage } from 'element-plus';

export default {
  data() {
    return {
      accountNumber: this.$route.query.account_number || '',
      Data: [
        {card_id : '1123213', date: '2024.05.12', amount: '100.00', target_id: '111', message: 'aaa'},
        {card_id : '1123213', date: '2025.05.20', amount: '100.00', target_id: '111', message: 'aaa'}
      ],
      Cond:{
        cardID: '',
        target_id: '',
        MinAmount: '',
        MaxAmount: '',
        MinDate: '',
        MaxDate: '',
        Remark: ''
      }
    };
  },
  methods:{
    QueryData(){
      this.Cond = {
        cardID: '',
        target_id: '',
        MinAmount: '',
        MaxAmount: '',
        MinDate: '',
        MaxDate: '',
        Remark: ''
      },
      this.QueryCond()
    },
    ReturnAcc(){
      this.$router.push('/personalBank/user/account',);
    },
    async QueryCond(){
      this.Data = []
      try {
        const tg = this.Cond.target_card_id ? Number(this.Cond.target_card_id) : this.Cond.target_card_id ;
        const cd = this.Cond.cardID ? Number(this.Cond.cardID) : this.Cond.cardID ;
        const ia = this.Cond.MinAmount ? Number(this.Cond.MinAmount) : this.Cond.MinAmount ;
        const xa = this.Cond.MaxAmount ? Number(this.Cond.MaxAmount) : this.Cond.MaxAmount ;
        const st = this.Cond.MinDate;
        const et = this.Cond.MaxDate;
        axios.defaults.headers.common['Authorization'] = Cookies.get('token');
        let response = await axios.get("/account/trans", 
          {
            params: {
              "target_card_id": tg,
              "transfer_card_id": cd,
              "MinAmount": ia,
              "MaxAmount": xa,
              "start_time": st,
              "end_time" : et,
              "Remark": this.Cond.Remark
            }
          })
        let querydata = response.data
        if (querydata.code === 0) {
          querydata.payload.forEach(item => {
            this.Date.push({
              card_id: item.card_id,
              target_id: item.target_id,
              amout: parseFloat(item.amout).toFixed(2),
              date: item.time,
              message: item.remark
            });
          });
        } else {
          ElMessage.error(querydata.err);
          return;
        }
      } catch (error) {
        console.log(error);
      }
    }
  }
}
</script>

<style scoped>

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

