// 引入 Vue 3 的路由模块
import { createRouter, createWebHistory } from 'vue-router';
import AdminBaseLayout from '../components/AdminBaseLayout.vue';
import UserBaseLayout from '../components/UserBaseLayout.vue';

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
            }
        ]
    },
    {
        path: '/personalBank/admin',
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
        ]
    },
];

// 创建并配置路由
const router = createRouter({
    history: createWebHistory(),
    routes,  // 等同于 routes: routes
});

export default router;