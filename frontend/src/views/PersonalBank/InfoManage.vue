<template>
  <el-container style="width: 100%;">
    <el-main style="height: 100%; width: 100%; position: relative;">
      <div style="background-color: white; width: 100%; height: auto; padding: 20px; margin-bottom: 20px;">
        <div style="font-size: 24px;">账户信息管理</div>
        <button @click="JumpToCreditCard" style="position: absolute; top: 40px; right: 160px; background-color: blue; color: white; border: none; padding: 10px 20px; cursor: pointer; font-size: 16px; border-radius: 4px;">添加新账户</button>
        <button @click="OpenAddAccount" style="position: absolute; top: 40px; right: 20px; background-color: blue; color: white; border: none; padding: 10px 20px; cursor: pointer; font-size: 16px; border-radius: 4px;">添加新账户</button>

        <el-table :data="AccountData" height="600" 
            style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw; table-layout: fixed;">
          <el-table-column prop="account_number" label="账户号码" min-width="150"></el-table-column>
          <el-table-column prop="category" label="账户类别" min-width="150"></el-table-column>
          <el-table-column label="操作" min-width="200">
            <template v-slot="scope">
              <el-button type="text" @click="Check(scope.row)" style="border: 1px solid red; color: red; height: 24px; line-height: 22px;">交易明细</el-button>
              <el-button type="text" @click="Transfer(scope.row)" style="border: 1px solid green; color: green; height: 24px; line-height: 22px;">转账</el-button>
              <el-button type="text" @click="PreLoss(scope.row)" style="border: 1px solid blue; color: blue; height: 24px; line-height: 22px;">挂失</el-button>
            </template>
          </el-table-column>
          <el-table-column label="余额" min-width="150">
            <template v-slot="scope">
              <span>{{ scope.row.balance }}</span>
              <el-button v-if="!scope.row.showBalance" type="text" @click="openBalance(scope.row)">
                <el-icon>
                  <View />
                </el-icon>
              </el-button>
              <el-button v-else type="text" @click="scope.row.showBalance = false, closeBalance(scope.row)">
                <el-icon>
                  <Hide />
                </el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-main>

    <el-dialog v-model="AddAccountVisible" title="绑定新账户" width="30%" >
      <el-form :model="AddAccount" :rules="rules" ref="AddAccount" style="width: 100%;">
        <el-form-item label="账户号" prop="accountNumber">
          <el-input v-model="AddAccount.accountNumber" style="margin-left: 12pt; width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="支付密码" prop="paymentPassword">
          <el-input v-model="AddAccount.paymentPassword" type="password" style="width: 100%;"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="AddAccountVisible = false">取消</el-button>
          <el-button type="primary" @click="ConfirmAddAccount"
              :disabled="AddAccount.accountNumber.length === 0 || AddAccount.paymentPassword.length === 0">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="TransVisible" title="转账" width="30%" >
      <el-form :model="Trans" :rules="transferRules" ref="Trans" style="width: 100%;">
        <el-form-item label="转入账户号" prop="toaccount">
          <el-input v-model="Trans.toaccount" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="Trans.amount" style="margin-left: 31pt; width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="支付密码" prop="password">
          <el-input v-model="Trans.password" type="password" style="margin-left: 10pt; width: 100%;"></el-input>
        </el-form-item>
        <el-form-item label="留言" prop="message">
          <el-input v-model="Trans.message" style="margin-left: 39pt; width: 100%;"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="TransVisible = false">取消</el-button>
          <el-button type="primary" @click="ConfirmTransfer"
          :disabled="Trans.toaccount.length === 0 || Trans.amount <= 0 || Trans.password.length === 0">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="PasswordVisible" title="密码确认" width="30%">
      <el-form :model="PasswordCheck" :rules="passRules" ref="PasswordCheck" style="width: 100%;">
        <el-form-item label="支付密码" prop="Password">
          <el-input v-model="PasswordCheck.Password" type="password" style="width: 100%;"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="PasswordVisible = false">取消</el-button>
          <el-button type="primary" @click="Password_Check"
          :disabled="PasswordCheck.Password.length === 0">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="PassVisible" title="密码确认" width="30%">
      <el-form :model="PasswordCheck" :rules="passRules"  style="width: 100%;">
        <el-form-item label="支付密码" prop="Password">
          <el-input v-model="PasswordCheck.Password" type="password" style="width: 100%;"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="PasswordVisible = false">取消</el-button>
          <el-button type="primary" @click="getBalance(Row)"
          :disabled="PasswordCheck.Password.length === 0">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="PassError" title="支付密码错误" width="30%">
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="PasswordError">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="LossVisible" title="确认挂失" width="30%">
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="LossVisible = false">取消</el-button>
          <el-button type="primary" @click="ConfirmLoss">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import CryptoJS from 'crypto-js'
