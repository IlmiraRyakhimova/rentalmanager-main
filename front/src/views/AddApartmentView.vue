<template>
  <div class="add-apartment-page">
    <header class="page-header">
      <div class="header-content">
        <router-link to="/dashboard" class="back-btn">
          ← Назад к списку
        </router-link>
        <h1 class="page-title">Добавить апартаменты</h1>
      </div>
    </header>

    <main class="page-main">
      <form @submit.prevent="handleSubmit" class="apartment-form">
        <!-- Основная информация -->
        <section class="form-section">
          <h2 class="section-heading">
            <span class="section-icon">🏠</span>
            Основная информация
          </h2>

          <div class="form-group">
            <label for="title">Название <span class="required">*</span></label>
            <input
              id="title"
              v-model="formData.title"
              type="text"
              class="form-input"
              placeholder="Уютная квартира в центре"
              required
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="accommodationType">Тип жилья <span class="required">*</span></label>
              <select id="accommodationType" v-model="formData.accommodationType" class="form-input" required>
                <option value="Apartment">Квартира</option>
                <option value="House">Дом</option>
                <option value="Studio">Студия</option>
              </select>
            </div>

            <div class="form-group">
              <label for="pricePerNight">Цена за ночь (₽) <span class="required">*</span></label>
              <input
                id="pricePerNight"
                v-model.number="formData.pricePerNight"
                type="number"
                class="form-input"
                placeholder="5000"
                min="0"
                step="100"
                required
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="area">Площадь (м²) <span class="required">*</span></label>
              <input
                id="area"
                v-model.number="formData.area"
                type="number"
                class="form-input"
                placeholder="65.5"
                min="0"
                step="0.1"
                required
              />
            </div>

            <div class="form-group">
              <label for="numberOfRooms">Комнат <span class="required">*</span></label>
              <input
                id="numberOfRooms"
                v-model.number="formData.numberOfRooms"
                type="number"
                class="form-input"
                placeholder="2"
                min="1"
                required
              />
            </div>

            <div class="form-group">
              <label for="numberOfBathrooms">Ванных комнат <span class="required">*</span></label>
              <input
                id="numberOfBathrooms"
                v-model.number="formData.numberOfBathrooms"
                type="number"
                class="form-input"
                placeholder="1"
                min="1"
                required
              />
            </div>
          </div>
        </section>

        <!-- Владелец -->
        <section class="form-section">
          <h2 class="section-heading">
            <span class="section-icon">👤</span>
            Владелец
          </h2>

          <!-- Переключатель режима -->
          <div class="owner-mode-toggle">
            <button
              type="button"
              class="toggle-btn"
              :class="{ active: ownerMode === 'existing' }"
              @click="ownerMode = 'existing'"
            >
              Выбрать существующего
            </button>
            <button
              type="button"
              class="toggle-btn"
              :class="{ active: ownerMode === 'new' }"
              @click="ownerMode = 'new'"
            >
              Создать нового
            </button>
          </div>

          <!-- Выбор существующего владельца -->
          <div v-if="ownerMode === 'existing'" class="existing-owner-section">
            <div class="form-group">
              <label for="ownerSearch">Поиск владельца</label>
              <div class="search-wrapper">
                <input
                  id="ownerSearch"
                  v-model="ownerSearchQuery"
                  type="text"
                  class="form-input"
                  placeholder="Введите имя, email или телефон..."
                  @input="searchOwners"
                />
                <span v-if="searchingOwners" class="search-spinner"></span>
              </div>
            </div>

            <!-- Список найденных владельцев -->
            <div v-if="filteredOwners.length > 0" class="owners-list">
              <div
                v-for="owner in filteredOwners"
                :key="owner.id"
                class="owner-item"
                :class="{ selected: selectedOwner?.id === owner.id }"
                @click="selectOwner(owner)"
              >
                <div class="owner-info">
                  <span class="owner-name">{{ owner.name }}</span>
                  <span class="owner-email">{{ owner.email }}</span>
                  <span class="owner-phone">{{ owner.phoneNumber }}</span>
                </div>
                <span v-if="selectedOwner?.id === owner.id" class="check-icon">✓</span>
              </div>
            </div>

            <div v-else-if="ownerSearchQuery && !searchingOwners" class="no-results">
              Владельцы не найдены. 
              <button type="button" class="link-btn" @click="ownerMode = 'new'">
                Создать нового?
              </button>
            </div>

            <!-- Выбранный владелец -->
            <div v-if="selectedOwner" class="selected-owner-card">
              <h4>Выбранный владелец:</h4>
              <div class="owner-details">
                <p><strong>Имя:</strong> {{ selectedOwner.name }}</p>
                <p><strong>Email:</strong> {{ selectedOwner.email }}</p>
                <p><strong>Телефон:</strong> {{ selectedOwner.phoneNumber }}</p>
              </div>
              <button type="button" class="clear-btn" @click="clearSelectedOwner">
                Очистить выбор
              </button>
            </div>
          </div>

          <!-- Создание нового владельца -->
          <div v-else class="new-owner-section">
            <div class="form-group">
              <label for="ownerName">Имя владельца <span class="required">*</span></label>
              <input
                id="ownerName"
                v-model="formData.owner.name"
                type="text"
                class="form-input"
                placeholder="Иван Иванов"
                required
              />
            </div>

            <div class="form-row">
              <div class="form-group">
                <label for="ownerEmail">Email <span class="required">*</span></label>
                <input
                  id="ownerEmail"
                  v-model="formData.owner.email"
                  type="email"
                  class="form-input"
                  placeholder="owner@example.com"
                  required
                />
              </div>

              <div class="form-group">
                <label for="ownerPhone">Телефон <span class="required">*</span></label>
                <input
                  id="ownerPhone"
                  v-model="formData.owner.phoneNumber"
                  type="tel"
                  class="form-input"
                  placeholder="+79991234567"
                  required
                />
              </div>
            </div>
          </div>
        </section>

        <!-- Адрес -->
        <section class="form-section">
          <h2 class="section-heading">
            <span class="section-icon">📍</span>
            Адрес
          </h2>

          <!-- Умный поиск адреса -->
          <div class="form-group">
            <AddressAutocomplete
              v-model="addressSearchQuery"
              label="Поиск адреса"
              placeholder="Начните вводить адрес (например: Москва, Тверская 10)"
              @select="handleAddressSelect"
            />
            <p class="helper-text">💡 Начните вводить адрес для автоматического заполнения полей</p>
          </div>

          <div class="divider">или заполните вручную</div>

          <div class="form-row">
            <div class="form-group">
              <label for="country">Страна <span class="required">*</span></label>
              <input
                id="country"
                v-model="formData.address.country"
                type="text"
                class="form-input"
                placeholder="Россия"
                required
              />
            </div>

            <div class="form-group">
              <label for="city">Город <span class="required">*</span></label>
              <input
                id="city"
                v-model="formData.address.city"
                type="text"
                class="form-input"
                placeholder="Москва"
                required
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="district">Район</label>
              <input
                id="district"
                v-model="formData.address.district"
                type="text"
                class="form-input"
                placeholder="Центральный"
              />
            </div>

            <div class="form-group">
              <label for="postalCode">Индекс</label>
              <input
                id="postalCode"
                v-model="formData.address.postalCode"
                type="text"
                class="form-input"
                placeholder="123456"
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group flex-2">
              <label for="street">Улица <span class="required">*</span></label>
              <input
                id="street"
                v-model="formData.address.street"
                type="text"
                class="form-input"
                placeholder="Тверская"
                required
              />
            </div>

            <div class="form-group">
              <label for="buildingNumber">Дом <span class="required">*</span></label>
              <input
                id="buildingNumber"
                v-model="formData.address.buildingNumber"
                type="text"
                class="form-input"
                placeholder="10"
                required
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="floorNumber">Этаж</label>
              <input
                id="floorNumber"
                v-model.number="formData.address.floorNumber"
                type="number"
                class="form-input"
                placeholder="5"
                min="0"
              />
            </div>

            <div class="form-group">
              <label for="apartmentNumber">Квартира</label>
              <input
                id="apartmentNumber"
                v-model.number="formData.address.apartmentNumber"
                type="number"
                class="form-input"
                placeholder="42"
                min="1"
              />
            </div>
          </div>
        </section>

        <!-- Ошибка -->
        <div v-if="formError" class="form-error">{{ formError }}</div>

        <!-- Кнопки -->
        <div class="form-actions">
          <router-link to="/dashboard" class="btn-secondary">Отмена</router-link>
          <button type="submit" class="btn-primary" :disabled="formLoading">
            <span v-if="formLoading" class="spinner"></span>
            <span v-else>Создать квартиру</span>
          </button>
        </div>
      </form>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useApartmentStore } from '@/stores/apartments'
