import { ref } from 'vue'
import { defineStore } from 'pinia'
import { apartmentApi } from '@/api/apartments'

export const useApartmentStore = defineStore('apartment', () => {
  const apartments = ref([])
  const currentApartment = ref(null)
  const loading = ref(false)
  const error = ref(null)

  // Получить все квартиры
  async function fetchAll() {
    loading.value = true
    error.value = null

    try {
      const response = await apartmentApi.getAll()
      apartments.value = response.data
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при загрузке квартир'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Получить квартиры текущего пользователя
  async function fetchMyApartments(ownerEmail) {
    loading.value = true
    error.value = null

    try {
      const response = await apartmentApi.getByOwnerEmail(ownerEmail)
      apartments.value = response.data
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при загрузке квартир'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Создать квартиру
  async function createApartment(apartmentData) {
    loading.value = true
    error.value = null

    try {
      const response = await apartmentApi.create(apartmentData)
      apartments.value.push(response.data)
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при создании квартиры'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Обновить квартиру
  async function updateApartment(id, apartmentData) {
    loading.value = true
    error.value = null

    try {
      const response = await apartmentApi.update(id, apartmentData)
      const index = apartments.value.findIndex((apt) => apt.id === id)
      if (index !== -1) {
        apartments.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при обновлении квартиры'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Удалить квартиру
  async function deleteApartment(id) {
    loading.value = true
    error.value = null

    try {
      await apartmentApi.delete(id)
      apartments.value = apartments.value.filter((apt) => apt.id !== id)
    } catch (err) {
      error.value = err.response?.data?.message || 'Ошибка при удалении квартиры'
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    apartments,
    currentApartment,
    loading,
    error,
    fetchAll,
    fetchMyApartments,
    createApartment,
    updateApartment,
    deleteApartment,
  }
})
