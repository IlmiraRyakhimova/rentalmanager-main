<template>
  <div class="owner-dashboard">
    <header class="dashboard-header">
      <div class="header-content">
        <h1 class="dashboard-title">RENTAL MANAGER</h1>
        <div class="user-info">
          <span class="user-role-badge">Собственник</span>
          <span class="user-name">{{ user?.name }}</span>
          <button @click="goToSettings" class="settings-btn">⚙️ Настройки</button>
          <button @click="handleLogout" class="logout-btn">Выйти</button>
        </div>
      </div>
    </header>

    <main class="dashboard-main">
      <!-- Вкладки -->
      <div class="tabs">
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'apartments' }"
          @click="activeTab = 'apartments'"
        >
          🏢 Мои апартаменты ({{ apartments.length }})
        </button>
        <button
          class="tab-btn"
          :class="{ active: activeTab === 'bookings' }"
          @click="activeTab = 'bookings'"
        >
          📅 Бронирования ({{ bookings.length }})
        </button>
      </div>

      <!-- Загрузка -->
      <div v-if="loading" class="loading-container">
        <div class="spinner-large"></div>
        <p>Загрузка данных...</p>
      </div>

      <!-- Ошибка -->
      <div v-else-if="error" class="error-container">
        <p class="error-message">{{ error }}</p>
        <button @click="loadData" class="retry-btn">Повторить</button>
      </div>

      <!-- Вкладка: Мои квартиры -->
      <div v-else-if="activeTab === 'apartments'" class="tab-content">
        <div v-if="apartments.length === 0" class="empty-state">
          <div class="empty-icon">🏢</div>
          <h3>У вас пока нет апартаментов</h3>
          <p>Ваш агент добавит апартаменты, и они появятся здесь</p>
        </div>

        <div v-else class="apartments-grid">
          <div v-for="apartment in apartments" :key="apartment.id" class="apartment-card">
            <div class="apartment-header">
              <h3 class="apartment-title">{{ apartment.title }}</h3>
              <span class="apartment-type">{{ apartment.accommodationType }}</span>
            </div>

            <div class="apartment-details">
              <div class="detail-row">
                <span class="detail-icon">📍</span>
                <span class="detail-text">
                  {{ apartment.address?.city }}, {{ apartment.address?.street }} {{ apartment.address?.buildingNumber }}
                </span>
              </div>
              <div class="detail-row">
                <span class="detail-icon">🛏️</span>
                <span class="detail-text">{{ apartment.numberOfRooms }} комн.</span>
              </div>
              <div class="detail-row">
                <span class="detail-icon">📐</span>
                <span class="detail-text">{{ apartment.area }} м²</span>
              </div>
              <div class="detail-row">
                <span class="detail-icon">💰</span>
                <span class="detail-text price">{{ apartment.pricePerNight }} {{ currentCurrencySymbol }}/ночь</span>
              </div>
            </div>

            <div class="apartment-bookings-count">
              <span class="bookings-icon">📅</span>
              <span>Бронирований: {{ getApartmentBookingsCount(apartment.id) }}</span>
            </div>

            <div class="apartment-total-income">
              <span class="income-icon">💵</span>
              <span class="income-text">Общий доход: <strong>{{ formatPrice(getApartmentTotalIncome(apartment.id)) }} {{ currentCurrencySymbol }}</strong></span>
            </div>
          </div>
        </div>
      </div>

      <!-- Вкладка: Бронирования -->
      <div v-else-if="activeTab === 'bookings'" class="tab-content">
        <div v-if="bookings.length === 0" class="empty-state">
          <div class="empty-icon">📅</div>
          <h3>Бронирований пока нет</h3>
          <p>Когда появятся бронирования ваших апартаментов, они отобразятся здесь</p>
        </div>

        <div v-else class="bookings-list">
          <div
            v-for="booking in sortedBookings"
            :key="booking.id"
            class="booking-card"
            :class="'status-' + booking.bookingStatus?.toLowerCase()"
          >
            <div class="booking-header">
              <span class="booking-code">{{ booking.bookingCode }}</span>
              <div class="booking-statuses">
                <span class="booking-status" :class="'status-' + booking.bookingStatus?.toLowerCase()">
                  {{ getBookingStatusText(booking.bookingStatus) }}
                </span>
                <span class="payment-status" :class="'payment-' + booking.paymentStatus?.toLowerCase()">
                  {{ getPaymentStatusText(booking.paymentStatus) }}
                </span>
              </div>
            </div>

            <div class="booking-apartment">
              <span class="apartment-icon">🏢</span>
              <span>{{ getApartmentTitle(booking) }}</span>
            </div>

            <div class="booking-details">
              <div class="detail-row">
                <span class="detail-icon">👤</span>
                <span class="detail-text">{{ booking.mainGuest?.name || booking.guestName || '—' }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-icon">📅</span>
                <span class="detail-text">
                  {{ formatDate(booking.checkInDate) }} — {{ formatDate(booking.checkOutDate) }}
                </span>
              </div>
              <div class="detail-row">
                <span class="detail-icon">🌙</span>
                <span class="detail-text">{{ getNightsCount(booking.checkInDate, booking.checkOutDate) }} ночей</span>
              </div>
            </div>

            <div class="booking-price-section">
              <span class="price-label">Сумма бронирования:</span>
              <span class="booking-total-price">{{ formatPrice(getBookingPrice(booking)) }} {{ currentCurrencySymbol }}</span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ownerApi } from '@/api/owner'

