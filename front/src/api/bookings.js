import apiClient from './axios'

export const bookingsApi = {
  /**
   * Получить все бронирования
   */
  getAll() {
    return apiClient.get('/api/bookings')
  },

  /**
   * Получить бронирование по ID
   * @param {string} id - ID бронирования
   */
  getById(id) {
    return apiClient.get(`/api/bookings/${id}`)
  },

  /**
   * Создать новое бронирование
   * @param {Object} bookingData - Данные бронирования
   * @param {string} bookingData.apartmentId - ID квартиры
   * @param {string} bookingData.guestName - Имя гостя
   * @param {string} bookingData.guestEmail - Email гостя
   * @param {string} bookingData.guestPhoneNumber - Телефон гостя
   * @param {string} bookingData.checkInDate - Дата заезда (YYYY-MM-DD)
   * @param {string} bookingData.checkOutDate - Дата выезда (YYYY-MM-DD)
   */
  create(bookingData) {
    return apiClient.post('/api/bookings', bookingData)
  },

  /**
   * Обновить бронирование полностью
   * @param {string} id - ID бронирования
   * @param {Object} bookingData - Данные бронирования
   */
  update(id, bookingData) {
    return apiClient.put(`/api/bookings/${id}`, bookingData)
  },

  /**
   * Обновить бронирование частично
   * @param {string} id - ID бронирования
   * @param {Object} partialData - Частичные данные
   */
  patch(id, partialData) {
    return apiClient.patch(`/api/bookings/${id}`, partialData)
  },

  /**
   * Удалить бронирование
   * @param {string} id - ID бронирования
   */
  delete(id) {
    return apiClient.delete(`/api/bookings/${id}`)
  },

  /**
   * Обновить статус бронирования
   * @param {string} id - ID бронирования
   * @param {Object} data - { bookingStatus: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED' }
   */
  updateBookingStatus(id, data) {
    return apiClient.patch(`/api/bookings/booking-status/${id}`, data)
  },

  /**
   * Обновить статус оплаты
   * @param {string} id - ID бронирования
   * @param {Object} data - { paymentStatus: 'PENDING' | 'PAID' | 'REFUNDED' }
   */
  updatePaymentStatus(id, data) {
    return apiClient.patch(`/api/bookings/payment-status/${id}`, data)
  },

  /**
   * Поиск по ID квартиры
   * @param {string} apartmentId - ID квартиры
   */
  getByApartmentId(apartmentId) {
    return apiClient.get(`/api/bookings/search/by-apartment-id/${apartmentId}`)
  },

  /**
   * Поиск по коду бронирования
   * @param {string} bookingCode - Код бронирования
   */
  getByBookingCode(bookingCode) {
    return apiClient.get(`/api/bookings/search/by-booking-code/${bookingCode}`)
  },

  /**
   * Поиск по имени гостя
   * @param {string} guestName - Имя гостя
   */
  getByGuestName(guestName) {
    return apiClient.get(`/api/bookings/search/by-guest-name/${encodeURIComponent(guestName)}`)
  },

  /**
   * Поиск по email гостя
   * @param {string} guestEmail - Email гостя
   */
  getByGuestEmail(guestEmail) {
    return apiClient.get(`/api/bookings/search/by-guest-email/${encodeURIComponent(guestEmail)}`)
  },

  /**
   * Поиск по статусу бронирования
   * @param {string} status - Статус (PENDING, CONFIRMED, CANCELLED, COMPLETED)
   */
  getByBookingStatus(status) {
    return apiClient.get(`/api/bookings/search/by-booking-status/${status}`)
  },

  /**
   * Поиск по статусу оплаты
   * @param {string} status - Статус (PENDING, PAID, REFUNDED)
   */
  getByPaymentStatus(status) {
    return apiClient.get(`/api/bookings/search/by-payment-status/${status}`)
  },
}
