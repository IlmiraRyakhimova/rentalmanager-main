<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">RENTAL MANAGER</h1>
          <p class="auth-subtitle">Вход для агентов</p>
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
              placeholder="agent@example.com"
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

          <!-- Подтвердить email -->
          <div class="verify-email-section">
            <button
              type="button"
              class="verify-email-btn"
              @click="showVerifyEmailModal = true"
            >
              📧 Подтвердить email
            </button>
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
        </form>

        <div class="auth-footer">
          <p class="footer-text">
            Нет аккаунта?
            <router-link to="/register" class="footer-link">Зарегистрироваться</router-link>
          </p>
        </div>
      </div>
    </div>

    <!-- Модальное окно подтверждения email -->
    <div v-if="showVerifyEmailModal" class="modal-overlay" @click.self="showVerifyEmailModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Подтверждение email</h2>
          <button @click="showVerifyEmailModal = false" class="modal-close">&times;</button>
        </div>
        <form @submit.prevent="handleResendVerification" class="verify-form">
          <p class="modal-description">
            Введите ваш email для повторной отправки письма с подтверждением.
            Это необходимо, если вы зарегистрировались как собственник при добавлении апартаментов.
          </p>
          <div class="form-group">
            <label for="verifyEmail" class="form-label">Email</label>
            <input
              id="verifyEmail"
              v-model="verifyEmail"
              type="email"
              class="form-input"
              placeholder="owner@example.com"
              required
            />
          </div>
          <div v-if="verifyError" class="server-error">{{ verifyError }}</div>
          <div v-if="verifySuccess" class="success-message">{{ verifySuccess }}</div>
          <div class="modal-actions">
            <button type="button" @click="showVerifyEmailModal = false" class="btn-secondary">
              Отмена
            </button>
            <button type="submit" class="btn-primary" :disabled="verifyLoading">
              <span v-if="verifyLoading" class="spinner"></span>
              <span v-else>Отправить письмо</span>
            </button>
          </div>
        </form>
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
const showPassword = ref(false)
const loading = ref(false)
const serverError = ref('')

// Модальное окно подтверждения email
const showVerifyEmailModal = ref(false)
const verifyEmail = ref('')
const verifyLoading = ref(false)
const verifyError = ref('')
const verifySuccess = ref('')

// Валидация полей
const validators = {
  email: (value) => {
    if (!value) return 'Email обязателен для заполнения'
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) return 'Введите корректный email'
    return null
  },
  password: (value) => {
    if (!value) return 'Пароль обязателен для заполнения'
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
    await authStore.signIn(formData.value)
    router.push('/dashboard')
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
  verifyLoading.value = true
  verifyError.value = ''
  verifySuccess.value = ''

  try {
    await authApi.resendVerificationEmail(verifyEmail.value)
    verifySuccess.value = 'Письмо с подтверждением отправлено на указанный email'
    setTimeout(() => {
      showVerifyEmailModal.value = false
      router.push({ path: '/email-pending', query: { email: verifyEmail.value } })
    }, 1500)
  } catch (error) {
    if (error.response?.status === 404) {
      verifyError.value = 'Пользователь с таким email не найден'
    } else if (error.response?.data?.message) {
      verifyError.value = error.response.data.message
    } else {
      verifyError.value = 'Произошла ошибка. Попробуйте позже.'
    }
  } finally {
    verifyLoading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0ea5e9 0%, #3b82f6 100%);
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 440px;
}

.auth-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 40px;
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

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 8px 0;
  letter-spacing: 1px;
}

.auth-subtitle {
  font-size: 16px;
  color: #718096;
  margin: 0;
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
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.2s;
  outline: none;
  box-sizing: border-box;
}

.form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-input.input-error {
  border-color: #e53e3e;
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
  font-size: 18px;
  padding: 4px;
  opacity: 0.6;
  transition: opacity 0.2s;
}

.password-toggle:hover {
  opacity: 1;
}

.error-message {
  font-size: 13px;
  color: #e53e3e;
  margin-top: -4px;
}

.forgot-password-link {
  text-align: right;
  margin-top: -8px;
}

.forgot-password-link .link {
  font-size: 13px;
  color: #3b82f6;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.forgot-password-link .link:hover {
  color: #0ea5e9;
  text-decoration: underline;
}

.verify-email-section {
  margin-top: 8px;
}

.verify-email-btn {
  width: 100%;
  padding: 12px;
  background: transparent;
  border: 2px solid #3b82f6;
  color: #3b82f6;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.verify-email-btn:hover {
  background: rgba(59, 130, 246, 0.1);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 16px;
  max-width: 450px;
  width: 100%;
  padding: 32px;
  animation: slideUp 0.3s ease-out;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.modal-header h2 {
  font-size: 22px;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.modal-close {
  background: none;
  border: none;
  font-size: 28px;
  color: #718096;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.modal-close:hover {
  color: #1a202c;
}

.modal-description {
  font-size: 14px;
  color: #4a5568;
  line-height: 1.6;
  margin: 0 0 20px 0;
}

.verify-form .form-group {
  margin-bottom: 20px;
}

.modal-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.btn-secondary {
  flex: 1;
  padding: 12px;
  background: transparent;
  border: 2px solid #e2e8f0;
  color: #718096;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-secondary:hover {
  border-color: #3b82f6;
  color: #3b82f6;
}

.btn-primary {
  flex: 1;
  padding: 12px;
  background: linear-gradient(135deg, #0ea5e9 0%, #3b82f6 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.success-message {
  padding: 12px;
  background: #f0fff4;
  border: 1px solid #9ae6b4;
  border-radius: 8px;
  color: #276749;
  font-size: 14px;
  text-align: center;
}

.server-error {
  padding: 12px;
  background: #fff5f5;
  border: 1px solid #feb2b2;
  border-radius: 8px;
  color: #c53030;
  font-size: 14px;
  text-align: center;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #0ea5e9 0%, #3b82f6 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 8px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(59, 130, 246, 0.3);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
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
  font-size: 14px;
  color: #718096;
  margin: 0;
}

.footer-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.footer-link:hover {
  color: #0ea5e9;
  text-decoration: underline;
}

@media (max-width: 640px) {
  .auth-card {
    padding: 28px 24px;
  }

  .auth-title {
    font-size: 24px;
  }
}
</style>
