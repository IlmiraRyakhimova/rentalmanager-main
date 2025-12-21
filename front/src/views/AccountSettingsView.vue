<template>
  <div class="account-settings">
    <header class="settings-header">
      <div class="header-content">
        <h1 class="settings-title">RENTAL MANAGER</h1>
        <div class="header-actions">
          <button @click="goBack" class="back-btn">
            ← Назад
          </button>
        </div>
      </div>
    </header>

    <main class="settings-main">
      <div class="settings-container">
        <h2 class="section-title">Настройки аккаунта</h2>

        <!-- Информация о пользователе -->
        <div class="user-info-section">
          <h3>Информация о профиле</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">Имя:</span>
              <span class="info-value">{{ user?.name }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Email:</span>
              <span class="info-value">{{ user?.email }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Телефон:</span>
              <span class="info-value">{{ user?.phoneNumber }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">Роль:</span>
              <span class="info-value">{{ getRoleText(user?.role) }}</span>
            </div>
          </div>
        </div>

        <!-- Редактирование профиля -->
        <div class="profile-edit-section">
          <h3>Редактирование профиля</h3>

          <form @submit.prevent="handleUpdateProfile" class="profile-form">
            <div class="form-group">
              <label for="name">Имя</label>
              <input
                type="text"
                id="name"
                v-model="profileForm.name"
                placeholder="Введите ваше имя"
                required
              />
            </div>

            <div class="form-group">
              <label for="phoneNumber">Номер телефона</label>
              <input
                type="tel"
                id="phoneNumber"
                v-model="profileForm.phoneNumber"
                placeholder="+66812345678"
                required
              />
            </div>

            <div v-if="profileError" class="error-message">
              {{ profileError }}
            </div>

            <div v-if="profileSuccess" class="success-message">
              {{ profileSuccess }}
            </div>

            <button
              type="submit"
              class="submit-btn"
              :disabled="profileLoading"
            >
              <span v-if="profileLoading" class="spinner"></span>
              <span v-else>Сохранить изменения</span>
            </button>
          </form>
        </div>

        <!-- Смена пароля -->
        <div class="password-section">
          <h3>Смена пароля</h3>

          <form @submit.prevent="handleChangePassword" class="password-form">
            <div class="form-group">
              <label for="oldPassword">Текущий пароль</label>
              <input
                type="password"
                id="oldPassword"
                v-model="passwordForm.oldPassword"
                placeholder="Введите текущий пароль"
                required
              />
            </div>

            <div class="form-group">
              <label for="newPassword">Новый пароль</label>
              <input
                type="password"
                id="newPassword"
                v-model="passwordForm.newPassword"
                placeholder="Введите новый пароль (минимум 8 символов)"
                required
                minlength="8"
              />
            </div>

            <div class="form-group">
              <label for="confirmPassword">Подтверждение пароля</label>
              <input
                type="password"
                id="confirmPassword"
                v-model="passwordForm.confirmPassword"
                placeholder="Повторите новый пароль"
                required
              />
            </div>

            <div v-if="error" class="error-message">
              {{ error }}
            </div>

            <div v-if="success" class="success-message">
              {{ success }}
            </div>

            <button
              type="submit"
              class="submit-btn"
              :disabled="loading"
            >
              <span v-if="loading" class="spinner"></span>
              <span v-else>Изменить пароль</span>
            </button>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import apiClient from '@/api/axios'

const router = useRouter()
const authStore = useAuthStore()

const user = computed(() => authStore.user)

const profileForm = ref({
  name: '',
  phoneNumber: ''
})

const profileLoading = ref(false)
const profileError = ref(null)
const profileSuccess = ref(null)

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const loading = ref(false)
const error = ref(null)
const success = ref(null)

// Инициализация формы профиля данными пользователя
onMounted(() => {
  if (user.value) {
    profileForm.value = {
      name: user.value.name || '',
      phoneNumber: user.value.phoneNumber || ''
    }
  }
})

function getRoleText(role) {
  const roles = {
    AGENT: 'Агент',
    OWNER: 'Собственник',
    ADMIN: 'Администратор'
  }
  return roles[role] || role
}

function goBack() {
  const userRole = authStore.user?.role
  if (userRole === 'OWNER') {
    router.push({ name: 'owner-dashboard' })
  } else {
    router.push({ name: 'dashboard' })
  }
}

async function handleUpdateProfile() {
  profileError.value = null
  profileSuccess.value = null

  // Валидация
  if (!profileForm.value.name || profileForm.value.name.trim() === '') {
    profileError.value = 'Имя не может быть пустым'
    return
  }

  if (!profileForm.value.phoneNumber || profileForm.value.phoneNumber.trim() === '') {
    profileError.value = 'Телефон не может быть пустым'
    return
  }

  profileLoading.value = true

  try {
    // Изменяем имя пользователя
    await apiClient.post('/api/account-settings/change-user-name', {
      newName: profileForm.value.name
    })

    // Изменяем номер телефона
    await apiClient.post('/api/account-settings/change-phone-number', {
      newPhoneNumber: profileForm.value.phoneNumber
    })

    // Обновляем данные пользователя в store
    await authStore.fetchUserProfile()

    profileSuccess.value = 'Профиль успешно обновлён'
  } catch (err) {
    console.error('Ошибка обновления профиля:', err)
    profileError.value = err.response?.data?.message || err.response?.data || 'Ошибка при обновлении профиля'
  } finally {
    profileLoading.value = false
  }
}

async function handleChangePassword() {
  error.value = null
  success.value = null

  // Валидация
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    error.value = 'Пароли не совпадают'
    return
  }

  if (passwordForm.value.newPassword.length < 8) {
    error.value = 'Пароль должен содержать минимум 8 символов'
    return
  }

  loading.value = true

  try {
    await apiClient.post('/api/account-settings/change-password', {
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })

    success.value = 'Пароль успешно изменён'
    passwordForm.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (err) {
    console.error('Ошибка смены пароля:', err)
    error.value = err.response?.data?.message || err.response?.data || 'Ошибка при смене пароля'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.account-settings {
  min-height: 100vh;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
}

.settings-header {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 1rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.settings-title {
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: 2px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.settings-main {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.settings-container {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #1565c0;
}

.user-info-section,
.profile-edit-section,
.password-section {
  margin-bottom: 2rem;
}

.user-info-section h3,
.profile-edit-section h3,
.password-section h3 {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 1rem;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.info-item {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
}

.info-label {
  display: block;
  font-size: 0.85rem;
  color: #888;
  margin-bottom: 0.25rem;
}

.info-value {
  font-size: 1rem;
  color: #333;
  font-weight: 500;
}

.profile-form,
.password-form {
  max-width: 400px;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 0.5rem;
}

.form-group input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #1565c0;
}

.error-message {
  background: #fee;
  color: #c00;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.success-message {
  background: #efe;
  color: #060;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.submit-btn {
  width: 100%;
  padding: 1rem;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.4);
}

.submit-btn:disabled {
  opacity: 0.7;
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
  to { transform: rotate(360deg); }
}

@media (max-width: 600px) {
  .settings-main {
    padding: 1rem;
  }

  .settings-container {
    padding: 1.5rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>

