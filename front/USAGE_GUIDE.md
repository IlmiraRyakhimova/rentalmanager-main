# 🎯 Rental Manager - Руководство по использованию фронтенда

## ✅ Что было реализовано

### 1. **Современная архитектура**
- **Vue 3 Composition API** - использование современных паттернов разработки
- **Pinia Store** - централизованное управление состоянием пользователя
- **Vue Router** - клиентский роутинг с защитой маршрутов
- **Axios с интерцепторами** - автоматическое добавление токенов и их обновление

### 2. **Страница регистрации** (`/register`) 🌟
Самая проработанная страница с полным функционалом:

**Функции:**
- ✅ Полная валидация всех полей в реальном времени
- ✅ Индикатор силы пароля с градиентом (слабый → сильный)
- ✅ Показ/скрытие пароля
- ✅ Проверка совпадения паролей
- ✅ Красивые сообщения об ошибках
- ✅ Анимация загрузки при отправке
- ✅ Адаптивный дизайн

**Валидация полей:**
- Имя: минимум 2 символа
- Email: валидный формат email
- Телефон: формат +79991234567 (10-15 цифр)
- Пароль: 8-72 символа
- Подтверждение пароля: совпадение с паролем

### 3. **Страница входа** (`/login`)
**Функции:**
- ✅ Валидация email и пароля
- ✅ Показ/скрытие пароля
- ✅ Обработка ошибок авторизации
- ✅ Переход к регистрации

### 4. **Главная страница** (`/`)
**Функции:**
- ✅ Приветственный экран с описанием
- ✅ Кнопки "Зарегистрироваться" и "Войти"
- ✅ Карточки с основными возможностями
- ✅ Современный градиентный дизайн

### 5. **Dashboard** (`/dashboard`)
**Функции:**
- ✅ Приветствие пользователя по имени
- ✅ Информационные карточки (заглушки для будущего функционала)
- ✅ Кнопка выхода
- ✅ Защищенный маршрут (требует авторизации)

## 🎨 Дизайн

### Цветовая схема
- **Основной градиент**: `#667eea` → `#764ba2`
- **Фон карточек**: Белый
- **Текст**: Темно-серый (#1a202c, #2d3748)
- **Акценты**: Фиолетовый (#667eea)

### Анимации
- Плавное появление карточек (slideUp, fadeIn)
- Hover эффекты на кнопках
- Плавные переходы focus состояний
- Градиент индикатора силы пароля

## 🔐 Безопасность

### JWT Токены
- Access Token хранится в localStorage
- Refresh Token для обновления сессии
- Автоматическое обновление при истечении

### Защита маршрутов
```javascript
// В router/index.js
router.beforeEach((to, from, next) => {
  // Проверка авторизации для защищенных страниц
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' })
  }
  // Редирект авторизованных с гостевых страниц
  else if (to.meta.requiresGuest && authStore.isAuthenticated) {
    next({ name: 'dashboard' })
  }
})
```

## 🚀 Запуск

```bash
# Установка зависимостей
npm install

# Запуск в режиме разработки
npm run dev

# Приложение доступно на http://localhost:5173
```

## 📱 Тестирование

### Регистрация нового агента
1. Перейти на http://localhost:5173/register
2. Заполнить форму:
   - Имя: `Иван Петров`
   - Email: `ivan@agent.com`
   - Телефон: `+79991234567`
   - Пароль: `TestPass123` (минимум 8 символов)
   - Подтвердить пароль
3. Нажать "Зарегистрироваться"
4. При успехе → автоматический переход на `/dashboard`

### Вход существующего агента
1. Перейти на http://localhost:5173/login
2. Ввести email и пароль
3. Нажать "Войти"
4. При успехе → переход на `/dashboard`

## 🛠️ Интеграция с бэкендом

### API эндпоинты
```javascript
// Регистрация
POST http://localhost:8080/api/auth/sign-up
Body: {
  name, email, phoneNumber, password, role: "AGENT"
}

// Вход
POST http://localhost:8080/api/auth/sign-in
Body: { email, password }

// Обновление токена
POST http://localhost:8080/api/auth/refresh-token
Body: { refreshToken }

// Выход
POST http://localhost:8080/api/auth/log-out
```

### Настройка CORS
Убедитесь, что бэкенд разрешает запросы с `http://localhost:5173`

## 📂 Структура файлов

```
front/src/
├── api/
│   ├── axios.js          # Настройка axios + интерцепторы
│   └── auth.js           # API методы аутентификации
├── stores/
│   └── auth.js           # Pinia store для auth
├── router/
│   └── index.js          # Маршруты + guards
├── views/
│   ├── HomeView.vue      # Главная страница
│   ├── LoginView.vue     # Вход
│   ├── RegisterView.vue  # Регистрация ⭐
│   └── DashboardView.vue # Dashboard
└── App.vue               # Корневой компонент
```

## 💡 Современные практики

### 1. **Composition API**
```vue
<script setup>
import { ref, computed } from 'vue'
// Более чистый и понятный код
</script>
```

### 2. **Reactive State Management**
```javascript
// Pinia store с Composition API
export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const isAuthenticated = computed(() => !!accessToken.value)
  return { user, isAuthenticated, signIn, signUp }
})
```

### 3. **Async/Await**
```javascript
async function handleSubmit() {
  try {
    await authStore.signUp(formData.value)
    router.push('/dashboard')
  } catch (error) {
    serverError.value = error.message
  }
}
```

### 4. **Интерцепторы Axios**
```javascript
// Автоматическое добавление токена
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Автообновление токена при 401
apiClient.interceptors.response.use(
  response => response,
  async error => {
    if (error.response?.status === 401) {
      // Обновить токен и повторить запрос
    }
  }
)
```

## 🎯 Особенности реализации

### Индикатор силы пароля
```javascript
const passwordStrength = computed(() => {
  let strength = 0
  if (password.length >= 8) strength++
  if (password.length >= 12) strength++
  if (/[a-z].*[A-Z]|[A-Z].*[a-z]/.test(password)) strength++
  if (/\d/.test(password)) strength++
  if (/[^a-zA-Z\d]/.test(password)) strength++
  return Math.min(strength, 4)
})
```

### Валидация в реальном времени
- `@blur` - валидация при потере фокуса
- `@input` - очистка ошибки при вводе
- Мгновенная обратная связь пользователю

## 📊 Что дальше?

Готовая архитектура позволяет легко добавить:
- Управление квартирами (CRUD)
- Календарь бронирований
- Список клиентов
- Отчеты и статистику
- Профиль пользователя
- Настройки

Все API методы уже подготовлены в бэкенде согласно `api_doc.md`.

## 🎉 Результат

✅ Полностью рабочий фронтенд с современным дизайном  
✅ Профессиональная страница регистрации с валидацией  
✅ Интеграция с бэкенд API  
✅ JWT аутентификация с автообновлением токенов  
✅ Защищенные маршруты  
✅ Адаптивный дизайн  
✅ Готов к расширению функционала  
