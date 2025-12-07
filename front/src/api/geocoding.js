import axiosInstance from './axios'

/**
 * API для работы с геокодированием и поиском адресов
 */

/**
 * Поиск адресов по запросу
 * @param {string} query - Поисковый запрос (например, "Москва, Тверская 10")
 * @returns {Promise<Array>} Список найденных адресов
 */
export const searchAddress = (query) => {
  return axiosInstance.get('/api/geocoding/search', {
    params: { q: query },
    paramsSerializer: {
      encode: (value) => encodeURIComponent(value)
    }
  })
}

/**
 * Обратное геокодирование (координаты -> адрес)
 * @param {number} lat - Широта
 * @param {number} lon - Долгота
 * @returns {Promise<Object>} Найденный адрес
 */
export const reverseGeocode = (lat, lon) => {
  return axiosInstance.get('/api/geocoding/reverse', {
    params: { lat, lon }
  })
}

/**
 * Получение координат по адресу
 * @param {string} address - Полный адрес
 * @returns {Promise<Object>} Координаты и детали адреса
 */
export const getCoordinates = (address) => {
  return axiosInstance.get('/api/geocoding/coordinates', {
    params: { address }
  })
}

/**
 * Валидация адреса
 * @param {Object} addressData - Данные адреса
 * @returns {Promise<boolean>} true если адрес валиден
 */
export const validateAddress = (addressData) => {
  return axiosInstance.get('/api/geocoding/validate', {
    params: {
      country: addressData.country,
      city: addressData.city,
      street: addressData.street,
      houseNumber: addressData.houseNumber
    }
  })
}
