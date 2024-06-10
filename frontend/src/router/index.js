// 引入 Vue 3 的路由模块
import {createRouter, createWebHistory} from 'vue-router';
import AdminBaseLayout from '../components/AdminBaseLayout.vue';
import UserBaseLayout from '../components/UserBaseLayout.vue';
import creditCardUserBaseLayout from "@/components/CreditCardUserBaseLayout.vue";
import DataOperatorBaseLayout from '../components/DataOperatorLayout.vue';
import UserFcBaseLayout from '../components/UserFcLayout.vue';
import ManagerMainLayout from "@/components/ManagerMainLayout.vue";
import OfficerMainLayout from "@/components/OfficerMainLayout.vue";
import UserMainLayout from "@/components/UserMainLayout.vue";

// loan
import LoginSelector from '@/views/Loan/LoginSelector.vue';
import OfficerLogin from '@/views/Loan/OfficerLogin.vue';
import ManagerLogin from '@/views/Loan/ManagerLogin.vue';
// Officer 子路由组件
import LoanApproval from '@/views/Loan/LoanApproval.vue';
import LoanHistory from '@/views/Loan/LoanHistory.vue';
import LoanInquire from '@/views/Loan/LoanInquire.vue';
import SecretKey from '@/views/Loan/changerView.vue';
// Manager 子路由组件
import add from '@/views/Loan/addView.vue';
import change1 from '@/views/Loan/change1View.vue';
import change2 from '@/views/Loan/change2View.vue';
import Mdelete from '@/views/Loan/deleteView.vue';

import userLoanVue from '@/views/Loan/userloanView.vue'
import userPayBackVue from '@/views/Loan/userpaybackView.vue'
import userInquireVue from '@/views/Loan/userinquireView.vue'
import InformVue from '@/views/Loan/informView.vue'
import userCreditVue from '@/views/Loan/usercreditView.vue'

