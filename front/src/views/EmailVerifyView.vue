<template>
  <div class="verify-page">
    <div class="verify-container">
      <div class="verify-card">
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
          <p class="info-text">Ваш аккаунт успешно активирован. Сейчас вы будете перенаправлены на вход...</p>
          <div class="redirect-timer">{{ countdown }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const error = ref('')
const userName = ref('')
const countdown = ref(3)

onMounted(async () => {
  const token = route.query.token

  if (!token) {
    error.value = 'Токен верификации не найден'
    loading.value = false
    return
  }

  try {
    // Вызываем API бэкенда для верификации
    const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
    const response = await axios.get(`${API_URL}/api/auth/verify-email?token=${token}`)

    // Бекенд возвращает URL для редиректа с токенами
    const redirectUrl = response.data
    
    if (redirectUrl && typeof redirectUrl === 'string' && redirectUrl.includes('/auth/verified')) {
      // Перенаправляем на URL с токенами
      window.location.href = redirectUrl
    } else {
      // Старая логика для обратной совместимости
      userName.value = 'Пользователь'
      loading.value = false

      const timer = setInterval(() => {
        countdown.value--
        if (countdown.value === 0) {
          clearInterval(timer)
          router.push('/login')
        }
      }, 1000)
    }
  } catch (err) {
    console.error('Verification error:', err)
    if (err.response?.status === 404) {
      error.value = 'Токен верификации не найден или истек'
    } else if (err.response?.data?.message) {
      error.value = err.response.data.message
    } else {
      error.value = 'Произошла ошибка при верификации. Попробуйте позже.'
    }
    loading.value = false
  }
})
</script>

<style scoped>
.verify-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3b82f6 0%, #0ea5e9 100%);
  padding: 20px;
}

.verify-container {
  width: 100%;
  max-width: 500px;
}

.verify-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 50px 40px;
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

.spinner-large {
  width: 60px;
  height: 60px;
  border: 5px solid rgba(59, 130, 246, 0.2);
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.icon {
  font-size: 80px;
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
  filter: drop-shadow(0 4px 8px rgba(72, 187, 120, 0.3));
}

.error-icon {
  filter: drop-shadow(0 4px 8px rgba(229, 62, 62, 0.3));
}

h1 {
  font-size: 32px;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.success-message,
.error-message {
  font-size: 18px;
  color: #4a5568;
  margin: 0;
}

.success-message strong {
  color: #3b82f6;
}

.info-text {
  font-size: 14px;
  color: #718096;
  margin: 10px 0 0;
}

.redirect-timer {
  font-size: 48px;
  font-weight: 700;
  color: #3b82f6;
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.btn {
  padding: 14px 32px;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  margin-top: 10px;
}

.btn-primary {
  background: linear-gradient(135deg, #3b82f6 0%, #0ea5e9 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}
</style>
