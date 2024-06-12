<template>
  <el-container style="width: 100%; padding: 20px; box-sizing: border-box;">
    <el-main style="height: 100%; width: 100%; position: relative;">
      <div style="background-color: white; padding: 20px; box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); border-radius: 8px;">
        <div style="font-size: 24px;">交易明细-{{ accountNumber }}卡号</div>
        <div style="display: flex; justify-content: flex-end; gap: 20px;">
          <el-button @click="ReturnAcc" type="primary">返回</el-button>
        </div>
        <div style="font-size: 18px; margin-left: 20px;">
          筛选
        </div>
        <div style="padding-top:3vh;">
          <el-input v-model="this.Cond.cardID" style="display:inline; margin-left: 20px;" placeholder="付款账户"></el-input>
          <el-input v-model="this.Cond.target_id" style="display:inline; margin-left: 20px;" placeholder="收款账户"></el-input>
          <el-input v-model="this.Cond.MinAmount" style="display:inline; margin-left: 20px;" placeholder="交易金额最小值"></el-input> -
          <el-input v-model="this.Cond.MaxAmount" style="display:inline; margin-left: 0;" placeholder="交易金额最大值"></el-input>
        </div>
        <div style="padding-top:2vh;">
          <el-date-picker v-model="this.Cond.MinDate" type="date" :style="{marginLeft: '20px', width: '192.29px'}" placeholder="开始日期" value-format="yyyy-MM-dd"></el-date-picker> -
          <el-date-picker v-model="this.Cond.MaxDate" type="date" :style="{marginLeft: '4px', width: '192.29px'}" placeholder="结束日期" value-format="yyyy-MM-dd"></el-date-picker>
          <el-input v-model="this.Cond.Remark" :style="{marginLeft: '20px', width: '320px'}" placeholder="备注"></el-input>
          <el-button style="margin-left: 20px;" type="primary" @click="QueryCond">查询</el-button>
        </div>

        <el-table :data="Data" style="margin-top: 20px;" border>
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
        let response = await axios.post("/card/history", 
         {
            "card_id": Number(this.accountNumber), 
            "target_card_id": tg,
            "transfer_card_id": cd,
            "least_amount": ia,
            "most_amount": xa,
            "start_time": st,
            "end_time" : et,
            "remark": this.Cond.Remark
          })
        let querydata = response.data
        if (querydata.code === 0) {
          console.log(querydata['payload'])
          querydata['payload'].forEach(item => {
            this.Data.push({
              card_id: item.card_id,
              target_id: item.target_id,
              amount: parseFloat(item.amount).toFixed(2),
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
  },
  mounted() {
    this.QueryData(); 
  }
}
</script>

<style scoped>

</style>

