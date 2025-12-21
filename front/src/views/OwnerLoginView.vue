<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">RENTAL MANAGER</h1>
          <p class="auth-subtitle">Вход для собственников</p>
        </div>

        <form @submit.prevent="handleSubmit" class="auth-form">
          <!-- Email -->
          <div class="form-group">
            <label for="email" class="form-label">
              Email <span class="required">*</span>
            </label>
            <input
              id="email"
              v-model="formData.email"
              type="email"
              class="form-input"
              :class="{ 'input-error': errors.email }"
              placeholder="owner@example.com"
              @blur="validateField('email')"
              @input="clearError('email')"
              autocomplete="email"
            />
            <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
          </div>

          <!-- Пароль -->
          <div class="form-group">
            <label for="password" class="form-label">
              Пароль <span class="required">*</span>
            </label>
            <div class="password-wrapper">
              <input
                id="password"
                v-model="formData.password"
                :type="showPassword ? 'text' : 'password'"
                class="form-input"
                :class="{ 'input-error': errors.password }"
                placeholder="Введите пароль"
                @blur="validateField('password')"
                @input="clearError('password')"
                autocomplete="current-password"
              />
              <button
                type="button"
                class="password-toggle"
                @click="showPassword = !showPassword"
                tabindex="-1"
              >
                {{ showPassword ? '👁️' : '👁️‍🗨️' }}
              </button>
            </div>
            <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
          </div>

          <!-- Забыли пароль -->
          <div class="forgot-password-link">
            <router-link to="/forgot-password" class="link">Забыли пароль?</router-link>
          </div>

          <!-- Общая ошибка -->
          <div v-if="serverError" class="server-error">
            {{ serverError }}
          </div>

          <!-- Кнопка отправки -->
          <button type="submit" class="submit-btn" :disabled="loading || !isFormValid">
            <span v-if="loading" class="spinner"></span>
            <span v-else>Войти</span>
          </button>

          <!-- Кнопка подтверждения email -->
          <button
            type="button"
            class="verify-email-btn"
            @click="handleResendVerification"
            :disabled="resendingVerification || !formData.email"
          >
            <span v-if="resendingVerification" class="spinner spinner-dark"></span>
            <span v-else>📧 Подтвердить email</span>
          </button>
        </form>

        <div class="auth-footer">
          <p class="footer-text">
            Вы агент?
            <router-link to="/login" class="footer-link">Вход для агентов</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/auth'

const router = useRouter()
const authStore = useAuthStore()

const formData = ref({
  email: '',
  password: '',
})

const errors = ref({})
const serverError = ref('')
const loading = ref(false)
const showPassword = ref(false)
const resendingVerification = ref(false)

const validators = {
  email: (value) => {
    if (!value) return 'Email обязателен'
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) return 'Введите корректный email'
    return null
  },
  password: (value) => {
    if (!value) return 'Пароль обязателен'
    if (value.length < 6) return 'Пароль должен быть не менее 6 символов'
    return null
  },
}

const validateField = (field) => {
  const error = validators[field](formData.value[field])
  if (error) {
    errors.value[field] = error
  } else {
    delete errors.value[field]
  }
}

const clearError = (field) => {
  delete errors.value[field]
  serverError.value = ''
}

const isFormValid = computed(() => {
  return formData.value.email && formData.value.password && Object.keys(errors.value).length === 0
})

const handleSubmit = async () => {
  // Валидация всех полей
  Object.keys(validators).forEach(validateField)

  if (!isFormValid.value) return

  loading.value = true
  serverError.value = ''

  try {
    const response = await authStore.signIn(formData.value)

    // Проверяем что это действительно собственник
    if (response.role !== 'OWNER') {
      serverError.value = 'Этот аккаунт не является аккаунтом собственника'
      await authStore.clearAuthData()
      loading.value = false
      return
    }

    router.push('/owner-dashboard')
  } catch (error) {
    if (error.response?.status === 401) {
      serverError.value = 'Неверный email или пароль'
    } else if (error.response?.data?.message) {
      serverError.value = error.response.data.message
    } else {
      serverError.value = 'Произошла ошибка при входе. Попробуйте позже.'
    }
  } finally {
    loading.value = false
  }
}

const handleResendVerification = async () => {
  if (!formData.value.email) {
    errors.value.email = 'Введите email для подтверждения'
    return
  }

  const emailError = validators.email(formData.value.email)
  if (emailError) {
    errors.value.email = emailError
    return
  }

  resendingVerification.value = true
  serverError.value = ''

  try {
    await authApi.resendVerificationEmail(formData.value.email)
    router.push({ name: 'email-pending' })
  } catch (error) {
    if (error.response?.data?.message) {
      serverError.value = error.response.data.message
    } else {
      serverError.value = 'Ошибка при отправке письма. Попробуйте позже.'
    }
  } finally {
    resendingVerification.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 450px;
}

.auth-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 40px;
  animation: fadeIn 0.6s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-title {
  font-size: 32px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 8px 0;
  letter-spacing: 2px;
}

.auth-subtitle {
  font-size: 16px;
  color: #1565c0;
  margin: 0;
  font-weight: 500;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 14px;
  font-weight: 600;
  color: #2d3748;
}

.required {
  color: #e53e3e;
}

.form-input {
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 15px;
  transition: all 0.3s;
  outline: none;
}

.form-input:focus {
  border-color: #1565c0;
  box-shadow: 0 0 0 3px rgba(30, 136, 229, 0.1);
}

.form-input.input-error {
  border-color: #fc8181;
}

.password-wrapper {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
  padding: 4px;
  opacity: 0.6;
  transition: opacity 0.3s;
}

.password-toggle:hover {
  opacity: 1;
}

.error-message {
  color: #e53e3e;
  font-size: 13px;
}

.forgot-password-link {
  text-align: right;
  margin-top: -8px;
}

.link {
  color: #1565c0;
  font-size: 14px;
  text-decoration: none;
  transition: color 0.3s;
}

.link:hover {
  color: #1565c0;
  text-decoration: underline;
}

.server-error {
  padding: 12px 16px;
  background: #fed7d7;
  color: #c53030;
  border-radius: 8px;
  font-size: 14px;
  text-align: center;
}

.submit-btn {
  padding: 14px;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 8px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(30, 136, 229, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.verify-email-btn {
  padding: 14px;
  background: white;
  color: #1565c0;
  border: 2px solid #1565c0;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.verify-email-btn:hover:not(:disabled) {
  background: #e3f2fd;
}

.verify-email-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner-dark {
  border: 2px solid rgba(30, 136, 229, 0.3);
  border-top-color: #1565c0;
}

.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.auth-footer {
  margin-top: 24px;
  text-align: center;
}

.footer-text {
  color: #718096;
  font-size: 14px;
  margin: 0;
}

.footer-link {
  color: #1565c0;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

.footer-link:hover {
  color: #1565c0;
  text-decoration: underline;
}

/* Responsive */
@media (max-width: 480px) {
  .auth-card {
    padding: 30px 20px;
  }

  .auth-title {
    font-size: 28px;
  }
}
</style>
