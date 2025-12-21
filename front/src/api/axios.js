import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Интерцептор для добавления токена к запросам
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')

    // Проверка на невалидные токены - JWT токен должен начинаться с "eyJ"
    const isValidToken = token &&
                         token !== 'null' &&
                         token !== 'undefined' &&
                         token.length > 20 &&
                         token.startsWith('eyJ')

    console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`, {
      hasToken: !!token,
      isValidToken,
      tokenPreview: token ? (token.startsWith('eyJ') ? `${token.substring(0, 20)}...` : `INVALID: ${token}`) : 'none'
    })

    if (isValidToken) {
      config.headers.Authorization = `Bearer ${token}`
    } else if (token) {
      // Если токен есть, но невалиден - очищаем его немедленно
      console.error('[API Request] Invalid token detected, clearing:', {
        token,
        reason: token === 'null' ? 'Token is string "null"' :
                token === 'undefined' ? 'Token is string "undefined"' :
                !token.startsWith('eyJ') ? 'Token does not start with eyJ' :
                'Token too short'
      })
      // Очищаем невалидные данные
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('user')
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// Интерцептор для обработки ошибок и обновления токена
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config

    // Обработка 401 (Unauthorized) - попытка обновить токен
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshToken = localStorage.getItem('refreshToken')
        if (refreshToken) {
          const { data } = await axios.post(`${API_URL}/api/auth/refresh-token`, {
            refreshToken,
          })

          localStorage.setItem('accessToken', data.accessToken)
          localStorage.setItem('refreshToken', data.refreshToken)

          originalRequest.headers.Authorization = `Bearer ${data.accessToken}`
          return apiClient(originalRequest)
        }
      } catch (refreshError) {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('user')
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }

    // Обработка 403 (Forbidden) - недостаточно прав или невалидный токен
    if (error.response?.status === 403) {
      const token = localStorage.getItem('accessToken')
      const isTokenInvalid = !token || token === 'null' || token === 'undefined' || !token.startsWith('eyJ')

      console.error('403 Forbidden: Доступ запрещен.', {
        hasToken: !!token,
        isTokenInvalid,
        tokenPreview: token?.substring(0, 20)
      })

      // Если токен отсутствует или невалиден, очищаем и перенаправляем на логин
      if (isTokenInvalid) {
        console.log('[Auth] Clearing invalid auth data and redirecting to login')
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('user')
        window.location.href = '/login'
      }
    }

    return Promise.reject(error)
  },
)

export default apiClient
