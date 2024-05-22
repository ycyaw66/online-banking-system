// 引入 Vue 3 的路由模块
import {createRouter, createWebHistory} from 'vue-router';

// 定义路由
const routes = [
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
        path: '/creditCard/customer/info',
        name: 'customerInfo',
        component: () => import('../views/CreditCard/customer/CustomerInfoView.vue')
    },
    {
        path: '/creditCard/customer/card',
        name: 'cardInfo',
        component: () => import('../views/CreditCard/customer/AboutCreditCardView.vue')
    },
    {
        path: '/creditCard/customer/pay',
        name: 'bankPay',
        component: () => import('../views/CreditCard/customer/BankPayView.vue')
    },
    {
        path: '/creditCard/customer/simulation',
        name: 'paymentSimulation',
        component: () => import('../views/CreditCard/customer/PaymentSimulationView.vue')
    },
    {
        path: '/creditCard/customer/response',
        name: 'responseOfRequest',
        component: () => import('../views/CreditCard/customer/ResponseView.vue')
    },
    {
        path:'/creditCard/admin/inspector',
        name:'adminManageInspector',
        component:()=>import('../views/CreditCard/admin/ManageInspectorView.vue')
    },
    {
        path:'/creditCard/inspector/request',
        name:'responseToRequests',
        component:()=>import('../views/CreditCard/inspector/CustomerRequestView.vue')
    },
    {
        path: '/',
        redirect: '/creditCard/customer/login'
    }
];

// 创建并配置路由
const router = createRouter({
    history: createWebHistory(),
    routes,  // 等同于 routes: routes
});

export default router;