import Cookies from 'js-cookie'

export default {
  data(){
    return {
      Acc_Info: {
        username: '',
        id_number: '',
        phone_number: '',
        email: ''
      },
      Trans:{
        fromaccount: '',
        toaccount: '',
        amount: 0,
        password: '',
        message: ''
      },
      AccountData: [
        { account_number: '1123213', category: '信用卡', balance: '***', showBalance: false},
        { account_number: '3123112', category: '存折', balance: '***', showBalance: false},
        { account_number: '1231321', category: '信用卡', balance: '***', showBalance: false},
        { account_number: '1231321', category: '信用卡', balance: '***', showBalance: false},
        { account_number: '1231231', category: '存折', balance: '***', showBalance: false}
      ],
      AddAccountVisible: false,
      TransVisible: false,
      PassVisible: false,
      PasswordVisible: false,
      LossVisible: false,
      PassError: false,
      AddAccount: {
        accountType: '',
        accountNumber: '',
        paymentPassword: ''
      },
      PasswordCheck:{
        op: '',
        account_number: '',
        Password: '',
        Row: ''
      },
      Loss_account_number: '',
      rules: {
        accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }],
        accountNumber: [{ required: true, message: '请输入账户号', trigger: 'blur' }],
        paymentPassword: [{ required: true, message: '请输入支付密码', trigger: 'blur' }]
      },
      transferRules: {
        toaccount: [{ required: true, message: '请输入转入账户号', trigger: 'blur' }],
        amount: [{ required: true, message: '请输入转账金额', trigger: 'blur' }],
        password: [{ required: true, message: '请输入支付密码', trigger: 'blur' }],
      },
      passRules: {
        Password: [{ required: true, message: '请输入支付密码', trigger: 'blur' }]
      }
    }
  },
  methods:{
    JumpToCreditCard(){
      this.$router.push("/creditCard/customer/card");
    },
    OpenAddAccount(){
      this.AddAccountVisible = true;
      this.AddAccount = {
        accountNumber: '',
        paymentPassword: ''
      };
    },
    async GetAccInfo(){
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      await axios.get("/user/profile")
        .then(response => {
          if (response.data.code === 0) {
            this.Acc_Info.username = response.data.payload.username;
            this.Acc_Info.id_number = response.data.payload.id_number;
            this.Acc_Info.phone_number = response.data.payload.phone_number;
            this.Acc_Info.email = response.data.payload.email;
          } else {
            ElMessage.error(response.data.err);
            return;
          }
        })
        .catch(error => {
          console.log(error);
        })
    },
    async ConfirmAddAccount() {
      const encrypted = CryptoJS.SHA256(this.AddAccount.paymentPassword).toString();
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.post("/account/bind",
        {
          "username": this.Acc_Info.username,
          "card_id": Number(this.AddAccount.accountNumber),
          "password": encrypted
        })
        .then(response => {
          if (response.data.code == 0) {
            ElMessage.success("绑定成功");
            this.AddAccountVisible = false;
            this.QueryAccount();
          } else {
            ElMessage.error(response.data.err);
            return;
          }
        })
        .catch(error => {
          console.log(error);
        })
    },
    async QueryAccount(){
      this.AccountData = [] 
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      await axios.get("/card")
        .then(response => {
          if (response.data.code == 0) {
            response.data.payload.forEach(item => {
              this.AccountData.push({
                account_number: item.card_id,
                category: item.card_type == 'CREDIT_CARD' ? '信用卡' : '存折',
                balance: '***',
                showBalance: false
              });
            });
          } else {
            ElMessage.error(response.data.err);
            return;
          }
        })
        .catch(error => {
          console.log(error);
        })
    },
    openBalance(row){
      this.PasswordCheck.account_number = row.account_number,
      this.PassVisible = true,
      this.PasswordCheck.Row = row
    },
    async getBalance(){
      try {
        const encrypted = CryptoJS.SHA256(this.PasswordCheck.Password).toString();
        axios.defaults.headers.common['Authorization'] = Cookies.get('token');
        let response = await axios.get("/card/balance", 
          {
            "card_id": Number(this.PasswordCheck.account_number),
            "password": encrypted
          });
        this.PasswordCheck.Row.balance = parseFloat(response.data.balance).toFixed(2),
        this.PassVisible = false,
        this.PasswordCheck.Row.showBalance = true
      } catch (error) {
        console.log(error);
      }
    }, 
    closeBalance(row){
      row.balance = '***'
    },
    TransactionDetail() {
      this.$router.push({
        path: '/personalBank/transfer',
        query: { account_number: this.PasswordCheck.account_number }
      });
    },
    Transfer(row) {
      this.TransVisible = true,
      this.Trans = {
        fromaccount: row.account_number,
        toaccount: '',
        amount: 0,
        password: '',
        message: ''
      };
    },
    Password_Check(){
      const encrypted = CryptoJS.SHA256(this.PasswordCheck.Password).toString();
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.post("/password/check", 
        {
          "card_id": Number(this.PasswordCheck.account_number),
          "password": encrypted
        })
        .then(response => {
          if (response.data.code === 0){
            this.PasswordVisible = false;
            if (this.PasswordCheck.op == '1') {
              this.TransactionDetail();
            }
            if (this.PasswordCheck.op == '2') {
              this.LossVisible = true;
            }
          }
          else {
            this.PassError = true;
          }
        })
        .catch(error => {
          console.log(error);
        })
    },
    PasswordError(){
      this.PassError = false,
      this.PasswordCheck.Password = ''
    },
    async ConfirmTransfer(){ 
      const encrypted = CryptoJS.SHA256(this.Trans.password).toString();
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.post("/option/transfer",
        {
          "target_card": Number(this.Trans.toaccount),
          "card_id": Number(this.Trans.fromaccount),
          "password": encrypted,
          "amount": this.Trans.amount.toString(),
          "remark": this.Trans.message  
        })
        .then(response => {
          if (response.data.code == 0) {
            ElMessage.success("转账成功");
            this.TransVisible = false;
          } else {
            ElMessage.error(response.data.err);
            return;
          }
        })
        .catch(error => {
          console.log(error);
        })
    },
    Check(row){
      this.PasswordCheck.account_number = row.account_number,
      this.PasswordCheck.op = '1',
      this.PasswordCheck.Password = '',
      this.PasswordVisible = true
    },  
    PreLoss(row){
      this.Loss_account_number = row.account_number,
      this.PasswordCheck.account_number = row.account_number,
      this.PasswordCheck.op = '2',
      this.PasswordCheck.Password = '',
      this.PasswordVisible = true
    },
    async ConfirmLoss() { 
      axios.defaults.headers.common['Authorization'] = Cookies.get('token');
      axios.post("/card/loss",
        {
          "card_id": Number(this.Loss_account_number)
        })
        .then(response => {
          if (response.data.code == 0) {
            ElMessage.success("挂失成功");
            this.LossVisible = false;
          } else {
            ElMessage.error(response.data.err);
            return;
          }
        })
        .catch(error => {
          console.log(error);
        })
    },
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

