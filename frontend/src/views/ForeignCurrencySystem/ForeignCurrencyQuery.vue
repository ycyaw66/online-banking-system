<!-- TODO: YOUR CODE HERE -->
<template>
  <el-scrollbar height="100%" style="width: 100%;">
    <!-- 汇率表格 -->
    <el-input v-model="toSearchCurrencyInfo" :prefix-icon="Search"
              style=" width: 15vw;min-width: 150px; margin-left: 30px; margin-right: 30px; float: right; ;"
              clearable/>
    <div style="margin-top: 20px; margin-left: 40px; font-size: 1.5em; font-weight: bold; ">外币列表
      <el-table :data="filterTableData" style="width: 100%">
        <el-table-column label="外币ID" prop="fc_id"/>
        <el-table-column label="外币名称" prop="currency_name"/>
        <el-table-column label="当日汇率" prop="fc_rate"/>
      </el-table>
    </div>
    <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
      <!-- 搜索框及按钮 -->
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
  computed: {
    filterTableData() { // 搜索规则
      return this.tableData.filter(
          (tuple) =>
              (this.toSearchCurrencyInfo === '') || // 搜索框为空，即不搜索
              tuple.currency_name.toString().includes(this.toSearchCurrencyInfo) || // 图书号与搜索要求一致
              tuple.fc_id.toString().includes(this.toSearchCurrencyInfo) ||
              tuple.fc_rate.toString() === (this.toSearchCurrencyInfo) // 借出时间包含搜索要求
      )
    }
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
      // this.operationInfo.data_operator_id = store.state.person.id
      this.operationInfo.data_operator_id = Cookies.get('storePersonId');
      this.operationInfo.opid = 0
    },
    handleEdit(currency, currency_rate) {
      console.log(currency)
      this.cleanOperation()
      this.editCurrencyVisible = true
      this.operationInfo.fc_name = currency.currency_name
      // this.operationInfo.data_operator_id = store.state.person.id
      this.operationInfo.data_operator_id = Cookies.get('storePersonId');
      this.operationInfo.opid = 1
      const strs = currency_rate.history_date.split("T")
      this.operationInfo.dest_date = strs[0] + ' ' + strs[1]
    },
    handleDelete(currency, currency_rate) {
      this.deleteCurrencyVisible = true
      this.operationInfo.fc_name = currency.currency_name
      // this.operationInfo.data_operator_id = store.state.person.id
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