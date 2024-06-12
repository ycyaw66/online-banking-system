<template>
  <el-main style="background-color: #f1f1f1;">
    <br>
    <div style="text-align: center;">您所有发出请求的结果</div>
    <br>
    <div style="display: flex; justify-content: center;">
      <el-table :data="request_responses" stripe style="width: 1100px;">
        <el-table-column prop="id" label="请求编号" width="200px"/>
        <el-table-column prop="credit_card_id" label="信用卡id" width="200px">
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
            <span v-if="row.type === 1">创建一张新的信用卡,额度为{{ row.amount }}元</span>
            <span v-else-if="row.type === 2">更新信用卡的额度为{{ row.amount }}元</span>
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
</template>

<script>
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

export default {
  data() {
    return {
      request_responses: []
    }
  },
  methods: {
    exit() {
      this.$router.push('/personalBank/user/account');
    },
    queryResponses() {
      axiosInstance.get("/credit-card/query-request")
          .then(response => {
            this.request_responses = []
            let responses = response.data.payload
            // console.log(responses);
            responses.forEach(element => {
              this.request_responses.push(element)
            })
          }).catch(error => {
        console.error('query response error:', error);
      })

    }
  },
  mounted() {
    this.queryResponses();
  }
}
</script>

<style scoped>
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