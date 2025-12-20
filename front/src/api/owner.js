import apiClient from './axios'

export const ownerApi = {
  /**
   * Получить квартиры текущего собственника
   */
  getMyApartments() {
    return apiClient.get('/api/owner/my-apartments')
  },

  /**
   * Получить бронирования квартир текущего собственника
   */
  getMyBookings() {
    return apiClient.get('/api/owner/my-bookings')
  },
}
