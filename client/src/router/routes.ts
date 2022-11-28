import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/IndexPage.vue') },
      { path: 'news/:id', component: () => import('pages/ChatPage.vue'), meta: { auth: true, socket: true } }
    ],
  },
  {
    path: '/dashboard',
    component: () => import('layouts/DashboardLayout.vue'),
    meta: {
      auth: true,
      staff: true
    },
    children: [
      { path: '', component: () => import('pages/DashboardPage.vue') },
      { path: 'category', component: () => import('pages/dashboard/CategoryDashboardPage.vue') },
      { path: 'news', component: () => import('pages/dashboard/NewsDashboardPage.vue') },
      { path: 'users', component: () => import('pages/dashboard/UsersDashboardPage.vue') }
    ],
  },
  {
    path: '/',
    component: () => import('layouts/AuthLayout.vue'),
    children: [
      { path: 'login', component: () => import('pages/auth/LoginPage.vue')},
      { path: 'register', component: () => import('pages/auth/RegisterPage.vue')}
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
