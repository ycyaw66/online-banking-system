<!-- TODO: YOUR CODE HERE -->
<template>
  <el-scrollbar height="100%" style="width: 100%;">
    <!-- 汇率表格 -->
    <div style="margin-top: 20px; margin-left: 40px; font-size: 1.5em; font-weight: bold; ">外币列表
      <el-table :data="tableData" :border="parentBorder" style="width: 100%">
        <el-table-column type="expand">
          <template #default="props">
            <div m="4">
              <el-table :data="props.row.currency_rate" :border="childBorder"
                        :default-sort="{ prop: 'history_date', order: 'descending' }">
                <el-table-column label="历史汇率" prop="fc_rate"/>
                <el-table-column sortable label="时间" prop="history_date"/>
                <el-table-column align="right">
                  <template #default="scope">
                    <el-button size="small" @click="handleEdit(props.row, scope.row)">
                      Edit
                    </el-button>
                    <el-button size="small" type="danger" @click="handleDelete(props.row, scope.row)">
                      Delete
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="外币ID" prop="fc_id"/>
        <el-table-column label="外币名称" prop="currency_name"/>
        <el-table-column label="当日汇率" prop="fc_rate"/>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" @click="handleNew(scope.row)">
              New
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
      <!-- 搜索框及按钮 -->

      <!-- 新增对话框 -->
      <el-dialog title="新增汇率" v-model="newRateVisible" width="30%">
        <el-form :model="operationInfo" label-width="80px">
          <el-form-item label="外币名称">
            <el-input v-model="operationInfo.fc_name"/>
          </el-form-item>
          <el-form-item label="外币汇率">
            <el-input v-model="operationInfo.rate"/>
          </el-form-item>
          <el-button type="primary" @click="operationInfo.rate = predictNextExchangeRate(this.tuple_data)">汇率预测</el-button>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="newRateVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmNew">确 定</el-button>
      </span>
        </template>
      </el-dialog>

      <!-- 修改对话框 -->
      <el-dialog title="新增汇率" v-model="editCurrencyVisible" width="30%">
        <el-form :model="operationInfo" label-width="80px">
          <el-form-item label="外币名称">
            <el-input v-model="operationInfo.fc_name"/>
          </el-form-item>
          <el-form-item label="目标时间">
            <el-input v-model="operationInfo.dest_date"/>
          </el-form-item>
          <el-form-item label="外币汇率">
            <el-input v-model="operationInfo.rate"/>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="editCurrencyVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmEdit">确 定</el-button>
      </span>
        </template>
      </el-dialog>

      <!-- 搜索图书对话框 -->
      <el-dialog title="搜索外币" v-model="searchCurrencyVisible" width="30%">
        <el-form :model="toSearchCurrencyInfo" label-width="80px">
          <el-form-item label="外币名称">
            <el-input v-model="toSearchCurrencyInfo"/>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="searchCurrencyVisible = fasle">取 消</el-button>
        <el-button type="primary" @click="confirmSearch">确 定</el-button>
      </span>
        </template>
      </el-dialog>

      <!-- 删除确认对话框 -->
      <el-dialog title="删除确认" v-model="deleteCurrencyVisible" width="30%" align-center>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirmDelete">确 定</el-button>
      </span>
        </template>
      </el-dialog>

      <!-- 删除确认对话框 -->
      <el-dialog title="退出确认" v-model="logoutVisible" width="30%" align-center>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirmQuit">确 定</el-button>
      </span>
        </template>
      </el-dialog>

    </el-scrollbar>
  </el-scrollbar>
</template>

<script>
import {Search} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
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

axios.defaults.baseURL = 'http://localhost:8082'

