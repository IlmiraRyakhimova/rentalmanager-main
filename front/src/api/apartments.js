import apiClient from './axios'

export const apartmentApi = {
  /**
   * Получить все апартаменты
   */
  getAll() {
    return apiClient.get('/api/apartments')
  },

  /**
   * Получить апартаменты по ID
   * @param {string} id - ID апартаментов
   */
  getById(id) {
    return apiClient.get(`/api/apartments/${id}`)
  },

  /**
   * Создать новые апартаменты
   * @param {Object} apartmentData - Данные апартаментов
   */
  create(apartmentData) {
    return apiClient.post('/api/apartments', apartmentData)
  },

  /**
   * Обновить апартаменты полностью
   * @param {string} id - ID апартаментов
   * @param {Object} apartmentData - Данные апартаментов
   */
  update(id, apartmentData) {
    return apiClient.put(`/api/apartments/${id}`, apartmentData)
  },

  /**
   * Обновить апартаменты частично
   * @param {string} id - ID апартаментов
   * @param {Object} partialData - Частичные данные
   */
  patch(id, partialData) {
    return apiClient.patch(`/api/apartments/${id}`, partialData)
  },

  /**
   * Удалить апартаменты
   * @param {string} id - ID апартаментов
   */
  delete(id) {
    return apiClient.delete(`/api/apartments/${id}`)
  },

  /**
   * Поиск апартаментов по владельцу (текущий пользователь)
   * @param {string} ownerId - ID владельца
   */
  getByOwnerId(ownerId) {
    return apiClient.get(`/api/apartments/search/by-owner-id/${ownerId}`)
  },

  /**
   * Поиск по email владельца
   * @param {string} email - Email владельца
   */
  getByOwnerEmail(email) {
    return apiClient.get(`/api/apartments/search/by-owner-email/${email}`)
  },
}
