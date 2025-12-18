<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">RENTAL MANAGER</h1>
          <p class="auth-subtitle">Регистрация агента</p>
        </div>

        <form @submit.prevent="handleSubmit" class="auth-form">
          <!-- Имя -->
          <div class="form-group">
            <label for="name" class="form-label">
              Полное имя <span class="required">*</span>
            </label>
            <input
              id="name"
              v-model="formData.name"
              type="text"
              class="form-input"
              :class="{ 'input-error': errors.name }"
              placeholder="Иван Иванов"
              @blur="validateField('name')"
              @input="clearError('name')"
            />
            <span v-if="errors.name" class="error-message">{{ errors.name }}</span>
          </div>

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
            />
            <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
          </div>

          <!-- Телефон -->
          <div class="form-group">
            <label for="phone" class="form-label">
              Номер телефона <span class="required">*</span>
            </label>
            <input
              id="phone"
              v-model="formData.phoneNumber"
              type="tel"
              class="form-input"
              :class="{ 'input-error': errors.phoneNumber }"
              placeholder="+79991234567"
              @blur="validateField('phoneNumber')"
              @input="clearError('phoneNumber')"
            />
            <span v-if="errors.phoneNumber" class="error-message">{{ errors.phoneNumber }}</span>
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
            <span v-if="errors.confirmPassword" class="error-message">{{
              errors.confirmPassword
            }}</span>
          </div>

          <!-- Общая ошибка -->
          <div v-if="serverError" class="server-error">
            {{ serverError }}
          </div>

          <!-- Кнопка отправки -->
          <button type="submit" class="submit-btn" :disabled="loading || !isFormValid">
            <span v-if="loading" class="spinner"></span>
            <span v-else>Подтвердить email и зарегистрироваться</span>
          </button>
        </form>

        <div class="auth-footer">
          <p class="footer-text">
            Уже есть аккаунт?
            <router-link to="/login" class="footer-link">Войти</router-link>
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

const router = useRouter()
const authStore = useAuthStore()

const formData = ref({
  name: '',
  email: '',
  phoneNumber: '',
  password: '',
  confirmPassword: '',
  role: 'AGENT',
})

const errors = ref({})
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const loading = ref(false)
const serverError = ref('')

// Валидация полей
const validators = {
  name: (value) => {
    if (!value || value.trim().length === 0) return 'Имя обязательно для заполнения'
    if (value.trim().length < 2) return 'Имя должно содержать минимум 2 символа'
    return null
  },
  email: (value) => {
    if (!value) return 'Email обязателен для заполнения'
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) return 'Введите корректный email'
    return null
  },
  phoneNumber: (value) => {
    if (!value) return 'Номер телефона обязателен для заполнения'
    const phoneRegex = /^\+?\d{10,15}$/
    if (!phoneRegex.test(value)) return 'Введите корректный номер телефона (10-15 цифр)'
    return null
  },
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
    formData.value.name &&
    formData.value.email &&
    formData.value.phoneNumber &&
    formData.value.password &&
    formData.value.confirmPassword &&
    Object.keys(errors.value).length === 0
  )
})

// Сила пароля
const passwordStrength = computed(() => {
  const password = formData.value.password
  if (!password) return 0

  let strength = 0
  if (password.length >= 8) strength++
  if (password.length >= 12) strength++
  if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++
  if (/\d/.test(password)) strength++
  if (/[^a-zA-Z\d]/.test(password)) strength++

  return Math.min(strength, 4)
})

const passwordStrengthWidth = computed(() => {
  return `${(passwordStrength.value / 4) * 100}%`
})

const handleSubmit = async () => {
  // Валидация всех полей
  Object.keys(validators).forEach(validateField)

  if (!isFormValid.value) return

  loading.value = true
  serverError.value = ''

  try {
    const { confirmPassword, ...signUpData } = formData.value
    await authStore.signUp(signUpData)
    router.push({ name: 'email-pending', query: { email: formData.value.email } })
  } catch (error) {
    if (error.response?.data?.message) {
      serverError.value = error.response.data.message
    } else if (error.response?.status === 400) {
      serverError.value = 'Пользователь с таким email уже существует'
    } else {
      serverError.value = 'Произошла ошибка при регистрации. Попробуйте позже.'
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
  background: linear-gradient(135deg, #0ea5e9 0%, #3b82f6 100%);
  padding: 20px;
}

.auth-container {
  width: 100%;
  max-width: 480px;
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

.password-strength {
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  overflow: hidden;
  margin-top: 4px;
}

.password-strength-bar {
  height: 100%;
  transition: all 0.3s;
  border-radius: 2px;
}

.strength-0 {
  width: 0%;
  background: #e2e8f0;
}

.strength-1 {
  background: #e53e3e;
}

.strength-2 {
  background: #ed8936;
}

.strength-3 {
  background: #ecc94b;
}

.strength-4 {
  background: #48bb78;
}

.error-message {
  font-size: 13px;
  color: #e53e3e;
  margin-top: -4px;
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
