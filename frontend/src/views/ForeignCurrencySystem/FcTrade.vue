<template>
  <el-scrollbar height="100%" style="width: 100%;">
    <div style="margin-top: 20px; margin-left: 40px; font-size: 2em; font-weight: bold;">
      外币交易
      <el-input v-model="toSearch" :prefix-icon="Search"
                style=" width: 15vw;min-width: 150px; margin-left: 30px; margin-right: 30px; float: right; ;"
                clearable/>
    </div>
    <!-- 输入购买的外币种类和金额 -->
    <el-form style="margin-top: 20px; margin-left: 40px; font-size: 1.5em; font-weight: bold; ">
      <el-form-item label="选择外币种类">
        <el-select style="width: 10vw;" v-model="selectedFcType" placeholder="请选择外币类型">
          <el-option
              v-for="fcType in fcTypes"
              :key="fcType.fc_id"
              :label="fcType.fc_name"
              :value="fcType.fc_id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="输入交易数额">
        <el-input style="width: 10vw;" v-model="amount" type="number" placeholder="金额" @change="mapping"/>
        <el-input style="width: 10vw;" v-model="amount_cny" type="number" placeholder="人民币金额" disabled/>
      </el-form-item>
      <el-form-item label="输入交易账号">
        <el-input style="width: 10vw;" v-model="accountId" placeholder="账号ID"/>
      </el-form-item>
      <el-form-item label="选择交易类型">
        <el-select style="width: 10vw;" v-model="tradeType" placeholder="请选择交易类型">
          <el-option
              v-for="tradeType in tradeTypes"
              :key="tradeType.type"
              :label="tradeType.name"
              :value="tradeType.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 60px;" type="primary" @click="executeTrade">执行交易</el-button>
      </el-form-item>
    </el-form>
    <!-- 显示交易结果 -->
  </el-scrollbar>
</template>

<script>
import {ElMessage} from 'element-plus';
import axios from 'axios';
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
      fcTypes: [
        {fc_id: 'USD', fc_name: '美元', fc_rate: 6},
        {fc_id: 'EUR', fc_name: '欧元', fc_rate: 0},
        // 其他外币种类...
      ],
      tradeTypes: [
        {
          type: 'Buy',
          name: '买入',
          value: true
        },
        {
          type: 'Sell',
          name: '卖出',
          value: false
        }
      ],
      tradeType: null,
      selectedFcType: null, // 选择的外币种类
      amount: 0, // 交易金额
      amount_cny: 0,
      accountId: '', // 与交易的账号ID
      transactionResult: '' // 交易结果
    };
  },
  methods: {
    mapping() {
      var rate
      this.fcTypes.forEach(fcType => {
        if (fcType.fc_id === this.selectedFcType) {
          rate = fcType.fc_rate
        }
      })
      this.amount_cny = this.amount * rate;
    },
    executeTrade() {
      // 模拟后端交易逻辑
      // 验证账号ID是否有效
      console.log(this.selectedFcType);
      if (this.isValidAccountId(this.accountId)) {
        // 验证交易金额是否合法
        if (this.amount <= 0) {
          ElMessage.error('交易失败：请输入有效的金额')
        } else {
          // 向后端发送交易请求，执行交易逻辑
          axiosInstance.post('/fc/trade/execute', {
            trade_id: null,
            user_id: this.getuserId(), // 无效语句
            credit_card_id: this.accountId,
            fc_id: this.selectedFcType,
            amount_cny: this.amount_cny,
            amount_foreign_currency: this.amount,
            is_buy_in: this.tradeType
          })
          .then(response => {
            // 处理后端返回的交易结果
            if (response.data.code === 0) {
              ElMessage.success('交易成功');
            }else{
              ElMessage.error('交易失败：' + response.data.err);
            }
          })
          .catch(error => {
            ElMessage.error('交易失败：' + error.message);
          });
        }
      } else {
        ElMessage.error('交易失败:账号ID无效');
      }
    },
    isValidAccountId(accountId) {
      console.log(accountId);
      return true;
    },
    Query() {
      this.fcTypes = []
      axiosInstance.get('/fc/currency/all') // 向/book发出GET请求
          .then(response => {
            if (response.data.code === 0) {
              let currencys = response.data.payload // 接收响应负载
              currencys.forEach(fc => { // 对于每个图书
                if (fc.fc_rate === 0)
                  fc.fc_rate = null
                this.fcTypes.push({fc_id: fc.fc_id, fc_name: fc.fc_name, fc_rate: fc.fc_rate})
              })
            } else {
              ElMessage.error(response.data.err)
            }
          })
    },
    getuserId() {
      //TODO: 获取用户ID
      return 0;
    }
  },
  mounted() {
    this.Query();
  }
};
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