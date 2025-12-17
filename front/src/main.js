import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { useAuthStore } from '@/stores/auth'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// Инициализация auth store из localStorage
const authStore = useAuthStore()
authStore.initAuth()

// Логирование для диагностики
console.log('Auth initialized:', {
  isAuthenticated: authStore.isAuthenticated,
  user: authStore.user,
  hasToken: !!authStore.accessToken
})

app.mount('#app')
