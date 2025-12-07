import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const loading = ref(false)
  const error = ref(null)

  const isAuthenticated = computed(() => !!accessToken.value)

  // Инициализация из localStorage
  function initAuth() {
    const storedToken = localStorage.getItem('accessToken')
    const storedRefreshToken = localStorage.getItem('refreshToken')
    const storedUser = localStorage.getItem('user')

    if (storedToken && storedRefreshToken && storedUser) {
      accessToken.value = storedToken
      refreshToken.value = storedRefreshToken
      user.value = JSON.parse(storedUser)
    }
  }

  // Сохранение данных авторизации
  function saveAuthData(authData) {
    accessToken.value = authData.accessToken
    refreshToken.value = authData.refreshToken
    user.value = {
      email: authData.email,
      name: authData.name,
      phoneNumber: authData.phoneNumber,
      role: authData.role,
    }

    localStorage.setItem('accessToken', authData.accessToken)
    localStorage.setItem('refreshToken', authData.refreshToken)
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  // Очистка данных авторизации
  function clearAuthData() {
    accessToken.value = null
    refreshToken.value = null
    user.value = null

    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
  }

  // Регистрация
  async function signUp(userData) {
    loading.value = true
    error.value = null

    try {
      const response = await authApi.signUp(userData)
      saveAuthData(response.data)
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при регистрации'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Вход
  async function signIn(credentials) {
    loading.value = true
    error.value = null

    try {
      const response = await authApi.signIn(credentials)
      saveAuthData(response.data)
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при входе'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Выход
  async function logOut() {
    try {
      await authApi.logOut()
    } catch (err) {
      console.error('Ошибка при выходе:', err)
    } finally {
      clearAuthData()
    }
  }

  // Повторная отправка письма верификации
  async function resendVerification(email) {
    loading.value = true
    error.value = null

    try {
      await authApi.resendVerificationEmail(email)
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при отправке письма'
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    user,
    accessToken,
    refreshToken,
    loading,
    error,
    isAuthenticated,
    initAuth,
    signUp,
    signIn,
    logOut,
    resendVerification,
  }
})
