<template>
  <el-scrollbar height="100%" style="width: 100%;">

    <!-- 标题和搜索框 -->
    <div style="margin-top: 20px; margin-left: 40px; font-size: 2em; font-weight: bold;">
      交易历史查询
      <el-input v-model="toSearch" :prefix-icon="Search"
                style=" width: 15vw;min-width: 150px; margin-left: 30px; margin-right: 30px; float: right; ;"
                clearable/>
    </div>

    <!-- 查询框 -->
    <div style="width:100%; margin:0 auto; padding-top:5vh; align-items: center;">
      <el-button style="margin-left: 600px;" type="primary" @click="QueryTrade">显示所有交易记录</el-button>
      <el-button v-if="!searchVisible" style="margin-left: 50px;" type="primary" @click="ToSearch">精确搜索</el-button>
      <el-button v-if="searchVisible" style="margin-left: 50px;" type="primary" @click="SearchTrade">确认搜索
      </el-button>
      <el-button v-if="searchVisible" style="margin-left: 50px;" type="primary" @click="StopSearch">取消</el-button>
    </div>

    <div v-if="searchVisible" style="margin-top: 30px; margin-left: 50px;">

      <el-form :model="searchForm" class="search-form">
        <!--        <el-form-item label="用户ID">-->
        <!--          <el-input v-model="queryForm.userId"></el-input>-->
        <!--        </el-form-item>-->
        <!-- 第一行 -->
        <div class="form-row">
          <el-form-item label="外币名称:">
            <el-input style="width: 6vw;" v-model="searchForm.fcId"></el-input>
          </el-form-item>
          <el-form-item label="信用卡ID:" style="margin-left: 20px;">
            <el-input style="width: 12vw;" v-model="searchForm.creditCardId"></el-input>
          </el-form-item>
          <el-form-item label="交易时间范围:" style="margin-left: 20px;">
            <el-date-picker
                style="width: 15vw;"
                v-model="searchForm.tradeTimeRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="交易类型:" style="margin-left: 20px;">
            <el-select style="width: 5vw;" v-model="searchForm.isBuyIn">
              <el-option label="买入" :value="true"></el-option>
              <el-option label="卖出" :value="false"></el-option>
              <el-option label="全部" :value="null"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <!-- 第二行 -->
        <div class="form-row">
          <el-form-item label="人民币金额范围:">
            <el-input style="width: 10vw; margin-left: 20px;" v-model.number="searchForm.amountCNYMin"
                      placeholder="最小金额"></el-input>
            <el-input style="width: 10vw; margin-left: 20px;" v-model.number="searchForm.amountCNYMax"
                      placeholder="最大金额"></el-input>
          </el-form-item>
          <el-form-item label="外币金额范围:" style="margin-left: 40px;">
            <el-input style="width: 10vw; margin-left: 20px;" v-model.number="searchForm.amountForeignCurrencyMin"
                      placeholder="最小金额"></el-input>
            <el-input style="width: 10vw; margin-left: 20px;" v-model.number="searchForm.amountForeignCurrencyMax"
                      placeholder="最大金额"></el-input>
          </el-form-item>
        </div>
      </el-form>
    </div>

    <!-- 结果表格 -->
    <el-table v-if="isShow" :data="tableData" height="600"
              :default-sort="{ prop: 'trade_time', order: 'ascending' }" :table-layout="'auto'"
              style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw;">
      <el-table-column prop="trade_id" label="交易ID" sortable/>
      <el-table-column prop="credit_card_id" label="所用卡号" sortable/>
      <el-table-column
          prop="is_buy_in"
          label="交易类型"
          sortable
          :formatter="formatTradeType"
      />
      <el-table-column prop="fc_id" label="交易外币" sortable/>
      <el-table-column prop="trade_time" label="交易时间" sortable/>
      <el-table-column prop="amount_cny" label="人民币金额" sortable/>
      <el-table-column prop="amount_foreign_currency" label="外币金额" sortable/>
    </el-table>

  </el-scrollbar>
</template>

<script>
import axios from 'axios';
import {Search} from '@element-plus/icons-vue'
import Cookies from "js-cookie";

const axiosInstance = axios.create();
axiosInstance.interceptors.request.use(config => {
  const token = Cookies.get('token');
  if (token) {
    config.headers.Authorization = token;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

axios.defaults.baseURL = 'http://localhost:8082';

export default {
  data() {
    return {
      isShow: true, // 结果表格展示状态
      searchForm: {
        userId: null,
        fcId: null,
        tradeTimeRange: null,
        amountCNYMin: null,
        amountCNYMax: null,
        amountForeignCurrencyMin: null,
        amountForeignCurrencyMax: null,
        creditCardId: null,
        isBuyIn: null,
      },
      searchVisible: false,
      tableData: [{ // 列表项
        trade_id: '1',
        fc_id: 'USD',
        trade_time: "2024.05.30 21:48",
        amount_cny: 200.00,
        amount_foreign_currency: 27.5966,
        credit_card_id: "abc123",
        is_buy_in: false
      }],
      userId: "user2",
      creditCardId: "cc124",
      toQuery: '', // 待查询内容(对某一借书证号进行查询)
      toSearch: '', // 待搜索内容(对查询到的结果进行搜索)
      Search
    }
  },

  computed: {},
  methods: {
    formatTradeType(row) {
      return row.is_buy_in ? '买入' : '卖出';
    },
    ToSearch() {
      this.searchVisible = true;
    },
    StopSearch() {
      this.searchVisible = false;
    },
    async SearchTrade() {
      try {
        this.tableData = [] // 清空列表
        this.searchForm.userId = this.getUserId()
        const response = await axiosInstance.post('/fc/trade/history/search', this.searchForm);
        console.log(response);
        this.tableData = response.data.payload;
        this.isShow = true // 显示结果列表
      } catch (error) {
        console.error("Error fetching trade records:", error);
      }
    },
    async QueryTrade() {
      this.tableData = [] // 清空列表
      this.userId = this.getUserId()
      axiosInstance.get(`/fc/trade/history/${this.userId}`).then(response => {
        console.log(response);
        this.tableData = response.data // 获取响应负载
        this.isShow = true // 显示结果列表
      }).catch(error => {
        console.log(error)
      })
    },
    // To o: get user id
    getUserId() {
      return 0;
    }
  }
}
</script>

<style scoped>
.search-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form-row {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-bottom: 10px;
}
</style>