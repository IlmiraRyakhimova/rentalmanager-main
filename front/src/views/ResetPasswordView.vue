<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">RENTAL MANAGER</h1>
          <p class="auth-subtitle">Сброс пароля</p>
        </div>

        <!-- Форма сброса пароля -->
        <form v-if="!resetSuccess" @submit.prevent="handleSubmit" class="auth-form">
          <p class="form-description">
            Введите новый пароль для вашей учетной записи.
          </p>

          <!-- Новый пароль -->
          <div class="form-group">
            <label for="password" class="form-label">
              Новый пароль <span class="required">*</span>
            </label>
            <div class="password-wrapper">
              <input
                id="password"
                v-model="formData.password"
                :type="showPassword ? 'text' : 'password'"
                class="form-input"
                :class="{ 'input-error': errors.password }"
                placeholder="Минимум 8 символов"
                @blur="validateField('password')"
                @input="clearError('password')"
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
            <div class="password-strength">
              <div
                class="password-strength-bar"
                :class="'strength-' + passwordStrength"
                :style="{ width: passwordStrengthWidth }"
              ></div>
            </div>
          </div>

          <!-- Подтверждение пароля -->
          <div class="form-group">
            <label for="confirmPassword" class="form-label">
              Подтвердите пароль <span class="required">*</span>
            </label>
            <div class="password-wrapper">
              <input
                id="confirmPassword"
                v-model="formData.confirmPassword"
                :type="showConfirmPassword ? 'text' : 'password'"
                class="form-input"
                :class="{ 'input-error': errors.confirmPassword }"
                placeholder="Повторите пароль"
                @blur="validateField('confirmPassword')"
                @input="clearError('confirmPassword')"
              />
              <button
                type="button"
                class="password-toggle"
                @click="showConfirmPassword = !showConfirmPassword"
                tabindex="-1"
              >
                {{ showConfirmPassword ? '👁️' : '👁️‍🗨️' }}
              </button>
            </div>
            <span v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</span>
          </div>

          <!-- Общая ошибка -->
          <div v-if="serverError" class="server-error">
            {{ serverError }}
          </div>

          <!-- Кнопка отправки -->
          <button type="submit" class="submit-btn" :disabled="loading || !isFormValid">
            <span v-if="loading" class="spinner"></span>
            <span v-else>Сбросить пароль</span>
          </button>
        </form>

        <!-- Успешный сброс -->
        <div v-else class="success-message">
          <div class="success-icon">✅</div>
          <h3>Пароль изменен!</h3>
          <p>
            Ваш пароль успешно обновлен. Теперь вы можете войти с новым паролем.
          </p>
          <router-link to="/login" class="submit-btn" style="display: inline-block; text-decoration: none;">
            Войти
          </router-link>
        </div>

        <div v-if="!resetSuccess" class="auth-footer">
          <p class="footer-text">
            <router-link to="/login" class="footer-link">Вернуться к входу</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { authApi } from '@/api/auth'

const route = useRoute()

const formData = ref({
  password: '',
  confirmPassword: '',
})

const token = ref('')
const errors = ref({})
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const loading = ref(false)
const serverError = ref('')
const resetSuccess = ref(false)

// Извлекаем токен из URL
onMounted(() => {
  token.value = route.query.token || ''
  if (!token.value) {
    serverError.value = 'Токен сброса пароля не найден. Запросите новую ссылку.'
  }
})

// Валидация полей
const validators = {
  password: (value) => {
    if (!value) return 'Пароль обязателен для заполнения'
    if (value.length < 8) return 'Пароль должен содержать минимум 8 символов'
    if (value.length > 72) return 'Пароль не должен превышать 72 символа'
    return null
  },
  confirmPassword: (value) => {
    if (!value) return 'Подтвердите пароль'
    if (value !== formData.value.password) return 'Пароли не совпадают'
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
  return (
    token.value &&
    formData.value.password &&
    formData.value.confirmPassword &&
    Object.keys(errors.value).length === 0
  )
})

// Оценка сложности пароля
const passwordStrength = computed(() => {
  const password = formData.value.password
  if (!password) return 0

  let strength = 0
  if (password.length >= 8) strength++
  if (password.length >= 12) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^A-Za-z0-9]/.test(password)) strength++

  if (strength <= 2) return 1
  if (strength <= 3) return 2
  return 3
})

const passwordStrengthWidth = computed(() => {
  return passwordStrength.value * 33.33 + '%'
})

const handleSubmit = async () => {
  // Валидация всех полей
  Object.keys(validators).forEach(validateField)

  if (!isFormValid.value) return

  loading.value = true
  serverError.value = ''

  try {
    await authApi.resetPassword(token.value, formData.value.password)
    resetSuccess.value = true
  } catch (error) {
    if (error.response?.status === 400) {
      serverError.value = 'Недействительный или истекший токен. Запросите новую ссылку.'
    } else if (error.response?.data?.message) {
      serverError.value = error.response.data.message
    } else {
      serverError.value = 'Произошла ошибка. Попробуйте позже.'
    }
  } finally {
    loading.value = false
  }
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

.password-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  padding-right: 45px;
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

.password-toggle {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  padding: 4px;
}

.password-strength {
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  overflow: hidden;
  margin-top: 4px;
}

.password-strength-bar {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 2px;
}

.strength-0 {
  width: 0%;
}

.strength-1 {
  background: #e53e3e;
}

.strength-2 {
  background: #ecc94b;
}

.strength-3 {
  background: #48bb78;
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
  margin: 0 0 24px;
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
