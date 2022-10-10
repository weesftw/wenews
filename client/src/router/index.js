import AuthService from '../service/UserService'
import {createRouter, createWebHashHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ChatView from '../views/ChatView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import DashboardView from '../views/DashboardView.vue'

const routes = [
    {
        path: '/',
        component: HomeView
    },
    {
        path: '/chat',
        component: ChatView,
        beforeEnter(_, __, next) {
            if (!AuthService.isSignedIn()) {
                next('/signIn')
                return;
            }
            next();
        }
    },
    {
        path: '/signIn',
        component: LoginView,
        beforeEnter (_, __, next) {
            if (!AuthService.isSignedIn()) {
                next();
                return;
            }
            next('/')
        }
    },
    {
        path: '/register',
        component: RegisterView
    },
    {
        path: '/dashboard',
        component: DashboardView,
        beforeEnter(_, __, next) {
            if (!AuthService.isSignedIn()) {
                next('/signIn')
                return;
            }

            if(AuthService.isAdmin()) {
                next();
                return;
            }

            next('/');
        }
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router