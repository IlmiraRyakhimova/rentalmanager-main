<template>
  <div class="verified-page">
    <div class="verified-container">
      <div class="verified-card">
        <div v-if="loading" class="loading-state">
          <div class="spinner-large"></div>
          <p>Верифицируем ваш email...</p>
        </div>

        <div v-else-if="error" class="error-state">
          <div class="icon error-icon">❌</div>
          <h1>Ошибка верификации</h1>
          <p class="error-message">{{ error }}</p>
          <router-link to="/register" class="btn btn-primary">
            Зарегистрироваться снова
          </router-link>
        </div>

        <div v-else class="success-state">
          <div class="icon success-icon">✅</div>
          <h1>Email подтвержден!</h1>
          <p class="success-message">
            Добро пожаловать, <strong>{{ userName }}</strong>!
          </p>
          <p class="info-text">Ваш аккаунт успешно активирован. Сейчас вы будете перенаправлены...</p>
          <div class="redirect-timer">{{ countdown }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const error = ref('')
const userName = ref('')
const countdown = ref(3)

onMounted(async () => {
  const { accessToken, refreshToken, email, name, role } = route.query

  if (!accessToken || !refreshToken || !email || !name) {
    error.value = 'Неверные параметры верификации'
    loading.value = false
    return
  }

  try {
    // Сохраняем токены в store
    const authData = {
      accessToken,
      refreshToken,
      email,
      name,
      role: role || 'AGENT', // По умолчанию AGENT для обратной совместимости
    }

    // Используем внутренний метод для сохранения
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('user', JSON.stringify({ email, name, role: authData.role }))

    // Обновляем store
    authStore.initAuth()

    userName.value = name
    loading.value = false

    // Определяем куда перенаправить в зависимости от роли
    const dashboardPath = authData.role === 'OWNER' ? '/owner-dashboard' : '/dashboard'

    // Обратный отсчет и редирект
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value === 0) {
        clearInterval(timer)
        router.push(dashboardPath)
      }
    }, 1000)
  } catch (err) {
    error.value = 'Произошла ошибка при активации аккаунта'
    loading.value = false
  }
})
</script>

<style scoped>
.verified-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  padding: 20px;
}

.verified-container {
  width: 100%;
  max-width: 500px;
}

.verified-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 60px 40px;
  text-align: center;
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.loading-state,
.error-state,
.success-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.icon {
  font-size: 64px;
  animation: scaleIn 0.5s ease-out;
}

@keyframes scaleIn {
  from {
    transform: scale(0);
  }
  to {
    transform: scale(1);
  }
}

.success-icon {
  animation: scaleIn 0.5s ease-out, pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.error-icon {
  opacity: 0.8;
}

h1 {
  font-size: 28px;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.success-message {
  font-size: 18px;
  color: #2d3748;
  margin: 0;
}

.success-message strong {
  color: #1565c0;
}

.info-text {
  font-size: 14px;
  color: #718096;
  margin: 0;
}

.error-message {
  font-size: 16px;
  color: #e53e3e;
  margin: 0;
}

.redirect-timer {
  font-size: 48px;
  font-weight: 700;
  color: #1565c0;
  margin-top: 20px;
  animation: countdown 1s ease-in-out infinite;
}

@keyframes countdown {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}

.spinner-large {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(59, 130, 246, 0.2);
  border-top-color: #1565c0;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-state p {
  font-size: 16px;
  color: #718096;
  margin: 0;
}

.btn {
  padding: 14px 32px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s;
  cursor: pointer;
  border: none;
  display: inline-block;
  margin-top: 20px;
}

.btn-primary {
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(59, 130, 246, 0.3);
}

@media (max-width: 640px) {
  .verified-card {
    padding: 40px 28px;
  }

  h1 {
    font-size: 24px;
  }

  .icon {
    font-size: 48px;
  }
}
</style>
