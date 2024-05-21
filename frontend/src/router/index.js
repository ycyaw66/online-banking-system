// 引入 Vue 3 的路由模块
import { createRouter, createWebHistory } from 'vue-router';

// 定义路由
const routes = [
    {
        path: '/creditCard/firstView',
        name: 'firstView',
        component: () => import('../views/CreditCard/FirstView.vue')
    },
    {
        path: '/creditCard/secondview',
        name: 'secondview',
        component: () => import('../views/CreditCard/SecondView.vue')
    },
    {
        path: '/',
        redirect: '/creditCard/firstView'
    }
];

// 创建并配置路由
const router = createRouter({
    history: createWebHistory(),
    routes,  // 等同于 routes: routes
});

export default router;