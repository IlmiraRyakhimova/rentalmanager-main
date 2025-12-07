import apiClient from './axios'

export const apartmentApi = {
  /**
   * Получить все квартиры
   */
  getAll() {
    return apiClient.get('/api/apartments')
  },

  /**
   * Получить квартиру по ID
   * @param {string} id - ID квартиры
   */
  getById(id) {
    return apiClient.get(`/api/apartments/${id}`)
  },

  /**
   * Создать новую квартиру
   * @param {Object} apartmentData - Данные квартиры
   */
  create(apartmentData) {
    return apiClient.post('/api/apartments', apartmentData)
  },

  /**
   * Обновить квартиру полностью
   * @param {string} id - ID квартиры
   * @param {Object} apartmentData - Данные квартиры
   */
  update(id, apartmentData) {
    return apiClient.put(`/api/apartments/${id}`, apartmentData)
  },

  /**
   * Обновить квартиру частично
   * @param {string} id - ID квартиры
   * @param {Object} partialData - Частичные данные
   */
  patch(id, partialData) {
    return apiClient.patch(`/api/apartments/${id}`, partialData)
  },

  /**
   * Удалить квартиру
   * @param {string} id - ID квартиры
   */
  delete(id) {
    return apiClient.delete(`/api/apartments/${id}`)
  },

  /**
   * Поиск квартир по владельцу (текущий пользователь)
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
