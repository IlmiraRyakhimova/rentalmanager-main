import apiClient from './axios'

export const authApi = {
  /**
   * Регистрация нового агента
   * @param {Object} userData - Данные пользователя
   * @param {string} userData.name - Имя
   * @param {string} userData.email - Email
   * @param {string} userData.phoneNumber - Номер телефона
   * @param {string} userData.password - Пароль
   * @param {string} userData.role - Роль (AGENT)
   */
  signUp(userData) {
    return apiClient.post('/api/auth/sign-up', userData)
  },

  /**
   * Вход в систему
   * @param {Object} credentials - Учетные данные
   * @param {string} credentials.email - Email
   * @param {string} credentials.password - Пароль
   */
  signIn(credentials) {
    return apiClient.post('/api/auth/sign-in', credentials)
  },

  /**
   * Обновление токена доступа
   * @param {string} refreshToken - Refresh токен
   */
  refreshToken(refreshToken) {
    return apiClient.post('/api/auth/refresh-token', { refreshToken })
  },

  /**
   * Выход из системы
   */
  logOut() {
    return apiClient.post('/api/auth/log-out')
  },

  /**
   * Повторная отправка письма верификации
   * @param {string} email - Email для верификации
   */
  resendVerificationEmail(email) {
    return apiClient.post('/api/auth/resend-verification-email', { email })
  },

  /**
   * Запрос на восстановление пароля
   * @param {string} email - Email пользователя
   */
  forgotPassword(email) {
    return apiClient.post('/api/auth/forgot-password', { email })
  },

  /**
   * Сброс пароля с токеном
   * @param {string} token - Токен сброса пароля
   * @param {string} newPassword - Новый пароль
   */
  resetPassword(token, newPassword) {
    return apiClient.post('/api/auth/reset-password', { token, newPassword })
  },

  /**
   * Переотправка письма с паролем собственнику
   * @param {string} email - Email собственника
   */
  resendOwnerCredentials(email) {
    return apiClient.post('/api/auth/resend-owner-credentials', { email })
  },
}
