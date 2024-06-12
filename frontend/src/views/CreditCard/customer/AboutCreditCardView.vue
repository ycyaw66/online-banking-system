<template>
  <div
      style="display: flex; justify-content: space-between; align-items: center; margin-top: 20px; margin-left: 20px;">
    <div style="font-weight: bold; font-size: 1.5em;">用户信用卡信息</div>
    <el-button type="primary"
               @click="new_card.limit = '0', new_card.first_password = '', new_card.second_password = '', add_new_card_visible = true">
      注册新的信用卡
    </el-button>
  </div>

  <!-- 信用卡卡片显示区 -->
  <div style="display: flex;flex-wrap: wrap; justify-content: start;">

    <!-- 信用卡卡片 -->
    <div class="cardBox" v-for="card in credit_cards" :key="card.id" style="background-color: white;">
      <div>
        <!-- 卡片内容 -->
        <div style="margin-left: 10px; text-align: start; font-size: 16px;">
          <p>信用卡 ID: {{ card.id }}</p>
          <p>信用卡总额度(元): {{ card.card_limit }}</p>
          <p>信用卡已用额度(元): {{ card.loan }}</p>
          <p>信用卡可用额度(元): {{ (card.card_limit - card.loan) }}</p>
          <p>是否挂失: {{ card.is_lost === 1 ? '已挂失' : '未挂失' }}</p>
        </div>

        <el-divider/>

        <!-- 卡片操作 -->
        <div style="margin-top: 10px;" v-if="card.is_lost === 0">
          <el-button type="primary"
                     @click="modify_password_card_id = card.id, modify_password_old_password = card.password, modify_password.old_password = '', modify_password.new_password = '', modify_password.new_password_again = '', modify_password_visible = true">
            修改密码
          </el-button>
          <el-button type="primary"
                     @click="modify_limit_loan = card.loan, modify_limit_card_id = card.id, modify_limit_password = card.password, modify_limit.password = '', modify_limit.new_limit = '0', modify_card_limit_visible = true">
            修改额度
          </el-button>
          <el-button type="primary"
                     @click="return_money.card_id = card.id, return_money_password = card.password, return_money.amount = '', return_money.loan = card.loan, return_money.password = '', return_money_visible = true">
            还款
          </el-button>
          <br><br>
          <el-button type="warning"
                     @click="card_lost_visible = true, lost_card_id = card.id, lost_card_password = card.password, this.lost_card.password = ''">
            挂失信用卡
          </el-button>
          <el-button type="danger"
                     @click="cancel_card_id = card.id, cancel_card_password = card.password, cancel_card_loan = card.loan, this.cancel_card.password = '', card_cancel_visible = true">
            注销信用卡
          </el-button>
        </div>
        <div style="margin-top: 10px; color: red; font-weight: bold;" v-else>
          <p>您不能对挂失的信用卡进行操作</p>
        </div>

      </div>
    </div>

    <!--表单区域-->
    <el-dialog title="新建信用卡" v-model="add_new_card_visible" style="width: 25vw;">
      <el-form :model="new_card">
        <el-form-item label="申请额度" :label-width="formLabelWidth">
          <el-input v-model="new_card.limit" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请输入密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="new_card.first_password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
        <el-form-item label="请再次输入密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="new_card.second_password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="add_new_card_visible = false">取 消</el-button>
        <el-button type="primary" @click="addNewCard">确 定</el-button>
      </template>
    </el-dialog>

    <el-dialog title="修改密码" v-model="modify_password_visible" style="width: 25vw;">
      <el-form :model="modify_password">
        <el-form-item label="请输入原密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="modify_password.old_password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
        <el-form-item label="请输入新密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="modify_password.new_password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
        <el-form-item label="请再次输入新密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="modify_password.new_password_again" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="modify_password_visible = false">取 消</el-button>
        <el-button type="primary"
                   @click="modifyPassword(modify_password_card_id)">确 定
        </el-button>
      </template>
    </el-dialog>

    <el-dialog title="修改额度" v-model="modify_card_limit_visible" style="width: 25vw;">
      <el-form :model="modify_limit">
        <el-form-item label="请输入申请额度" :label-width="formLabelWidth">
          <el-input v-model="modify_limit.new_limit" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请输入密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="modify_limit.password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="modify_card_limit_visible = false">取 消</el-button>
        <el-button type="primary"
                   @click="modifyLimit(modify_limit_card_id, modify_limit_password, modify_limit_loan)">确 定
        </el-button>
      </template>
    </el-dialog>

    <el-dialog title="信用卡挂失" v-model="card_lost_visible" style="width: 25vw;">
      <el-form :model="lost_card">
        <el-form-item label="请输入信用卡密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="lost_card.password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="card_lost_visible = false">取 消</el-button>
        <el-button type="primary" @click="reportLost(lost_card_id)">确 定</el-button>
      </template>
    </el-dialog>

    <el-dialog title="信用卡注销" v-model="card_cancel_visible" style="width: 25vw;">
      <el-form :model="cancel_card">
        <el-form-item label="请输入信用卡密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="cancel_card.password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="card_cancel_visible = false">取 消</el-button>
        <el-button type="primary"
                   @click="cancelCard(cancel_card_id, cancel_card_loan)">
          确 定
        </el-button>
      </template>
    </el-dialog>

    <el-dialog title="还款" v-model="return_money_visible" style="width: 25vw;">
      <el-form :model="return_money">
        <el-form-item label="请输入还款金额" :label-width="formLabelWidth">
          <el-input v-model="return_money.amount" autocomplete="off" style="width: 12.5vw;"></el-input>
        </el-form-item>
        <el-form-item label="请输入密码" :label-width="formLabelWidth">
          <el-input type="password" v-model="return_money.password" autocomplete="off"
                    style="width: 12.5vw;" show-password></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="return_money_visible = false">取 消</el-button>
        <el-button type="primary" @click="returnMoney()">确 定</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script>

