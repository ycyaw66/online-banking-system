// 引入 Vue 3 的路由模块
import {createRouter, createWebHistory} from 'vue-router';
import AdminBaseLayout from '../components/AdminBaseLayout.vue';
import UserBaseLayout from '../components/UserBaseLayout.vue';
import creditCardUserBaseLayout from "@/components/CreditCardUserBaseLayout.vue";
import DataOperatorBaseLayout from '../components/DataOperatorLayout.vue';
import UserFcBaseLayout from '../components/UserFcLayout.vue';

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

export default router;