# Документация API для фронтенда

## Базовый URL
`http://localhost:8080`

---

## 1. Аутентификация (`/api/auth`)

> **Примечание:** Эти эндпоинты не требуют авторизации

### 1.1 Регистрация
- **Метод:** `POST`
- **URL:** `/api/auth/sign-up`
- **Headers:**
    - `Content-Type: application/json`
- **Body:**
```json
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "phoneNumber": "+79991234567",
  "password": "myPassword123",
  "role": "OWNER"
}
```
- **Возможные значения `role`:** `OWNER`, `AGENT` (я тут еще буду настраивать права для OWNER, пока все эндпоинты для агента)
- **Response (200 OK):**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "type": "Bearer",
  "email": "ivan@example.com",
  "name": "Иван Иванов"
}
```

### 1.2 Вход
- **Метод:** `POST`
- **URL:** `/api/auth/sign-in`
- **Headers:**
    - `Content-Type: application/json`
- **Body:**
```json
{
  "email": "ivan@example.com",
  "password": "myPassword123"
}
```
- **Response (200 OK):** аналогичен регистрации

### 1.3 Забыли пароль
- **Метод:** 'POST'
- - **URL:** `/api/auth/forgot-password`
- **Headers:**
    - `Content-Type: application/json`
- **Body:**
```json
{
  "email": "ivan@example.com"
}
```
- **Response (200 OK):** `Письмо для сброса пароля отправлено`



### 1.3 Обновление токена
- **Метод:** `POST`
- **URL:** `/api/auth/refresh-token`
- **Headers:**
    - `Content-Type: application/json`
- **Body:**
```json
{
  "refreshToken": "eyJhbGc..."
}
```
- **Response (200 OK):** аналогичен регистрации

---

## 2. Пользователи (`/api/users`)

> **Требуют авторизации!**

**Headers для всех запросов:**
- `Authorization: Bearer <accessToken>`
- `Content-Type: application/json`

### 2.1 Получить всех пользователей
- **Метод:** `GET`
- **URL:** `/api/users`
- **Response (200 OK):**
```json
[
  {
    "id": "uuid",
    "name": "string",
    "email": "string",
    "phoneNumber": "string",
    "role": "OWNER"
  }
]
```

### 2.2 Создать пользователя
- **Метод:** `POST`
- **URL:** `/api/users`
- **Body:**
```json
{
  "name": "Петр Петров",
  "email": "petr@example.com",
  "phoneNumber": "+79991234568",
  "role": "AGENT"
}
```
- **Response (200 OK):** объект созданного пользователя

### 2.3 Получить пользователя по ID
- **Метод:** `GET`
- **URL:** `/api/users/{id}`
- **Параметры:** `id` - UUID пользователя
- **Response (200 OK):** объект пользователя

### 2.4 Обновить пользователя (полностью)
- **Метод:** `PUT`
- **URL:** `/api/users/{id}`
- **Body:** аналогичен созданию (все поля обязательны)

### 2.5 Обновить пользователя (частично) (не знаю насчет частичного обновления, это надо?)
- **Метод:** `PATCH`
- **URL:** `/api/users/{id}`
- **Body:** (можно отправить только изменяемые поля)
```json
{
  "name": "Новое имя",
  "phoneNumber": "+79999999999"
}
```

### 2.6 Удалить пользователя
- **Метод:** `DELETE`
- **URL:** `/api/users/{id}`
- **Response (204 No Content)**

### 2.7 Поиск по имени
- **Метод:** `GET`
- **URL:** `/api/users/search/by-name/{name}`
- **Response:** массив пользователей

### 2.8 Поиск по email
- **Метод:** `GET`
- **URL:** `/api/users/search/by-email/{email}`
- **Response:** объект пользователя

### 2.9 Поиск по телефону
- **Метод:** `GET`
- **URL:** `/api/users/search/by-phone-number/{phoneNumber}`
- **Response:** объект пользователя

---

## 3. Квартиры (`/api/apartments`)

> **Требуют авторизации!**

**Headers для всех запросов:**
- `Authorization: Bearer <accessToken>`
- `Content-Type: application/json`

### 3.1 Получить все квартиры
- **Метод:** `GET`
- **URL:** `/api/apartments`
- **Response (200 OK):** массив квартир

### 3.2 Создать квартиру
- **Метод:** `POST`
- **URL:** `/api/apartments`
- **Body:**
```json
{
  "title": "Уютная квартира в центре",
  "accommodationType": "Apartment",
  "owner": {
    "name": "Иван Иванов",
    "email": "ivan@example.com",
    "phoneNumber": "+79991234567",
    "role": "OWNER"
  },
  "address": {
    "postalCode": "123456",
    "country": "Россия",
    "city": "Москва",
    "district": "Центральный",
    "street": "Тверская",
    "buildingNumber": "10",
    "floorNumber": 5,
    "apartmentNumber": 42
  },
  "pricePerNight": 5000.00,
  "area": 65.5,
  "numberOfRooms": 2,
  "numberOfBathrooms": 1
}
```
- **Response (200 OK):** объект созданной квартиры

### 3.3 Получить квартиру по ID
- **Метод:** `GET`
- **URL:** `/api/apartments/{id}`

### 3.4 Обновить квартиру (полностью)
- **Метод:** `PUT`
- **URL:** `/api/apartments/{id}`
- **Body:** аналогичен созданию

### 3.5 Обновить квартиру (частично)
- **Метод:** `PATCH`
- **URL:** `/api/apartments/{id}`
- **Body:** (можно отправить только изменяемые поля)
```json
{
  "title": "Новое название",
  "pricePerNight": 6000.00
}
```

### 3.6 Удалить квартиру
- **Метод:** `DELETE`
- **URL:** `/api/apartments/{id}`
- **Response (204 No Content)**

### 3.7 Поиск квартир (GET)
- По владельцу: `/api/apartments/search/by-owner-id/{ownerId}`
- По имени владельца: `/api/apartments/search/by-owner-name/{ownerName}`
- По email владельца: `/api/apartments/search/by-owner-email/{ownerEmail}`
- По телефону владельца: `/api/apartments/search/by-owner-phone/{ownerPhoneNumber}`
- По названию: `/api/apartments/search/by-title/{title}`
- По типу жилья: `/api/apartments/search/by-accommodation-type/{accommodationType}`
- По почтовому индексу: `/api/apartments/search/by-postal-code/{postalCode}`
- По стране: `/api/apartments/search/by-country/{country}`
- По городу: `/api/apartments/search/by-city/{city}`
- По району: `/api/apartments/search/by-district/{district}`
- По улице: `/api/apartments/search/by-street/{street}`
- По ценовому диапазону: `/api/apartments/search/by-price-per-night/{minPrice}/{maxPrice}`
- По площади: `/api/apartments/search/by-area/{minArea}/{maxArea}`
- По количеству комнат: `/api/apartments/search/by-number-of-rooms/{numberOfRooms}`
- По количеству ванных комнат: `/api/apartments/search/by-number-of-bathrooms/{numberOfBathrooms}`

**Все поисковые запросы возвращают массив квартир**

---

## 4. Бронирования (`/api/bookings`)

> **Требуют авторизации!**

**Headers для всех запросов:**
- `Authorization: Bearer <accessToken>`
- `Content-Type: application/json`

### 4.1 Создать бронирование
- **Метод:** `POST`
- **URL:** `/api/bookings`
- **Body:**
```json
{
  "apartmentId": "uuid квартиры",
  "guestName": "Петр Петров",
  "guestEmail": "petr@example.com",
  "guestPhoneNumber": "+79991234568",
  "checkInDate": "2025-01-01",
  "checkOutDate": "2025-01-05"
}
```
- **Response (200 OK):** объект созданного бронирования

### 4.2 Получить все бронирования
- **Метод:** `GET`
- **URL:** `/api/bookings`
- **Response (200 OK):** массив бронирований

### 4.3 Получить бронирование по ID
- **Метод:** `GET`
- **URL:** `/api/bookings/{id}`

### 4.4 Обновить бронирование (полностью)
- **Метод:** `PUT`
- **URL:** `/api/bookings/{id}`
- **Body:** аналогичен созданию

### 4.5 Обновить бронирование (частично)
- **Метод:** `PATCH`
- **URL:** `/api/bookings/{id}`
- **Body:** (можно отправить только изменяемые поля)
```json
{
  "guestName": "Новое имя",
  "checkInDate": "2025-01-02"
}
```

### 4.6 Удалить бронирование
- **Метод:** `DELETE`
- **URL:** `/api/bookings/{id}`
- **Response (204 No Content)**

### 4.7 Обновить статус бронирования
- **Метод:** `PATCH`
- **URL:** `/api/bookings/booking-status/{id}`
- **Body:**
```json
{
  "bookingStatus": "CONFIRMED"
}
```
- **Возможные значения:** `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED`

### 4.8 Обновить статус оплаты
- **Метод:** `PATCH`
- **URL:** `/api/bookings/payment-status/{id}`
- **Body:**
```json
{
  "paymentStatus": "PAID"
}
```
- **Возможные значения:** `PENDING`, `PAID`, `REFUNDED`

### 4.9 Поиск бронирований (GET)
- По коду бронирования: `/api/bookings/search/by-booking-code/{bookingCode}`
- По имени гостя: `/api/bookings/search/by-guest-name/{guestName}`
- По email гостя: `/api/bookings/search/by-guest-email/{guestEmail}`
- По телефону гостя: `/api/bookings/search/by-guest-phone-number/{guestPhoneNumber}`
- По статусу бронирования: `/api/bookings/search/by-booking-status/{bookingStatus}`
- По статусу оплаты: `/api/bookings/search/by-payment-status/{paymentStatus}`
- По ID квартиры: `/api/bookings/search/by-apartment-id/{apartmentId}`
- По названию квартиры: `/api/bookings/search/by-apartment-title/{apartmentTitle}`
- По ID владельца квартиры: `/api/bookings/search/by-apartment-owner-id/{ownerId}`
- По имени владельца: `/api/bookings/search/by-apartment-owner-name/{ownerName}`
- По email владельца: `/api/bookings/search/by-apartment-owner-email/{ownerEmail}`
- По телефону владельца: `/api/bookings/search/by-apartment-owner-phone-number/{ownerPhoneNumber}`

---

## 5. Настройки аккаунта(`/api/account-settings`)
### 5.1 Изменить пароль
- **Метод:** `POST`
- **URL:** `/api/account-settings/change-password`
- **Body:**
```json
{
  "oldPassword": "MyPassword123",
  "newPassword": "123MyPassword"
}
```
- **Response (200 OK):** `Пароль изменен`

## Обработка ошибок

Все эндпоинты могут вернуть следующие коды ошибок:

### 400 Bad Request
```json
{
  "fieldName": "сообщение об ошибке валидации"
}
```

### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2025-01-20T10:30:00"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Entity not found",
  "timestamp": "2025-01-20T10:30:00"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "An unexpected error occurred",
  "timestamp": "2025-01-20T10:30:00"
}
```