export default {
  data() {
    return {
      tableData: [
        {
          fc_id: "",
          currency_name: 'dollar',
          fc_rate: 1.0,
          currency_rate: [
            {
              history_date: '2022-01-01',
              fc_rate: 1
            },
            {
              history_date: 1.0,
              fc_rate: '2022-01-02'
            }
          ]
        },
        {
          fc_id: "",
          currency_name: 'usd',
          fc_rate: 1.0,
          currency_rate: [
            {
              history_date: 1.0,
              fc_rate: '2022-01-01'
            },
            {
              history_date: 1.0,
              fc_rate: '2022-01-02'
            }
          ]
        }
      ],
      id: "",
      toSearchCurrencyInfo: '',
      tuple_data: [],
      newRateVisible: false,
      searchCurrencyVisible: false,
      deleteCurrencyVisible: false,
      editCurrencyVisible: false,
      quitVisible: false,
      operationInfo: { // 操作信息
        fc_name: '',
        rate: 0,
        data_operator_id: '',
        opid: 0,
        dest_date: ''
      },
      Search
    };
  },
  methods: {
    cleanOperation() {
      this.operationInfo.fc_name = ''
      this.operationInfo.rate = 0
      this.operationInfo.data_operator_id = ''
      this.operationInfo.opid = 0
      this.operationInfo.dest_date = ''
    },
    handleNew(currency) {
      this.cleanOperation()
      this.newRateVisible = true
      this.operationInfo.fc_name = currency.currency_name
      this.operationInfo.data_operator_id = Cookies.get('storePersonId');
      this.operationInfo.opid = 0
      this.tuple_data = currency.currency_rate
    },
    handleEdit(currency, currency_rate) {
      console.log(currency)
      this.cleanOperation()
      this.editCurrencyVisible = true
      this.operationInfo.fc_name = currency.currency_name
      this.operationInfo.data_operator_id = Cookies.get('storePersonId');
      this.operationInfo.opid = 1
      const strs = currency_rate.history_date.split("T")
      this.operationInfo.dest_date = strs[0] + ' ' + strs[1]
    },
    handleDelete(currency, currency_rate) {
      this.deleteCurrencyVisible = true
      this.operationInfo.fc_name = currency.currency_name
      this.operationInfo.data_operator_id = Cookies.get('storePersonId');
      this.operationInfo.opid = 2
      const strs = currency_rate.history_date.split("T")
      this.operationInfo.dest_date = strs[0] + ' ' + strs[1]
    },
    confirmNew() {
      axiosInstance.post('/fc/currency/operation', this.operationInfo).then(response => {
        if (response.data.code === 0) {
          console.log(response)
          ElMessage.success("增加成功")
          this.newRateVisible = false
          this.Query()
        } else
          ElMessage.error(response.data.err)
      }).catch(error => {
        console.log(error)
        ElMessage.error("增加失败")
      })
    },
    confirmDelete() {
      axiosInstance.post('/fc/currency/operation',
          this.operationInfo).then(response => {
        console.log(response)
        if (response.data.code === 0) {
          ElMessage.success("删除成功")
          this.deleteCurrencyVisible = false
          this.Query()
        }
        ElMessage.error(response.data.err)
      }).catch(error => {
        console.log(error)
        ElMessage.error("删除失败")
      })
    },
    confirmEdit() {
      axiosInstance.post("/fc/currency/operation", this.operationInfo).then(response => {
        if (response.data.code === 0) {
          ElMessage.success("修改成功")
          this.editCurrencyVisible = false
          this.Query()
        } else
          ElMessage.error(response.data.err)
      }).catch(error => {
        console.log(error)
        ElMessage.error("修改失败")
      })
    },
    confirmSearch() {
      axiosInstance.get('/fc/currency/' + this.toSearchCurrencyInfo)
          .then(response => {
            if (response.data.code === 0) {
              this.tableData = []
              let currencys = response.data.payload // 接收响应负载
              currencys.forEach(fc => { // 对于每个图书
                if (fc.fc_rate === 0)
                  fc.fc_rate = null
                this.tableData.push(fc) // 将其加入到列表中
              })
              this.searchCurrencyVisible = false
              this.toSearchCurrencyInfo = ''
            } else {
              ElMessage.error(response.data.err)
              this.searchCurrencyVisible = false
            }
          }).catch(error => {
        console.log(error)
        ElMessage.error("搜索失败")
      })
    },
    Query() {
      this.tableData = [] // 清空列表
      axiosInstance.get('/fc/currency/all') // 向/book发出GET请求
          .then(response => {
            if (response.data.code === 0) {
              let currencys = response.data.payload // 接收响应负载
              currencys.forEach(fc => { // 对于每个图书
                if (fc.fc_rate === 0)
                  fc.fc_rate = null
                this.tableData.push(fc) // 将其加入到列表中
              })
            } else {
              ElMessage.error(response.data.err)
            }
          })
    },
    predictNextExchangeRate(exchangeRates) {
      // 计算数据长度
      const n = exchangeRates.length;
      if(n<=1){
        ElMessage.error("数据过少，无法预测");
        return 0;
      }
  
      // 计算时间序列（这里简化为1, 2, 3...）
      const timeSeries = Array.from({length: n}, (_, i) => i + 1);
  
      // 计算汇率和时间序列的平均值
      const avgRate = exchangeRates.reduce((sum, rateObj) => sum + rateObj.fc_rate, 0) / n;
      const avgTime = timeSeries.reduce((sum, time) => sum + time, 0) / n;
  
      // 计算斜率（即日均变化率）
      let numerator = 0;
      let denominator = 0;
      for (let i = 0; i < n; i++) {
        numerator += (exchangeRates[i].fc_rate - avgRate) * (timeSeries[i] - avgTime);
        denominator += Math.pow(timeSeries[i] - avgTime, 2);
      }
      const slope = numerator / denominator; // 预测模型
  
      // 使用最后一个汇率和斜率预测下一个汇率
      const lastRate = exchangeRates[n - 1].fc_rate;
      const nextDay = timeSeries[n - 1] + 1;
      const predictedRate = lastRate + slope * (nextDay - timeSeries[n - 1]);
  
      return predictedRate;
    }
  },
  mounted() {
    if (Cookies.get('storePersonId') === '')
      this.$router.push('/fc/data_operator/start')
    this.Query()
  }
};
</script>

<style scoped>
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

.el-aside .el-menu-item a,
.el-aside .el-sub-menu__title,
.el-aside .el-sub-menu a {
  color: black !important;
}
</style>