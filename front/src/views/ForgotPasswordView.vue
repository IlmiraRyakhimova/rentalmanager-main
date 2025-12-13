<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">RENTAL MANAGER</h1>
          <p class="auth-subtitle">Восстановление пароля</p>
        </div>

        <!-- Форма запроса восстановления -->
        <form v-if="!emailSent" @submit.prevent="handleSubmit" class="auth-form">
          <p class="form-description">
            Введите email, указанный при регистрации. Мы отправим вам ссылку для сброса пароля.
          </p>

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

          <!-- Общая ошибка -->
          <div v-if="serverError" class="server-error">
            {{ serverError }}
          </div>

          <!-- Кнопка отправки -->
          <button type="submit" class="submit-btn" :disabled="loading || !isFormValid">
            <span v-if="loading" class="spinner"></span>
            <span v-else>Отправить ссылку</span>
          </button>
        </form>

        <!-- Успешная отправка -->
        <div v-else class="success-message">
          <div class="success-icon">✉️</div>
          <h3>Письмо отправлено!</h3>
          <p>
            Мы отправили ссылку для сброса пароля на
            <strong>{{ formData.email }}</strong>.
            Проверьте вашу почту.
          </p>
          <p class="hint">Если письмо не пришло, проверьте папку "Спам".</p>
          <button @click="resetForm" class="btn-secondary">Отправить повторно</button>
        </div>

        <div class="auth-footer">
          <p class="footer-text">
            Вспомнили пароль?
            <router-link to="/login" class="footer-link">Войти</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

const formData = ref({
  email: '',
})

const errors = ref({})
const loading = ref(false)
const serverError = ref('')
const emailSent = ref(false)

// Валидация полей
const validators = {
  email: (value) => {
    if (!value) return 'Email обязателен для заполнения'
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) return 'Введите корректный email'
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
  return formData.value.email && Object.keys(errors.value).length === 0
})

const handleSubmit = async () => {
  // Валидация всех полей
  Object.keys(validators).forEach(validateField)

  if (!isFormValid.value) return

  loading.value = true
  serverError.value = ''

  try {
    await authApi.forgotPassword(formData.value.email)
    emailSent.value = true
  } catch (error) {
    if (error.response?.data?.message) {
      serverError.value = error.response.data.message
    } else {
      serverError.value = 'Произошла ошибка. Попробуйте позже.'
    }
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  emailSent.value = false
  serverError.value = ''
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  margin: 0 0 8px;
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

.form-description {
  color: #4a5568;
  font-size: 14px;
  line-height: 1.5;
  margin: 0;
  text-align: center;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
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
  font-size: 16px;
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}

.input-error {
  border-color: #e53e3e;
}

.input-error:focus {
  box-shadow: 0 0 0 3px rgba(229, 62, 62, 0.15);
}

.error-message {
  font-size: 13px;
  color: #e53e3e;
}

.server-error {
  background: #fed7d7;
  color: #c53030;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  text-align: center;
}

.submit-btn {
  padding: 14px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  min-height: 50px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.success-message {
  text-align: center;
  padding: 20px 0;
}

.success-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.success-message h3 {
  color: #1a202c;
  font-size: 20px;
  margin: 0 0 12px;
}

.success-message p {
  color: #4a5568;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 8px;
}

.success-message .hint {
  color: #718096;
  font-size: 13px;
  margin-bottom: 24px;
}

.btn-secondary {
  padding: 12px 24px;
  background: transparent;
  color: #667eea;
  border: 2px solid #667eea;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background: #667eea;
  color: white;
}

.auth-footer {
  margin-top: 32px;
  text-align: center;
}

.footer-text {
  font-size: 14px;
  color: #718096;
  margin: 0;
}

.footer-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s ease;
}

.footer-link:hover {
  color: #764ba2;
}
</style>