import { usersApi } from '@/api/users'
import AddressAutocomplete from '@/components/AddressAutocomplete.vue'

const router = useRouter()
const apartmentStore = useApartmentStore()

const formLoading = ref(false)
const formError = ref('')
const addressSearchQuery = ref('')

// Режим выбора владельца
const ownerMode = ref('new') // 'existing' или 'new'
const ownerSearchQuery = ref('')
const searchingOwners = ref(false)
const owners = ref([])
const selectedOwner = ref(null)

// Данные формы
const formData = ref({
  title: '',
  accommodationType: 'Apartment',
  pricePerNight: null,
  area: null,
  numberOfRooms: 1,
  numberOfBathrooms: 1,
  owner: {
    name: '',
    email: '',
    phoneNumber: '',
    role: 'OWNER',
  },
  address: {
    postalCode: '',
    country: 'Россия',
    city: '',
    district: '',
    street: '',
    buildingNumber: '',
    floorNumber: null,
    apartmentNumber: null,
  },
})

// Фильтрация владельцев
const filteredOwners = computed(() => {
  if (!ownerSearchQuery.value) return owners.value
  
  const query = ownerSearchQuery.value.toLowerCase()
  return owners.value.filter(owner => 
    owner.name?.toLowerCase().includes(query) ||
    owner.email?.toLowerCase().includes(query) ||
    owner.phoneNumber?.includes(query)
  )
})

