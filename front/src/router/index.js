import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import OwnerLoginView from '@/views/OwnerLoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import DashboardView from '@/views/DashboardView.vue'
import OwnerDashboardView from '@/views/OwnerDashboardView.vue'
import EmailVerifyView from '@/views/EmailVerifyView.vue'
import EmailVerifiedView from '@/views/EmailVerifiedView.vue'
import EmailPendingView from '@/views/EmailPendingView.vue'
import ForgotPasswordView from '@/views/ForgotPasswordView.vue'
import ResetPasswordView from '@/views/ResetPasswordView.vue'
import AddApartmentView from '@/views/AddApartmentView.vue'
import AccountSettingsView from '@/views/AccountSettingsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresGuest: true },
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresGuest: true },
    },
    {
      path: '/owner-login',
      name: 'owner-login',
      component: OwnerLoginView,
      meta: { requiresGuest: true },
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { requiresGuest: true },
    },
    {
      path: '/email-pending',
      name: 'email-pending',
      component: EmailPendingView,
      meta: { requiresGuest: true },
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: ForgotPasswordView,
      meta: { requiresGuest: true },
    },
    {
      path: '/reset-password',
      name: 'reset-password',
      component: ResetPasswordView,
      meta: { requiresGuest: true },
    },
    {
      path: '/auth/verify',
      name: 'email-verify',
      component: EmailVerifyView,
    },
    {
      path: '/auth/verified',
      name: 'email-verified',
      component: EmailVerifiedView,
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView,
      meta: { requiresAuth: true, roles: ['AGENT', 'ADMIN'] },
    },
    {
      path: '/owner-dashboard',
      name: 'owner-dashboard',
      component: OwnerDashboardView,
      meta: { requiresAuth: true, roles: ['OWNER'] },
    },
    {
      path: '/apartments/add',
      name: 'add-apartment',
      component: AddApartmentView,
      meta: { requiresAuth: true, roles: ['AGENT', 'ADMIN'] },
    },
    {
      path: '/account-settings',
      name: 'account-settings',
      component: AccountSettingsView,
      meta: { requiresAuth: true, roles: ['AGENT', 'OWNER', 'ADMIN'] },
    },
  ],
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const userRole = authStore.user?.role

  // Функция для получения правильного dashboard по роли
  const getDashboardByRole = (role) => {
    if (role === 'OWNER') {
      return 'owner-dashboard'
    }
    return 'dashboard'
  }

  // Проверка требуется ли аутентификация
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' })
  }
  // Проверка ролей
  else if (to.meta.requiresAuth && to.meta.roles && !to.meta.roles.includes(userRole)) {
    // Перенаправляем на соответствующий dashboard
    next({ name: getDashboardByRole(userRole) })
  }
  // Проверка для гостевых страниц (если пользователь уже авторизован)
  else if (to.meta.requiresGuest && authStore.isAuthenticated) {
    next({ name: getDashboardByRole(userRole) })
  } else {
    next()
  }
})

export default router
