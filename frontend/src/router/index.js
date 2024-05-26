// 引入 Vue 3 的路由模块
import { createRouter, createWebHistory } from 'vue-router';

// 定义路由
const routes = [
    {
        path: '/personalBank/login',
        name: 'login',
        component: () => import('../views/PersonalBank/LoginView.vue')
    },
    {
        path: '/personalBank/register',
        name: 'register',
        component: () => import('../views/PersonalBank/RegisterView.vue')
    },
    {
        path: '/personalBank/forget',
        name: 'forget',
        component: () => import('../views/PersonalBank/ForgetPassword.vue')
    },
    {
        path: '/',
        redirect: '/personalBank/login'
    }
];

// 创建并配置路由
const router = createRouter({
    history: createWebHistory(),
    routes,  // 等同于 routes: routes
});

export default router;