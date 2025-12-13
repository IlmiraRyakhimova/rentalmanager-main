<template>
  <div class="dashboard">
    <header class="dashboard-header">
      <div class="header-content">
        <h1 class="dashboard-title">RENTAL MANAGER</h1>
        <div class="user-info">
          <span class="user-name">{{ user?.name }}</span>
          <button @click="handleLogout" class="logout-btn">Выйти</button>
        </div>
      </div>
    </header>

    <main class="dashboard-main">
      <div class="welcome-section">
        <h2 class="section-title">Мои квартиры</h2>
        <router-link to="/apartments/add" class="add-btn">
          <span class="add-icon">+</span>
          Добавить апартаменты
        </router-link>
      </div>

      <!-- Список квартир -->
      <div v-if="loading" class="loading-container">
        <div class="spinner-large"></div>
        <p>Загрузка квартир...</p>
      </div>

      <div v-else-if="apartments.length === 0" class="empty-state">
        <div class="empty-icon">🏢</div>
        <h3>Пока нет квартир</h3>
        <p>Добавьте свою первую квартиру, чтобы начать управление</p>
        <router-link to="/apartments/add" class="btn-primary">Добавить квартиру</router-link>
      </div>

      <div v-else class="apartments-list">
        <div 
          v-for="apartment in apartments" 
          :key="apartment.id" 
          class="apartment-row"
          :class="{ expanded: expandedApartment === apartment.id }"
        >
          <!-- Заголовок (всегда видимый) -->
          <div class="apartment-row-header" @click="toggleApartment(apartment.id)">
            <div class="apartment-main-info">
              <span class="expand-icon">{{ expandedApartment === apartment.id ? '▼' : '▶' }}</span>
              <h3 class="apartment-title">{{ apartment.title }}</h3>
              <span class="apartment-type-badge">{{ apartment.accommodationType }}</span>
            </div>
            <div class="apartment-quick-info">
              <span class="quick-info-item">
                <span class="info-icon">📍</span>
                {{ apartment.address?.city }}, {{ apartment.address?.street }} {{ apartment.address?.buildingNumber }}
              </span>
              <span class="quick-info-item price">
                <span class="info-icon">💰</span>
                {{ apartment.pricePerNight }} ₽/ночь
              </span>
              <span class="quick-info-item">
                <span class="info-icon">🛏️</span>
                {{ apartment.numberOfRooms }} комн.
              </span>
              <span class="quick-info-item">
                <span class="info-icon">📐</span>
                {{ apartment.area }} м²
              </span>
            </div>
            <div class="apartment-header-actions">
              <button @click.stop="editApartment(apartment)" class="action-btn-small edit-btn" title="Редактировать">
                ✏️
              </button>
              <button @click.stop="deleteApartmentConfirm(apartment)" class="action-btn-small delete-btn" title="Удалить">
                🗑️
              </button>
            </div>
          </div>

          <!-- Развернутое содержимое -->
          <div v-if="expandedApartment === apartment.id" class="apartment-row-content">
            <div class="content-grid">
              <!-- Информация о квартире -->
              <div class="content-section">
                <h4 class="section-title">📋 Детали</h4>
                <div class="details-grid">
                  <div class="detail-row">
                    <span class="detail-label">Страна:</span>
                    <span class="detail-value">{{ apartment.address?.country || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Город:</span>
                    <span class="detail-value">{{ apartment.address?.city || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Район:</span>
                    <span class="detail-value">{{ apartment.address?.district || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Улица:</span>
                    <span class="detail-value">{{ apartment.address?.street }} {{ apartment.address?.buildingNumber }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Этаж:</span>
                    <span class="detail-value">{{ apartment.address?.floorNumber || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Квартира:</span>
                    <span class="detail-value">{{ apartment.address?.apartmentNumber || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Ванных комнат:</span>
                    <span class="detail-value">{{ apartment.numberOfBathrooms }}</span>
                  </div>
                </div>
              </div>

              <!-- Владелец -->
              <div class="content-section">
                <h4 class="section-title">👤 Владелец</h4>
                <div class="details-grid">
                  <div class="detail-row">
                    <span class="detail-label">Имя:</span>
                    <span class="detail-value">{{ apartment.owner?.name || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Email:</span>
                    <span class="detail-value">{{ apartment.owner?.email || '—' }}</span>
                  </div>
                  <div class="detail-row">
                    <span class="detail-label">Телефон:</span>
                    <span class="detail-value">{{ apartment.owner?.phoneNumber || '—' }}</span>
                  </div>
                </div>
              </div>

              <!-- Бронирования -->
              <div class="content-section bookings-section">
                <div class="section-header">
                  <h4 class="section-title">📅 Бронирования</h4>
                  <button @click="openBookingModal(apartment)" class="add-booking-btn">
                    + Добавить бронирование
                  </button>
                </div>
                <div v-if="apartmentBookings[apartment.id]?.length > 0" class="bookings-list">
                  <div 
                    v-for="booking in apartmentBookings[apartment.id]" 
                    :key="booking.id" 
                    class="booking-item"
                    :class="'status-' + booking.bookingStatus?.toLowerCase()"
                  >
                    <div class="booking-info">
                      <span class="booking-code">{{ booking.bookingCode }}</span>
                      <span class="booking-guest">{{ booking.guestName }}</span>
                      <span class="booking-dates">
                        {{ formatDate(booking.checkInDate) }} — {{ formatDate(booking.checkOutDate) }}
                      </span>
                    </div>
                    <div class="booking-statuses">
                      <span class="booking-status" :class="'status-' + booking.bookingStatus?.toLowerCase()">
                        {{ getBookingStatusText(booking.bookingStatus) }}
                      </span>
                      <span class="payment-status" :class="'payment-' + booking.paymentStatus?.toLowerCase()">
                        {{ getPaymentStatusText(booking.paymentStatus) }}
                      </span>
                    </div>
                    <div class="booking-actions">
                      <button @click="editBooking(booking)" class="booking-action-btn" title="Редактировать">✏️</button>
                      <button @click="deleteBooking(booking)" class="booking-action-btn delete" title="Удалить">🗑️</button>
                    </div>
                  </div>
                </div>
                <div v-else class="no-bookings">
                  Бронирований пока нет
                </div>
              </div>
            </div>

            <div class="content-actions">
              <button @click="editApartment(apartment)" class="action-btn edit-btn">
                ✏️ Редактировать квартиру
              </button>
              <button @click="deleteApartmentConfirm(apartment)" class="action-btn delete-btn">
                🗑️ Удалить квартиру
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Модальное окно бронирования -->
    <div v-if="showBookingModal" class="modal-overlay" @click.self="closeBookingModal">
      <div class="modal-content booking-modal">
        <div class="modal-header">
          <h2>{{ editingBooking ? 'Редактировать' : 'Новое' }} бронирование</h2>
          <button @click="closeBookingModal" class="modal-close">&times;</button>
        </div>

        <form @submit.prevent="handleBookingSubmit" class="booking-form">
          <div class="form-group">
            <label for="guestName">Имя гостя <span class="required">*</span></label>
            <input
              id="guestName"
              v-model="bookingFormData.guestName"
              type="text"
              class="form-input"
              placeholder="Петр Петров"
              required
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="guestEmail">Email гостя <span class="required">*</span></label>
              <input
                id="guestEmail"
                v-model="bookingFormData.guestEmail"
                type="email"
                class="form-input"
                placeholder="guest@example.com"
                required
              />
            </div>
            <div class="form-group">
              <label for="guestPhoneNumber">Телефон <span class="required">*</span></label>
              <input
                id="guestPhoneNumber"
                v-model="bookingFormData.guestPhoneNumber"
                type="tel"
                class="form-input"
                placeholder="+79991234567"
                required
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="checkInDate">Дата заезда <span class="required">*</span></label>
              <input
                id="checkInDate"
                v-model="bookingFormData.checkInDate"
                type="date"
                class="form-input"
                required
              />
            </div>
            <div class="form-group">
              <label for="checkOutDate">Дата выезда <span class="required">*</span></label>
              <input
                id="checkOutDate"
                v-model="bookingFormData.checkOutDate"
                type="date"
                class="form-input"
                required
              />
            </div>
          </div>

          <div v-if="editingBooking" class="form-row">
            <div class="form-group">
              <label for="bookingStatus">Статус бронирования</label>
              <select id="bookingStatus" v-model="bookingFormData.bookingStatus" class="form-input">
                <option value="PENDING">Ожидает</option>
                <option value="CONFIRMED">Подтверждено</option>
                <option value="CANCELLED">Отменено</option>
                <option value="COMPLETED">Завершено</option>
              </select>
            </div>
            <div class="form-group">
              <label for="paymentStatus">Статус оплаты</label>
              <select id="paymentStatus" v-model="bookingFormData.paymentStatus" class="form-input">
                <option value="PENDING">Ожидает</option>
                <option value="PAID">Оплачено</option>
                <option value="REFUNDED">Возврат</option>
              </select>
            </div>
          </div>

          <div v-if="bookingError" class="form-error">{{ bookingError }}</div>

          <div class="form-actions">
            <button type="button" @click="closeBookingModal" class="btn-secondary">Отмена</button>
            <button type="submit" class="btn-primary" :disabled="bookingLoading">
              <span v-if="bookingLoading" class="spinner"></span>
              <span v-else>{{ editingBooking ? 'Сохранить' : 'Создать' }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Модальное окно добавления квартиры -->
    <div v-if="showAddModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ editingApartment ? 'Редактировать' : 'Добавить' }} апартаменты</h2>
          <button @click="closeModal" class="modal-close">&times;</button>
        </div>

        <form @submit.prevent="handleSubmit" class="apartment-form">
          <!-- Основная информация -->
          <div class="form-section">
            <h3 class="section-heading">Основная информация</h3>

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
          </div>

          <!-- Адрес -->
          <div class="form-section">
            <h3 class="section-heading">Адрес</h3>

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
          </div>

          <!-- Ошибка -->
          <div v-if="formError" class="form-error">{{ formError }}</div>

          <!-- Кнопки -->
          <div class="form-actions">
            <button type="button" @click="closeModal" class="btn-secondary">Отмена</button>
            <button type="submit" class="btn-primary" :disabled="formLoading">
              <span v-if="formLoading" class="spinner"></span>
              <span v-else>{{ editingApartment ? 'Сохранить' : 'Создать' }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useApartmentStore } from '@/stores/apartments'
import { bookingsApi } from '@/api/bookings'
import AddressAutocomplete from '@/components/AddressAutocomplete.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const apartmentStore = useApartmentStore()

const user = computed(() => authStore.user)
const apartments = computed(() => apartmentStore.apartments)
const loading = ref(false)

// Раскрытие карточек квартир
const expandedApartment = ref(null)
const apartmentBookings = ref({})

// Модалка бронирования
const showBookingModal = ref(false)
const editingBooking = ref(null)
const currentBookingApartment = ref(null)
const bookingLoading = ref(false)
const bookingError = ref('')
const bookingFormData = ref({
  guestName: '',
  guestEmail: '',
  guestPhoneNumber: '',
  checkInDate: '',
  checkOutDate: '',
  bookingStatus: 'PENDING',
  paymentStatus: 'PENDING'
})

const showAddModal = ref(false)
const editingApartment = ref(null)
const formLoading = ref(false)
const formError = ref('')
const addressSearchQuery = ref('')

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
    role: 'AGENT',
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

const handleLogout = async () => {
  await authStore.logOut()
  router.push('/')
}

const resetForm = () => {
  formData.value = {
    title: '',
    accommodationType: 'Apartment',
    pricePerNight: null,
    area: null,
    numberOfRooms: 1,
    numberOfBathrooms: 1,
    owner: {
      name: user.value?.name || '',
      email: user.value?.email || '',
      phoneNumber: user.value?.phoneNumber || '',
      role: user.value?.role || 'AGENT',
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
  }
  addressSearchQuery.value = ''
  formError.value = ''
}

const closeModal = () => {
  showAddModal.value = false
  editingApartment.value = null
  resetForm()
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

const handleSubmit = async () => {
  formLoading.value = true
  formError.value = ''

  // Устанавливаем данные владельца из профиля
  formData.value.owner.name = user.value?.name || ''
  formData.value.owner.email = user.value?.email || ''
  formData.value.owner.phoneNumber = user.value?.phoneNumber || ''
  formData.value.owner.role = user.value?.role || 'AGENT'

  try {
    if (editingApartment.value) {
      await apartmentStore.updateApartment(editingApartment.value.id, formData.value)
    } else {
      await apartmentStore.createApartment(formData.value)
    }
    closeModal()
  } catch (error) {
    formError.value = error.response?.data?.message || 'Произошла ошибка'
  } finally {
    formLoading.value = false
  }
}

const editApartment = (apartment) => {
  editingApartment.value = apartment
  formData.value = JSON.parse(JSON.stringify(apartment))
  showAddModal.value = true
}

const deleteApartmentConfirm = async (apartment) => {
  if (confirm(`Удалить квартиру "${apartment.title}"?`)) {
    try {
      await apartmentStore.deleteApartment(apartment.id)
    } catch (error) {
      alert('Ошибка при удалении квартиры')
    }
  }
}

const loadApartments = async () => {
  loading.value = true
  try {
    // Для агентов загружаем все квартиры, для владельцев - только свои
    if (user.value?.role === 'AGENT') {
      await apartmentStore.fetchAll()
    } else if (user.value?.email) {
      await apartmentStore.fetchMyApartments(user.value.email)
    }
  } catch (error) {
    console.error('Error fetching apartments:', error)
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await loadApartments()
})

// Перезагружать квартиры при возврате на страницу Dashboard
watch(() => route.path, (newPath, oldPath) => {
  if (newPath === '/dashboard' && oldPath === '/apartments/add') {
    loadApartments()
  }
})

// === Функции для работы с квартирами (раскрытие) ===
const toggleApartment = async (apartmentId) => {
  if (expandedApartment.value === apartmentId) {
    expandedApartment.value = null
  } else {
    expandedApartment.value = apartmentId
    // Загружаем бронирования для этой квартиры
    await loadBookingsForApartment(apartmentId)
  }
}

// === Функции для работы с бронированиями ===
const loadBookingsForApartment = async (apartmentId) => {
  try {
    const response = await bookingsApi.getByApartmentId(apartmentId)
    apartmentBookings.value[apartmentId] = response.data
  } catch (error) {
    console.error('Ошибка загрузки бронирований:', error)
    apartmentBookings.value[apartmentId] = []
  }
}

const openBookingModal = (apartment) => {
  currentBookingApartment.value = apartment
  editingBooking.value = null
  bookingFormData.value = {
    guestName: '',
    guestEmail: '',
    guestPhoneNumber: '',
    checkInDate: '',
    checkOutDate: '',
    bookingStatus: 'PENDING',
    paymentStatus: 'PENDING'
  }
  bookingError.value = ''
  showBookingModal.value = true
}

const closeBookingModal = () => {
  showBookingModal.value = false
  editingBooking.value = null
  currentBookingApartment.value = null
}

const editBooking = (booking) => {
  editingBooking.value = booking
  bookingFormData.value = {
    guestName: booking.guestName,
    guestEmail: booking.guestEmail,
    guestPhoneNumber: booking.guestPhoneNumber,
    checkInDate: booking.checkInDate,
    checkOutDate: booking.checkOutDate,
    bookingStatus: booking.bookingStatus || 'PENDING',
    paymentStatus: booking.paymentStatus || 'PENDING'
  }
  bookingError.value = ''
  showBookingModal.value = true
}

const deleteBooking = async (booking) => {
  if (confirm(`Удалить бронирование ${booking.bookingCode}?`)) {
    try {
      await bookingsApi.delete(booking.id)
      // Обновляем список бронирований
      if (expandedApartment.value) {
        await loadBookingsForApartment(expandedApartment.value)
      }
    } catch (error) {
      alert('Ошибка при удалении бронирования')
    }
  }
}

const handleBookingSubmit = async () => {
  bookingLoading.value = true
  bookingError.value = ''

  try {
    if (editingBooking.value) {
      // Обновление бронирования
      await bookingsApi.update(editingBooking.value.id, {
        ...bookingFormData.value,
        apartmentId: editingBooking.value.apartmentId
      })
      // Обновляем статусы отдельно
      await bookingsApi.updateBookingStatus(editingBooking.value.id, { 
        bookingStatus: bookingFormData.value.bookingStatus 
      })
      await bookingsApi.updatePaymentStatus(editingBooking.value.id, { 
        paymentStatus: bookingFormData.value.paymentStatus 
      })
    } else {
      // Создание нового бронирования
      await bookingsApi.create({
        apartmentId: currentBookingApartment.value.id,
        guestName: bookingFormData.value.guestName,
        guestEmail: bookingFormData.value.guestEmail,
        guestPhoneNumber: bookingFormData.value.guestPhoneNumber,
        checkInDate: bookingFormData.value.checkInDate,
        checkOutDate: bookingFormData.value.checkOutDate
      })
    }

    closeBookingModal()
    // Обновляем список бронирований
    if (expandedApartment.value) {
      await loadBookingsForApartment(expandedApartment.value)
    }
  } catch (error) {
    bookingError.value = error.response?.data?.message || 'Произошла ошибка'
  } finally {
    bookingLoading.value = false
  }
}

// Форматирование даты
const formatDate = (dateStr) => {
  if (!dateStr) return '—'
  const date = new Date(dateStr)
  return date.toLocaleDateString('ru-RU', { day: '2-digit', month: '2-digit', year: 'numeric' })
}

// Текст статуса бронирования
const getBookingStatusText = (status) => {
  const statuses = {
    PENDING: 'Ожидает',
    CONFIRMED: 'Подтверждено',
    CANCELLED: 'Отменено',
    COMPLETED: 'Завершено'
  }
  return statuses[status] || status
}

// Текст статуса оплаты
const getPaymentStatusText = (status) => {
  const statuses = {
    PENDING: 'Ожидает оплаты',
    PAID: 'Оплачено',
    REFUNDED: 'Возврат'
  }
  return statuses[status] || status
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.dashboard-header {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1.5rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dashboard-title {
  font-size: 1.75rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.user-name {
  font-size: 1rem;
  font-weight: 500;
  color: #2d3748;
}

.logout-btn {
  padding: 0.6rem 1.5rem;
  border: 2px solid #667eea;
  background: transparent;
  color: #667eea;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: #667eea;
  color: white;
  transform: translateY(-1px);
}

.dashboard-main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 3rem 2rem;
}

.welcome-section {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.add-btn {
  padding: 0.8rem 1.8rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.add-icon {
  font-size: 1.3rem;
  font-weight: 700;
}

.loading-container {
  text-align: center;
  padding: 4rem 2rem;
  color: white;
}

.spinner-large {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

.empty-state {
  background: white;
  border-radius: 16px;
  padding: 4rem 2rem;
  text-align: center;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: 1rem;
  opacity: 0.6;
}

.empty-state h3 {
  font-size: 1.5rem;
  color: #1a202c;
  margin: 0 0 0.5rem 0;
}

.empty-state p {
  font-size: 1.05rem;
  color: #718096;
  margin: 0 0 2rem 0;
}

.apartments-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.5rem;
}

/* === Новый вид квартир - горизонтальный список === */
.apartments-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.apartment-row {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.apartment-row:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.apartment-row.expanded {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.apartment-row-header {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  cursor: pointer;
  transition: background 0.2s ease;
  gap: 1.5rem;
}

.apartment-row-header:hover {
  background: #f8f9fa;
}

.apartment-main-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  min-width: 300px;
}

.expand-icon {
  color: #667eea;
  font-size: 0.75rem;
  width: 20px;
  transition: transform 0.2s ease;
}

.apartment-row .apartment-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
}

.apartment-type-badge {
  padding: 0.25rem 0.6rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 600;
  white-space: nowrap;
}

.apartment-quick-info {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex: 1;
  color: #4a5568;
  font-size: 0.9rem;
}

.quick-info-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  white-space: nowrap;
}

.quick-info-item.price {
  color: #667eea;
  font-weight: 600;
}

.info-icon {
  font-size: 1rem;
}

.apartment-header-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn-small {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn-small.edit-btn {
  background: #ebf4ff;
  color: #667eea;
}

.action-btn-small.edit-btn:hover {
  background: #667eea;
  color: white;
}

.action-btn-small.delete-btn {
  background: #fff5f5;
  color: #e53e3e;
}

.action-btn-small.delete-btn:hover {
  background: #e53e3e;
  color: white;
}

/* Развернутое содержимое */
.apartment-row-content {
  border-top: 1px solid #e2e8f0;
  padding: 1.5rem;
  background: #f8fafc;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.content-section {
  background: white;
  padding: 1.25rem;
  border-radius: 10px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.content-section.bookings-section {
  grid-column: 1 / -1;
}

.content-section .section-title {
  font-size: 1rem;
  font-weight: 600;
  color: #2d3748;
  margin: 0 0 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.section-header .section-title {
  margin-bottom: 0;
}

.details-grid {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
}

.detail-label {
  color: #718096;
}

.detail-value {
  color: #2d3748;
  font-weight: 500;
}

.add-booking-btn {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.add-booking-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

/* Список бронирований */
.bookings-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.booking-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  background: #f8fafc;
  border-radius: 8px;
  border-left: 4px solid #cbd5e0;
  transition: all 0.2s ease;
}

.booking-item:hover {
  background: #f1f5f9;
}

.booking-item.status-pending {
  border-left-color: #ecc94b;
}

.booking-item.status-confirmed {
  border-left-color: #48bb78;
}

.booking-item.status-cancelled {
  border-left-color: #e53e3e;
}

.booking-item.status-completed {
  border-left-color: #667eea;
}

.booking-info {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.booking-code {
  font-family: monospace;
  font-size: 0.85rem;
  color: #667eea;
  font-weight: 600;
}

.booking-guest {
  font-weight: 500;
  color: #2d3748;
}

.booking-dates {
  color: #718096;
  font-size: 0.9rem;
}

.booking-statuses {
  display: flex;
  gap: 0.75rem;
}

.booking-status,
.payment-status {
  padding: 0.25rem 0.6rem;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 600;
}

.booking-status.status-pending { background: #fef3c7; color: #92400e; }
.booking-status.status-confirmed { background: #d1fae5; color: #065f46; }
.booking-status.status-cancelled { background: #fee2e2; color: #991b1b; }
.booking-status.status-completed { background: #ddd6fe; color: #5b21b6; }

.payment-status.payment-pending { background: #fef3c7; color: #92400e; }
.payment-status.payment-paid { background: #d1fae5; color: #065f46; }
.payment-status.payment-refunded { background: #fce7f3; color: #9d174d; }

.booking-actions {
  display: flex;
  gap: 0.5rem;
}

.booking-action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 6px;
  background: #e2e8f0;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.booking-action-btn:hover {
  background: #667eea;
  color: white;
}

.booking-action-btn.delete:hover {
  background: #e53e3e;
}

.no-bookings {
  text-align: center;
  padding: 2rem;
  color: #718096;
  font-style: italic;
}

.content-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

/* Модалка бронирования */
.booking-modal {
  max-width: 550px;
}

.booking-form .form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.apartment-card {
  background: white;
  border-radius: 16px;
  padding: 1.8rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.apartment-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}

.apartment-header {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-bottom: 1.2rem;
  gap: 1rem;
}

.apartment-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
  margin: 0;
  flex: 1;
}

.apartment-type {
  padding: 0.4rem 0.8rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 600;
  white-space: nowrap;
}

.apartment-details {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  margin-bottom: 1.5rem;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 0.7rem;
  font-size: 0.95rem;
  color: #4a5568;
}

.detail-icon {
  font-size: 1.1rem;
}

.apartment-actions {
  display: flex;
  gap: 0.8rem;
  padding-top: 1.2rem;
  border-top: 1px solid #e2e8f0;
}

.action-btn {
  flex: 1;
  padding: 0.7rem;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.edit-btn {
  background: #f7fafc;
  color: #667eea;
  border: 2px solid #667eea;
}

.edit-btn:hover {
  background: #667eea;
  color: white;
}

.delete-btn {
  background: #fff5f5;
  color: #e53e3e;
  border: 2px solid #e53e3e;
}

.delete-btn:hover {
  background: #e53e3e;
  color: white;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 1rem;
  overflow-y: auto;
}

.modal-content {
  background: white;
  border-radius: 16px;
  max-width: 700px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.8rem 2rem;
  border-bottom: 1px solid #e2e8f0;
  position: sticky;
  top: 0;
  background: white;
  z-index: 1;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #1a202c;
}

.modal-close {
  background: none;
  border: none;
  font-size: 2rem;
  color: #718096;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  transition: all 0.2s;
}

.modal-close:hover {
  background: #f7fafc;
  color: #1a202c;
}

.apartment-form {
  padding: 2rem;
}

.form-section {
  margin-bottom: 2rem;
}

.section-heading {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2d3748;
  margin: 0 0 1.2rem 0;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e2e8f0;
}

.form-group {
  margin-bottom: 1.2rem;
  flex: 1;
}

.form-group label {
  display: block;
  font-size: 0.9rem;
  font-weight: 600;
  color: #2d3748;
  margin-bottom: 0.5rem;
}

.required {
  color: #e53e3e;
}

.helper-text {
  font-size: 0.85rem;
  color: #718096;
  margin: 0.5rem 0 0 0;
  font-style: italic;
}

.divider {
  text-align: center;
  color: #a0aec0;
  font-size: 0.85rem;
  font-weight: 500;
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

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.2s;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-row {
  display: flex;
  gap: 1rem;
}

.flex-2 {
  flex: 2;
}

.form-error {
  background: #fff5f5;
  color: #c53030;
  padding: 0.8rem 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  border-left: 4px solid #e53e3e;
}

.form-actions {
  display: flex;
  gap: 1rem;
  padding-top: 1.5rem;
  border-top: 1px solid #e2e8f0;
}

.btn-primary {
  flex: 1;
  padding: 0.9rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  text-decoration: none;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  flex: 1;
  padding: 0.9rem;
  background: #f7fafc;
  color: #4a5568;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background: #edf2f7;
  border-color: #cbd5e0;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 1rem;
  }

  .welcome-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .apartments-grid {
    grid-template-columns: 1fr;
  }

  .form-row {
    flex-direction: column;
  }

  .modal-content {
    margin: 0;
    border-radius: 0;
    max-height: 100vh;
  }
}
</style>
