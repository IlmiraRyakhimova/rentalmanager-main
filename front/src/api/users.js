import apiClient from './axios'

export const usersApi = {
  /**
   * Получить всех пользователей
   */
  getAll() {
    return apiClient.get('/api/users')
  },

  /**
   * Получить пользователя по ID
   * @param {string} id - ID пользователя
   */
  getById(id) {
    return apiClient.get(`/api/users/${id}`)
  },

  /**
   * Создать нового пользователя (владельца)
   * @param {Object} userData - Данные пользователя
   * @param {string} userData.name - Имя
   * @param {string} userData.email - Email
   * @param {string} userData.phoneNumber - Номер телефона
   * @param {string} userData.role - Роль (OWNER или AGENT)
   */
  create(userData) {
    return apiClient.post('/api/users', userData)
  },

  /**
   * Обновить пользователя
   * @param {string} id - ID пользователя
   * @param {Object} userData - Данные пользователя
   */
  update(id, userData) {
    return apiClient.put(`/api/users/${id}`, userData)
  },

  /**
   * Удалить пользователя
   * @param {string} id - ID пользователя
   */
  delete(id) {
    return apiClient.delete(`/api/users/${id}`)
  },

  /**
   * Поиск по имени
   * @param {string} name - Имя пользователя
   */
  searchByName(name) {
    return apiClient.get(`/api/users/search/by-name/${encodeURIComponent(name)}`)
  },

  /**
   * Поиск по email
   * @param {string} email - Email пользователя
   */
  searchByEmail(email) {
    return apiClient.get(`/api/users/search/by-email/${encodeURIComponent(email)}`)
  },

  /**
   * Поиск по телефону
   * @param {string} phoneNumber - Номер телефона
   */
  searchByPhone(phoneNumber) {
    return apiClient.get(`/api/users/search/by-phone-number/${encodeURIComponent(phoneNumber)}`)
  },
}