// Загрузка владельцев при монтировании
onMounted(async () => {
  try {
    const response = await usersApi.getAll()
    owners.value = response.data.filter(u => u.role === 'OWNER' || u.role === 'AGENT')
  } catch (error) {
    console.error('Ошибка загрузки пользователей:', error)
  }
})

// Поиск владельцев с debounce
let searchTimeout = null
const searchOwners = () => {
  clearTimeout(searchTimeout)
  searchingOwners.value = true
  
  searchTimeout = setTimeout(async () => {
    try {
      if (ownerSearchQuery.value.trim()) {
        // Пробуем поиск по имени
        const response = await usersApi.searchByName(ownerSearchQuery.value)
        owners.value = Array.isArray(response.data) ? response.data : [response.data]
      } else {
        // Загружаем всех
        const response = await usersApi.getAll()
        owners.value = response.data
      }
    } catch (error) {
      console.error('Ошибка поиска:', error)
    } finally {
      searchingOwners.value = false
    }
  }, 300)
}

// Выбор владельца
const selectOwner = (owner) => {
  selectedOwner.value = owner
  formData.value.owner = {
    name: owner.name,
    email: owner.email,
    phoneNumber: owner.phoneNumber,
    role: owner.role || 'OWNER',
  }
}

// Очистка выбранного владельца
const clearSelectedOwner = () => {
  selectedOwner.value = null
  formData.value.owner = {
    name: '',
    email: '',
    phoneNumber: '',
    role: 'OWNER',
  }
}

// Обработка выбора адреса из автодополнения
const handleAddressSelect = (suggestion) => {
  if (suggestion.country) {
    formData.value.address.country = suggestion.country
  }
  if (suggestion.city) {
    formData.value.address.city = suggestion.city
  }
  if (suggestion.street) {
    formData.value.address.street = suggestion.street
  }
  if (suggestion.houseNumber) {
    formData.value.address.buildingNumber = suggestion.houseNumber
  }
  if (suggestion.postalCode) {
    formData.value.address.postalCode = suggestion.postalCode
  }
}

// Отправка формы
const handleSubmit = async () => {
  // Валидация владельца
  if (ownerMode.value === 'existing' && !selectedOwner.value) {
    formError.value = 'Выберите владельца или создайте нового'
    return
  }

  if (ownerMode.value === 'new') {
    if (!formData.value.owner.name || !formData.value.owner.email || !formData.value.owner.phoneNumber) {
      formError.value = 'Заполните все данные владельца'
      return
    }
  }

  formLoading.value = true
  formError.value = ''

  try {
    const createdApartment = await apartmentStore.createApartment(formData.value)
    // Перенаправляем на дашборд после успешного создания
    await router.push('/dashboard')
  } catch (error) {
    formError.value = error.response?.data?.message || 'Произошла ошибка при создании квартиры'
  } finally {
    formLoading.value = false
  }
}
</script>

