import {createRouter, createWebHashHistory, type RouteRecordRaw} from "vue-router";

const routes:RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Home',
        component: () => import('../views/Home.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/DashBoard.vue'),
        children: [
            {
                path: "",
                name: "default",
                component: () => import("../views/Statistic.vue"),
            },
            {
                path: "user",
                name: "user",
                component: () => import("../views/Users.vue"),
            },
            {
                path: "device",
                name: "Device",
                component: () => import("../views/Device.vue"),
            },
            {
                path: "order",
                name: "Order",
                component: () => import("../views/Order.vue"),
            },
            {
                path: "behavior",
                name: "UserBehavior",
                component: () => import("../views/UserBehavior.vue"),
            },

            {
                path: "setting",
                name: "setting",
                component: () => import("../views/Setting.vue"),
            },]
    },
    // 404页面
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
    }
]

const router=createRouter({
    history:createWebHashHistory(),
    routes
})

export default router