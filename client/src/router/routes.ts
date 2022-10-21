import { RouteRecordRaw } from 'vue-router';
import { userService } from 'src/service/UserService';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('pages/IndexPage.vue') }],
  },
  {
    path: '/',
    component: () => import('layouts/BlankLayout.vue'),
    children: [
      { path: '/login', component: () => import('pages/auth/LoginPage.vue') },
      { path: '/register', component: () => import('pages/auth/RegisterPage.vue') }
    ],
    beforeEnter (_, __, next) {
      if (!userService.isSignedIn()) {
        next();
        return;
      }
      next('/')
    }
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