// 定义路由
const routes = [
    {
        path: '/',
        redirect: '/personalBank/user/login'
    },
    {
        path: '/personalBank/user/login',
        name: 'login',
        component: () => import('../views/PersonalBank/LoginView.vue')
    },
    {
        path: '/personalBank/admin/login',
        name: 'adminLogin',
        component: () => import('../views/PersonalBank/AdminLoginView.vue')
    },
    {
        path: '/personalBank/user/register',
        name: 'register',
        component: () => import('../views/PersonalBank/RegisterView.vue')
    },
    {
        path: '/personalBank/user/forget',
        name: 'forget',
        component: () => import('../views/PersonalBank/ForgetPassword.vue')
    },
    {
        path: '/officer-login',
        name: 'OfficerLogin',
        component: OfficerLogin
    },
    {
        path: '/manager-login',
        name: 'ManagerLogin',
        component: ManagerLogin
    },
    {
        path: '/loan/admin',
        name: 'LoginSelector',
        component: LoginSelector
    },
    {
        path: '/user-main',
        name: 'userMain',
        component: UserMainLayout,
        meta: { requiresAuth: true },
        children: [
            {
                path: 'ask',
                name: 'userloanVue',
                component: userLoanVue,
                meta: { requiresAuth: true }
            },
            {
                path: 'back',
                name: 'userpaybackVue',
                component: userPayBackVue,
                meta: { requiresAuth: true }
            },
            {
                path: 'inquire',
                name: 'userinquireVue',
                component: userInquireVue,
                meta: { requiresAuth: true }
            },
            {
                path: 'me',
                name: 'usercreditVue',
                component: userCreditVue,
                meta: { requiresAuth: true }
            },
            {
                path: 'inform',
                name: 'InformVue',
                component: InformVue,
                meta: { requiresAuth: true }
            }
        ]
    },
    {
        path: '/officer-main',
        name: 'OfficerMain',
        component: OfficerMainLayout,
        meta: { requiresAuth: true },
        children: [
            {
                path: 'loan-approval',
                name: 'LoanApproval',
                component: LoanApproval,
                meta: { requiresAuth: true }
            },
            {
                path: 'loan-history',
                name: 'LoanHistory',
                component: LoanHistory,
                meta: { requiresAuth: true }
            },
            {
                path: 'loan-inquire',
                name: 'LoanInquire',
                component: LoanInquire,
                meta: { requiresAuth: true }
            },
            {
                path: 'secret',
                name: 'SecretKey',
                component: SecretKey,
                meta: { requiresAuth: true }
            }
        ]
    },
    {
        path: '/manager-main',
        name: 'ManagerMain',
        component: ManagerMainLayout,
        meta: { requiresAuth: true },
        children: [
            {
                path: 'add',
                name: 'add',
                component: add,
                meta: { requiresAuth: true }
            },
            {
                path: 'change1',
                name: 'change1',
                component: change1,
                meta: { requiresAuth: true }
            },
            {
                path: 'change2',
                name: 'change2',
                component: change2,
                meta: { requiresAuth: true }
            },
            {
                path: 'delete',
                name: 'delete',
                component: Mdelete,
                meta: { requiresAuth: true }
            }
        ]
    },
    {
        path: '/fc/data_operator',
        component: DataOperatorBaseLayout,
        children: [
            {
                path: 'ability',
                name: 'opability',
                component: () => import('../views/ForeignCurrencySystem/dataoperator/DataOperator.vue')
            },
            {
                path: 'account',
                name: 'opaccount',
                component: () => import('../views/ForeignCurrencySystem/dataoperator/DataOperatorAccount.vue')
            }
        ]
    },
    {
        path: '/fc/admin/login',
        name: 'fcAdminLogin',
        component: () => import('../views/ForeignCurrencySystem/fcAdmin/FcAdminLoginView.vue')
    },
    {
        path: '/fc/admin/home',
        name: 'fcAdminHome',
        component: () => import('../views/ForeignCurrencySystem/fcAdmin/fcAdminHomeView.vue')
    },
    {
        path: '/fc/admin/operationRecord',
        name: 'operationRecordQuery',
        component: () => import('../views/ForeignCurrencySystem/fcAdmin/fcAdminOperationQuery.vue')
    },
    {
        path: '/fc/data_operator/start',
        name: 'data_operator_login',
        component: () => import('../views/ForeignCurrencySystem/dataoperator/DataOperatorLogin.vue')
    },
    {
        path: '/fc/user',
        component: UserFcBaseLayout,
        children: [
            {
                path: 'record',
                name: 'fctrade_record',
                component: () => import('../views/ForeignCurrencySystem/TradeRecord.vue')
            },
            {
                path: 'account',
                name: 'fcaccount',
                component: () => import('../views/ForeignCurrencySystem/ForeignAccount.vue')
            },
            {
                path: 'trade',
                name: 'fctrade',
                component: () => import('../views/ForeignCurrencySystem/FcTrade.vue')
            },
            {
                path: 'query',
                name: 'fcquery',
                component: () => import('../views/ForeignCurrencySystem/ForeignCurrencyQuery.vue')
            }
        ]
    },
    {
        path: '/personalBank/user',
        component: UserBaseLayout,
        children: [
            {
                path: 'profile',
                name: 'profile',
                component: () => import('../views/PersonalBank/UserProfile.vue')
            },
            {
                path: 'account',
                name: 'account',
                component: () => import('../views/PersonalBank/InfoManage.vue')
            },
            {
                path: 'transfer',
                name: 'transfer',
                component: () => import('../views/PersonalBank/TransDetail.vue')
            },
            {
                path: 'foreign_exchange',
                redirect: '/fc/user/record',
            }
        ]
    },
    {
        path: '/onlineBank/admin',
        component: AdminBaseLayout,
        children: [
            {
                path: 'privilege',
                name: 'privilege',
                component: () => import('../views/PersonalBank/Privilege.vue')
            },
            {
                path: 'blackList',
                name: 'blackList',
                component: () => import('../views/PersonalBank/BlackList.vue')
            },
            {
                path: 'inspector',
                name: 'adminManageInspector',
                component: () => import('../views/CreditCard/admin/ManageInspectorView.vue')
            },
            {
                path: 'cashier',
                name: 'adminManageCashier',
                component: () => import('../views/Counter/admin/ManageCashierView.vue')
            },
        ]
    },
    {
        path: '/creditCard/customer',
        component: creditCardUserBaseLayout,
        children: [
            {
                path: 'info',
                name: 'customerInfo',
                component: () => import('../views/CreditCard/customer/CustomerInfoView.vue')
            },
            {
                path: 'card',
                name: 'cardInfo',
                component: () => import('../views/CreditCard/customer/AboutCreditCardView.vue')
            },
            {
                path: 'pay',
                name: 'bankPay',
                component: () => import('../views/CreditCard/customer/BankPayView.vue')
            },
            {
                path: 'simulation',
                name: 'paymentSimulation',
                component: () => import('../views/CreditCard/customer/PaymentSimulationView.vue')
            },
            {
                path: 'response',
                name: 'responseOfRequest',
                component: () => import('../views/CreditCard/customer/ResponseView.vue')
            },
        ]
    },
    {
        path: '/creditCard/inspector/login',
        name: 'loginInspector',
        component: () => import('../views/CreditCard/login/InspectorLoginView.vue')
    },
    {
        path: '/creditCard/admin/login',
        name: 'loginAdmin',
        component: () => import('../views/CreditCard/login/AdminLoginView.vue')
    },
    {
        path: '/creditCard/customer/login',
        name: 'loginCustomer',
        component: () => import('../views/CreditCard/login/CustomerLoginView.vue')
    },
    {
        path: '/creditCard/inspector/request',
        name: 'responseToRequests',
        component: () => import('../views/CreditCard/inspector/CustomerRequestView.vue')
    },
    {
        path: '/counter/admin/login',
        name: 'loginAdminOfCashier',
        component: () => import('../views/Counter/login/AdminLoginView.vue')
    },
    {
        path: '/counter/cashier/login',
        name: 'loginCashier',
        component: () => import('../views/Counter/login/CashierLoginView.vue')
    },
    {
        path: '/counter/cashier/currentDeposit',
        name: 'cashierCurrentDeposit',
        component: () => import('../views/Counter/cashier/CurrentDepositView.vue')
    },
    {
        path: '/counter/cashier/currentWithdrawal',
        name: 'cashierCurrentWithdrawal',
        component: () => import('../views/Counter/cashier/CurrentWithdrawalView.vue')
    },
    {
        path: '/counter/cashier/freeAndUnfreeze',
        name: 'cashierFreeAndUnfreeze',
        component: () => import('../views/Counter/cashier/FreezeAndUnfreezewView.vue')
    },
    {
        path: '/counter/cashier/lossAndReissue',
        name: 'cashierLossAndReissue',
        component: () => import('../views/Counter/cashier/LossAndReissueView.vue')
    },
    {
        path: '/counter/cashier/openAccount',
        name: 'cashierOpenAccount',
        component: () => import('../views/Counter/cashier/OpenAccountView.vue')
    },
    {
        path: '/counter/cashier/timeDeposit',
        name: 'cashierTimeDeposit',
        component: () => import('../views/Counter/cashier/TimeDepositView.vue')
    },
    {
        path: '/counter/cashier/timeWithdrawal',
        name: 'cashierTimeWithdrawal',
        component: () => import('../views/Counter/cashier/TimeWithdrawalView.vue')
    },
    {
        path: '/counter/cashier/transferAccount',
        name: 'cashierTransferAccount',
        component: () => import('../views/Counter/cashier/TransferAccountView.vue')
    },

];

// 创建并配置路由
const router = createRouter({
    history: createWebHistory(),
    routes,  // 等同于 routes: routes
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    console.log(`Navigating to: ${to.name}, Token: ${token}`);

    if (to.matched.some(record => record.meta.requiresAuth) && !token) {
        console.log('No token found, redirecting to login...');
        next({ name: 'LoginSelector' });
    } else if ((to.name === 'OfficerMain' || to.name === 'ManagerMain') && !from.name) {
        // Prevent direct access to the main route
        console.log('Direct access to main route is not allowed, redirecting to login...');
        next({ name: 'LoginSelector' });
    } else {
        next();
    }
});
export default router;