const router = useRouter()
const authStore = useAuthStore()

const user = computed(() => authStore.user)
const loading = ref(true)
const error = ref(null)
const activeTab = ref('apartments')

const apartments = ref([])
const bookings = ref([])

// Валюта
const selectedCurrency = ref('THB')
const currencies = [
  { code: 'THB', symbol: '฿', name: 'Тайский бат' },
  { code: 'RUB', symbol: '₽', name: 'Российский рубль' },
  { code: 'USD', symbol: '$', name: 'Доллар США' },
  { code: 'EUR', symbol: '€', name: 'Евро' },
]

const currentCurrencySymbol = computed(() => {
  const currency = currencies.find(c => c.code === selectedCurrency.value)
  return currency?.symbol || '฿'
})

// Сортировка бронирований по дате заезда (ближайшие сначала)
const sortedBookings = computed(() => {
  return [...bookings.value].sort((a, b) => {
    return new Date(a.checkInDate) - new Date(b.checkInDate)
  })
})

// Загрузка данных
async function loadData() {
  loading.value = true
  error.value = null

  try {
    const [apartmentsRes, bookingsRes] = await Promise.all([
      ownerApi.getMyApartments(),
      ownerApi.getMyBookings()
    ])

    apartments.value = apartmentsRes.data || []
    bookings.value = bookingsRes.data || []
  } catch (err) {
    console.error('Ошибка загрузки данных:', err)
    error.value = err.response?.data?.message || 'Ошибка загрузки данных'
  } finally {
    loading.value = false
  }
}

// Получить количество бронирований для квартиры
function getApartmentBookingsCount(apartmentId) {
  return bookings.value.filter(b => {
    // Проверяем оба варианта: apartmentId напрямую или apartment.id
    const bookingApartmentId = b.apartmentId || b.apartment?.id
    return bookingApartmentId === apartmentId
  }).length
}

// Получить общий доход квартиры за все бронирования
function getApartmentTotalIncome(apartmentId) {
  return bookings.value
    .filter(b => {
      const bookingApartmentId = b.apartmentId || b.apartment?.id
      // Учитываем только подтверждённые и завершённые бронирования
      const isValidStatus = b.bookingStatus !== 'CANCELLED'
      return bookingApartmentId === apartmentId && isValidStatus
    })
    .reduce((total, b) => {
      const price = getBookingPrice(b)
      return total + price
    }, 0)
}

// Получить цену бронирования
function getBookingPrice(booking) {
  // Если есть totalPrice - используем её
  if (booking.totalPrice) {
    return Number(booking.totalPrice)
  }
  // Иначе вычисляем: ночи * цена за ночь
  const nights = getNightsCount(booking.checkInDate, booking.checkOutDate)
  const pricePerNight = booking.apartment?.pricePerNight || 0
  return nights * Number(pricePerNight)
}

// Форматирование цены с разделителями
function formatPrice(price) {
  if (!price && price !== 0) return '0'
  return new Intl.NumberFormat('ru-RU').format(Math.round(price))
}

// Получить название квартиры по ID или из объекта бронирования
function getApartmentTitle(booking) {
  // Сначала пробуем взять из apartmentTitle
  if (booking.apartmentTitle) {
    return booking.apartmentTitle
  }
  // Затем из объекта apartment
  if (booking.apartment?.title) {
    return booking.apartment.title
  }
  // Ищем в списке квартир
  const apartmentId = booking.apartmentId || booking.apartment?.id
  const apartment = apartments.value.find(a => a.id === apartmentId)
  return apartment?.title || 'Апартаменты'
}

// Форматирование даты
function formatDate(dateString) {
  if (!dateString) return '—'
  const date = new Date(dateString)
  return date.toLocaleDateString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  })
}