import axios from "axios";
import Cookies from "js-cookie";
import CryptoJS from "crypto-js";

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
      formLabelWidth: '150px',
      id_number: Cookies.get('credit_card_user_id_card'),
      credit_cards: [{
        id: '1',
        id_number: '3220101234',
        card_limit: '100',
        loan: '10',
        is_lost: '0'
      }, {
        id: '2',
        id_number: '3220105678',
        card_limit: '200',
        loan: '20',
        is_lost: '1'
      }, {
        id: '3',
        id_number: '3220109123',
        card_limit: '300',
        loan: '30',
        is_lost: '0'
      }],
      new_card: {
        limit: '',
        first_password: '',
        second_password: '',
      },
      add_new_card_visible: false,
      modify_password_visible: false,
      modify_card_limit_visible: false,
      card_lost_visible: false,
      card_cancel_visible: false,
      return_money_visible: false,
      modify_password: {
        old_password: '',
        new_password: '',
        new_password_again: '',
      },
      modify_limit: {
        new_limit: '',
        password: '',
      },
      lost_card: {
        password: '',
      },
      cancel_card: {
        password: ''
      },
      return_money: {
        card_id: '',
        password: '',
        amount: '',
        loan: '',
      },
      modify_password_old_password: '',
      modify_password_card_id: '',
      modify_limit_password: '',
      modify_limit_card_id: '',
      modify_limit_loan: '',
      lost_card_password: '',
      lost_card_id: '',
      cancel_card_password: '',
      cancel_card_id: '',
      cancel_card_loan: '',
      return_money_password: '',
    }
  },
  methods: {
    exit() {
      this.$store.state.user.ID_number = '';
      this.$router.push('/personalBank/user/account');
    },
    addNewCard() {
      // 检查信用卡额度是否为空
      if (isNaN(this.new_card.limit) || this.new_card.limit === '') {
        // 如果不是数字，就弹出警告并返回，不继续执行函数
        this.$message.error('请输入数字作为信用卡额度！');
        return;
      }
      var limit = this.new_card.limit * 100;
      var isInt = limit % 1 === 0;
      if (!isInt) {
        this.$message.error('额度的小数部分最多两位');
        return;
      }
      // 检查密码是否为空
      if (this.new_card.first_password === '' || this.new_card.second_password === '') {
        this.$message.error('密码不能为空');
        return;
      }
      // 检查两次密码输入是否一致
      if (this.new_card.first_password !== this.new_card.second_password) {
        // 如果不一致，就弹出警告并返回，不继续执行函数
        this.$message.error('两次输入的密码不一致，请重新输入！');
        return;
      }
      // 检查密码是否超过50个字符
      if (this.new_card.first_password.length > 50) {
        // 如果超过50个字符，就弹出警告并返回，不继续执行函数
        this.$message.error('密码不能超过50个字符！');
        return;
      }

      // 如果密码一致，就关闭模态框并弹出成功提示
      //this.$message.success('添加信用卡额度为' + this.new_card.limit)
      const encrypted_password = CryptoJS.SHA256(this.new_card.first_password).toString();

      axiosInstance.post("/credit-card/register", null, {
        params: {
          card_limit: this.new_card.limit,
          password: encrypted_password
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
        } else {
          this.$message.success("申请成功")
        }
        this.add_new_card_visible = false;
        this.queryCards();
      }).catch(error => {
        console.error('POST error:', error);
        this.$message.error('请求失败');
      });

    },
    modifyPassword(card_id) {// 修改信用卡密码
      // 检查新密码是否为空
      if (this.modify_password.new_password === '') {
        this.$message.error('新密码不能为空');
        return;
      }
      // if (this.modify_password.old_password !== password) {
      //   // 如果不一致，就弹出警告并返回，不继续执行函数
      //   this.$message.error('原密码错误!');
      //   return;
      // }
      if (this.modify_password.new_password !== this.modify_password.new_password_again) {
        // 如果不一致，就弹出警告并返回，不继续执行函数
        this.$message.error('两次输入的新密码不一致，请重新输入！');
        return;
      }
      // 检查新密码是否超过50个字符
      if (this.modify_password.new_password.length > 50) {
        // 如果超过50个字符，就弹出警告并返回，不继续执行函数
        this.$message.error('新密码不能超过50个字符！');
        return;
      }
      // this.$message.success('修改信用卡' + card_id + '密码成功，新密码为' + this.modify_password.new_password);

      const encrypted_new_password = CryptoJS.SHA256(this.modify_password.new_password).toString();
      const encrypted_old_password = CryptoJS.SHA256(this.modify_password.old_password).toString();


      axiosInstance.post("/credit-card/modify-password", null, {
        params: {
          card_id: card_id,
          old_password: encrypted_old_password,
          new_password: encrypted_new_password,
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
          return;
        } else {
          this.$message.success("修改成功")
        }
        this.modify_password_visible = false;
        this.queryCards();
      }).catch(error => {
        console.error('POST error:', error);
        this.$message.error('修改失败');
      })

    },
    modifyLimit(card_id, password, loan) {// 修改信用卡额度
      if (isNaN(this.modify_limit.new_limit) || this.modify_limit.new_limit === '') {
        // 如果不是数字，就弹出警告并返回，不继续执行函数
        this.$message.error('请输入数字作为信用卡额度！');
        return;
      }
      console.log(this.modify_limit.new_limit);
      console.log(loan);
      var limit = this.modify_limit.new_limit * 100;
      var isInt = limit % 1 === 0;
      if (!isInt) {
        this.$message.error('新额度小数部分最多两位');
        return;
      }
      // 检查新的额度是否超过现有的贷款额度
      if (this.modify_limit.new_limit < loan) {
        this.$message.error('新额度不能小于当前欠款');
        return;
      }
      // if (this.modify_limit.password !== password) {
      //   // 如果不一致，就弹出警告并返回，不继续执行函数
      //   this.$message.error('信用卡密码错误!');
      //   return;
      // }
      //this.$message.success('修改信用卡' + card_id + '的额度至' + limit + '元');

      const encrypted_old_password = CryptoJS.SHA256(this.modify_limit.password).toString();

      axiosInstance.post("/credit-card/update-limit", null, {
        params: {
          card_id: card_id,
          limit: this.modify_limit.new_limit,
          password: encrypted_old_password
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
          return;
        } else {
          this.$message.success('申请成功')
        }
        this.modify_card_limit_visible = false;
        this.queryCards();
      }).catch(error => {
        console.error('POST error:', error);
        this.$message.error('申请失败');
      })

    },
    reportLost(card_id) {
      // if (this.lost_card.password !== password) {
      //   // 如果不一致，就弹出警告并返回，不继续执行函数
      //   this.$message.error('信用卡密码错误!');
      //   return;
      // }
      // this.$message.success('挂失信用卡id为' + card_id)

      const encrypted_password = CryptoJS.SHA256(this.lost_card.password).toString();

      axiosInstance.post("/credit-card/lost", null, {params: {card_id: card_id, password: encrypted_password}})
          .then(response => {
            if (response.data.code === 1) {
              this.$message.error(response.data.err);
              return;
            } else {
              this.$message.success('挂失成功');
            }
            this.card_lost_visible = false;
            this.queryCards();
          }).catch(error => {
        console.error('GET error:', error);
        this.$message.error('挂失失败');
      })

    },
    cancelCard(card_id, loan) {
      if (loan !== 0) {
        this.$message.error('信用卡还存在欠额,无法注销!');
        return;
      }
      // if (this.cancel_card.password !== password) {
      //   // 如果不一致，就弹出警告并返回，不继续执行函数
      //   this.$message.error('信用卡密码错误!');
      //   return;
      // }
      // this.$message.success('注销信用卡id为' + card_id)

      const encrypted_password = CryptoJS.SHA256(this.cancel_card.password).toString();

      axiosInstance.post("/credit-card/delete", null, {params: {card_id: card_id, password: encrypted_password}})
          .then(response => {
            if (response.data.code === 1) {
              this.$message.error(response.data.err);
              return;
            } else {
              this.$message.success('注销成功')
            }
            this.card_cancel_visible = false;
            this.queryCards();
          }).catch(error => {
        console.error('GET error:', error);
        this.$message.error('注销失败');
      })

    },
    returnMoney() {
      // 检查密码是否为空
      if (this.return_money.password === '') {
        this.$message.error('密码不能为空');
        return;
      }

      // 检查密码是否正确
      // if (this.return_money.password !== password) {
      //   this.$message.error('密码错误');
      //   return;
      // }

      // 检查还款金额是否为空
      if (this.return_money.amount === '') {
        this.$message.error('还款金额不能为空');
        return;
      }

      // 判断还款金额是否为数字
      if (isNaN(this.return_money.amount)) {
        this.$message.error('还款金额必须为数字');
        return;
      }

      // 判断输入的还款金额是否大于欠额（loan）
      console.log('还款金额为' + this.return_money.amount + ' and 欠额为' + this.return_money.loan);
      if (Number(this.return_money.amount) > Number(this.return_money.loan)) {
        this.$message.error('还款金额不能大于信用卡欠额');
        return;
      }

      var amount = this.return_money.amount * 100;
      var isInt = amount % 1 === 0;
      if (!isInt) {
        this.$message.error('还款金额的小数部分最多两位');
        return;
      }

      //this.$message.success('还款信用卡id为' + this.return_money.card_id + '成功，还款金额为' + this.return_money.amount);

      const encrypted_password = CryptoJS.SHA256(this.return_money.password).toString();
      // 如果所有验证都通过，执行后续还款操作
      axiosInstance.post("/credit-card/return", null, {
        params: {
          card_id: this.return_money.card_id,
          amount: this.return_money.amount,
          password: encrypted_password,
        }
      }).then(response => {
        if (response.data.code === 1) {
          this.$message.error(response.data.err);
          return;
        } else {
          this.$message.success('还款成功');
        }
        this.return_money_visible = false;
        this.queryCards();
      }).catch(error => {
        console.error('POST error:', error);
        this.$message.error('还款失败');
      })

    },
    queryCards() {
      this.credit_cards = [];
      axiosInstance.get('/credit-card/getAllCard')
          .then(response => {
            let cards = response.data.payload;
            // console.log(cards);
            cards.forEach(card => {
              this.credit_cards.push(card);
            })
          }).catch(error => {
        console.error('query cards error:', error);
      })
    }
  },
  mounted() {
    this.queryCards();
  }
}
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

.cardBox {
  height: 330px;
  width: 280px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  text-align: center;
  margin-top: 40px;
  margin-left: 27.5px;
  margin-right: 10px;
  padding: 7.5px;
  padding-right: 10px;
  padding-top: 15px;
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

.aside {
  min-height: calc(100vh - 60px);
  width: 180px;
  background-color: red;
}

.logout-button {
  position: absolute;
  bottom: 10%;
  left: 50%;
  transform: translateX(-50%);
}

</style>