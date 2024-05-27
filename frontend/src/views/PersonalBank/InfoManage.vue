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
                    <div style="font-size: 24px;">账户信息管理</div>
                    <button @click="OpenAddAccount" style="position: absolute; top: 40px; right: 20px; background-color: blue; color: white; border: none; padding: 10px 20px; cursor: pointer; font-size: 16px; border-radius: 4px;">添加新账户</button>

                    <el-table :data="AccountData" height="600" 
                        style="width: 100%; margin-left: 50px; margin-top: 30px; margin-right: 50px; max-width: 80vw; table-layout: fixed;">
                        <el-table-column prop="account_number" label="账户号码" min-width="150"></el-table-column>
                        <el-table-column prop="category" label="账户类别" min-width="150"></el-table-column>
                        <el-table-column label="操作" min-width="200">
                            <template v-slot="scope">
                                <el-button type="text" @click="ConfirmPassword('1', scope.row)" style="border: 1px solid red; color: red; height: 24px; line-height: 22px;">交易明细</el-button>
                                <el-button type="text" @click="Transfer(scope.row)" style="border: 1px solid green; color: green; height: 24px; line-height: 22px;">转账</el-button>
                                <el-button type="text" @click="ConfirmPassword('2', scope.row)" style="border: 1px solid blue; color: blue; height: 24px; line-height: 22px;">挂失</el-button>
                            </template>
                        </el-table-column>
                        <el-table-column prop="balance" label="余额" min-width="150"></el-table-column>
                    </el-table>
                </div>
            </el-main>

            <el-dialog v-model="AddAccountVisible" title="绑定新账户" width="30%" >
                <el-form :model="AddAccount" :rules="rules" ref="AddAccount" style="width: 100%;">
                    <el-form-item label="账户类型" prop="accountType">
                        <el-radio-group v-model="AddAccount.accountType" @change="handleAccountTypeChange">
                            <el-radio :label="1">信用卡</el-radio>
                            <el-radio :label="2">存折</el-radio>
                        </el-radio-group>
                    </el-form-item>
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
                            :disabled="AddAccount.accountType.length === 0 || AddAccount.accountNumber.length === 0 || AddAccount.paymentPassword.length === 0">确定</el-button>
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
        </el-container>
      </div>
    </div>
  
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import CryptoJS from 'crypto-js'
import Cookies from 'js-cookie'

export default {
    data(){
        return{
            token : '',
            Acc_Info: {
                username: '',
                password: '',
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
                { account_number: '1123213', category: '信用卡', balance: 1200.3 },
                { account_number: '3123112', category: '存折', balance: 600.2 },
                { account_number: '1231321', category: '信用卡', balance: 1500 },
                { account_number: '1231321', category: '信用卡', balance: 1500 },
                { account_number: '1231231', category: '存折', balance: 200 }
            ],
            AddAccountVisible: false,
            TransVisible: false,
            PasswordVisible: false,
            LossVisible: false,
            PassError: false,
            PasswordCheck:{
                account_number: '',
                Option: '',
                Password: ''
            },
            AddAccount: {
                accountType: '',
                accountNumber: '',
                paymentPassword: ''
            },
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
        OpenAddAccount(){
            this.AddAccountVisible = true;
            this.AddAccount = {
                accountType: '',
                accountNumber: '',
                paymentPassword: ''
            };
        },
        handleAccountTypeChange(value) {
            if (value === '信用卡') {
                this.AddAccount.accountType = 1;
            }else if (value === '存折') {
                this.AddAccount.accountType = 2;
            }
        },
        async ConfirmAddAccount() {
            const encrypted = CryptoJS.SHA256(this.AddAccount.paymentPassword).toString();
            this.Acc_Info = {
                username: '',
                password: '',
                id_number: '',
                phone_number: '',
                email: ''
            };
            axios.post("/account/bind",
                {
                    "username": this.Acc_Info.username,
                    "accounttype": this.AddAccount.accountType,
                    "accountnumber": this.AddAccount.accountNumber,
                    "password": encrypted
                }, {headers: { 'token': Cookies.get('token') } })
                .then(response => {
                    ElMessage.success(response.data);
                    this.AddAccountVisible = false;
                })
                .catch(error => {
                    ElMessage.error(error.response.data);
                })
        },
        async QueryAccount(){
            this.AccountData = [] 
            let response = await axios.get("/card") 
                .then(response => {
                    let AccountData = response.data
                    AccountData.forEach(Account => { 
                        this.AccountData.push(Account) 
                    })
                })
        },
        ConfirmPassword(op, row){
            this.PasswordCheck.Password = '',
            this.PasswordCheck.Option = op,
            this.PasswordCheck.account_number = row.account_number,
            this.PasswordVisible = true
        },
        async Password_Check(){
            const encrypted = CryptoJS.SHA256(this.PasswordCheck.Password).toString();
            await axios.get("/account/pass", { params: { "account_number": this.PasswordCheck.account_number } })
                .then(response => {
                    const receivedPassword = response.data; 
                    const encryptedReceivedPassword = CryptoJS.SHA256(receivedPassword).toString();
                    if (encrypted === encryptedReceivedPassword) {
                        if (this.PasswordCheck.Option === '1') {this.TransactionDetail();}
                        if (this.PasswordCheck.Option === '2') {this.LossVisible = true;}
                        this.PasswordVisible = false;
                    } else {
                        this.PassError = true;
                        console.log("Password does not match");
                    }
            })
            .catch(error => {
                console.error("Error fetching password:", error);
            });
        },
        TransactionDetail() {
            this.$router.push({
                path: '/personalBank/transDetail',
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
        PasswordError(){
            this.PassError = false,
            this.PasswordCheck.Password = ''
        },
        async ConfirmTransfer(){
            const encrypted = CryptoJS.SHA256(this.Trans.password).toString();
            axios.post("/option/transfer",
                {
                    "target_card": Number(this.Trans.toaccount),
                    "card_id": Number(this.Trans.fromaccount),
                    "password": encrypted,
                    "amount": this.Trans.amount.toString(),
                    "remark": this.Trans.message  
                }, {headers: { 'token': Cookies.get('token') } })
                .then(response => {
                    ElMessage.success(response.data);
                    this.AddAccountVisible = false;
                })
                .catch(error => {
                    ElMessage.error(error.response.data);
                })
        },
        async ConfirmLoss() {
            axios.post("/option/loss",
                {
                    "card_id": Number(this.PasswordCheck.account_number)
                })
                .then(response => {
                    ElMessage.success(response.data);
                    this.LossVisible = false;
                })
                .catch(error => {
                    ElMessage.error(error.response.data);
                })
        },
    },
    mounted() { // 当页面被渲染时
        this.token = Cookies.get('token')
        //this.QueryAccount() // 查询账户信息
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

