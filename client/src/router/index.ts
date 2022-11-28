import {route} from 'quasar/wrappers';
import {
  createMemoryHistory,
  createRouter,
  createWebHashHistory,
  createWebHistory,
} from 'vue-router';
import routes from './routes';
import {useUserStore} from 'stores/UserStore';

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default route(function (/* { store, ssrContext } */) {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : (process.env.VUE_ROUTER_MODE === 'history' ? createWebHistory : createWebHashHistory);

  const userStore = useUserStore();

  const Router = createRouter({
    scrollBehavior: () => ({left: 0, top: 0}),
    routes,

    // Leave this as is and make changes in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    history: createHistory(
      process.env.MODE === 'ssr' ? void 0 : process.env.VUE_ROUTER_BASE
    ),
  });

  Router.beforeEach((to, from, next) => {
    const userConnected = userStore.getUser
    if(userStore.socket != null) {
      userStore.disconnectSocket()
    }

    if (to.matched.some(route => route.meta.auth)) {
      if (userStore.isAuth) {
        if (userConnected != null) {
          if (userConnected.banned) {
            next('/')
            return;
          } else {
            if (to.matched.some(route => route.meta.staff)) {
              if (!userConnected.roles.includes('ROLE_ADMIN' || 'ROLE_MOD')) {
                next('/')
                return;
              }
            }
          }

          next();
        }
      } else {
        next('/login');
      }
    } else {
      next();
    }
  });

  return Router;
});