<style scoped>
.add-apartment-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
}

.page-header {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 1.5rem 2rem;
}

.back-btn {
  display: inline-block;
  color: #1565c0;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
  transition: color 0.3s ease;
}

.back-btn:hover {
  color: #0d47a1;
}

.page-title {
  font-size: 1.75rem;
  font-weight: 700;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}

.page-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
}

.apartment-form {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 2.5rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #e2e8f0;
}

.form-section:last-of-type {
  border-bottom: none;
  margin-bottom: 1.5rem;
}

.section-heading {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0 0 1.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-icon {
  font-size: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.form-group.flex-2 {
  grid-column: span 2;
}

.form-group label {
  font-size: 0.9rem;
  font-weight: 600;
  color: #2d3748;
}

.required {
  color: #e53e3e;
}

.form-input {
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: #1565c0;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.helper-text {
  font-size: 0.8rem;
  color: #718096;
  margin: 0.25rem 0 0;
}

.divider {
  text-align: center;
  color: #718096;
  font-size: 0.85rem;
  margin: 1.5rem 0;
  position: relative;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 40%;
  height: 1px;
  background: #e2e8f0;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

/* Владелец - переключатель режима */
.owner-mode-toggle {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  background: #f7fafc;
  padding: 0.25rem;
  border-radius: 10px;
}

.toggle-btn {
  flex: 1;
  padding: 0.75rem 1rem;
  border: none;
  background: transparent;
  color: #718096;
  font-size: 0.9rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toggle-btn.active {
  background: white;
  color: #1565c0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.toggle-btn:hover:not(.active) {
  background: rgba(255, 255, 255, 0.5);
}

/* Поиск владельца */
.search-wrapper {
  position: relative;
}

.search-spinner {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 18px;
  height: 18px;
  border: 2px solid #e2e8f0;
  border-top-color: #1565c0;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: translateY(-50%) rotate(360deg);
  }
}

/* Список владельцев */
.owners-list {
  max-height: 240px;
  overflow-y: auto;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.owner-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  cursor: pointer;
  transition: background 0.2s ease;
  border-bottom: 1px solid #e2e8f0;
}

.owner-item:last-child {
  border-bottom: none;
}

.owner-item:hover {
  background: #f7fafc;
}

.owner-item.selected {
  background: #ebf4ff;
  border-color: #1565c0;
}

.owner-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.owner-name {
  font-weight: 600;
  color: #1a202c;
}

.owner-email {
  font-size: 0.85rem;
  color: #1565c0;
}

.owner-phone {
  font-size: 0.85rem;
  color: #718096;
}

.check-icon {
  color: #1565c0;
  font-size: 1.25rem;
  font-weight: bold;
}

.no-results {
  text-align: center;
  padding: 1.5rem;
  color: #718096;
}

.link-btn {
  background: none;
  border: none;
  color: #1565c0;
  font-weight: 600;
  cursor: pointer;
  text-decoration: underline;
}

.link-btn:hover {
  color: #0d47a1;
}

/* Выбранный владелец */
.selected-owner-card {
  background: #f7fafc;
  border-radius: 8px;
  padding: 1rem;
  margin-top: 1rem;
}

.selected-owner-card h4 {
  margin: 0 0 0.5rem;
  color: #2d3748;
  font-size: 0.9rem;
}

.owner-details p {
  margin: 0.25rem 0;
  font-size: 0.9rem;
  color: #4a5568;
}

.clear-btn {
  margin-top: 0.75rem;
  padding: 0.5rem 1rem;
  background: white;
  border: 1px solid #e53e3e;
  color: #e53e3e;
  border-radius: 6px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.clear-btn:hover {
  background: #e53e3e;
  color: white;
}

/* Ошибка формы */
.form-error {
  background: #fed7d7;
  color: #c53030;
  padding: 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
  text-align: center;
  margin-bottom: 1rem;
}

/* Кнопки формы */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding-top: 1rem;
}

.btn-primary {
  padding: 0.875rem 2rem;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  min-width: 180px;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  padding: 0.875rem 2rem;
  background: transparent;
  color: #1565c0;
  border: 2px solid #1565c0;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  text-align: center;
}

.btn-secondary:hover {
  background: #1565c0;
  color: white;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* Responsive */
@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .form-group.flex-2 {
    grid-column: span 1;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
  }
}
</style>
