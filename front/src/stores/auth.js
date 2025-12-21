// filepath: /Users/ilmiraryakhimova/Desktop/rentalmanager/rentalmanager-main/front/src/stores/auth.js
import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const accessToken = ref(null)
  const refreshToken = ref(null)
  const loading = ref(false)
  const error = ref(null)

  const isAuthenticated = computed(() => !!accessToken.value && accessToken.value !== 'null')

  // Инициализация из localStorage
  function initAuth() {
    const storedToken = localStorage.getItem('accessToken')
    const storedRefreshToken = localStorage.getItem('refreshToken')
    const storedUser = localStorage.getItem('user')

    console.log('[Auth] initAuth - Loading from localStorage:', {
      hasAccessToken: !!storedToken,
      hasRefreshToken: !!storedRefreshToken,
      hasUser: !!storedUser,
      accessTokenPreview: storedToken?.substring(0, 20) + '...',
      refreshTokenPreview: storedRefreshToken?.substring(0, 20) + '...'
    })

    // Проверка на невалидные токены (строки "null" или "undefined")
    const isAccessTokenValid = storedToken &&
                               storedToken !== 'null' &&
                               storedToken !== 'undefined' &&
                               storedToken.length > 20 &&
                               storedToken.startsWith('eyJ')
    const isRefreshTokenValid = storedRefreshToken &&
                                storedRefreshToken !== 'null' &&
                                storedRefreshToken !== 'undefined' &&
                                storedRefreshToken.length > 20 &&
                                storedRefreshToken.startsWith('eyJ')

    if (!isAccessTokenValid || !isRefreshTokenValid) {
      console.warn('[Auth] initAuth - Invalid tokens detected, clearing localStorage:', {
        accessToken: storedToken,
        refreshToken: storedRefreshToken,
        isAccessTokenValid,
        isRefreshTokenValid
      })
      // Очищаем невалидные данные
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('user')
      return
    }

    // Восстанавливаем данные из localStorage
    accessToken.value = storedToken
    refreshToken.value = storedRefreshToken

    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser)
      } catch (e) {
        console.error('[Auth] Error parsing stored user:', e)
        user.value = null
      }
    }

    console.log('[Auth] initAuth - Auth restored:', {
      isAuthenticated: isAuthenticated.value,
      user: user.value
    })
  }

  // Сохранение данных авторизации
  function saveAuthData(authData) {
    console.log('[Auth] saveAuthData - Received data:', {
      hasAccessToken: !!authData?.accessToken,
      hasRefreshToken: !!authData?.refreshToken,
      accessTokenPreview: authData?.accessToken?.substring(0, 30) + '...',
      refreshTokenPreview: authData?.refreshToken?.substring(0, 30) + '...'
    })

    // Строгая валидация токенов
    const accessTokenValue = authData?.accessToken
    const refreshTokenValue = authData?.refreshToken

    const isAccessTokenValid = accessTokenValue &&
                               typeof accessTokenValue === 'string' &&
                               accessTokenValue !== 'null' &&
                               accessTokenValue !== 'undefined' &&
                               accessTokenValue.startsWith('eyJ')

    const isRefreshTokenValid = refreshTokenValue &&
                                typeof refreshTokenValue === 'string' &&
                                refreshTokenValue !== 'null' &&
                                refreshTokenValue !== 'undefined' &&
                                refreshTokenValue.startsWith('eyJ')

    if (!isAccessTokenValid || !isRefreshTokenValid) {
      console.error('[Auth] ERROR: Invalid tokens received!', {
        accessToken: accessTokenValue,
        refreshToken: refreshTokenValue,
        isAccessTokenValid,
        isRefreshTokenValid,
        accessTokenType: typeof accessTokenValue,
        refreshTokenType: typeof refreshTokenValue
      })
      throw new Error('Невалидные токены получены от сервера')
    }

    accessToken.value = accessTokenValue
    refreshToken.value = refreshTokenValue
    user.value = {
      email: authData.email,
      name: authData.name,
      phoneNumber: authData.phoneNumber,
      role: authData.role,
    }

    // Сохраняем в localStorage
    localStorage.setItem('accessToken', accessTokenValue)
    localStorage.setItem('refreshToken', refreshTokenValue)
    localStorage.setItem('user', JSON.stringify(user.value))

    console.log('[Auth] saveAuthData - Data saved successfully')
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
      // При регистрации не сохраняем токены, так как нужно подтвердить email
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

      console.log('[Auth] Login response:', {
        fullData: response.data,
        accessToken: response.data?.accessToken,
        refreshToken: response.data?.refreshToken,
        email: response.data?.email,
        name: response.data?.name,
        role: response.data?.role
      })

      saveAuthData(response.data)

      console.log('[Auth] After save:', {
        accessToken: accessToken.value?.substring(0, 20) + '...',
        refreshToken: refreshToken.value?.substring(0, 20) + '...',
        user: user.value
      })

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

  // Получение текущего про��иля пользователя
  async function fetchUserProfile() {
    try {
      const response = await authApi.getCurrentUser()
      user.value = response.data
      // Обновляем в localStorage
      localStorage.setItem('user', JSON.stringify(response.data))
    } catch (err) {
      console.error('Error fetching user profile:', err)
      throw err
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
    clearAuthData,
    fetchUserProfile,
  }
})