// Подсчёт количества ночей
function getNightsCount(checkIn, checkOut) {
  if (!checkIn || !checkOut) return 0
  const start = new Date(checkIn)
  const end = new Date(checkOut)
  const diffTime = Math.abs(end - start)
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
}

// Текст статуса бронирования
function getBookingStatusText(status) {
  const statuses = {
    PENDING: 'Ожидает',
    CONFIRMED: 'Подтверждено',
    CANCELLED: 'Отменено',
    COMPLETED: 'Завершено'
  }
  return statuses[status] || status
}

// Текст статуса оплаты
function getPaymentStatusText(status) {
  const statuses = {
    PENDING: 'Не оплачено',
    PAID: 'Оплачено',
    REFUNDED: 'Возврат'
  }
  return statuses[status] || status
}

// Выход
async function handleLogout() {
  await authStore.logOut()
  router.push({ name: 'login' })
}

// Переход к настройкам аккаунта
function goToSettings() {
  router.push({ name: 'account-settings' })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.owner-dashboard {
  min-height: 100vh;
  background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
}

.dashboard-header {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 1rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dashboard-title {
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: 2px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.currency-select {
  padding: 0.4rem 0.6rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 600;
  font-size: 0.85rem;
  cursor: pointer;
  outline: none;
  transition: all 0.3s;
}

.currency-select:hover {
  background: rgba(255, 255, 255, 0.3);
}

.currency-select:focus {
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.2);
}

.currency-select option {
  background: #1565c0;
  color: white;
}

.user-role-badge {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
}

.user-name {
  color: white;
  font-weight: 500;
}

.settings-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.settings-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.logout-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.dashboard-main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

/* Вкладки */
.tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.tab-btn {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1rem 2rem;
  border-radius: 12px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s;
}

.tab-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.tab-btn.active {
  background: white;
  color: #1565c0;
  border-color: white;
}

/* Загрузка и ошибка */
.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  color: white;
}

.spinner-large {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-message {
  color: #ff6b6b;
  margin-bottom: 1rem;
}

.retry-btn {
  background: white;
  color: #1565c0;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
}

/* Пустое состояние */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  color: white;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.empty-state h3 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.empty-state p {
  opacity: 0.8;
}

/* Сетка квартир */
.apartments-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.apartment-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.apartment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.apartment-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.apartment-type {
  background: #1565c0;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
}

.apartment-details {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.detail-icon {
  font-size: 1.1rem;
}

.detail-text {
  color: #555;
}

.detail-text.price {
  font-weight: 600;
  color: #1565c0;
}

.apartment-bookings-count {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #888;
}

.apartment-total-income {
  margin-top: 0.75rem;
  padding: 0.75rem;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.income-icon {
  font-size: 1.2rem;
}

.income-text {
  color: #2e7d32;
  font-size: 0.95rem;
}

.income-text strong {
  font-size: 1.1rem;
}

/* Список бронирований */
.bookings-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.booking-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border-left: 4px solid #ccc;
}

.booking-card.status-pending {
  border-left-color: #f0ad4e;
}

.booking-card.status-confirmed {
  border-left-color: #5cb85c;
}

.booking-card.status-cancelled {
  border-left-color: #d9534f;
}

.booking-card.status-completed {
  border-left-color: #5bc0de;
}

.booking-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.booking-code {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
}

.booking-statuses {
  display: flex;
  gap: 0.5rem;
}

.booking-status,
.payment-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.booking-status.status-pending {
  background: #fcf8e3;
  color: #8a6d3b;
}

.booking-status.status-confirmed {
  background: #dff0d8;
  color: #3c763d;
}

.booking-status.status-cancelled {
  background: #f2dede;
  color: #a94442;
}

.booking-status.status-completed {
  background: #d9edf7;
  color: #31708f;
}

.payment-status.payment-pending {
  background: #fcf8e3;
  color: #8a6d3b;
}

.payment-status.payment-paid {
  background: #dff0d8;
  color: #3c763d;
}

.payment-status.payment-refunded {
  background: #f2dede;
  color: #a94442;
}

.booking-apartment {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-weight: 500;
  color: #555;
}

.booking-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.75rem;
}

.booking-price-section {
  margin-top: 1rem;
  padding: 1rem;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-radius: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-label {
  color: #1565c0;
  font-size: 0.95rem;
  font-weight: 500;
}

.booking-total-price {
  font-size: 1.4rem;
  font-weight: 700;
  color: #1565c0;
}

/* Responsive */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 1rem;
  }

  .tabs {
    flex-direction: column;
  }

  .apartments-grid {
    grid-template-columns: 1fr;
  }

  .booking-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .booking-details {
    grid-template-columns: 1fr;
  }
}
</style>
